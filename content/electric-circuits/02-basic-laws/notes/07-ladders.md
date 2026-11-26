## The ladder network

A **ladder** is a chain of repeated sections, each with a **series** element and a **shunt** element (to the bottom rail). It looks like a ladder lying on its side.

![A ladder: series elements along the top, shunt elements down to the bottom rail](fig:c2-ladder)

### A finite ladder: start from the far end

Combine from the end **away from the input**:

1. The last shunt and the last series element: series or parallel, whichever they are.
2. Put the result in parallel with the next shunt, then in series with the next series element.
3. Repeat until you reach the input.

**Example 1.** Input, then 2 Ω series, 3 Ω shunt, 1 Ω series, 6 Ω shunt (last).
- Far end: the 1 Ω series element leads into the 6 Ω shunt, so together they form 1 + 6 = 7 Ω.
- In parallel with 3 Ω: $3 \parallel 7 = 2.1$ Ω.
- In series with 2 Ω: $R_{in} = 4.1$ Ω.

### Working backwards for voltages (the "assume 1 A" trick)

To find the output of a ladder, **assume** the current or voltage at the far end (say 1 A or 1 V), work back to the input with Ohm's law and KCL, and then scale everything by (actual input) ÷ (input you got). This works because the network is linear.

**Example 2.** Ladder: 1 Ω series, 1 Ω shunt, 1 Ω series, 1 Ω shunt (last), fed with 13 V. Find the voltage across the last shunt.
- Assume 1 V across the last shunt: it carries 1 A; the series 1 Ω before it carries 1 A too, dropping 1 V. So the middle node is at 2 V.
- Middle shunt carries 2 A. Total current in the first series 1 Ω: $1 + 2 = 3$ A, dropping 3 V. Input $= 2 + 3 = 5$ V.
- Actual input is 13 V, so scale by $13/5$: output $= 13/5 = 2.6$ V.

## The infinite ladder

If the ladder goes on forever, adding one more section at the input does not change the input resistance. That gives an equation.

Let one section be a series $R_s$ then a shunt $R_p$, and let the whole ladder have input resistance $R$. Then

$$R = R_s + (R_p \parallel R) = R_s + \frac{R_pR}{R_p + R}$$

Multiply out: $R^2 - R_sR - R_sR_p = 0$, so

$$R = \frac{R_s + \sqrt{R_s^2 + 4R_sR_p}}{2}$$

(take the positive root: a resistance cannot be negative).

**Example 3.** $R_s = 2$ Ω, $R_p = 1$ Ω: $R^2 - 2R - 2 = 0$ ⇒ $R = 1 + \sqrt3 \approx 2.73$ Ω.

**Example 4 (the same ladder, but starting with a shunt).** If the infinite ladder starts with the 1 Ω **shunt** at the input, followed by the ladder of Example 3, then $R_B = 1 \parallel R_A = \dfrac{R_A}{1 + R_A}$.

**Example 5.** $R_s = R_p = R$: $x^2 - x - 1 = 0$ (with $x = R_{in}/R$) gives $R_{in} = \dfrac{1 + \sqrt5}{2}R \approx 1.618R$ (the golden ratio).

## Without a calculator

- For an infinite ladder, **do not solve the quadratic**: test the options in $R = R_s + R_p \parallel R$. The right option satisfies it.
- Bounds: $R_s < R_{in} < R_s + R_p$ for a ladder starting with a series element.
- For finite ladders, the "assume 1 V at the end" trick needs only additions.
