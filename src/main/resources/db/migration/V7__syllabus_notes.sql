-- Syllabus tab: each top-level syllabus item (section) belongs to a subject; items can carry a note on what the APSC papers ask.
ALTER TABLE syllabus_item ADD COLUMN subject_id INT REFERENCES subject(id), ADD COLUMN note TEXT;
CREATE INDEX idx_syllabus_item_exam ON syllabus_item(exam_code, sort_order);
-- APGCL 2023 asked two 8085 questions although 8085 is outside the GATE EE syllabus: keep it in the study plan.
UPDATE subject SET in_syllabus = TRUE WHERE slug = '8085-microprocessor';
