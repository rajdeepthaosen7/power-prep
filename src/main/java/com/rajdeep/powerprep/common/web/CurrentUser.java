package com.rajdeep.powerprep.common.web;

import com.rajdeep.powerprep.common.error.UnauthorizedException;

/**
 * The signed-in user of a request, or anonymous. Controllers take it as a parameter
 * (filled by {@link CurrentUserArgumentResolver}) instead of reading the security context themselves.
 */
public record CurrentUser(Long id) {
    public static final CurrentUser ANONYMOUS = new CurrentUser(null);

    public boolean isSignedIn() { return id != null; }

    /** The user id; throws 401 for anonymous requests. */
    public long requireId() {
        if (id == null) throw new UnauthorizedException("sign in first");
        return id;
    }

    /** The user id, or {@code fallback} for anonymous requests (for queries that count "my" rows). */
    public long idOr(long fallback) { return id == null ? fallback : id; }
}
