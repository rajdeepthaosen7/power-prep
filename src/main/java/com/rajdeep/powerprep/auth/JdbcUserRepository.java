package com.rajdeep.powerprep.auth;

import com.rajdeep.powerprep.common.jdbc.Rows;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
class JdbcUserRepository implements UserRepository {
    private static final String COLS = "id, email, name, avatar_url, role, status";
    private final JdbcClient jdbc;

    JdbcUserRepository(JdbcClient jdbc) { this.jdbc = jdbc; }

    @Override
    public Optional<AppUser> findById(long id) {
        return jdbc.sql("SELECT " + COLS + " FROM app_user WHERE id = :id").param("id", id).query(Rows.of(AppUser.class)).optional();
    }

    @Override
    public AppUser upsertSignedIn(IdentityToken who, boolean admin) {
        return jdbc.sql("""
                INSERT INTO app_user (email, name, avatar_url, auth_provider, provider_subject, email_verified,
                                      role, status, approved_at, last_login_at)
                VALUES (:email, :name, :avatar, 'GOOGLE', :sub, TRUE, :role, :status, CASE WHEN :admin THEN now() END, now())
                ON CONFLICT ((lower(email))) DO UPDATE SET
                    name             = EXCLUDED.name,
                    avatar_url       = EXCLUDED.avatar_url,
                    auth_provider    = 'GOOGLE',
                    provider_subject = EXCLUDED.provider_subject,
                    email_verified   = TRUE,
                    last_login_at    = now(),
                    role        = CASE WHEN :admin THEN 'ADMIN' ELSE app_user.role END,
                    status      = CASE WHEN :admin THEN 'APPROVED' ELSE app_user.status END,
                    approved_at = CASE WHEN :admin AND app_user.approved_at IS NULL THEN now() ELSE app_user.approved_at END
                RETURNING """ + " " + COLS)
            .param("email", who.email()).param("name", who.name()).param("avatar", who.picture()).param("sub", who.subject())
            .param("role", (admin ? Role.ADMIN : Role.USER).name())
            .param("status", (admin ? UserStatus.APPROVED : UserStatus.PENDING).name())
            .param("admin", admin)
            .query(Rows.of(AppUser.class)).single();
    }

    @Override
    public List<AdminUserView> list(UserStatus status) {
        String sql = "SELECT id, email, name, avatar_url, role, status, created_at, approved_at, last_login_at FROM app_user"
                + (status == null ? "" : " WHERE status = :status") + " ORDER BY created_at DESC";
        JdbcClient.StatementSpec q = jdbc.sql(sql);
        if (status != null) q = q.param("status", status.name());
        return q.query(Rows.of(AdminUserView.class)).list();
    }

    @Override
    public boolean setStatus(long id, UserStatus status, long adminId) {
        return jdbc.sql("""
                UPDATE app_user SET status = :status,
                       approved_at = CASE WHEN :status = 'APPROVED' THEN now() ELSE approved_at END,
                       approved_by = CASE WHEN :status = 'APPROVED' THEN :admin ELSE approved_by END
                WHERE id = :id""")
            .param("status", status.name()).param("admin", adminId).param("id", id).update() > 0;
    }

    @Override
    public boolean setRole(long id, Role role) {
        return jdbc.sql("UPDATE app_user SET role = :role WHERE id = :id").param("role", role.name()).param("id", id).update() > 0;
    }
}
