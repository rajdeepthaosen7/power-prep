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
- Goal: the reader can solve every probable question from the source pools: GATE EE 1-mark (+ easy 2-mark), GATE EC (Signals/Digital/Analog), ESE EE prelims, J.B. Gupta-type objective banks, and APSC 2023 PYQs.
- Answers: mark each as verified (official key) or unverified (solved by us) until confirmed.
- Textbook/bank questions are rewritten with new values (public portal). Exam PYQs keep original wording.

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
- Since 26 Nov 2026 the live API is AWS Lambda in Sydney (SnapStart, Function URL https://bxi3fbizgkqn4ab5l7cexysxzi0kgihd.lambda-url.ap-southeast-2.on.aws, cost kill switch): see deploy/aws/README.md. Render still runs as a fallback until the owner deletes it. Secrets are changed only in CloudShell (Parameter Store, then `bash setup.sh`), never in GitHub. Lambda-only code lives in src/lambda/java (Maven profile `lambda`); never put the Lambda adapter in the main build, it replaces Tomcat and breaks the Render jar.
- Frontend: repo power-prep-web on Cloudflare, site at https://power.rajdeepthaosen.com
- Health check must not touch the DB (keeps Neon able to sleep).

## Users
- Sign-in with Google only. The ADMIN_EMAIL account is ADMIN + APPROVED automatically.
- Everyone else starts as USER + PENDING; the owner approves, rejects or disables them in the admin section. No email notifications.
- The API issues its own 30-day token; roles and status are read from the DB on every request.
- Render env: DATABASE_URL, DB_USER, DB_PASSWORD, GOOGLE_CLIENT_ID, ADMIN_EMAIL, JWT_SECRET, CONTENT_REQUIRES_APPROVAL (true once the new UI has login).
- Progress is tracked per account.
