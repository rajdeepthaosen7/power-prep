package com.rajdeep.powerprep.auth;

/** Verifies the ID token the browser got from the identity provider. */
public interface IdentityTokenVerifier {
    /** @throws InvalidIdentityTokenException when the token is not valid */
    IdentityToken verify(String idToken);

    class InvalidIdentityTokenException extends RuntimeException {
        public InvalidIdentityTokenException(Throwable cause) { super(cause); }
    }
}
