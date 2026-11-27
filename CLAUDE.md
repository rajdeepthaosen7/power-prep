# Power Prep - project rules

Electrical Engineering prep hub for the APSC power-sector exams (APGCL / APDCL / AEGCL Assistant Manager, Electrical).
The owner uses this as his main exam prep tool. It is serious work: **no placeholders, no fake data.**
If anything is missing (a source file, an answer key, a syllabus item), ask the owner instead of guessing.

## APSC exam format (owner's rules)
- APSC asks **MCQ only** (no NAT, no MSQ). NAT questions from GATE are kept for practice only.
- **No calculator** in the APSC exam. Every analysis must also show the calculator-free way: easy numbers,
  approximation, ratios, option elimination, unit/limit checks. Notes should teach these mental-math shortcuts.
- Practice questions written by us must be MCQ with numbers that can be worked by hand.

## Mock tests (Tests tab, owner's rules)
- **Always study the APSC papers first** (`sources/03_apsc_papers`, OCR in the workspace) before setting a mock: copy their
  style (4 options, short stems, easy hand numbers, some "None of the above", textbook theory one-liners, GATE questions turned into MCQ).
- APSC format: 100 Q in 2 h (72 s per question), +1 / −0.25 (Paper I: 1.5 marks, same 25 % negative). Mocks use +1 / −0.25 and 1.2 min per question.
- **Three mocks per chapter**: PYQ only, fresh only (our questions), mixed (about half each). Defined in `<chapter>/mocks.yml`.
- **Question count follows coverage**, never a fixed number: every concept of the chapter at least once.
- Our questions live in `<chapter>/practice-*.yml` (no sources, origin PRACTICE). They are shown only inside tests, never in Study or PYQ.
  NAT PYQs get 4 `options` so tests can ask them as MCQ (Study still asks them as NAT).
- Subject-wise mock: only after **all** chapters of that subject are ready.

## Prep strategy (owner's rule: every build decision must serve this)
Full doc: https://claude.ai/artifact/BjEpYbj8eGATVTrSGrfNmJ
- Target 80+/100 in APSC AM (100 MCQs, 2 h, no calculator, -0.25). Skip no subject.
- Subject order: Circuits, Machines, Power Systems, Measurements, Control, Power Electronics, Analog & Digital, Signals, EMF.
- Two tracks (from 26 Nov 2026): Circuits = main track; Power Systems in parallel. PS order: first the chapters that need no AC theory
  (generation & economics, insulators, cables & grounding, line parameters, protection & breakers, HVDC); AC-heavy PS chapters
  (per unit, line performance, power flow, compensation, load flow, ELD, LFC, stability, faults) after Circuits phasors and 3-phase.
  Build alternately (one Circuits chapter, one PS chapter) and keep both at least a week ahead.
- Chapter loop: notes + one-page short note -> PYQs (attempt first) -> mistake log -> 3 mocks (PYQ, Fresh, Mixed) -> revision after 1, 7, 21 days.
  A chapter is done at 85%+ on its Mixed mock.
- Phases: learn (~16 weeks), test (subject + full mocks), sharpen (short notes + mistake log + full mocks).
- Content stays at least one week ahead of the owner's study. Full notes for heavy subjects (Power Systems, Machines, Circuits, Control);
  shorter exam-focused notes for light ones (EMF, Measurements). Every chapter keeps its 3 mocks.
- The owner's time goes to studying, not building: build only what the loop needs, keep it simple.

## Where APSC questions come from (Analysis 1-to-1, 27 Nov 2026)
Doc: https://claude.ai/artifact/6okvjysxivqE27LCp5rjZz. Data: `content/analysis/apsc-2023-sources.csv` (one row per question,
with `gate_marks`) and `content/analysis/chapter-map.csv` (every question placed in a portal chapter, with `in_portal`).
Patterns and best practices: `content/analysis/README.md`.
- APGCL 2023: 74 of 100 are word-for-word GATE EE (43 of them from GATE 1992-2006). APDCL and AEGCL 2023: only 10 each are
  word-for-word GATE EE; 41 more (between them) are JE/AE/PSU, ESE or GATE EC PYQs (GATE EC for Signals and Control); most of
  the rest are one-line facts in question-bank wording. Traced: APGCL 95, APDCL 67, AEGCL 72 of 100.
- APSC takes GATE 1-mark and 2-mark questions alike (51 vs 47 of the 101 copied), never the old 5-mark descriptive ones.
  So chapters include 2-mark GATE PYQs too, with a calculator-free way.
- When writing a chapter: start from its rows in `chapter-map.csv`. Every APSC 2023 question in it goes in. Its earlier
  source goes in as an extra `sources` entry when it is GATE EE or ESE EE (e.g. APGCL 2023 Q36 + GATE 2005); for other exams
  (ESE E&T, JE/AE/PSU, GATE EC) add an "**Asked before:**" line in the solution, because build_sql.py only knows GATE/ESE EE
  titles. Write any traced GATE EE PYQ that is not in the portal yet. Then set `in_portal` in the map.
- Coupled-coil questions APDCL Q43 and AEGCL Q84 live in Circuits Ch1; Ch9 (coupled circuits) links to them, no copies.
- PYQ mock: draw on all GATE EE years, old ones (1991-2006) included. Fresh mock: APDCL/AEGCL style (one-line facts and
  hand-worked textbook numericals). Mixed mock: about half each, as before.
- Every chapter keeps a fact list (one-line facts: uses, definitions, "which bridge measures what") in its notes or short note.
- GATE EE 1991-2006 is not in the dump yet (only examside.com question lists were used): see DUMP.md.

## GATE PYQ plan (27 Nov 2026)
Plan and progress: `docs/pyq-plan.md`. Every GATE EE question 1992-2026 goes in once, tagged to chapter and topic, and shows both
under its topic (Study) and in year / subject / chapter tests (Tests). Prepare and test mode; APSC style turns NAT and MSQ into
single-answer MCQ. Self-mark (knew / guessed / didn't know) feeds the retest list (Mistakes). Papers are loaded one at a time,
most APSC 2023 copies first. Second source: GATE EC (Signals, Control, Analog & Digital, Maths only).
Backend (phase 1) is in: package `pyq` (papers, sets, start), `mocktest` modes/styles (`TestMode`, `TestStyle`, `AskedQuestion`,
`TestScore.gate`), `attempt.SelfMark`, `mistakes.RetestRule`; API list in `docs/pyq-plan.md`. A paper is "ready" only as far as
its questions are written (status READY with a stem); the GATE 2007-2026 skeleton rows (topic + marks, no text) are filled
by writing the questions in chapter YAML with their GATE `sources`.

## Portal features (Nov 2026)
- Today (`/today`, home): next chapter per track, revisions due, open mistakes, mixed mocks below 85 %. API `/api/today`.
- Mistake log (`/mistakes`): built from the attempt table (a question is open while its latest attempt is wrong); one-tap reason in
  `mistake_note`. Re-attempts use mode REVISION; our practice questions can be re-attempted once the user has seen them in a mock.
- Spaced revision: `chapter_progress` (studied, then revisions after 1, 7, 21 days). A chapter is marked studied automatically when all
  its written PYQs are attempted, or by the "I have finished this chapter" button.
- Syllabus tab: `content/syllabus/apgcl-am-2026.yml` -> `python3 content/tools/build_syllabus.py` -> `R__syllabus.sql`.
  Paper I = official APSC text (No.8 PSC/CON/Exam-13/2026-2027), i.e. GATE EE without maths; 8085 kept because APGCL 2023 asked it.
  Every topic must map to an item; an item may name its own `subject` when it is taught elsewhere (R.M.S. line).
  APGCL also has Paper II (General Studies & General English, 100 MCQ, APSC No.25PSC/CON/Exam-13/2026-2027): not covered yet.
- One-page summary: `/study/<subject>/c/<chapter>/print` prints the `*-chapter-summary` topic.

## Writing concept notes (Study tab)
- Write like the best teacher. Style: Lakhmir Singh (S. Chand) - very clear, detailed, conceptual, simple English.
- Teaching must be **integral, gradual and complete**:
  1. Start from basics of the topic, build each idea step by step (definitions -> physical meaning -> laws -> derivations -> formulas -> special cases -> common traps).
  2. Use small worked examples after each idea, easy to hard.
  3. Only after the concept is complete, move to the source questions. Never jump straight into explaining one question.
- Every solved question: show the full reasoning, not just the final formula. Link it back to the concept section it uses.
- Worked numericals (owner, 26 Nov 2026: "so abstract, took me 1 hour"): start from a small table or a plain-words picture of what
  happens, then the formula. Every example is numbered steps, one operation per step, every multiplication/division shown
  (no "⇒" jumps), followed by a quick sense check. Prefer the physical route (e.g. "ohms per °C first") over pure algebra;
  show the textbook algebra afterwards as a second way.
- Never use a symbol before defining it (name, meaning in plain words, unit, one small number example). Owner, 26 Nov 2026:
  "you use J but never mention J as current density".
- Goal: the reader can solve every probable question from the source pools: GATE EE 1-mark (+ easy 2-mark), GATE EC (Signals/Digital/Analog), ESE EE prelims, J.B. Gupta-type objective banks, and APSC 2023 PYQs.
- Answers: mark each as verified (official key) or unverified (solved by us) until confirmed.
- Textbook/bank questions are rewritten with new values (public portal). Exam PYQs keep original wording.

## Backend design (owner, 26 Nov 2026: "follow LLD and SOLID")
- Package by feature under `com.rajdeep.powerprep`: catalog, study, practice, mocktest, mistakes, revision, today, syllabus, auth.
  Shared: `question` (QuestionBank: options, sources, figures, review; `grading` strategy), `attempt` (AttemptRecorder), `common`
  (ApiException family + GlobalExceptionHandler, CurrentUser argument, `Rows.of(Record.class)` row mapper), `config`.
- Every feature: Controller (HTTP only) -> Service (rules, @Transactional) -> Repository **interface** + `Jdbc...Repository`
  (SQL with JdbcClient, named parameters). Responses are Java records; never return `Map<String, Object>`.
- Rules live in small classes with unit tests: AnswerGrader per answer type (MCQ/MSQ/NAT, add a type = add a class),
  TestScore (+1/-0.25), RevisionSchedule (1/7/21 days, injected Clock), PivotDimension (whitelisted SQL), enums for every
  status/action/reason. Throw NotFound/BadRequest/Conflict/UnauthorizedException; the message is shown to the user.
- Any change to an endpoint: run `tools/golden/golden.py` with the old and the new jar (fresh copy of a snapshot DB,
  90 scripted calls incl. writes) and `cmp.py`; only intended differences may show. `mvn package` runs the unit tests.

## Content and data
- All questions, solutions, notes and tags live in Postgres, served by the Java API. The frontend only displays.
- Content is added through versioned files in the repo: `content/<subject>/chapters.yml`, chapter folders with
  `notes/*.md`, `questions-*.yml` and `figures/*.svg` (drawn with `content/tools/svgkit.py` + schemdraw).
  `python3 content/tools/build_sql.py <subject>` generates the repeatable migration `R__content_<subject>.sql`.
- Study flow: the API sends a question's correct answer and analysis only after the signed-in user has attempted it.
- Hierarchy: subject -> chapter -> topic, linked to the official syllabus. One question can have many sources (e.g. APGCL 2023 Q36 and GATE 2005).
- Source material ("dump"): Google Drive folder `dump` (always) and a gitignored `dump/` folder in this repo (when on PC).
  Keep a list of what is dumped and what is still to be processed: `DUMP.md`. The owner's open items live in `TODO.md`.

## Infra
- Backend: Spring Boot 4.2 (Java 21, Flyway 12) on Render (free, Docker), Postgres on Neon (Singapore), API at https://power-api.rajdeepthaosen.com
- Since 26 Nov 2026 the live API is AWS Lambda in Sydney (SnapStart, Function URL https://bxi3fbizgkqn4ab5l7cexysxzi0kgihd.lambda-url.ap-southeast-2.on.aws, cost kill switch): see deploy/aws/README.md. Render still runs as a fallback until the owner deletes it. Render also runs Flyway on the same Neon DB on every push, so a Lambda deploy can fail with "Number of retries exceeded while attempting to acquire PostgreSQL advisory lock" (seen 27 Nov 2026). The live API is not changed when that happens; re-run the deploy (`gh run rerun <id>`) after a few minutes. Secrets are changed only in CloudShell (Parameter Store, then `bash setup.sh`), never in GitHub. Lambda-only code lives in src/lambda/java (Maven profile `lambda`); never put the Lambda adapter in the main build, it replaces Tomcat and breaks the Render jar.
- Frontend: repo power-prep-web on Cloudflare, site at https://power.rajdeepthaosen.com. Its Worker (worker/index.js) passes /api/* to the Lambda Function URL, so the API is https://power.rajdeepthaosen.com/api/... (same origin, no CORS preflight). power-api.rajdeepthaosen.com (Render) is unused.
- Health check must not touch the DB (keeps Neon able to sleep).

## Users
- Sign-in with Google only. The ADMIN_EMAIL account is ADMIN + APPROVED automatically.
- Everyone else starts as USER + PENDING; the owner approves, rejects or disables them in the admin section. No email notifications.
- The API issues its own 30-day token; roles and status are read from the DB on every request.
- Render env: DATABASE_URL, DB_USER, DB_PASSWORD, GOOGLE_CLIENT_ID, ADMIN_EMAIL, JWT_SECRET, CONTENT_REQUIRES_APPROVAL (true once the new UI has login).
- Progress is tracked per account.
