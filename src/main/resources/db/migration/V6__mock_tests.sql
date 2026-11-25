-- V6: chapter-wise mock tests in the APSC format.
-- * question.origin: PYQ = an exam question (has question_source rows); PRACTICE = written by us for mock tests.
--   PRACTICE questions are shown only inside tests, never in Study or the PYQ tab, so they stay fresh.
-- * test.subject_id / chapter_id / variant: a mock belongs to a chapter (or, later, to a whole subject).
--   variant: PYQ = only exam questions, FRESH = only our questions, MIX = both, as APSC mixes them.
-- * test.content_code links a test to content/<subject>/<chapter>/mocks.yml (upserted by the content migration).

ALTER TABLE question ADD COLUMN origin TEXT NOT NULL DEFAULT 'PYQ' CHECK (origin IN ('PYQ', 'PRACTICE'));

ALTER TABLE test
  ADD COLUMN subject_id  INT REFERENCES subject(id),
  ADD COLUMN chapter_id  INT REFERENCES chapter(id),
  ADD COLUMN variant     TEXT CHECK (variant IN ('PYQ', 'FRESH', 'MIX')),
  ADD COLUMN description TEXT,
  ADD COLUMN sort_order  INT NOT NULL DEFAULT 0;
CREATE INDEX idx_test_chapter ON test(chapter_id, sort_order);

ALTER TABLE test_attempt
  ADD COLUMN time_ms   BIGINT,
  ADD COLUMN max_score NUMERIC(7,3);
CREATE INDEX idx_test_attempt_test_user ON test_attempt(test_id, user_id, started_at DESC);
