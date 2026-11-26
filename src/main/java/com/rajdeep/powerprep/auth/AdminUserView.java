package com.rajdeep.powerprep.auth;

import java.time.Instant;

/** A user in the admin list. */
public record AdminUserView(long id, String email, String name, String avatarUrl, String role, String status,
                            Instant createdAt, Instant approvedAt, Instant lastLoginAt) {}
