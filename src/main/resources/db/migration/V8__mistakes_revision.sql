-- Mistake log: questions a user got wrong are found from the attempt table; this keeps the user's one-tap reason.
CREATE TABLE mistake_note (
    user_id     BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    question_id INT    NOT NULL REFERENCES question(id) ON DELETE CASCADE,
    reason      TEXT   CHECK (reason IN ('CONCEPT', 'FORMULA', 'CALCULATION', 'MISREAD', 'GUESS', 'TIME')),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (user_id, question_id)
);

-- Spaced revision of chapters: studied once, then revised after 1, 7 and 21 days.
CREATE TABLE chapter_progress (
    user_id    BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    chapter_id INT    NOT NULL REFERENCES chapter(id) ON DELETE CASCADE,
    studied_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    rev1_at    TIMESTAMPTZ,
    rev2_at    TIMESTAMPTZ,
    rev3_at    TIMESTAMPTZ,
    PRIMARY KEY (user_id, chapter_id)
);

