## The bridge network

![Bridge: four arms P, Q, R, S with a middle branch between C and D](fig:c2-bridge)

Four resistors form a diamond A–C–B–D: $P$ (A–C), $Q$ (C–B), $R$ (A–D), $S$ (D–B). A fifth branch (a resistor or a galvanometer) joins C and D. The supply is connected across A and B.

The middle branch makes the network neither series nor parallel. But in one important case it carries **no current**.

## The balance condition

The bridge is **balanced** when C and D are at the same potential. Then no current flows in the middle branch, whatever it is.

Find $V_C$ and $V_D$ by the voltage divider (with the middle branch removed). With $V$ across A–B and B as 0 V:

$$V_C = V\frac{Q}{P + Q},\qquad V_D = V\frac{S}{R + S}$$

These are equal when $\dfrac{Q}{P + Q} = \dfrac{S}{R + S}$, which simplifies to

$$\boxed{\;\frac{P}{Q} = \frac{R}{S}\quad\text{or}\quad PS = QR\;}$$

**Products of opposite arms are equal.** The same ratio idea works for impedances in AC bridges (Measurements).

## What to do with a balanced bridge

Because the middle branch carries no current, you may either:

- **remove** it (open it): then $R_{AB} = (P + Q) \parallel (R + S)$; or
- **short** it (join C and D): then $R_{AB} = (P \parallel R) + (Q \parallel S)$.

Both give the same answer, and the middle resistor's value does not matter.

**Example 1.** All five resistors are 10 Ω. Balanced ($10\times10 = 10\times10$). $R_{AB} = 20 \parallel 20 = 10$ Ω.

**Example 2.** 1 kΩ in all four arms, 2 kΩ in the middle, 6 V across A–B. Balanced, so the current in the 2 kΩ is **0 mA**.

**Example 3.** Arms 2, 4 (top path) and 3, 6 (bottom path), middle 5 Ω. $2\times6 = 4\times3$: balanced. $R_{AB} = 6 \parallel 9 = 3.6$ Ω.

## Voltage between the middle points of an unbalanced bridge

If the bridge is not balanced and the middle branch is **open** (no element, just two terminals A and B to measure), find each side's potential by the divider and subtract.

**Example 4.** 10 V across the bridge. Left path: 10 Ω then 20 Ω; right path: 30 Ω then 40 Ω (from the + end). Taking the − end as 0 V: point on the left $= 10\times\dfrac{20}{30} = 6.67$ V; point on the right $= 10\times\dfrac{40}{70} = 5.71$ V. Difference $\approx 0.95$ V.

If the middle branch has a resistor and the bridge is unbalanced, use star–delta (Topic 5), Thevenin (Chapter 4) or node analysis (Chapter 3).

## Symmetry: points at the same potential

Balance is a special case of a bigger idea:

> **If two points must be at the same potential, any element between them carries no current, and the two points may be joined or separated as you like.**

How to spot equal potentials: look for a **mirror line** in the network. If the network looks the same when reflected, with A and B swapped in a matching way, then points that map onto each other across the mirror (or that lie on a line that must sit half-way between the terminals) are at the same potential.

**Example 5 (crossed square).** A 5 V source is on the left side of a square. The top and bottom sides are 1 Ω each, the two diagonals are 1 Ω each, and a 2 Ω resistor is on the right side. Each right corner is joined to **both** left corners by 1 Ω (one straight, one diagonal). By symmetry, each right corner sits exactly half-way between the two left corners: both are at 2.5 V. So the 2 Ω resistor has 0 V across it: **current 0**.

**Example 6 (twelve equal resistors on a cube, between opposite corners).** From corner A, three edges go out to three corners that are all alike: they are at one potential and may be joined. In the same way the three corners next to B are at one potential. So $R = \dfrac{R}{3} + \dfrac{R}{6} + \dfrac{R}{3} = \dfrac{5}{6}R$.

## Without a calculator

- Always test $PS = QR$ first. If it holds, the middle branch disappears.
- Look for mirror symmetry before writing any equation.
- In a balanced bridge the middle element's value is a **distractor**.
