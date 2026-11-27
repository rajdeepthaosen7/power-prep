# Plan: prepare from GATE EE PYQs (1992-2026)

Agreed with the owner on 27 Nov 2026. The owner left the design to Claude: "the ultimate goal is to crack the exam, do what feels best".
The current approach (Study tab notes, topic questions, 3 mocks per chapter) stays. This plan adds to it.

## Why

The APSC 2023 analysis (`content/analysis/`) found that 101 of 300 questions are copied from GATE EE (75 of 100 in APGCL; same question or numbers changed),
both 1-mark and 2-mark. About half of those come from 1992-2006, half from 2007-2017, only 3 from 2018 or later.

## The idea in one line

Every GATE EE question goes into the portal **once**, tagged to its chapter and topic. The same question then shows up in two places:

1. **Study tab:** under its topic, after the notes (this already works for written chapters).
2. **Tests tab:** in a GATE EE paper (one year), a subject test across all years, or a chapter test across all years.

So both of the owner's approaches (topic first, or paper first) use the same questions, and progress is shared.

## How a question is practised

- **Prepare mode:** one question at a time, solution right after the answer, link to the topic in the Study tab.
- **Test mode:** timed, answers only after submit, then a full review.
- **Two styles in test mode:**
  - *APSC style* (default): every question is single-answer MCQ, no calculator, +1 / −0.25, 72 s per question.
    - NAT becomes MCQ: 4 options, the wrong ones built from common mistakes (APSC did exactly this in 2023).
      Chapter mocks already do this with the `options` field on NAT PYQs; the same field is used here.
    - MSQ becomes MCQ with combination options ("1 and 3 only", "2, 3 and 4"), the way ESE asks statement questions.
  - *GATE style*: the original format (MCQ, MSQ, NAT) and GATE marks. MSQ needs every correct option, no partial marks.
- **After each question:** the owner marks *I knew it*, *I guessed* or *I didn't know*. A correct guess is not knowing.
- **Retest list:** wrong + guessed + didn't know. Uses the existing Mistakes tab (it already has "Guessed" and re-solve).
  Can be filtered by year or subject. A question leaves the list after it is right and marked *I knew it* on two different days
  (fits the 1 / 7 / 21 day revision in the prep strategy).
- **Left out:** the old 5-mark descriptive questions (APSC never took them). Off-syllabus topics (for example 8085) are tagged and can be hidden.
- **Topic links:** a question in a chapter that is not written yet still gets a full solution; the link to the notes is added when the chapter is written.

## Phases

1. **Backend** (no UI change): GATE EE paper as a test (exam, year, set, question order, marks); subject and chapter tests across years;
   prepare/test mode; APSC-style options for NAT and MSQ; the three self-marks; retest rules. Unit tests.
2. **UI:** mockup first (new "GATE EE papers" part of the Tests tab, prepare mode in the test screen, self-mark buttons),
   build only after the owner sees it. No existing screen is changed beyond that.
3. **Content, one paper at a time.** Each paper is done completely: every question, checked answer key, solution, figure,
   chapter/topic tag. Years with several sets (2014-2017) are one paper per set.
   Order = most APSC 2023 copies first:
   2004 (10), 2016 (9), 1998, 2005, 2012, 2014 (6 each), 2001, 2008, 2010 (5 each), 2007, 2009 (4 each),
   1993, 1994, 2000, 2002, 2011, 2015, 2017 (3 each), then the rest, newest papers (2019-2026) last.
   - 2007-2026: from the dump (`EE/EE2007.pdf` ... `EE2026.pdf`).
   - 1992-2006: from examside.com (web, no downloads, as the owner said earlier). A PDF dump would make figures and keys surer.
   - Questions already in a written chapter are not copied again; the paper just points to them.
4. **Second source: GATE EC**, only for Signals & Systems, Control, Analog & Digital and Engineering Maths
   (10 APSC 2023 questions were word for word from GATE EC 2002-2017; no other single exam had more than 4).
5. **Third source:** a JE/AE-level objective set for Electrical Machines (about 30 APSC 2023 questions came from SSC JE, state
   JE/AE and PSU papers, spread over about 25 different exams, strongest in Machines).

Chapter writing (Circuits, Power Systems, then the strategy order) goes on in parallel. Standard one-line facts
(about 70 APSC 2023 questions) are covered by the chapter notes, not by a single source.

## Progress

Tick each paper when it is complete in the portal.

| Phase | Status |
|---|---|
| 1. Backend | done 27 Nov 2026 (API below) |
| 2. UI mockup, then UI | done 27 Nov 2026: mockup https://claude.ai/artifact/7bh3YuzxmTZjv1cnewn1dN approved; built on the frontend `preview` branch |
| 3. GATE EE papers | 2 of about 45 (counting sets): GATE 2016 Set 1 and Set 2 complete, 27 Nov 2026 (`content/gate-papers/2016-set1.yml` + Circuits Ch2 Q15, Q17; `2016-set2.yml` + Circuits Ch1 Q23) |
| 4. GATE EC (4 subjects) | not started |
| 5. JE/AE Machines set | not started |

## API (phase 1, 27 Nov 2026)

- `GET /api/pyq/papers?exam=GATE`: every paper (2014-2016 split by set, 2017 by source), questions known vs written
  (`ready`), and the user's attempts.
- `GET /api/pyq/sets?exam=GATE`: written / known PYQs per subject and chapter, with the user's set attempts.
- `POST /api/pyq/start` `{paper}` or `{subject, chapter?, yearFrom?, yearTo?}`, plus `mode` (TEST / PREPARE) and
  `style` (APSC / GATE): makes the user's own test (kind PYQ_SET, a snapshot of the written questions) and starts it.
- `POST /api/tests/{id}/start?mode=&style=`: the same for chapter mocks (default TEST + APSC, as before).
- `POST /api/test-attempts/{id}/answers` (PREPARE only): one answer `{questionId, chosen | chosenLabels | natValue,
  selfMark, timeMs}`; returns the solution, the right answer as asked, sources and the topic link at once.
- `POST /api/test-attempts/{id}/submit`: TEST mode grades the answers sent; PREPARE mode adds up the saved ones.
- `selfMark` (KNEW / GUESSED / DONT_KNOW) is also taken by `POST /api/questions/{id}/attempts` (Study).
- Mistakes log rows carry `lastSelfMark`, `sureDays` and `retest` (RetestRule); the Mistakes screen and Today use them
  (view `retest_state`, V10).
- Marking: APSC +1 / -0.25, 1.2 min per question. GATE: own marks, wrong MCQ -1/3 of its marks, no negative for MSQ / NAT,
  1.8 min per mark. Old GATE years had other negative rules; the portal uses the current one for all years.
- Content: an MSQ can have `mcq_options` (4, one right, A-D) in its YAML for APSC style (`question_mcq_option`).

## Phase 3 notes (27 Nov 2026)

- Pipeline: `content/gate-papers/<paper>.yml` + `figures/` -> `python3 content/tools/build_papers.py` -> `R__content_zz_gate_papers.sql`.
  Rules in `content/gate-papers/README.md`. Questions of written chapters stay in the chapter files.
- Source for 2007-2026: the dump PDFs (Drive `EE<year>.pdf`, which carry the official answer key at the end). Pre-2007 has no
  PDF in the dump yet; examside.com has no figures and no paper order, so a PDF dump of 1991-2006 is still needed for those papers.
- Order: the plan's order puts 2004 first, but 2004 needs that dump; 2016 (next most copied) was done first (both sets). Next: 2012,
  then 2014 (three sets), 2008, 2010.

