package com.rajdeep.powerprep.common.error;

import org.springframework.http.HttpStatus;

/** A failure the client caused or must know about. The message is safe to show to the user. */
public abstract class ApiException extends RuntimeException {
    private final HttpStatus status;

    protected ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus status() { return status; }
}
