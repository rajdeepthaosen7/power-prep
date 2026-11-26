#!/usr/bin/env bash
# Deploys target/power-prep-lambda.zip (build: mvn -Plambda package). Run by GitHub Actions.
# Publishing a SnapStart version starts the app once (Flyway migrations run then). If start-up fails,
# the publish fails and the live alias keeps pointing at the old version: users never see a broken API.
set -euo pipefail
REGION=${AWS_REGION:-ap-southeast-2}
FN=power-prep-api
P=/power-prep
ZIP=target/power-prep-lambda.zip
export AWS_DEFAULT_REGION=$REGION AWS_REGION=$REGION AWS_PAGER=""   # CloudShell sets AWS_REGION, which wins over AWS_DEFAULT_REGION
ACCOUNT=$(aws sts get-caller-identity --query Account --output text)
ROLE=arn:aws:iam::$ACCOUNT:role/power-prep-api-role

COMMON=(--runtime java21 --handler com.rajdeep.powerprep.lambda.StreamLambdaHandler::handleRequest
        --memory-size 1536 --timeout 30 --role "$ROLE" --snap-start ApplyOn=PublishedVersions
)
# the environment (secrets) is set by deploy/aws/setup.sh from CloudShell and left as it is here

if aws lambda get-function --function-name $FN >/dev/null; then
  echo "Updating $FN"
  aws lambda update-function-configuration --function-name $FN "${COMMON[@]}" >/dev/null
  aws lambda wait function-updated-v2 --function-name $FN
  aws lambda update-function-code --function-name $FN --zip-file "fileb://$ZIP" --architectures arm64 >/dev/null
  aws lambda wait function-updated-v2 --function-name $FN
else
  echo "Function $FN not found: run deploy/aws/setup.sh in CloudShell first"; exit 1
fi

echo "Publishing a version (starts the app once and takes the SnapStart snapshot)..."
VERSION=$(aws lambda publish-version --function-name $FN --description "${GITHUB_SHA:-manual}" --query Version --output text)
if ! aws lambda wait published-version-active --function-name "$FN:$VERSION"; then
  aws lambda get-function-configuration --function-name "$FN:$VERSION" --query '[State,StateReason]' --output text
  echo "Version $VERSION failed to start; the live API was not changed."
  exit 1
fi
echo "Version $VERSION is ready"

if aws lambda get-alias --function-name $FN --name live >/dev/null 2>&1; then
  aws lambda update-alias --function-name $FN --name live --function-version "$VERSION" >/dev/null
else
  aws lambda create-alias --function-name $FN --name live --function-version "$VERSION" >/dev/null
fi

# public HTTPS address on the live alias (CORS is handled by Spring)
if ! URL=$(aws lambda get-function-url-config --function-name $FN --qualifier live --query FunctionUrl --output text 2>/dev/null); then
  URL=$(aws lambda create-function-url-config --function-name $FN --qualifier live --auth-type NONE --query FunctionUrl --output text)
  aws lambda add-permission --function-name $FN --qualifier live --statement-id url-public \
    --action lambda:InvokeFunctionUrl --principal "*" --function-url-auth-type NONE >/dev/null
  aws lambda add-permission --function-name $FN --qualifier live --statement-id url-invoke \
    --action lambda:InvokeFunction --principal "*" --invoked-via-function-url >/dev/null 2>&1 || true
fi
echo "API address: $URL"

if [ "$(aws lambda get-function-concurrency --function-name $FN --query ReservedConcurrentExecutions --output text)" = "0" ]; then
  echo "The kill switch has the API switched off (concurrency 0). New version is deployed but not reachable."
  echo "Switch on: aws lambda delete-function-concurrency --function-name $FN --region $REGION"
  exit 0
fi

echo "Smoke test"
for i in 1 2 3 4 5 6; do
  CODE=$(curl -s -o /tmp/health.json -w "%{http_code}" "${URL%/}/actuator/health" || true)
  [ "$CODE" = "200" ] && break
  sleep 5
done
cat /tmp/health.json; echo
[ "$CODE" = "200" ] || { echo "Health check failed ($CODE)"; exit 1; }
CODE=$(curl -s -o /dev/null -w "%{http_code}" "${URL%/}/api/subjects")
echo "/api/subjects -> $CODE"
[ "$CODE" = "200" ] || exit 1

