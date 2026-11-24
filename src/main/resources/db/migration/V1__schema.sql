-- Subject -> Chapter -> Topic -> Question
CREATE TABLE subject (
  id          SERIAL PRIMARY KEY,
  name        TEXT NOT NULL UNIQUE,
  slug        TEXT NOT NULL UNIQUE,
  sort_order  INT  NOT NULL DEFAULT 0,
  in_syllabus BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE chapter (
  id         SERIAL PRIMARY KEY,
  subject_id INT NOT NULL REFERENCES subject(id),
  name       TEXT NOT NULL,
  slug       TEXT NOT NULL,
  sort_order INT NOT NULL DEFAULT 0,
  UNIQUE (subject_id, slug)
);

CREATE TABLE topic (
  id         SERIAL PRIMARY KEY,
  subject_id INT NOT NULL REFERENCES subject(id),
  chapter_id INT REFERENCES chapter(id),
  name       TEXT NOT NULL,
  slug       TEXT NOT NULL,
  UNIQUE (subject_id, slug)
);

-- One row per exam paper (APDCL 2023, GATE 2014 set 1, ...)
CREATE TABLE source (
  id     SERIAL PRIMARY KEY,
  code   TEXT NOT NULL UNIQUE,          -- e.g. APDCL-2023, GATE-2014
  kind   TEXT NOT NULL CHECK (kind IN ('STATE','GATE')),
  exam   TEXT NOT NULL,                 -- APDCL / APGCL / AEGCL / GATE
  year   INT  NOT NULL
);

CREATE TABLE question (
  id           SERIAL PRIMARY KEY,
  source_id    INT  NOT NULL REFERENCES source(id),
  q_no         TEXT NOT NULL,           -- "8" or "S1-Q1"
  subject_id   INT  NOT NULL REFERENCES subject(id),
  topic_id     INT  NOT NULL REFERENCES topic(id),
  q_type       TEXT,                    -- Numerical / Theory
  marks        INT,
  summary      TEXT,                    -- one-line description (available now)
  body         TEXT,                    -- full question text (to be filled from PDFs)
  options      JSONB,                   -- ["A text","B text",...]
  answer       TEXT,                    -- "A".."D" or numeric value
  explanation  TEXT,
  status       TEXT NOT NULL DEFAULT 'TAGGED' CHECK (status IN ('TAGGED','READY')),
  UNIQUE (source_id, q_no)
);
CREATE INDEX idx_question_topic   ON question(topic_id);
CREATE INDEX idx_question_subject ON question(subject_id);
CREATE INDEX idx_question_status  ON question(status);

-- GATE question <-> state question repeat links
CREATE TABLE question_link (
  gate_question_id  INT NOT NULL REFERENCES question(id),
  state_question_id INT NOT NULL REFERENCES question(id),
  level TEXT NOT NULL CHECK (level IN ('exact','values','concept')),
  PRIMARY KEY (gate_question_id, state_question_id)
);

-- Public users
CREATE TABLE app_user (
  id            BIGSERIAL PRIMARY KEY,
  email         TEXT NOT NULL UNIQUE,
  name          TEXT,
  password_hash TEXT NOT NULL,
  role          TEXT NOT NULL DEFAULT 'STUDENT' CHECK (role IN ('STUDENT','ADMIN')),
  created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Every answer a student gives
CREATE TABLE attempt (
  id           BIGSERIAL PRIMARY KEY,
  user_id      BIGINT NOT NULL REFERENCES app_user(id),
  question_id  INT    NOT NULL REFERENCES question(id),
  chosen       TEXT,
  is_correct   BOOLEAN,
  time_ms      INT,
  attempted_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_attempt_user ON attempt(user_id, attempted_at DESC);
CREATE INDEX idx_attempt_user_q ON attempt(user_id, question_id);

-- Revision state per user per question (Leitner boxes 1..5)
CREATE TABLE user_question_state (
  user_id        BIGINT NOT NULL REFERENCES app_user(id),
  question_id    INT    NOT NULL REFERENCES question(id),
  box            INT    NOT NULL DEFAULT 1 CHECK (box BETWEEN 1 AND 5),
  next_review_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  last_correct   BOOLEAN,
  bookmarked     BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (user_id, question_id)
);
CREATE INDEX idx_uqs_due ON user_question_state(user_id, next_review_at);
