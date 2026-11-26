package com.rajdeep.powerprep.lambda;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * Lambda has no Tomcat, so server.compression does nothing there.
 * Chapter pages carry notes and SVG figures (about 370 KB of JSON); gzip makes them about 5x smaller.
 */
@Component
@Profile("lambda")
@Order(Ordered.HIGHEST_PRECEDENCE)
class GzipFilter extends OncePerRequestFilter {
    private static final int MIN_BYTES = 2048;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String accept = req.getHeader("Accept-Encoding");
        if (accept == null || !accept.toLowerCase().contains("gzip")) {
            chain.doFilter(req, res);
            return;
        }
        ContentCachingResponseWrapper wrapper = new ContentCachingResponseWrapper(res);
        chain.doFilter(req, wrapper);
        byte[] body = wrapper.getContentAsByteArray();
        String type = wrapper.getContentType();
        boolean text = type != null && (type.startsWith("application/json") || type.startsWith("text/"));
        if (body.length < MIN_BYTES || !text || wrapper.getHeader("Content-Encoding") != null) {
            wrapper.copyBodyToResponse();
            return;
        }
        ByteArrayOutputStream buf = new ByteArrayOutputStream(body.length / 4);
        try (GZIPOutputStream gz = new GZIPOutputStream(buf)) {
            gz.write(body);
        }
        wrapper.resetBuffer();
        res.setHeader("Content-Encoding", "gzip");
        res.addHeader("Vary", "Accept-Encoding");
        res.setContentLength(buf.size());
        res.getOutputStream().write(buf.toByteArray());
    }
}
