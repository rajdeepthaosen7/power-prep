# Analysis 1-to-1: source of every APSC 2023 EE question

Data: `apsc-2023-sources.csv` (300 rows: paper, q, subject, topic, type, source, source_family, source_year, match, note).
Built 27 Nov 2026 from: GATE EE 2007–2026 and ESE EE 2017–2026 full text (word-for-word), GATE EE 1991–2006 question lists on
examside.com (stem match), and exact-phrase web searches. Published doc: see CLAUDE.md ("Analysis 1-to-1").

Use it when writing a chapter: give each APSC 2023 question its earlier sources (question_source), and use the patterns below for mocks.

## In one line

**APGCL copies GATE EE.** 74 of its 100 questions are word-for-word GATE EE questions (same numbers, same options), and more than half of those are from GATE 1992–2006. **APDCL and AEGCL are different:** only about 20 of 100 come from GATE or ESE; the rest are standard one-line theory facts and textbook-type numericals.

## What I checked

| Paper | Questions | Traced to an earlier exam or bank | Word-for-word GATE EE | From GATE 1992–2006 | Not traced |
|---|---|---|---|---|---|
| APGCL 2023 (15 Oct, Series A) | 100 | 83 | 74 | 43 | 17 |
| APDCL 2023 (10 Sep, Series B) | 100 | 20 | 10 | 7 | 80 |
| AEGCL 2023 (18 Nov, Series D) | 100 | 20 | 10 | 7 | 80 |

How each question was matched:
- **GATE EE 2007–2026 and ESE EE 2017–2026:** full text of every question, compared word by word (from our own dump).
- **GATE EE 1991–2006:** the question lists on examside.com, compared stem by stem. Figures and numbers are not shown there, so a few of these matches are from the stem alone (marked in the CSV).
- **Others:** web search for exact phrases (ISRO, SSC-JE, question-bank sites).
- **Match levels:** *Same* = same question and numbers; *Numbers changed* = same template, new values; *Same idea* = same fact asked differently; *In a bank* = found on a question-bank site, exam not named; *Not traced* = no earlier source found.

## The patterns

1. **APGCL = GATE EE, old and new.** The 74 copied questions come from 27 different GATE years, 1992 to 2022. The biggest single year is GATE 2004 (9 questions). APGCL usually keeps even the GATE options and their order.
2. **Old GATE matters most for APGCL.** 43 of its 76 GATE questions are from 1992–2006: papers most students never solve.
3. **APDCL and AEGCL = theory facts + textbook numericals.** About 80 questions in each are one-line facts ("Schering bridge is used for…", "DC series motors are used where…") or short textbook numericals (transformer efficiency, induction-motor slip, rotor frequency). They follow the wording of J.B. Gupta / V.K. Mehta / Theraja-type objective books.
4. **APSC repeats itself.** Four questions appear in two of the three 2023 papers: APGCL 46 = AEGCL 35 (Jacobian size), APGCL 61 = APDCL 93 (lead/lag compensators), APGCL 81 ≈ APDCL 16 (SAR ADC), APDCL 3 = AEGCL 59 (energy meter).
5. **Subject differences:** Power Systems is the most traceable (26 of 47). Signals and Machines theory are the least (most are standard facts). Engineering maths: 10 questions in APDCL, none in APGCL or AEGCL. 8085: 2 questions in APGCL.

## Best practices (what to do with this)

1. **Make GATE EE PYQs the core, all years 1991–2026.** For APGCL this alone covers about three quarters of the paper. Solve every GATE EE question of a chapter after its notes, including the old ones. Our dump has 2007–2026; **1991–2006 must be added** (I will add them chapter by chapter).
2. **Learn the question, not just the answer.** APGCL copies GATE word for word, so knowing each PYQ's exact answer gets marks directly. Re-solve them in revision until they are instant.
3. **Build a fact bank for APDCL/AEGCL-style questions.** For each chapter, keep a short list of one-line facts (uses, definitions, "which bridge measures what", "which relay protects what"). These are cheap marks and they cannot be calculated, only remembered.
4. **Practise textbook numericals without a calculator.** Transformer efficiency, slip/rotor frequency, DC machine EMF, SIL, demand factor, string efficiency: small numbers, one formula each.
5. **Revise all three 2023 APSC papers fully.** APSC reused 4 of its own questions within 2023; expect some 2023 questions again in 2026.
6. **Mocks will follow this mix:** APGCL-style mocks about 75 % GATE EE PYQs (spread over all years) + 25 % theory facts; APDCL/AEGCL-style mocks about 25 % GATE/ESE + 75 % facts and textbook numericals.
