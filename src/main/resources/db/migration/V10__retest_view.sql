-- V10: one place for the retest rule's data (mistakes.RetestRule), used by the Mistakes tab and the Today page.
-- A "bad" answer is a wrong one, or a right one marked GUESSED or DONT_KNOW. sure_days = distinct days (India time)
-- with a right, sure answer (self_mark KNEW or not given) after the last bad one. Open while sure_days < 2.
CREATE VIEW retest_state AS
WITH bad AS (
    SELECT user_id, question_id, max(attempted_at) AS last_bad_at
    FROM attempt WHERE is_correct = FALSE OR self_mark IN ('GUESSED', 'DONT_KNOW')
    GROUP BY user_id, question_id)
SELECT b.user_id, b.question_id, b.last_bad_at,
       (SELECT count(DISTINCT (x.attempted_at AT TIME ZONE 'Asia/Kolkata')::date) FROM attempt x
        WHERE x.user_id = b.user_id AND x.question_id = b.question_id AND x.is_correct
          AND (x.self_mark IS NULL OR x.self_mark = 'KNEW') AND x.attempted_at > b.last_bad_at) AS sure_days
FROM bad b;
