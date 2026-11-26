package com.rajdeep.powerprep.auth;

/** The signed-in user as the app shows it. */
public record UserView(long id, String email, String name, String avatarUrl, String role, String status) {
    public static UserView of(AppUser u) {
        return new UserView(u.id(), u.email(), u.name() == null ? "" : u.name(),
                u.avatarUrl() == null ? "" : u.avatarUrl(), u.role(), u.status());
    }
}
