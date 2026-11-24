-- V5: support content imports from versioned files in the repo (content/), loaded by repeatable migrations.
-- * question.content_key: stable key of a question written in a content file (e.g. "gate-2009-10").
-- * concept_section.slug: stable key of a notes section inside its topic.
-- * figure.svg: inline, theme-aware SVG drawing (figures are drawn for the portal, so they live in the DB too).

ALTER TABLE question ADD COLUMN content_key TEXT;
ALTER TABLE question ADD CONSTRAINT ux_question_content_key UNIQUE (content_key);

ALTER TABLE concept_section ADD COLUMN slug TEXT;
ALTER TABLE concept_section ADD CONSTRAINT ux_concept_section_topic_slug UNIQUE (topic_id, slug);

ALTER TABLE figure ADD COLUMN svg TEXT;

-- an attempt on an MSQ/MCQ stores chosen labels ("A" or "A,C"); NAT stores nat_value. Speed up "my attempts" lookups.
CREATE INDEX IF NOT EXISTS idx_attempt_q_user ON attempt(question_id, user_id, attempted_at DESC);
