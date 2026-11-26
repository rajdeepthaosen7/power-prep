package com.rajdeep.powerprep.auth;

import com.rajdeep.powerprep.common.error.BadRequestException;
import com.rajdeep.powerprep.common.error.NotFoundException;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

/** Admin section: approve, reject or disable users, and change roles. An admin cannot change their own account. */
@Service
public class AdminUserService {
    private final UserRepository users;

    public AdminUserService(UserRepository users) { this.users = users; }

    public List<AdminUserView> list(String status) {
        return users.list(status == null ? null : parse(UserStatus.class, status, "unknown status"));
    }

    public UserView setStatus(long adminId, long userId, String status) {
        UserStatus s = parse(UserStatus.class, status, "status must be APPROVED, REJECTED or DISABLED");
        if (s == UserStatus.PENDING) throw new BadRequestException("status must be APPROVED, REJECTED or DISABLED");
        if (userId == adminId) throw new BadRequestException("you cannot change your own status");
        if (!users.setStatus(userId, s, adminId)) throw new NotFoundException("user not found");
        return view(userId);
    }

    public UserView setRole(long adminId, long userId, String role) {
        Role r = parse(Role.class, role, "role must be USER or ADMIN");
        if (userId == adminId) throw new BadRequestException("you cannot change your own role");
        if (!users.setRole(userId, r)) throw new NotFoundException("user not found");
        return view(userId);
    }

    private UserView view(long userId) {
        return users.findById(userId).map(UserView::of).orElseThrow(() -> new NotFoundException("user not found"));
    }

    private static <E extends Enum<E>> E parse(Class<E> type, String value, String message) {
        try {
            return Enum.valueOf(type, value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BadRequestException(message);
        }
    }
}
