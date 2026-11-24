-- V4: simple roles (USER / ADMIN) and Google profile picture
ALTER TABLE app_user DROP CONSTRAINT app_user_role_check;
UPDATE app_user SET role = 'USER' WHERE role = 'STUDENT';
ALTER TABLE app_user ALTER COLUMN role SET DEFAULT 'USER';
ALTER TABLE app_user ADD CONSTRAINT app_user_role_check CHECK (role IN ('USER','ADMIN'));
ALTER TABLE app_user ADD COLUMN avatar_url TEXT;
CREATE INDEX idx_app_user_status ON app_user(status, created_at);
