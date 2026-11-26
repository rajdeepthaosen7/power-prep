package com.rajdeep.powerprep.common.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Turns {@link ApiException}s into JSON errors with a readable message. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handle(ApiException e) {
        return ResponseEntity.status(e.status())
                .body(new ApiError(e.status().value(), e.status().getReasonPhrase(), e.getMessage()));
    }
}
