## Mutual inductance (M): the fourth passive element

The syllabus lists "R, L, C, **M** elements". Here is the basic idea. Coupled circuits are covered fully in Chapter 9.

When two coils are near each other (or on the same core), part of the flux made by coil 1 also links coil 2. So a **changing** current in coil 1 induces a voltage in coil 2, even though no wire connects them:

$$v_2 = M\,\frac{di_1}{dt}$$

$$v_1 = M\,\frac{di_2}{dt}$$

$M$ is the **mutual inductance**, unit henry. The same $M$ works in both directions.

### How strong is the coupling?

$$M = k\sqrt{L_1L_2}$$

with $0 \le k \le 1$.

$k$ is the **coefficient of coupling**. $k = 1$ means all the flux of one coil links the other (perfect coupling); $k = 0$ means no coupling. Note the formula uses the **product** $L_1L_2$ under the root, not the sum.

### The dot convention

The sign of the mutually induced voltage depends on how the coils are wound. Instead of drawing the windings, we put a **dot** on one end of each coil:

> If current **enters** the dotted end of one coil, the voltage it induces in the other coil is **positive at the dotted end** of that coil.

So the dots let us decide whether the mutual voltage term is $+M\,di/dt$ or $-M\,di/dt$.

### Two coupled coils in series

The same current $i$ flows through both coils.

- **Aiding** (current enters both coils at their dots, or leaves both at their dots): $L_{eq} = L_1 + L_2 + 2M$
- **Opposing** (current enters one coil at its dot and the other at its undotted end): $L_{eq} = L_1 + L_2 - 2M$

Measuring both series connections gives $M$ directly:

$$M = \frac{L_{aiding} - L_{opposing}}{4}$$

**Example 1.** Two coils give 500 mH when joined one way and 300 mH when one coil is reversed. $M = (500 - 300)/4 = 50$ mH. Also $L_1 + L_2 = (500 + 300)/2 = 400$ mH.

**Example 2.** $L_1 = 4$ H, $L_2 = 9$ H, $k = 0.5$. $M = 0.5\sqrt{36} = 3$ H. Series aiding: $4 + 9 + 6 = 19$ H; series opposing: $4 + 9 - 6 = 7$ H.

With three or more coupled coils in series, add $\pm 2M$ for **every pair**, deciding each sign from that pair's dots. Chapter 9 gives many examples.

## Summary of this section

| Idea | Formula / fact |
|---|---|
| Mutual voltage | $v_2 = M\,di_1/dt$ |
| Coupling | $M = k\sqrt{L_1L_2}$, $0\le k\le1$ |
| Dot rule | current into a dot ⇒ induced voltage + at the other dot |
| Series | $L_1 + L_2 \pm 2M$ |
| Find M | $(L_{aid} - L_{opp})/4$ |
