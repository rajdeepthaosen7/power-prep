package com.rajdeep.powerprep.lambda;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.rajdeep.powerprep.PowerPrepApplication;
import org.crac.Core;
import org.crac.Resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * AWS Lambda entry point (Function URL, payload format 2.0).
 * The whole Spring app starts once in the init phase. With SnapStart that happens when a
 * version is published (so Flyway migrations run at deploy time), and later cold starts
 * restore the ready snapshot instead of starting Spring again.
 */
public class StreamLambdaHandler implements RequestStreamHandler, Resource {

    private static final SpringBootLambdaContainerHandler<HttpApiV2ProxyRequest, AwsProxyResponse> HANDLER;

    static {
        try {
            HANDLER = SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler(PowerPrepApplication.class, "lambda");
        } catch (ContainerInitializationException e) {
            throw new IllegalStateException("Spring app did not start", e);
        }
    }

    public StreamLambdaHandler() {
        Core.getGlobalContext().register(this);
    }

    @Override
    public void handleRequest(InputStream in, OutputStream out, Context context) throws IOException {
        HANDLER.proxyStream(in, out, context);
    }

    /** Before the snapshot: warm the request path (DbSnapshotHook then closes the DB connections). */
    @Override
    public void beforeCheckpoint(org.crac.Context<? extends Resource> ctx) {
        for (String path : new String[]{"/actuator/health", "/api/subjects"}) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                HANDLER.proxyStream(new ByteArrayInputStream(event(path).getBytes(StandardCharsets.UTF_8)), out, new WarmupContext());
            } catch (Exception ignored) {
                // warm-up only
            }
        }
    }

    @Override
    public void afterRestore(org.crac.Context<? extends Resource> ctx) {
        // nothing: the pool opens new connections on the first request
    }

    static String event(String path) {
        return """
            {"version":"2.0","routeKey":"$default","rawPath":"%s","rawQueryString":"",
             "headers":{"host":"localhost","accept":"application/json"},
             "requestContext":{"accountId":"anonymous","apiId":"warmup","domainName":"localhost","domainPrefix":"localhost",
               "http":{"method":"GET","path":"%s","protocol":"HTTP/1.1","sourceIp":"127.0.0.1","userAgent":"warmup"},
               "requestId":"warmup","routeKey":"$default","stage":"$default","time":"01/Jan/2026:00:00:00 +0000","timeEpoch":0},
             "isBase64Encoded":false}
            """.formatted(path, path);
    }
}
