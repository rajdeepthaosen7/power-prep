#!/usr/bin/env bash
# Power Prep: one-time AWS setup. Run it in AWS CloudShell:
#   curl -fsSL https://raw.githubusercontent.com/rajdeepthaosen7/power-prep/main/deploy/aws/setup.sh -o setup.sh && bash setup.sh
# Safe to run again (it updates what already exists).
#
# It creates:
#   - the secrets the API needs, stored encrypted in SSM Parameter Store (free)
#   - the role the API runs as, and a deploy role GitHub Actions can use (no AWS keys stored in GitHub)
#   - the cost kill switch: alarms + a $0.50 budget that switch the API off (concurrency 0) and email you
# The API function itself is created by the first GitHub Actions deploy.
set -euo pipefail

REGION=ap-southeast-2          # Sydney: the Free plan allows Lambda only in Sydney, us-east-1, us-west-2
SSM_REGION=$REGION            # secrets live in Sydney too (the Free plan blocks Singapore)
FN=power-prep-api
REPO=rajdeepthaosen7/power-prep
# GitHub now puts owner and repo ids in the OIDC "sub" claim: repo:owner@ownerId/repo@repoId:ref:...
REPO_IDS=rajdeepthaosen7@147642901/power-prep@1470408163
P=/power-prep
export AWS_DEFAULT_REGION=$REGION AWS_REGION=$REGION AWS_PAGER=""   # CloudShell sets AWS_REGION, which wins over AWS_DEFAULT_REGION

ACCOUNT=$(aws sts get-caller-identity --query Account --output text)
echo "AWS account $ACCOUNT, region $REGION"
echo

ask()  { local v; read -rp "$1: " v; echo "$v"; }
asks() { local v; read -rsp "$1 (hidden): " v; echo >&2; echo "$v"; }
have() { aws ssm get-parameter --region $SSM_REGION --name "$P/$1" >/dev/null 2>&1; }
put()  { aws ssm put-parameter --region $SSM_REGION --name "$P/$1" --type "$2" --value "$3" --overwrite >/dev/null; }

# ---------- 1. secrets ----------
if have DATABASE_URL && [ "${RESET_SECRETS:-}" != "1" ]; then
  echo "Secrets already saved (run with RESET_SECRETS=1 to type them again)."
  ALERT_EMAIL=$(aws ssm get-parameter --region $SSM_REGION --name "$P/ALERT_EMAIL" --query Parameter.Value --output text)
else
  echo "Copy the database values from the Neon console (Connection details). Render was deleted on 27 Nov 2026."
  ALERT_EMAIL=$(ask "Your email for cost alerts")
  ADMIN_EMAIL=$(ask "ADMIN_EMAIL (your Google sign-in email)")
  DATABASE_URL=$(ask "DATABASE_URL (starts with jdbc:postgresql://)")
  DB_USER=$(ask "DB_USER")
  DB_PASSWORD=$(asks "DB_PASSWORD")
  JWT_SECRET=$(asks "JWT_SECRET (press Enter to make a new one; then sign in again once)")
  [ -z "$JWT_SECRET" ] && JWT_SECRET=$(openssl rand -base64 48 | tr -d '\n')
  case "$DATABASE_URL" in jdbc:postgresql://*) ;; *) echo "DATABASE_URL must start with jdbc:postgresql://"; exit 1;; esac
  put ALERT_EMAIL String "$ALERT_EMAIL"
  put ADMIN_EMAIL String "$ADMIN_EMAIL"
  put GOOGLE_CLIENT_ID String "705442845625-h9sd1n52qa7ejs1aidc24p93r3i3ji4d.apps.googleusercontent.com"
  put CONTENT_REQUIRES_APPROVAL String "true"
  put DATABASE_URL SecureString "$DATABASE_URL"
  put DB_USER SecureString "$DB_USER"
  put DB_PASSWORD SecureString "$DB_PASSWORD"
  put JWT_SECRET SecureString "$JWT_SECRET"
  TYPED=1
  echo "Secrets saved."
fi

# ---------- 2. role the API runs as ----------
LAMBDA_TRUST='{"Version":"2012-10-17","Statement":[{"Effect":"Allow","Principal":{"Service":"lambda.amazonaws.com"},"Action":"sts:AssumeRole"}]}'
aws iam create-role --role-name power-prep-api-role --assume-role-policy-document "$LAMBDA_TRUST" >/dev/null 2>&1 || true
aws iam attach-role-policy --role-name power-prep-api-role \
  --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
API_ROLE=arn:aws:iam::$ACCOUNT:role/power-prep-api-role

# logs: keep 7 days only (free allowance is 5 GB/month)
aws logs create-log-group --log-group-name /aws/lambda/$FN 2>/dev/null || true
aws logs put-retention-policy --log-group-name /aws/lambda/$FN --retention-in-days 7

# ---------- 3. GitHub Actions deploy role (OIDC, no stored keys) ----------
OIDC=arn:aws:iam::$ACCOUNT:oidc-provider/token.actions.githubusercontent.com
aws iam get-open-id-connect-provider --open-id-connect-provider-arn "$OIDC" >/dev/null 2>&1 || \
  aws iam create-open-id-connect-provider --url https://token.actions.githubusercontent.com \
    --client-id-list sts.amazonaws.com --thumbprint-list 6938fd4d98bab03faadb97b34396831e3780aea1 >/dev/null
GH_TRUST=$(cat <<EOF
{"Version":"2012-10-17","Statement":[{"Effect":"Allow","Principal":{"Federated":"$OIDC"},
 "Action":"sts:AssumeRoleWithWebIdentity",
 "Condition":{"StringEquals":{"token.actions.githubusercontent.com:aud":"sts.amazonaws.com",
                              "token.actions.githubusercontent.com:sub":["repo:$REPO:ref:refs/heads/main","repo:$REPO_IDS:ref:refs/heads/main"]}}}]}
EOF
)
aws iam create-role --role-name power-prep-deploy --assume-role-policy-document "$GH_TRUST" >/dev/null 2>&1 || \
  aws iam update-assume-role-policy --role-name power-prep-deploy --policy-document "$GH_TRUST"
DEPLOY_POLICY=$(cat <<EOF
{"Version":"2012-10-17","Statement":[
 {"Effect":"Allow","Action":["lambda:GetFunction","lambda:GetFunctionConfiguration","lambda:CreateFunction",
   "lambda:UpdateFunctionCode","lambda:UpdateFunctionConfiguration","lambda:PublishVersion","lambda:ListVersionsByFunction",
   "lambda:DeleteFunction","lambda:GetAlias","lambda:CreateAlias","lambda:UpdateAlias",
   "lambda:GetFunctionUrlConfig","lambda:CreateFunctionUrlConfig","lambda:UpdateFunctionUrlConfig",
   "lambda:AddPermission","lambda:GetPolicy","lambda:GetFunctionConcurrency","lambda:TagResource"],
  "Resource":["arn:aws:lambda:$REGION:$ACCOUNT:function:$FN","arn:aws:lambda:$REGION:$ACCOUNT:function:$FN:*"]},
 {"Effect":"Allow","Action":"iam:PassRole","Resource":"$API_ROLE"},
 {"Effect":"Allow","Action":["logs:FilterLogEvents","logs:DescribeLogStreams","logs:GetLogEvents"],"Resource":"arn:aws:logs:$REGION:$ACCOUNT:log-group:/aws/lambda/$FN:*"}]}
EOF
)
aws iam put-role-policy --role-name power-prep-deploy --policy-name deploy --policy-document "$DEPLOY_POLICY"

# ---------- 3b. the API function and its environment ----------
# GitHub Actions only uploads new code; the secrets are set here, from CloudShell.
if [ "${TYPED:-}" != "1" ]; then
  get() { aws ssm get-parameter --region $SSM_REGION --with-decryption --name "$P/$1" --query Parameter.Value --output text; }
  if ! DATABASE_URL=$(get DATABASE_URL); then
    echo "Could not read the saved secrets. Run again with:  RESET_SECRETS=1 bash setup.sh"; exit 1
  fi
  DB_USER=$(get DB_USER); DB_PASSWORD=$(get DB_PASSWORD); JWT_SECRET=$(get JWT_SECRET); ADMIN_EMAIL=$(get ADMIN_EMAIL)
fi
ENV_FILE=$(mktemp)
export DATABASE_URL DB_USER DB_PASSWORD JWT_SECRET ADMIN_EMAIL
python3 - "$ENV_FILE" <<'PY' 
import json, os, sys
v = {k: os.environ[k] for k in ["DATABASE_URL","DB_USER","DB_PASSWORD","JWT_SECRET","ADMIN_EMAIL"]}
v.update(GOOGLE_CLIENT_ID="705442845625-h9sd1n52qa7ejs1aidc24p93r3i3ji4d.apps.googleusercontent.com",
         CONTENT_REQUIRES_APPROVAL="true",
         JAVA_TOOL_OPTIONS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1")
json.dump({"Variables": v}, open(sys.argv[1], "w"))
PY
if aws lambda get-function --function-name $FN >/dev/null 2>&1; then
  aws lambda update-function-configuration --function-name $FN --environment "file://$ENV_FILE" >/dev/null
  echo "API environment updated."
else
  ZTMP=$(mktemp -d); echo "code comes from GitHub Actions" > "$ZTMP/README"
  (cd "$ZTMP" && python3 -c "import zipfile; z=zipfile.ZipFile('p.zip','w'); z.write('README'); z.close()")
  sleep 10   # a new IAM role takes a few seconds before Lambda can use it
  for i in 1 2 3 4 5; do
    aws lambda create-function --function-name $FN --runtime java21 --architectures arm64 \
      --handler com.rajdeep.powerprep.lambda.StreamLambdaHandler::handleRequest --memory-size 1536 --timeout 30 \
      --role "$API_ROLE" --snap-start ApplyOn=PublishedVersions --environment "file://$ENV_FILE" \
      --zip-file "fileb://$ZTMP/p.zip" >/dev/null && break
    echo "waiting for the new role..."; sleep 10
  done
  aws lambda get-function --function-name $FN >/dev/null || { echo "Could not create the API function (see the error above). Send it to Claude."; exit 1; }
  echo "API function created (code arrives with the first GitHub deploy)."
fi
rm -f "$ENV_FILE"

# ---------- 4. kill switch ----------
# 4a. alert topic: emails you, and calls the kill-switch function
TOPIC=$(aws sns create-topic --name power-prep-alerts --query TopicArn --output text)
TOPIC_POLICY=$(cat <<EOF
{"Version":"2012-10-17","Statement":[
 {"Sid":"owner","Effect":"Allow","Principal":{"AWS":"arn:aws:iam::$ACCOUNT:root"},
  "Action":["SNS:Publish","SNS:Subscribe","SNS:Receive","SNS:GetTopicAttributes","SNS:SetTopicAttributes","SNS:ListSubscriptionsByTopic","SNS:DeleteTopic","SNS:RemovePermission","SNS:AddPermission"],
  "Resource":"$TOPIC"},
 {"Sid":"alarms","Effect":"Allow","Principal":{"Service":"cloudwatch.amazonaws.com"},"Action":"SNS:Publish","Resource":"$TOPIC"},
 {"Sid":"budgets","Effect":"Allow","Principal":{"Service":"budgets.amazonaws.com"},"Action":"SNS:Publish","Resource":"$TOPIC"}]}
EOF
)
aws sns set-topic-attributes --topic-arn "$TOPIC" --attribute-name Policy --attribute-value "$TOPIC_POLICY"
if ! aws sns list-subscriptions-by-topic --topic-arn "$TOPIC" --query "Subscriptions[?Protocol=='email'].Endpoint" --output text | grep -qi "$ALERT_EMAIL"; then
  aws sns subscribe --topic-arn "$TOPIC" --protocol email --notification-endpoint "$ALERT_EMAIL" >/dev/null
  NEED_CONFIRM=1
fi

# 4b. kill-switch function: sets the API's concurrency to 0 (every request is refused, nothing is charged)
aws iam create-role --role-name power-prep-kill-role --assume-role-policy-document "$LAMBDA_TRUST" >/dev/null 2>&1 || true
aws iam attach-role-policy --role-name power-prep-kill-role \
  --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
aws iam put-role-policy --role-name power-prep-kill-role --policy-name kill --policy-document \
  "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Action\":[\"lambda:PutFunctionConcurrency\"],\"Resource\":\"arn:aws:lambda:$REGION:$ACCOUNT:function:$FN\"}]}"
TMP=$(mktemp -d)
cat > "$TMP/kill.py" <<'PY'
import os, boto3
lam = boto3.client("lambda")

def handler(event, context):
    target = os.environ["TARGET"]
    lam.put_function_concurrency(FunctionName=target, ReservedConcurrentExecutions=0)
    print(f"{target} switched off. Alert: {event}")
    return "switched off"
PY
(cd "$TMP" && python3 -c "import zipfile; z=zipfile.ZipFile('kill.zip','w'); z.write('kill.py'); z.close()")
KILL_ARN=arn:aws:lambda:$REGION:$ACCOUNT:function:power-prep-kill-switch
if aws lambda get-function --function-name power-prep-kill-switch >/dev/null 2>&1; then
  aws lambda update-function-code --function-name power-prep-kill-switch --zip-file "fileb://$TMP/kill.zip" >/dev/null
else
  sleep 10   # a new IAM role takes a few seconds before Lambda can use it
  for i in 1 2 3 4 5; do
    aws lambda create-function --function-name power-prep-kill-switch --runtime python3.12 --architectures arm64 \
      --handler kill.handler --role arn:aws:iam::$ACCOUNT:role/power-prep-kill-role --timeout 30 --memory-size 128 \
      --environment "Variables={TARGET=$FN}" --zip-file "fileb://$TMP/kill.zip" >/dev/null && break
    echo "waiting for the new role..."; sleep 10
  done
  aws lambda get-function --function-name power-prep-kill-switch >/dev/null || { echo "Could not create the kill switch (see the error above). Send it to Claude."; exit 1; }
fi
aws lambda add-permission --function-name power-prep-kill-switch --statement-id sns --action lambda:InvokeFunction \
  --principal sns.amazonaws.com --source-arn "$TOPIC" >/dev/null 2>&1 || true
aws sns list-subscriptions-by-topic --topic-arn "$TOPIC" --query "Subscriptions[?Protocol=='lambda'].Endpoint" --output text | grep -q "$KILL_ARN" || \
  aws sns subscribe --topic-arn "$TOPIC" --protocol lambda --notification-endpoint "$KILL_ARN" >/dev/null

# 4c. alarms (10 alarms are free). Normal use is ~50 requests in a busy 5 minutes.
alarm() { # name metric period threshold description
  aws cloudwatch put-metric-alarm --alarm-name "$1" --namespace AWS/Lambda --metric-name "$2" \
    --dimensions Name=FunctionName,Value=$FN --statistic Sum --period "$3" --evaluation-periods 1 \
    --threshold "$4" --comparison-operator GreaterThanThreshold --treat-missing-data notBreaching \
    --alarm-description "$5" --alarm-actions "$TOPIC"
}
alarm power-prep-requests-5min Invocations 300   500     "More than 500 API requests in 5 minutes: API switched off"
alarm power-prep-compute-5min  Duration    300   150000  "More than 150 s of API run time in 5 minutes: API switched off"
alarm power-prep-requests-day  Invocations 86400 30000   "More than 30,000 API requests in a day: API switched off"
alarm power-prep-compute-day   Duration    86400 8000000 "More than 8,000 s of API run time in a day: API switched off"

# 4d. budget: if the month's bill (before credits) passes $0.50, email + switch off
BUDGET=$(cat <<EOF
{"BudgetName":"power-prep-50-cents","BudgetLimit":{"Amount":"0.5","Unit":"USD"},"TimeUnit":"MONTHLY","BudgetType":"COST",
 "CostTypes":{"IncludeCredit":false,"IncludeRefund":false}}
EOF
)
NOTIF='{"NotificationType":"ACTUAL","ComparisonOperator":"GREATER_THAN","Threshold":100,"ThresholdType":"PERCENTAGE"}'
if aws budgets describe-budget --account-id "$ACCOUNT" --budget-name power-prep-50-cents >/dev/null 2>&1; then
  aws budgets update-budget --account-id "$ACCOUNT" --new-budget "$BUDGET"
else
  aws budgets create-budget --account-id "$ACCOUNT" --budget "$BUDGET" --notifications-with-subscribers \
    "[{\"Notification\":$NOTIF,\"Subscribers\":[{\"SubscriptionType\":\"SNS\",\"Address\":\"$TOPIC\"},{\"SubscriptionType\":\"EMAIL\",\"Address\":\"$ALERT_EMAIL\"}]}]"
fi

echo
echo "================ DONE ================"
[ "${NEED_CONFIRM:-}" = "1" ] && echo "1. Open your email ($ALERT_EMAIL) and click 'Confirm subscription' in the mail from AWS Notifications."
echo "2. Tell Claude:  setup done, account $ACCOUNT"
echo
echo "If the API is ever switched off, switch it back on with:"
echo "   aws lambda delete-function-concurrency --function-name $FN --region $REGION"
