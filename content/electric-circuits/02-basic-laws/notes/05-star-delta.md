## Why we need star–delta

Some networks have no series or parallel pair anywhere (a bridge with five resistors is the usual example). Converting a group of three resistors from one shape to the other creates series and parallel pairs, and then the network reduces.

![Delta (left) and its equivalent star (right)](fig:c2-star-delta)

- **Delta (Δ, also π):** three resistors forming a triangle between three nodes a, b, c.
- **Star (Y, also T):** three resistors from a, b, c to one common centre point.

The two are equivalent if the resistance measured between **every pair** of terminals is the same.

Naming used here: in the delta, $R_a$ is the side **opposite** node a (between b and c), $R_b$ is opposite b (between c and a), $R_c$ is opposite c (between a and b). In the star, $R_1$ is the arm at node a, $R_2$ at b, $R_3$ at c.

## Delta to star

$$R_1 = \frac{R_bR_c}{R_a + R_b + R_c},\quad R_2 = \frac{R_cR_a}{R_a + R_b + R_c},\quad R_3 = \frac{R_aR_b}{R_a + R_b + R_c}$$

**Rule:** star arm at a node = (product of the two delta sides **touching** that node) ÷ (sum of all three sides).

**Derivation (one pair is enough).** Between a and b, the star gives $R_1 + R_2$. The delta gives $R_c \parallel (R_a + R_b) = \dfrac{R_c(R_a + R_b)}{R_a + R_b + R_c}$. Write the same for the other two pairs, add two of them and subtract the third, and the formulas above appear.

## Star to delta

$$R_a = \frac{R_1R_2 + R_2R_3 + R_3R_1}{R_1},\quad R_b = \frac{\text{same top}}{R_2},\quad R_c = \frac{\text{same top}}{R_3}$$

**Rule:** delta side = (sum of the products of the star arms taken two at a time) ÷ (the star arm **opposite** that side).

In conductances the star-to-delta rule looks like delta-to-star: $G_{ab} = \dfrac{G_1G_2}{G_1 + G_2 + G_3}$.

## Special cases to remember

- **All equal:** $R_Y = \dfrac{R_\Delta}{3}$, $R_\Delta = 3R_Y$.
- **Scaling:** if every delta resistor is multiplied by $k$, every star arm is multiplied by $k$ too (top is $k^2$, bottom is $k$). Resistance networks scale linearly.
- The star arms are always **smaller** than the delta sides they replace.

**Example 1.** $R_a = 20$ Ω, $R_b = 10$ Ω, $R_c = 10$ Ω. Sum = 40 Ω.
- $R_1$ (at a) $= \dfrac{R_bR_c}{40} = \dfrac{100}{40} = 2.5$ Ω.
- $R_2$ (at b) $= \dfrac{R_cR_a}{40} = \dfrac{200}{40} = 5$ Ω.
- $R_3$ (at c) $= \dfrac{R_aR_b}{40} = 5$ Ω.

**Example 2.** A star of three 4 Ω arms is the same as a delta of three 12 Ω sides.

**Example 3 (an unbalanced bridge).** Between A and B: 10 Ω (A–C), 20 Ω (A–D), 10 Ω (C–D), 30 Ω (C–B), 20 Ω (D–B). Convert the delta A–C–D (10, 20, 10; sum 40) to a star with centre O:
- arm at A $= \dfrac{10\times20}{40} = 5$ Ω, arm at C $= \dfrac{10\times10}{40} = 2.5$ Ω, arm at D $= \dfrac{20\times10}{40} = 5$ Ω.
- Now: A–O 5 Ω; then two paths to B: $2.5 + 30 = 32.5$ Ω and $5 + 20 = 25$ Ω in parallel $= \dfrac{32.5\times25}{57.5} \approx 14.1$ Ω.
- $R_{AB} \approx 19.1$ Ω.

## Without a calculator

- Equal resistors: divide or multiply by 3, nothing more.
- For delta to star, the denominator is the same for all three arms; compute it once.
- Before converting, check if the bridge is **balanced** (next topic). Then no conversion is needed.
