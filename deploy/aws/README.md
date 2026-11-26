# API on AWS Lambda

The same Spring Boot app runs on Lambda in Sydney (`ap-southeast-2`). The AWS Free plan blocks Lambda in Singapore (only Sydney, us-east-1 and us-west-2 are allowed), so it is not next to Neon (Singapore). Secrets are in SSM in Sydney too.

| Piece | What |
|---|---|
| Build | `mvn -Plambda package` → `target/power-prep-lambda.zip`. The profile adds the Lambda adapter and `src/lambda/java`. The normal build (Render jar) is unchanged. |
| Entry point | `com.rajdeep.powerprep.lambda.StreamLambdaHandler` (Function URL, payload 2.0), Spring profile `lambda` |
| Start-up | SnapStart on published versions: Spring + Flyway run once at deploy; cold starts restore the snapshot (~1–1.5 s). `DbSnapshotHook` closes DB connections before the snapshot. |
| Gzip | `GzipFilter` (no Tomcat on Lambda, so `server.compression` does nothing) |
| Deploy | GitHub Actions `deploy-lambda.yml` on push to main → `deploy/aws/deploy.sh` (OIDC role `power-prep-deploy`, no AWS keys in GitHub). A version that fails to start is never made live. |
| Secrets | Typed once into `deploy/aws/setup.sh` in CloudShell, kept in SSM `/power-prep/*`; setup.sh sets them on the function. GitHub never sees them (the Free-plan account blocks SSM reads from the deploy role anyway). |

## Cost kill switch

Lambda's free allowance (1M requests, 400,000 GB-s a month) is far above normal use (~30k requests, ~10k GB-s).
If usage jumps, the alert topic `power-prep-alerts` emails the owner **and** runs `power-prep-kill-switch`,
which sets the API's concurrency to 0 (every request refused, nothing charged).

| Trigger | Limit |
|---|---|
| Requests in 5 min | > 500 |
| Run time in 5 min | > 150 s |
| Requests in a day | > 30,000 |
| Run time in a day | > 8,000 s |
| Monthly bill before credits | > $0.50 |

Switch the API back on (CloudShell):

    aws lambda delete-function-concurrency --function-name power-prep-api --region ap-southeast-2

First deploy: 26 Nov 2026 (Sydney, ap-southeast-2).
