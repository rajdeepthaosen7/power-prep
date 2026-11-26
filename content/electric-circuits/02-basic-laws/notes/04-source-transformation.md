## Source transformation: the idea

Chapter 1 showed that a practical voltage source ($E$ in series with $r$) and a practical current source ($I$ in parallel with $r$) can be the same thing as seen from outside. Here we use this as a **tool to simplify circuits**.

$$E \text{ in series with } R \quad\Longleftrightarrow\quad I = \frac{E}{R} \text{ in parallel with } R$$

Rules:

- The **same $R$** appears in both forms (series in one, parallel in the other).
- The current source arrow points towards the terminal that was **+** of the voltage source.
- Both forms have the same open-circuit voltage ($E = IR$) and the same short-circuit current ($I = E/R$).
- They are equivalent **only for the outside circuit**. The power lost inside $R$ is not the same in the two forms.
- An ideal voltage source (no series $R$) or an ideal current source (no parallel $R$) cannot be transformed.

**Example 1.** 15 V in series with 4 Ω becomes 3.75 A in parallel with 4 Ω.

## Why it helps

A string of "source + resistor" branches in parallel becomes a set of current sources in parallel (which simply add) and resistors in parallel (which combine). A long chain becomes one source and one resistor.

**Example 2.** A 15 V source with 4 Ω in series feeds a node. From that node to ground: 2 Ω, a 6 A current source feeding the node, and a 3 Ω + 5 Ω path. Find the current in the 5 Ω.

- 15 V and 4 Ω → 3.75 A in parallel with 4 Ω.
- Current sources into the node: $3.75 + 6 = 9.75$ A.
- Resistances from node to ground: 4 Ω, 2 Ω and 8 Ω (3 + 5) in parallel.
- Current divider (Topic 3, Example 5): $I_{8\,\Omega} = \dfrac{39}{28}$ A ≈ 1.39 A.

**Example 3.** A 5 A source in parallel with 4 Ω, then 5 Ω in series, then 6 Ω in parallel with a 10 A source (both current sources feed the top wire). Convert both ends: 20 V with 4 Ω, and 60 V with 6 Ω. Now it is one loop: $I = \dfrac{60 - 20}{4 + 5 + 6} = \dfrac{40}{15} = 2.67$ A through the 5 Ω (from the 10 A side to the 5 A side).

## Millman's theorem: many sources in parallel

When several branches, each "$E_k$ in series with $R_k$", are connected between the **same two nodes**, the voltage between the nodes is

$$V = \frac{\dfrac{E_1}{R_1} + \dfrac{E_2}{R_2} + \dots}{\dfrac{1}{R_1} + \dfrac{1}{R_2} + \dots} = \frac{\sum E_k G_k}{\sum G_k}$$

It is just source transformation done for all branches at once: each branch gives $E_k/R_k$ amperes; the currents add; the resistances combine in parallel. A branch that is only a resistor has $E = 0$ (it adds to the bottom, not the top). A current source branch adds its current directly to the top. Signs: take $E_k$ positive when its + side faces the top node.

**Example 4.** Four branches, each a battery with a series resistor: 200 V/50 Ω, 160 V/40 Ω, 100 V/25 Ω, 80 V/20 Ω, and a fifth branch of 20 Ω only. Each battery branch gives $E/R = 4$ A. Top: $4\times4 = 16$ A. Bottom: $\tfrac1{50} + \tfrac1{40} + \tfrac1{25} + \tfrac1{20} + \tfrac1{20} = 0.185$ S. $V = 16/0.185 \approx 86.5$ V. Current in the plain 20 Ω $\approx 4.32$ A.

**Example 5 (two sources, one middle resistor).** 8 V with 12 Ω, and 2 V with 8 Ω, both + towards node M, and 8 Ω from M to the bottom N. $V_M = \dfrac{8/12 + 2/8}{1/12 + 1/8 + 1/8} = \dfrac{0.667 + 0.25}{0.333} = 2.75$ V. Current in the middle 8 Ω $= 2.75/8 \approx 0.34$ A from M to N.

## Without a calculator

- Look for "$E$/$R$" pairs that give equal or round currents (200/50 = 160/40 = 4 A).
- Work with conductances in a common unit: in Example 5, use $\tfrac1{24}$ as the unit: $\tfrac1{12} = \tfrac{2}{24}$, $\tfrac18 = \tfrac{3}{24}$.
- Check the result lies between the smallest and the largest source voltage when every branch has a source (a weighted average must).
