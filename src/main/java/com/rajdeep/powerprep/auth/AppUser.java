package com.rajdeep.powerprep.auth;

/** Row of app_user, as used by auth and admin code. */
public record AppUser(long id, String email, String name, String avatarUrl,
                      String role, String status) {
    public boolean isAdmin()    { return "ADMIN".equals(role); }
    public boolean isApproved() { return "APPROVED".equals(status); }
}
