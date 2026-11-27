-- V9: practise GATE PYQs as papers and sets (docs/pyq-plan.md).
-- * attempt.self_mark: the user's own mark after answering: KNEW, GUESSED or DONT_KNOW. A right guess is not knowing.
-- * test kind PYQ_SET: a user's own selection of PYQs (one GATE paper, or a subject / chapter across years).
--   It is a snapshot: the questions are copied into test_question when the user starts it, with their GATE marks.
-- * test_attempt.mode: TEST = timed, answers after submit; PREPARE = one question at a time, solution right away.
-- * test_attempt.style: APSC = every question as a single-answer MCQ, +1 / -0.25; GATE = original format and marks.
-- * question_mcq_option: the single-answer MCQ version of an MSQ ("1 and 3 only"), used in APSC style.
--   (NAT questions keep their 4 test options in question_option, as chapter mocks already do.)

ALTER TABLE attempt ADD COLUMN self_mark TEXT CHECK (self_mark IN ('KNEW', 'GUESSED', 'DONT_KNOW'));

ALTER TABLE test DROP CONSTRAINT test_kind_check;
ALTER TABLE test ADD CONSTRAINT test_kind_check CHECK (kind IN ('PYQ_PAPER', 'MOCK', 'CHAPTER', 'TOPIC', 'PYQ_SET'));
ALTER TABLE test ADD COLUMN owner_user_id BIGINT REFERENCES app_user(id) ON DELETE CASCADE;
CREATE INDEX idx_test_owner ON test(owner_user_id) WHERE owner_user_id IS NOT NULL;

ALTER TABLE test_question ADD COLUMN marks NUMERIC(4,2);

ALTER TABLE test_attempt
  ADD COLUMN mode  TEXT NOT NULL DEFAULT 'TEST' CHECK (mode IN ('TEST', 'PREPARE')),
  ADD COLUMN style TEXT NOT NULL DEFAULT 'APSC' CHECK (style IN ('APSC', 'GATE'));

CREATE TABLE question_mcq_option (
    question_id INT  NOT NULL REFERENCES question(id) ON DELETE CASCADE,
    label       TEXT NOT NULL CHECK (label IN ('A', 'B', 'C', 'D')),
    body_md     TEXT NOT NULL,
    is_correct  BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (question_id, label)
);
