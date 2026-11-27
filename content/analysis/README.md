# Analysis 1-to-1: source of every APSC 2023 EE question

Data: `apsc-2023-sources.csv` (300 rows: paper, q, subject, topic, type, source, source_family, source_year, match, note).
Built 27 Nov 2026 from: GATE EE 2007–2026 and ESE EE 2017–2026 full text (word-for-word), GATE EE 1991–2006 question lists on
examside.com (stem match), and exact-phrase web searches. Published doc: see CLAUDE.md ("Analysis 1-to-1").

Use it when writing a chapter: give each APSC 2023 question its earlier sources (question_source), and use the patterns below for mocks.

## In one line

**APGCL copies GATE EE.** 74 of its 100 questions are word-for-word GATE EE questions (same numbers, same options), and more than half of those are from GATE 1992–2006. **APDCL and AEGCL are different:** only 10 each are GATE EE word for word. About 45 more in each come from other exams (GATE EC, ESE, and JE/AE/PSU papers such as ISRO TA, DSSSB, PSPCL, BSNL, UPPSC, UPRVUNL, SSC-JE) or are standard facts found in question banks. About 30 in each are still not traced.

## What I checked

| Paper | Questions | Traced to an earlier exam or bank | Word-for-word GATE EE | From GATE 1992–2006 | Not traced |
|---|---|---|---|---|---|
| APGCL 2023 (15 Oct, Series A) | 100 | 95 | 74 | 43 | 5 |
| APDCL 2023 (10 Sep, Series B) | 100 | 67 | 10 | 7 | 33 |
| AEGCL 2023 (18 Nov, Series D) | 100 | 72 | 10 | 7 | 28 |

How each question was matched:
- **GATE EE 2007–2026 and ESE EE 2017–2026:** full text of every question, compared word by word (from our own dump).
- **GATE EE 1991–2006:** the question lists on examside.com, compared stem by stem. Figures and numbers are not shown there, so a few of these matches are from the stem alone (marked in the CSV).
- **Others:** web search for exact phrases (ISRO, SSC-JE, question-bank sites). Second pass (27 Nov 2026): 43 of the 177 untraced questions searched on testbook.com, byjus.com, sarthaks.com and DuckDuckGo; 29 more sources found. testbook names the exam a question was asked in. Third pass (same day): the other 134 were searched the same way. 32 were found in a named exam (28 word for word), 28 in question banks, 3 in textbooks. 19 basic textbook facts (for example "area under a unit impulse is 1") are marked as such without a separate search. 52 are still not traced; 20 of these are figure questions or have unreadable scan text.
- **Match levels:** *Same* = same question and numbers; *Numbers changed* = same template, new values; *Same idea* = same fact asked differently; *In a bank* = found on a question-bank site, exam not named; *Not traced* = no earlier source found. "Traced" in the table counts every level except *Not traced*, so it includes *Same idea* and basic textbook facts.

## The patterns

1. **APGCL = GATE EE, old and new.** The 74 copied questions come from 27 different GATE years, 1992 to 2022. The biggest single year is GATE 2004 (9 questions). APGCL usually keeps even the GATE options and their order.
2. **Old GATE matters most for APGCL.** 43 of its 76 GATE questions are from 1992–2006: papers most students never solve.
3. **APDCL and AEGCL = theory facts + textbook numericals.** About 80 questions in each are one-line facts ("Schering bridge is used for…", "DC series motors are used where…") or short textbook numericals (transformer efficiency, induction-motor slip, rotor frequency). They follow the wording of J.B. Gupta / V.K. Mehta / Theraja-type objective books.
4. **APSC repeats itself.** Four questions appear in two of the three 2023 papers: APGCL 46 = AEGCL 35 (Jacobian size), APGCL 61 = APDCL 93 (lead/lag compensators), APGCL 81 ≈ APDCL 16 (SAR ADC), APDCL 3 = AEGCL 59 (energy meter).
5. **Subject differences:** Power Systems is the most traceable (26 of 47). Signals and Machines theory are the least (most are standard facts). Engineering maths: 10 questions in APDCL, none in APGCL or AEGCL. 8085: 2 questions in APGCL.
6. **APDCL and AEGCL also copy JE/AE/PSU papers.** The second pass found their questions in ISRO Technical Assistant (2016, 2017, 2020), DSSSB JE 2019, PSPCL JE 2012, BSNL TTA 2016, MPSC AE 2018, UPPCL AE 2019, DFCCIL 2021, HPCL 2022, UGC NET Electronic Science and ESE 2013/2014. Almost all are on testbook.com's solved-PYQ pages. APDCL Q22 is P.S. Bimbhra's worked Example 4.6 (latching current, 200 V, 0.2 H). The third pass added UPPSC Polytechnic Lecturer 2022 (2), UPRVUNL JE 2014 (2), SSC JE 2017, PSPCL JE 2019, TANGEDCO AE 2015, TSPSC AEE 2023, MPPKVVCL JE 2018, UPPSC AE 2022, ISRO TA 2021 and ESE EE 2013/2015. In all, 41 APDCL/AEGCL questions match a JE/AE/PSU or GATE EC question word for word or with new numbers.
7. **GATE EC is a real source for APDCL and AEGCL.** 10 of their Signals and Control questions are GATE EC questions word for word (2007, 2009, 2010, 2014, 2015, 2016, 2017). APDCL Q64 is the Nyquist-rate example from Proakis and Manolakis (DSP, Ch 1).

## Best practices (what to do with this)

1. **Make GATE EE PYQs the core, all years 1991–2026.** For APGCL this alone covers about three quarters of the paper. Solve every GATE EE question of a chapter after its notes, including the old ones. Our dump has 2007–2026; **1991–2006 must be added** (I will add them chapter by chapter).
2. **Learn the question, not just the answer.** APGCL copies GATE word for word, so knowing each PYQ's exact answer gets marks directly. Re-solve them in revision until they are instant.
3. **Build a fact bank for APDCL/AEGCL-style questions.** For each chapter, keep a short list of one-line facts (uses, definitions, "which bridge measures what", "which relay protects what"). These are cheap marks and they cannot be calculated, only remembered.
4. **Practise textbook numericals without a calculator.** Transformer efficiency, slip/rotor frequency, DC machine EMF, SIL, demand factor, string efficiency: small numbers, one formula each.
4a. **Solve JE/AE/PSU PYQs too, for APDCL/AEGCL.** ISRO Technical Assistant, DSSSB/PSPCL/UPPCL JE-AE, BSNL TTA, MPSC AE, HPCL and DFCCIL papers: 41 APDCL/AEGCL questions were found in them (and in GATE EC) word for word or with new numbers. testbook.com lists these by topic with the exam name.
4b. **For Signals and Control, solve GATE EC PYQs too.** GATE EC 2007–2017 gave 10 APDCL/AEGCL questions word for word.
5. **Revise all three 2023 APSC papers fully.** APSC reused 4 of its own questions within 2023; expect some 2023 questions again in 2026.
6. **Mocks will follow this mix:** APGCL-style mocks about 75 % GATE EE PYQs (spread over all years) + 25 % theory facts; APDCL/AEGCL-style mocks about 20 % GATE EE/ESE + 20 % GATE EC and JE/AE/PSU PYQs + 60 % facts and textbook numericals.
