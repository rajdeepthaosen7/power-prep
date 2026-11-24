## What a source does

R, L and C cannot keep a circuit running on their own: they only use or store energy. A **source** supplies energy. An **independent source** has a value that does not depend on anything else in the circuit: a battery, a generator, a lab power supply.

There are two ideal sources. Learn each one through its v–i rule.

![Symbols of independent and dependent sources](fig:c1-source-symbols)

## The ideal voltage source

> An **ideal voltage source** keeps a fixed voltage $V_s$ between its terminals, **whatever current flows through it**.

- Its terminal voltage is completely independent of the current (and of the load).
- The **current** through it is **not** fixed by the source: it is decided by the circuit connected to it.
- Its internal resistance is **zero** (there is no internal voltage drop).
- On a graph of $v$ against $i$ it is a horizontal line at $v = V_s$.
- It can **deliver** power (current leaves its + terminal) or **absorb** power (current enters its + terminal, like a battery being charged).
- A voltage source of value zero ($V_s = 0$) is a **short circuit**.

## The ideal current source

> An **ideal current source** keeps a fixed current $I_s$ through itself, **whatever voltage appears across it**.

- The current is completely independent of the voltage.
- The **voltage across** it is decided by the circuit connected to it, and can be positive, negative or zero.
- Its internal resistance is **infinite**.
- On a graph of $v$ against $i$ it is a vertical line at $i = I_s$.
- A current source of value zero ($I_s = 0$) is an **open circuit**.

> **Remember:** For a voltage source you must find its **current** from the rest of the circuit. For a current source you must find its **voltage** from the rest of the circuit.

## Which connections are allowed

Because each ideal source forces one quantity, some connections are impossible:

| Connection | Allowed? | Why |
|---|---|---|
| Two ideal voltage sources of **different** values in **parallel** | ✗ | Parallel elements must have the same voltage (KVL). |
| An ideal voltage source **short-circuited** | ✗ | The short forces $v = 0$, the source forces $v = V_s$. |
| Two ideal current sources of **different** values in **series** | ✗ | Series elements must carry the same current (KCL). |
| An ideal current source **left open** | ✗ | The open forces $i = 0$, the source forces $i = I_s$. |
| Voltage source **in parallel with a current source** | ✓ | The voltage is set by the voltage source; KCL gives the voltage source's current. |
| Voltage source **in series with a current source** | ✓ | The current is set by the current source; KVL gives the current source's voltage. |

Two useful consequences for analysis:

- Anything connected **in parallel with an ideal voltage source** does not change the voltage or current seen by the rest of the circuit. For the outside world it can be removed (it only changes the source's own current).
- Anything connected **in series with an ideal current source** does not change the current delivered to the rest of the circuit. For the outside world it can be removed (it only changes the source's own voltage).

## Practical sources

A real battery's voltage falls when it supplies more current, because of its internal resistance $r$. Model it as an ideal source **plus** a resistor.

### Practical voltage source: $E$ in series with $r$

$$V = E - I\,r$$

This straight line on the $V$–$I$ graph is the source's **load line**. It cuts the axes at:

- open-circuit voltage $V_{oc} = E$ (when $I = 0$),
- short-circuit current $I_{sc} = E/r$ (when $V = 0$).

![Practical source load line, with the load resistance line crossing it at the operating point](fig:c1-load-line)

Connect a load $R_L$. The load's own line is $V = IR_L$. Where the two lines cross is the **operating point**:

$$I = \frac{E}{r + R_L}, \qquad V = E\,\frac{R_L}{r + R_L}$$

**Example 1.** A 12 V battery with $r = 0.5$ Ω feeds 5.5 Ω. $I = 12/6 = 2$ A; terminal voltage $V = 12 - 2\times0.5 = 11$ V.

**Example 2.** A source has the load line $v + 2i = 60$ ($v$ in volts, $i$ in amperes). Then $V_{oc} = 60$ V, $I_{sc} = 30$ A, $r = V_{oc}/I_{sc} = 2$ Ω. With a 3 Ω load: $v = 3i$, so $3i + 2i = 60$ ⇒ $i = 12$ A.

### Practical current source: $I_s$ in parallel with $r$

$$I = I_s - \frac{V}{r}$$

Same load line, written the other way. $I_{sc} = I_s$ and $V_{oc} = I_s\,r$.

### Converting one into the other (source transformation)

A voltage source $E$ in series with $r$ and a current source $I_s$ in parallel with the **same** $r$ behave identically at their terminals, when

$$E = I_s\,r \qquad (\text{same } r)$$

Two sources are equivalent at their terminals if and only if they have the **same open-circuit voltage and the same short-circuit current**. Then $r = V_{oc}/I_{sc}$ is automatically the same too. (They are equivalent only for the outside circuit; the power lost *inside* the two models is different.)

**Example 3.** 24 V in series with 4 Ω ⇔ 6 A in parallel with 4 Ω. Check: both give $V_{oc} = 24$ V and $I_{sc} = 6$ A.

An **ideal** voltage source ($r = 0$) has $I_{sc} = \infty$, and an ideal current source ($r = \infty$) has $V_{oc} = \infty$. So **an ideal voltage source cannot be converted into a current source**, and vice versa.

### When does a source behave as "constant voltage" or "constant current"?

Compare the internal resistance $r$ with the load $R_L$:

- $r \ll R_L$ (say $r = 2$ Ω, $R_L = 2$ kΩ): almost all of $E$ appears across the load, and changes in the load hardly change $V$. The source behaves as a **constant-voltage source**.
- $r \gg R_L$ (say $r = 100$ kΩ, $R_L = 100$ Ω): the current is almost $E/r$ whatever the load. The source behaves as a **constant-current source**.

This is why a good voltage source has very low internal resistance and a good current source has very high internal resistance.

## A first word on maximum power

From a practical source ($E$, $r$), the load power is $P = \left(\dfrac{E}{r+R_L}\right)^2R_L$. It is largest when $R_L = r$, giving $P_{max} = \dfrac{E^2}{4r}$. Chapter 4 proves this and extends it.

For a source that is **not** a simple $E$ and $r$, do not use $R_L = r$ blindly. Draw its actual v–i graph and find the point where $V\times I$ is largest.

**Example 4.** A bench supply works as an ideal 2 A current source as long as its voltage is below 12 V, and as an ideal 12 V voltage source otherwise. Along the current-source part, $P = 2V$ grows with $V$; along the voltage-source part, $P = 12I$ grows with $I$. So the maximum is at the corner: $V = 12$ V, $I = 2$ A, $P = 24$ W, reached with $R_L = 12/2 = 6$ Ω.

## The v–i graph of a source with a resistor

Combining a source and a resistor shifts and tilts the line:

- Current source $I_s$ in **parallel** with $R$, terminal current $I$ measured into the combination in the same direction as $I_s$ flows inside it: $I = I_s + V/R$. The graph starts at $I_s$ when $V = 0$ and **rises** with slope $1/R$.
- Voltage source $E$ in **series** with $R$: $V = E + IR$ (for current entering +) — a line through $(0, E)$ with slope $R$.

Always write the rule first, then sketch it; do not guess from the picture.

**Example 5.** A resistor $R$ connected to an ideal current source of $I$ amperes takes 32 W; connected to an ideal voltage source of the **same number** of volts ($V = I$ numerically) it takes 8 W. Then $I^2R = 32$ and $I^2/R = 8$. Dividing: $R^2 = 4$ ⇒ $R = 2$ Ω and $I = 4$ A.

## Summary of this section

| Source | Fixed quantity | Decided by the circuit | Internal r | Zero value acts as |
|---|---|---|---|---|
| Ideal voltage source | $v = V_s$ | its current | 0 | short circuit |
| Ideal current source | $i = I_s$ | its voltage | ∞ | open circuit |

- Not allowed: unequal voltage sources in parallel; unequal current sources in series.
- Practical voltage source: $V = E - Ir$; practical current source: $I = I_s - V/r$; equivalent when $E = I_s r$.
- Equivalent ⇔ same $V_{oc}$ and same $I_{sc}$.
- $r \ll R_L$ ⇒ constant voltage; $r \gg R_L$ ⇒ constant current.
