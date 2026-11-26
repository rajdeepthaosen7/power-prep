package com.rajdeep.powerprep.lambda;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

/** Minimal Lambda context for the warm-up requests sent before the SnapStart snapshot. */
class WarmupContext implements Context {
    public String getAwsRequestId() { return "warmup"; }
    public String getLogGroupName() { return "warmup"; }
    public String getLogStreamName() { return "warmup"; }
    public String getFunctionName() { return "warmup"; }
    public String getFunctionVersion() { return "warmup"; }
    public String getInvokedFunctionArn() { return "warmup"; }
    public CognitoIdentity getIdentity() { return null; }
    public ClientContext getClientContext() { return null; }
    public int getRemainingTimeInMillis() { return 30_000; }
    public int getMemoryLimitInMB() { return 1536; }
    public LambdaLogger getLogger() {
        return new LambdaLogger() {
            public void log(String message) { }
            public void log(byte[] message) { }
        };
    }
}
