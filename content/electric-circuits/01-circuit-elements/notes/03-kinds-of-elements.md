## An element is a rule between its voltage and current

Every two-terminal element is fully described by **one rule that connects its voltage $v$ and its current $i$**. Nothing else about it matters for circuit analysis.

| Element | Its rule (v–i relation) |
|---|---|
| Resistor | $v = R\,i$ |
| Capacitor | $i = C\,\dfrac{dv}{dt}$ |
| Inductor | $v = L\,\dfrac{di}{dt}$ |
| Short circuit | $v = 0$ (any current) |
| Open circuit | $i = 0$ (any voltage) |
| Ideal voltage source | $v = V_s$ (any current) |
| Ideal current source | $i = I_s$ (any voltage) |

This "rule" view is very useful. It also explains two strange elements that appear in theory and in GATE:

- **Nullator:** both $v = 0$ **and** $i = 0$ at the same time.
- **Norator:** no rule at all, $v$ and $i$ can be anything.

When elements are joined, their rules combine. For example, in **series** every element carries the **same current**. So if one element in a series string forces $i = 0$, the whole string carries $i = 0$, and you can then check what that does to the total voltage.

## Linear and non-linear elements

An element is **linear** if its v–i rule obeys **superposition**. Superposition has two parts:

1. **Additivity:** if input $x_1$ gives output $y_1$ and $x_2$ gives $y_2$, then $x_1 + x_2$ gives $y_1 + y_2$.
2. **Homogeneity (scaling):** if $x$ gives $y$, then $kx$ gives $ky$ for any constant $k$.

For a resistive element this means: **its v–i graph is a straight line through the origin**.

- $v = 5i$: linear (straight line through origin).
- $v = i^2$: non-linear (curve).
- $v = 5i + 2$: **non-linear!** The graph is a straight line, but it does not pass through the origin. Check: $i = 1$ gives $v = 7$, but $i = 2$ gives $v = 12$, not $14$. Homogeneity fails.

> **Trap:** "Straight line" is not enough. A linear element's graph must be a straight line **through the origin**.

R, L and C with constant values are linear, because $v = Ri$, $i = C\,dv/dt$ and $v = L\,di/dt$ all obey scaling and adding (differentiation is a linear operation).

**Non-linear examples:** diode, transistor, thermistor, lamp filament (its resistance rises a lot as it heats), electric arc, iron-cored coil when the core saturates.

### Where do independent sources fit?

An ideal DC source has the rule $v = V_s$ for every current. Its graph is a horizontal line that does not pass through the origin, so it does not satisfy homogeneity. That is why, when a question asks "which **elements** are linear", we count **R, L and C**, and not the source and not the diode. In circuit theory, independent sources are treated as the **inputs** (excitations) of a circuit, not as its linear elements. A "linear circuit" means: linear elements + independent sources.

### Why linearity matters

Superposition, Thevenin, Norton, and almost every theorem in Chapter 4 need linearity. For a non-linear element you cannot write one fixed "resistance". To solve a circuit with a non-linear element you:

1. write the straight-line equation of the rest of the circuit (the **load line**), and
2. solve it together with the non-linear rule, either by algebra (often a quadratic) or graphically (where the two graphs cross).

Ohm's law $v = Ri$ with one fixed $R$ cannot be used for the non-linear element itself.

## Bilateral and unilateral elements

An element is **bilateral** if it behaves the same for current in either direction: reversing $v$ simply reverses $i$, with the same size.

- R, L, C are bilateral.
- A diode is **unilateral**: it conducts one way only.

Note that a **non-linear element can still be bilateral**. Example: $v = i^3$ gives $v = -8$ for $i = -2$ and $v = +8$ for $i = +2$: same behaviour both ways, so bilateral, but clearly non-linear. A lamp filament is also non-linear but bilateral. Linearity and bilaterality are two **separate** properties.

## Active and passive elements

- A **passive** element can never give out more energy than it has taken in. The total energy it absorbs from the start, $\int_{-\infty}^{t} p\,dt$, is never negative.
- An **active** element can deliver net energy to the circuit for an unlimited time: batteries, generators, and the dependent sources used to model transistors and op-amps.

R, L and C are passive. But they are passive in different ways:

| Element | What it does with energy |
|---|---|
| Resistor | **dissipates** it (turns it into heat). It never gives anything back. |
| Inductor | **stores** it in the magnetic field. It can give it back, but only what it stored. |
| Capacitor | **stores** it in the electric field. It can give it back, but only what it stored. |

So a pure (ideal) inductor or capacitor **never dissipates** energy: it only stores and returns it. Over one full cycle of AC, an ideal capacitor or inductor takes energy in during one quarter-cycle and gives the same amount back in the next. Net energy over a full cycle $=0$. A conductor or a real resistor always dissipates.

## Other ways to classify elements

| Property | Meaning | Examples |
|---|---|---|
| **Lumped vs distributed** | Lumped: element sits at one point. Distributed: spread over a length. | R, L, C are lumped; a long transmission line is distributed. |
| **Time-invariant vs time-varying** | Does the element value change with time? | A fixed 10 Ω resistor is time-invariant; $R(t) = 2 + \sin t$ is time-varying. |
| **With memory vs memoryless** | Does today's $v$ depend on past $i$? | R is memoryless; L and C have memory (they "remember" stored energy). |

## Summary of this section

- An element = one v–i rule. Series ⇒ same current; parallel ⇒ same voltage.
- Linear ⇔ superposition (additivity + homogeneity) ⇔ straight line **through origin**.
- Non-linear elements can be bilateral. Linearity ≠ bilaterality.
- R dissipates; L and C only store (net zero energy per AC cycle). All three are passive.
- In "which elements are linear" questions, count R, L, C (not sources, not diodes).
