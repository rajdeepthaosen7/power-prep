# Whole GATE EE papers

One file per paper set (`2016-set1.yml`, ...), built by `python3 content/tools/build_papers.py` into
`R__content_zz_gate_papers.sql`. Plan and order: `docs/pyq-plan.md` (phase 3).

- A question of a **written chapter** goes in that chapter's `questions-*.yml` (with its GATE `sources`), not here.
  The builder refuses a question that a chapter file already has.
- Every other question of the paper goes here. It keeps the topic it is tagged with (the "skeleton" row), so it already
  shows in its subject in the Tests tab. When its chapter is written, move it to the chapter file with the same `key`.
- Wording is the paper's own. Answers come from the official key (`status: OFFICIAL_KEY`).
- Every solution: the idea used, numbered steps (one operation each), a quick check, and a **Without a calculator** way.
- NAT questions get 4 options (one right) so APSC style can ask them as MCQ; wrong options come from real slips.
- Figures: `figures/<key>.svg`, drawn with `content/tools/svgkit.py` / schemdraw like the chapter figures.
