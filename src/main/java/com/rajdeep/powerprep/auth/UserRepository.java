package com.rajdeep.powerprep.auth;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<AppUser> findById(long id);

    /** Creates or updates the user who signed in. The admin email is always ADMIN + APPROVED; others start USER + PENDING. */
    AppUser upsertSignedIn(IdentityToken identity, boolean isAdminEmail);

    /** All users, newest first; {@code status} null = every status. */
    List<AdminUserView> list(UserStatus status);

    /** @return false if no such user */
    boolean setStatus(long id, UserStatus status, long adminId);

    /** @return false if no such user */
    boolean setRole(long id, Role role);
}
