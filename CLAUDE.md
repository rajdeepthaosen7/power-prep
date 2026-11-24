# Power Prep - project rules

Electrical Engineering prep hub for the APSC power-sector exams (APGCL / APDCL / AEGCL Assistant Manager, Electrical).
The owner uses this as his main exam prep tool. It is serious work: **no placeholders, no fake data.**
If anything is missing (a source file, an answer key, a syllabus item), ask the owner instead of guessing.

## Writing concept notes (Study tab)
- Write like the best teacher. Style: Lakhmir Singh (S. Chand) - very clear, detailed, conceptual, simple English.
- Teaching must be **integral, gradual and complete**:
  1. Start from basics of the topic, build each idea step by step (definitions -> physical meaning -> laws -> derivations -> formulas -> special cases -> common traps).
  2. Use small worked examples after each idea, easy to hard.
  3. Only after the concept is complete, move to the source questions. Never jump straight into explaining one question.
- Every solved question: show the full reasoning, not just the final formula. Link it back to the concept section it uses.
- Goal: the reader can solve every probable question from the source pools: GATE EE 1-mark (+ easy 2-mark), GATE EC (Signals/Digital/Analog), ESE EE prelims, J.B. Gupta-type objective banks, and APSC 2023 PYQs.
- Answers: mark each as verified (official key) or unverified (solved by us) until confirmed.
- Textbook/bank questions are rewritten with new values (public portal). Exam PYQs keep original wording.

## Content and data
- All questions, solutions, notes and tags live in Postgres, served by the Java API. The frontend only displays.
- Content is added through versioned import files in the repo, loaded by migrations / admin import.
- Hierarchy: subject -> chapter -> topic, linked to the official syllabus. One question can have many sources (e.g. APGCL 2023 Q36 and GATE 2005).
- Source material ("dump"): Google Drive folder `dump` (always) and a gitignored `dump/` folder in this repo (when on PC).
  Keep a list of what is dumped and what is still to be processed.

## Infra
- Backend: Spring Boot 4.2 (Java 21, Flyway 12) on Render (free, Docker), Postgres on Neon (Singapore), API at https://power-api.rajdeepthaosen.com
- Frontend: repo power-prep-web on Cloudflare, site at https://power.rajdeepthaosen.com
- Health check must not touch the DB (keeps Neon able to sleep).

## Users
- Sign-in with Google only. The ADMIN_EMAIL account is ADMIN + APPROVED automatically.
- Everyone else starts as USER + PENDING; the owner approves, rejects or disables them in the admin section. No email notifications.
- The API issues its own 30-day token; roles and status are read from the DB on every request.
- Render env: DATABASE_URL, DB_USER, DB_PASSWORD, GOOGLE_CLIENT_ID, ADMIN_EMAIL, JWT_SECRET, CONTENT_REQUIRES_APPROVAL (true once the new UI has login).
- Progress is tracked per account.
