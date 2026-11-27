# Analysis 1-to-1: source of every APSC 2023 EE question

Files in this folder:
- `apsc-2023-sources.csv`: one row per question (300 rows): paper, q, subject, topic, type, source, source_family,
  source_year, match, gate_marks (1 or 2 in GATE, for GATE EE sources), note.
- `chapter-map.csv`: the same 300 questions placed in portal chapters: chapter, chapter_written, paper, q, topic, source,
  match, gate_marks, in_portal (the content key, if the question is already in the portal).

Published doc: see CLAUDE.md ("Analysis 1-to-1"). Built 27 Nov 2026.

**When writing a chapter:** take its rows from `chapter-map.csv`. Every APSC 2023 question in the chapter goes in, with
its earlier source from `apsc-2023-sources.csv` (as a `sources` entry for GATE EE / ESE EE, or an "Asked before" line in
the solution for other exams). Any traced GATE PYQ that is not in the portal yet goes in too.

## In one line

**APGCL copies GATE EE.** 74 of its 100 questions are GATE EE questions word for word, and 43 of those are from GATE
1992–2006. **APDCL and AEGCL mix many sources:** only 10 each are GATE EE word for word; about 45 more in each come from
GATE EC, ESE and JE/AE/PSU papers, or are standard facts found in question banks. **APSC takes 1-mark and 2-mark GATE
questions alike:** of the 101 GATE EE questions it copied, 51 were 1-mark and 47 were 2-mark in GATE (3 not known).

## The numbers

| Paper | Questions | Traced to an earlier exam or bank | Word-for-word GATE EE | From GATE 1992–2006 | Not traced |
|---|---|---|---|---|---|
| APGCL 2023 (15 Oct, Series A) | 100 | 95 | 74 | 43 | 5 |
| APDCL 2023 (10 Sep, Series B) | 100 | 67 | 10 | 7 | 33 |
| AEGCL 2023 (18 Nov, Series D) | 100 | 72 | 10 | 7 | 28 |

**1-mark or 2-mark in GATE?** For GATE 2007–2026 the question number gives the marks (2007–2009: Q1–20 are 1 mark;
2010–2021: Q1–25 of the EE part; 2022 on: Q11–35). For GATE 1992–2006 the marks come from examside.com's chapter pages,
which list each question under "Marks 1" or "Marks 2". "Copied" = same question, same or new numbers.

| Paper | Copied from GATE EE | 1-mark in GATE | 2-mark in GATE | Marks not shown |
|---|---|---|---|---|
| APGCL 2023 | 75 | 39 | 34 | 2 |
| APDCL 2023 | 12 | 5 | 6 | 1 |
| AEGCL 2023 | 14 | 7 | 7 | 0 |

APSC does not pick only 1-mark questions: almost half are GATE 2-mark numericals, kept with the same numbers. None came
from the old 5-mark descriptive part of GATE (before 2003). APGCL Q10 is marked "2 (unsure)": examside lists it under
Marks 2, but its link says marks-1.

## Where the questions come from

1. **APGCL = GATE EE, old and new.** The copied questions come from 27 different GATE years, 1992 to 2022. The biggest
   single year is GATE 2004 (9 questions). APGCL usually keeps even the GATE options and their order.
2. **Old GATE matters most for APGCL.** 43 of its copied GATE questions are from 1992–2006: papers most students never solve.
3. **APDCL and AEGCL = facts and short numericals from many places.** Besides GATE EE (10 each), they use GATE EC
   (10 Signals and Control questions, 2007–2017), ESE (EE and E&T) and JE/AE/PSU papers: ISRO Technical Assistant, DSSSB,
   PSPCL, BSNL TTA, SSC JE, UPPCL, UPPSC, UPRVUNL, TSPSC, MPSC, TANGEDCO, MPPKVVCL, HPCL, DFCCIL and UGC NET. 41 of their
   questions match these word for word or with new numbers. Most of the rest are one-line facts found in question banks,
   in the wording of J.B. Gupta / V.K. Mehta-type objective books.
4. **Some come straight from textbooks.** APDCL Q22 is P.S. Bimbhra's worked Example 4.6 (latching current); APDCL Q64 is
   the Nyquist-rate example in Proakis and Manolakis (DSP, Ch 1).
5. **APSC repeats itself.** Four questions appear in two of the three 2023 papers: APGCL 46 = AEGCL 35 (Jacobian size),
   APGCL 61 = APDCL 93 (lead/lag compensators), APGCL 81 ≈ APDCL 16 (SAR ADC), APDCL 3 = AEGCL 59 (energy meter).
6. **Subject differences.** Power Systems is the most traceable. Engineering maths: 10 questions in APDCL, none in APGCL
   or AEGCL. 8085: 2 questions in APGCL.

## What to do about it

1. **Make GATE EE PYQs the core, all years 1991–2026, 1-mark and 2-mark both.** For APGCL this alone covers about three
   quarters of the paper. Solve every GATE EE question of a chapter after its notes, old ones included. Our dump has
   2007–2026; **1991–2006 must be added** (chapter by chapter).
2. **Learn the question, not just the answer.** APGCL copies GATE word for word, so knowing each PYQ's exact answer gets
   marks directly. Re-solve them in revision until they are instant.
3. **Work every numerical by hand.** Almost half the copied GATE questions are 2-mark numericals and APSC gives no
   calculator. Keep the steps short: one formula, small numbers.
4. **Build a fact bank for APDCL/AEGCL-style questions.** For each chapter, keep a short list of one-line facts (uses,
   definitions, "which bridge measures what", "which relay protects what"). They cannot be calculated, only remembered.
5. **For APDCL/AEGCL, add JE/AE/PSU and GATE EC PYQs.** ISRO TA, DSSSB/PSPCL/UPPCL/UPRVUNL JE-AE, BSNL TTA, SSC JE and
   state AE papers, plus GATE EC for Signals and Control. testbook.com lists these by topic with the exam name.
6. **Revise all three 2023 APSC papers fully.** APSC reused 4 of its own questions within 2023; expect some 2023
   questions again in 2026.
7. **Mocks follow this mix:** APGCL-style mocks about 75 % GATE EE PYQs (all years) + 25 % theory facts; APDCL/AEGCL-style
   mocks about 20 % GATE EE/ESE + 20 % GATE EC and JE/AE/PSU PYQs + 60 % facts and textbook numericals.

## Chapter by chapter

| Chapter | APSC 2023 questions | Status |
|---|---|---|
| Circuits Ch1: Circuit elements and sources | 7 | Written. All 7 in the portal (APDCL Q43 and AEGCL Q84 added 27 Nov) |
| Circuits Ch2: KCL, KVL and network reduction | 4 | Written. All 4 in the portal |
| Power Systems Ch1: Generation and economics | 5 | Written. All 5 in the portal |
| Circuits Ch3–11 | 16 | Not written yet; listed in the map |
| Power Systems Ch2–17 | 42 | Not written yet; listed in the map |
| Other subjects | 226 | Chapters not planned yet; listed in the map by subject and topic |

The coupled-coil questions (APDCL Q43, AEGCL Q84) sit in Circuits Ch1 ("Mutual inductance: a first look"); when Ch9
(Magnetically coupled circuits) is written, link to them rather than adding them again.

## How this was checked

- **GATE EE 2007–2026 and ESE EE 2017–2026:** full text of every question, compared word by word (from our own dump).
- **GATE EE 1991–2006:** the question lists on examside.com, compared stem by stem. Figures and numbers are not always
  shown there, so a few matches rest on the stem alone (marked in the CSV).
- **Other exams:** exact-phrase web searches on testbook.com (which names the exam a question was asked in), byjus.com,
  sarthaks.com, examveda.com and DuckDuckGo, 27 Nov 2026. 19 very basic textbook facts are marked "Classic textbook fact"
  without a separate search. Of the 66 still not traced, 21 are figure questions or have unreadable scan text.
- **Match levels:** *Same* = same question and numbers; *Numbers changed* = same template, new values; *Same idea* =
  same fact asked differently; *In a bank* = found on a question-bank site, exam not named; *Not traced* = no earlier
  source found. "Traced" counts every level except *Not traced*.
