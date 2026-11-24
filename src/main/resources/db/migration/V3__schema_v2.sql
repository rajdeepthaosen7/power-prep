-- =====================================================================
-- V3: schema v2
--   * audit columns (created_at / updated_at) on every main table
--   * a question can now have many sources (question_source)
--   * options, figures, concept notes, syllabus, tests, todo, reviews
--   * users: approval status, Google/email login
-- Rule: never edit an applied migration. Add a new V<n>__*.sql instead.
-- =====================================================================

-- ---------- helper: keep updated_at fresh ----------
CREATE OR REPLACE FUNCTION set_updated_at() RETURNS trigger AS $$
BEGIN
  NEW.updated_at = now();
  RETURN NEW;
END
$$ LANGUAGE plpgsql;

-- ---------- audit columns on existing tables ----------
ALTER TABLE subject  ADD COLUMN created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                     ADD COLUMN updated_at TIMESTAMPTZ NOT NULL DEFAULT now();
ALTER TABLE chapter  ADD COLUMN description TEXT,
                     ADD COLUMN created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                     ADD COLUMN updated_at TIMESTAMPTZ NOT NULL DEFAULT now();
ALTER TABLE topic    ADD COLUMN description TEXT,
                     ADD COLUMN sort_order INT NOT NULL DEFAULT 0,
                     ADD COLUMN created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                     ADD COLUMN updated_at TIMESTAMPTZ NOT NULL DEFAULT now();
CREATE INDEX idx_topic_chapter ON topic(chapter_id);
CREATE INDEX idx_chapter_subject ON chapter(subject_id);

-- ---------- source = one exam paper ----------
ALTER TABLE source DROP CONSTRAINT source_kind_check;
ALTER TABLE source ADD CONSTRAINT source_kind_check CHECK (kind IN ('STATE','GATE','ESE','BANK'));
ALTER TABLE source
  ADD COLUMN title           TEXT,
  ADD COLUMN set_label       TEXT,                 -- booklet series / GATE set
  ADD COLUMN total_questions INT,
  ADD COLUMN full_marks      NUMERIC(6,2),
  ADD COLUMN duration_min    INT,
  ADD COLUMN marks_correct   NUMERIC(4,2),         -- null when marks differ per question (GATE)
  ADD COLUMN marks_wrong     NUMERIC(5,3),         -- negative marks per wrong answer
  ADD COLUMN drive_file_id   TEXT,                 -- original paper in the Drive dump
  ADD COLUMN notes           TEXT,
  ADD COLUMN created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
  ADD COLUMN updated_at      TIMESTAMPTZ NOT NULL DEFAULT now();

-- Facts from the 2023 test booklets
UPDATE source SET title='APDCL Assistant Manager (Electrical) Paper-I, 10.09.2023', set_label='B',
  total_questions=100, full_marks=150, duration_min=120, marks_correct=1.5, marks_wrong=0.375,
  drive_file_id='1GeG0jngI35JIql6Ek0nXkZL4JtYg69w2' WHERE code='APDCL-2023';
UPDATE source SET title='APGCL Assistant Manager (Electrical) Paper-I, 15.10.2023', set_label='A',
  total_questions=100, full_marks=100, duration_min=120, marks_correct=1, marks_wrong=0.25,
  drive_file_id='1B4eeDMfTYqjDpLemtCIHOuZQ0Of5W_jL' WHERE code='APGCL-2023';
UPDATE source SET title='AEGCL Assistant Manager (Electrical) Paper-II, 18.11.2023', set_label='D',
  total_questions=100, full_marks=100, duration_min=120, marks_correct=1, marks_wrong=0.25,
  drive_file_id='1cEh_lv54_evOfKzl5RB4lco2LqBHHcIp' WHERE code='AEGCL-2023';
UPDATE source SET title='GATE EE ' || year, total_questions=65, full_marks=100, duration_min=180
  WHERE kind='GATE';

-- ---------- question ----------
-- Guard: the columns we drop must be empty (they were never filled).
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM question WHERE options IS NOT NULL OR answer IS NOT NULL) THEN
    RAISE EXCEPTION 'question.options/answer contain data; migrate it before dropping';
  END IF;
END $$;

-- Many sources per question
CREATE TABLE question_source (
  source_id   INT  NOT NULL REFERENCES source(id),
  q_no        TEXT NOT NULL,                       -- "8", "S1-Q1"
  question_id INT  NOT NULL REFERENCES question(id) ON DELETE CASCADE,
  marks       NUMERIC(4,2),
  is_primary  BOOLEAN NOT NULL DEFAULT TRUE,
  source_url  TEXT,                                -- set for questions taken from the web
  created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
  PRIMARY KEY (source_id, q_no)
);
CREATE INDEX idx_qsource_question ON question_source(question_id);

INSERT INTO question_source (source_id, q_no, question_id, marks, is_primary)
SELECT source_id, q_no, id, marks, TRUE FROM question;

ALTER TABLE question DROP CONSTRAINT question_source_id_q_no_key;
ALTER TABLE question DROP COLUMN source_id, DROP COLUMN q_no, DROP COLUMN marks,
                     DROP COLUMN options,   DROP COLUMN answer;
ALTER TABLE question RENAME COLUMN q_type      TO nature;       -- Numerical / Theory
ALTER TABLE question RENAME COLUMN body        TO stem_md;      -- full question text (Markdown + LaTeX)
ALTER TABLE question RENAME COLUMN explanation TO solution_md;  -- full solution
UPDATE question SET nature = upper(nature) WHERE nature IS NOT NULL;
ALTER TABLE question ADD CONSTRAINT question_nature_check CHECK (nature IN ('NUMERICAL','THEORY'));

ALTER TABLE question DROP CONSTRAINT question_status_check;
ALTER TABLE question ADD CONSTRAINT question_status_check
  CHECK (status IN ('TAGGED','DRAFT','READY','RETIRED'));

ALTER TABLE question
  ADD COLUMN answer_type   TEXT NOT NULL DEFAULT 'MCQ' CHECK (answer_type IN ('MCQ','MSQ','NAT')),
  ADD COLUMN nat_min       NUMERIC,                -- NAT answer range
  ADD COLUMN nat_max       NUMERIC,
  ADD COLUMN difficulty    SMALLINT CHECK (difficulty BETWEEN 1 AND 3),
  ADD COLUMN answer_status TEXT NOT NULL DEFAULT 'UNVERIFIED'
        CHECK (answer_status IN ('OFFICIAL_KEY','TWO_SOURCES','REVIEWED','UNVERIFIED')),
  ADD COLUMN created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  ADD COLUMN updated_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  ADD CONSTRAINT question_nat_range_check CHECK (nat_min IS NULL OR nat_max IS NULL OR nat_min <= nat_max);

CREATE TABLE question_option (
  id          SERIAL PRIMARY KEY,
  question_id INT  NOT NULL REFERENCES question(id) ON DELETE CASCADE,
  label       TEXT NOT NULL CHECK (label IN ('A','B','C','D','E')),
  body_md     TEXT NOT NULL,
  is_correct  BOOLEAN NOT NULL DEFAULT FALSE,
  UNIQUE (question_id, label)
);

ALTER TABLE question_link ADD CONSTRAINT question_link_distinct_check
  CHECK (gate_question_id <> state_question_id);

-- ---------- concept notes ----------
CREATE TABLE concept_section (
  id         SERIAL PRIMARY KEY,
  topic_id   INT  NOT NULL REFERENCES topic(id),
  sort_order INT  NOT NULL DEFAULT 0,
  title      TEXT NOT NULL,
  body_md    TEXT NOT NULL,                        -- Markdown + LaTeX
  status     TEXT NOT NULL DEFAULT 'DRAFT' CHECK (status IN ('DRAFT','PUBLISHED')),
  version    INT  NOT NULL DEFAULT 1,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_concept_topic ON concept_section(topic_id, sort_order);

-- which concept section a question uses
CREATE TABLE question_concept (
  question_id        INT NOT NULL REFERENCES question(id) ON DELETE CASCADE,
  concept_section_id INT NOT NULL REFERENCES concept_section(id) ON DELETE CASCADE,
  PRIMARY KEY (question_id, concept_section_id)
);

-- figures for questions or concept sections (files live in object storage)
CREATE TABLE figure (
  id                 SERIAL PRIMARY KEY,
  question_id        INT REFERENCES question(id) ON DELETE CASCADE,
  concept_section_id INT REFERENCES concept_section(id) ON DELETE CASCADE,
  storage_key        TEXT NOT NULL UNIQUE,
  caption            TEXT,
  alt_text           TEXT,
  sort_order         INT NOT NULL DEFAULT 0,
  created_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
  CHECK (num_nonnulls(question_id, concept_section_id) = 1)
);

-- ---------- syllabus ----------
CREATE TABLE syllabus_item (
  id         SERIAL PRIMARY KEY,
  exam_code  TEXT NOT NULL,                        -- e.g. APGCL-AM-EE-2026
  code       TEXT NOT NULL,                        -- e.g. "1", "1.3"
  parent_id  INT REFERENCES syllabus_item(id),
  text       TEXT NOT NULL,
  sort_order INT NOT NULL DEFAULT 0,
  UNIQUE (exam_code, code)
);
CREATE TABLE syllabus_item_topic (
  syllabus_item_id INT NOT NULL REFERENCES syllabus_item(id) ON DELETE CASCADE,
  topic_id         INT NOT NULL REFERENCES topic(id) ON DELETE CASCADE,
  PRIMARY KEY (syllabus_item_id, topic_id)
);

-- ---------- users ----------
ALTER TABLE app_user ALTER COLUMN password_hash DROP NOT NULL;   -- Google users have no password
ALTER TABLE app_user DROP CONSTRAINT app_user_email_key;
CREATE UNIQUE INDEX ux_app_user_email ON app_user (lower(email));
ALTER TABLE app_user
  ADD COLUMN status           TEXT NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING','APPROVED','REJECTED','DISABLED')),
  ADD COLUMN auth_provider    TEXT NOT NULL DEFAULT 'EMAIL' CHECK (auth_provider IN ('EMAIL','GOOGLE')),
  ADD COLUMN provider_subject TEXT,                -- Google "sub"
  ADD COLUMN email_verified   BOOLEAN NOT NULL DEFAULT FALSE,
  ADD COLUMN approved_at      TIMESTAMPTZ,
  ADD COLUMN approved_by      BIGINT REFERENCES app_user(id),
  ADD COLUMN last_login_at    TIMESTAMPTZ,
  ADD COLUMN updated_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
  ADD CONSTRAINT app_user_password_check
        CHECK (auth_provider <> 'EMAIL' OR password_hash IS NOT NULL);
CREATE UNIQUE INDEX ux_app_user_provider ON app_user (auth_provider, provider_subject)
  WHERE provider_subject IS NOT NULL;

-- ---------- tests ----------
CREATE TABLE test (
  id            SERIAL PRIMARY KEY,
  code          TEXT NOT NULL UNIQUE,
  title         TEXT NOT NULL,
  kind          TEXT NOT NULL CHECK (kind IN ('PYQ_PAPER','MOCK','CHAPTER','TOPIC')),
  source_id     INT REFERENCES source(id),         -- set for PYQ_PAPER
  duration_min  INT,
  marks_correct NUMERIC(4,2),
  marks_wrong   NUMERIC(5,3),
  status        TEXT NOT NULL DEFAULT 'DRAFT' CHECK (status IN ('DRAFT','PUBLISHED')),
  created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE TABLE test_question (
  test_id     INT NOT NULL REFERENCES test(id) ON DELETE CASCADE,
  position    INT NOT NULL,
  question_id INT NOT NULL REFERENCES question(id),
  section     TEXT,
  PRIMARY KEY (test_id, position),
  UNIQUE (test_id, question_id)
);
CREATE TABLE test_attempt (
  id            BIGSERIAL PRIMARY KEY,
  user_id       BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  test_id       INT    NOT NULL REFERENCES test(id),
  status        TEXT   NOT NULL DEFAULT 'IN_PROGRESS' CHECK (status IN ('IN_PROGRESS','SUBMITTED')),
  started_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  submitted_at  TIMESTAMPTZ,
  score         NUMERIC(7,3),
  n_correct     INT,
  n_wrong       INT,
  n_unattempted INT
);
CREATE INDEX idx_test_attempt_user ON test_attempt(user_id, started_at DESC);

-- ---------- attempts and reviews ----------
ALTER TABLE attempt DROP CONSTRAINT attempt_user_id_fkey;
ALTER TABLE attempt ADD CONSTRAINT attempt_user_id_fkey
  FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE;
ALTER TABLE attempt RENAME COLUMN chosen TO chosen_labels;          -- "A" or "A,C"
ALTER TABLE attempt
  ADD COLUMN nat_value       NUMERIC,
  ADD COLUMN mode            TEXT NOT NULL DEFAULT 'PRACTICE'
        CHECK (mode IN ('STUDY','PRACTICE','TEST','REVISION')),
  ADD COLUMN test_attempt_id BIGINT REFERENCES test_attempt(id) ON DELETE CASCADE,
  ADD COLUMN working_md      TEXT;                                   -- the learner's own working
CREATE INDEX idx_attempt_test ON attempt(test_attempt_id) WHERE test_attempt_id IS NOT NULL;

CREATE TABLE attempt_review (
  id          BIGSERIAL PRIMARY KEY,
  attempt_id  BIGINT NOT NULL REFERENCES attempt(id) ON DELETE CASCADE,
  reviewer_id BIGINT REFERENCES app_user(id),
  verdict     TEXT NOT NULL CHECK (verdict IN ('CORRECT','PARTLY','WRONG')),
  review_md   TEXT NOT NULL,
  created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_attempt_review_attempt ON attempt_review(attempt_id);

ALTER TABLE user_question_state DROP CONSTRAINT user_question_state_user_id_fkey;
ALTER TABLE user_question_state ADD CONSTRAINT user_question_state_user_id_fkey
  FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE;
ALTER TABLE user_question_state
  ADD COLUMN times_seen      INT NOT NULL DEFAULT 0,
  ADD COLUMN times_correct   INT NOT NULL DEFAULT 0,
  ADD COLUMN last_attempt_at TIMESTAMPTZ,
  ADD COLUMN updated_at      TIMESTAMPTZ NOT NULL DEFAULT now();

-- ---------- todo and exam countdown ----------
CREATE TABLE todo_item (
  id         BIGSERIAL PRIMARY KEY,
  user_id    BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  title      TEXT NOT NULL,
  notes      TEXT,
  due_date   DATE,
  topic_id   INT REFERENCES topic(id),
  done_at    TIMESTAMPTZ,
  sort_order INT NOT NULL DEFAULT 0,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_todo_user ON todo_item(user_id, done_at, due_date);

CREATE TABLE exam_event (
  id        SERIAL PRIMARY KEY,
  code      TEXT NOT NULL UNIQUE,
  name      TEXT NOT NULL,
  exam_date DATE,                                  -- null until officially notified
  notes     TEXT
);
INSERT INTO exam_event (code, name, notes) VALUES
  ('APGCL-AM-EE-2026', 'APGCL Assistant Manager (Electrical), APSC Advt. 09/2026',
   'Screening test date not notified yet');

-- ---------- operations ----------
CREATE TABLE notification_outbox (
  id         BIGSERIAL PRIMARY KEY,
  kind       TEXT NOT NULL,                        -- e.g. SIGNUP
  recipient  TEXT NOT NULL,
  subject    TEXT NOT NULL,
  body       TEXT NOT NULL,
  status     TEXT NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING','SENT','FAILED')),
  attempts   INT  NOT NULL DEFAULT 0,
  last_error TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  sent_at    TIMESTAMPTZ
);
CREATE INDEX idx_outbox_pending ON notification_outbox(created_at) WHERE status = 'PENDING';

CREATE TABLE content_import (
  id          SERIAL PRIMARY KEY,
  file_name   TEXT NOT NULL,
  checksum    TEXT NOT NULL,                       -- sha256 of the imported file
  item_count  INT  NOT NULL,
  imported_by BIGINT REFERENCES app_user(id),
  imported_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  UNIQUE (file_name, checksum)
);

-- ---------- updated_at triggers ----------
DO $$
DECLARE t TEXT;
BEGIN
  FOREACH t IN ARRAY ARRAY['subject','chapter','topic','source','question','concept_section',
                           'app_user','test','attempt_review','user_question_state','todo_item']
  LOOP
    EXECUTE format('CREATE TRIGGER trg_%s_updated_at BEFORE UPDATE ON %I
                    FOR EACH ROW EXECUTE FUNCTION set_updated_at()', t, t);
  END LOOP;
END $$;
