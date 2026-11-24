package com.rajdeep.powerprep.auth;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private static final String COLS = "id, email, name, avatar_url, role, status";
    private static final RowMapper<AppUser> MAPPER = (rs, i) -> new AppUser(
            rs.getLong("id"), rs.getString("email"), rs.getString("name"),
            rs.getString("avatar_url"), rs.getString("role"), rs.getString("status"));

    private final JdbcTemplate jdbc;
    public UserRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public Optional<AppUser> findById(long id) {
        return jdbc.query("SELECT " + COLS + " FROM app_user WHERE id = ?", MAPPER, id).stream().findFirst();
    }

    /**
     * Create or update the user who signed in with Google.
     * The admin email is always ADMIN + APPROVED; everyone else starts as USER + PENDING.
     */
    public AppUser upsertGoogleUser(String sub, String email, String name, String avatarUrl, boolean isAdminEmail) {
        return jdbc.queryForObject("""
            INSERT INTO app_user (email, name, avatar_url, auth_provider, provider_subject, email_verified,
                                  role, status, approved_at, last_login_at)
            VALUES (?, ?, ?, 'GOOGLE', ?, TRUE, ?, ?, CASE WHEN ? THEN now() END, now())
            ON CONFLICT ((lower(email))) DO UPDATE SET
                name             = EXCLUDED.name,
                avatar_url       = EXCLUDED.avatar_url,
                auth_provider    = 'GOOGLE',
                provider_subject = EXCLUDED.provider_subject,
                email_verified   = TRUE,
                last_login_at    = now(),
                role        = CASE WHEN ? THEN 'ADMIN' ELSE app_user.role END,
                status      = CASE WHEN ? THEN 'APPROVED' ELSE app_user.status END,
                approved_at = CASE WHEN ? AND app_user.approved_at IS NULL THEN now() ELSE app_user.approved_at END
            RETURNING """ + " " + COLS,
            MAPPER,
            email, name, avatarUrl, sub,
            isAdminEmail ? "ADMIN" : "USER", isAdminEmail ? "APPROVED" : "PENDING", isAdminEmail,
            isAdminEmail, isAdminEmail, isAdminEmail);
    }

    public List<java.util.Map<String, Object>> list(String status) {
        String where = status == null ? "" : " WHERE status = ?";
        String sql = """
            SELECT id, email, name, avatar_url AS "avatarUrl", role, status,
                   created_at AS "createdAt", approved_at AS "approvedAt", last_login_at AS "lastLoginAt"
            FROM app_user""" + where + " ORDER BY created_at DESC";
        return status == null ? jdbc.queryForList(sql) : jdbc.queryForList(sql, status);
    }

    public int setStatus(long id, String status, long adminId) {
        return jdbc.update("""
            UPDATE app_user SET status = ?,
                   approved_at = CASE WHEN ? = 'APPROVED' THEN now() ELSE approved_at END,
                   approved_by = CASE WHEN ? = 'APPROVED' THEN ? ELSE approved_by END
            WHERE id = ?""", status, status, status, adminId, id);
    }

    public int setRole(long id, String role) {
        return jdbc.update("UPDATE app_user SET role = ? WHERE id = ?", role, id);
    }
}
