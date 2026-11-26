package com.rajdeep.powerprep.common.error;

/** Error body. The frontend shows {@code message} to the user. */
public record ApiError(int status, String error, String message) {}
