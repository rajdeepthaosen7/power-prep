## What an inductor is

An inductor is a coil of wire. When current $i$ flows, it creates a magnetic flux $\phi$ that links the $N$ turns of the coil. The **flux linkage** $\lambda = N\phi$ is proportional to the current:

$$\lambda = L\,i$$

$L$ is the **inductance** (self-inductance), unit henry (H). An iron core gives a much larger $L$ than air.

## The voltage–current rule

When the current changes, the flux linkage changes, and by Faraday's law a voltage is induced: $v = d\lambda/dt$. With constant $L$ (passive sign convention):

$$\boxed{\,v = L\,\frac{di}{dt}\,}$$

Read it the same way as the capacitor rule, with $v$ and $i$ swapped:

1. **Voltage appears only while the current is changing.** If $i$ is constant (DC), $v = 0$.
2. So in **DC steady state, an inductor behaves as a short circuit.**
3. The voltage depends on the **slope** of the current graph, not its height. The induced voltage always opposes the change of current (Lenz's law); that is the inductor's "inertia".

## Current from voltage (the integral form)

$$\boxed{\,i(t) = i(t_0) + \frac{1}{L}\int_{t_0}^{t} v\,dt\,}$$

**Change in inductor current = (area under the voltage graph) / L**, plus the starting current. The inductor has **memory**: once the voltage is removed, an ideal (lossless) inductor keeps carrying whatever current it had reached. (A lossless coil short-circuited on itself would carry that current for ever.)

**Example 1.** A 0.5 H inductor's current rises in a straight line from 0 to 4 A in 2 s. Slope $= 2$ A/s, so $v = 0.5\times2 = 1$ V during the rise, and 0 V after the current becomes constant.

**Example 2.** $v = 6t^2$ V is applied to a 4 H inductor for $t \ge 0$ with $i(0) = 0$. $i = \dfrac14\int_0^t 6t^2\,dt = \dfrac14\times2t^3 = 0.5\,t^3$ A.

**Example 3.** A 5 V pulse lasting 3 s is applied to a lossless 2 H inductor (initial current 0). During the pulse $di/dt = 5/2 = 2.5$ A/s, so the current rises linearly to $7.5$ A at 3 s. After the pulse $v = 0$, so $di/dt = 0$ and the current **stays at 7.5 A**. (It does **not** fall back to zero, and it is **not** a pulse.)

### Standard waveform pairs

Because $v = L\,di/dt$, the voltage graph is the **slope graph** of the current graph:

| Current through L | Voltage across L |
|---|---|
| constant (DC) | zero |
| ramp | constant |
| triangular wave | square wave |
| step (sudden jump) | impulse |
| $I_m\sin\omega t$ | $\omega L I_m\cos\omega t$ (voltage leads by 90°) |

And the reverse (voltage given, find current): a constant voltage across an ideal inductor gives a current that **keeps rising in a straight line**.

### Identifying an element from its waveforms

Given the $v$ and $i$ graphs of an unknown element, test each rule:

- Is $v/i$ constant at every instant? ⇒ resistor, $R = v/i$.
- Is $i$ proportional to the **slope** of $v$? ⇒ capacitor, $C = i \big/ \dfrac{dv}{dt}$.
- Is $v$ proportional to the **slope** of $i$? ⇒ inductor, $L = v \big/ \dfrac{di}{dt}$.

**Example 4.** $v = 3$ V for $0 < t < 4$ s and 0 afterwards; $i$ rises linearly from 0 to 6 A over 0–4 s and then stays at 6 A. While $i$ rises, $di/dt = 1.5$ A/s and $v = 3$ V is constant; when $i$ stops changing, $v = 0$. That is the inductor rule: $L = 3/1.5 = 2$ H. (A capacitor would need the *current* to be proportional to the slope of $v$; here $v$ is flat while $i$ is non-zero, so it cannot be a capacitor.)

**Example 5.** $v = 10t$ V and $i = t^2$ A. Slope of $i$ is $2t$, so $v/(di/dt) = 10t/2t = 5$: an inductor of 5 H. (Check capacitor: $i/(dv/dt) = t^2/10$ is not constant, so not a capacitor.)

## The inductor current cannot jump

To change $i$ by a finite amount in zero time, $di/dt$ would be infinite, needing infinite voltage. So:

> **Rule:** The current through an inductor cannot change instantly: $i_L(0^+) = i_L(0^-)$, as long as the voltages are finite.

What voltage source keeps a **constant DC current** through an ideal inductor? Once the current is flowing, $v = L\,di/dt = 0$, no voltage is needed at all. But to *start* that current from zero instantly, the source must give a jump of current, which needs $v = L\,I\,\delta(t)$: an **ideal impulse** of voltage at the start, and zero voltage afterwards.

## A real coil: resistance plus inductance

A practical coil has the resistance $R$ of its wire in series with its inductance $L$:

$$v = R\,i + L\,\frac{di}{dt}$$

**Example 6.** A coil has $R = 4$ Ω and $L = 0.2$ H. At one instant $i = 5$ A and rising at 30 A/s. $v = 4\times5 + 0.2\times30 = 20 + 6 = 26$ V.

## Power and energy in an inductor

$$p = v\,i = L\,i\,\frac{di}{dt}$$

- $p > 0$ while $|i|$ is increasing (energy goes into the field).
- $p < 0$ while $|i|$ is decreasing (the field gives energy back).
- $p = 0$ when $i$ is constant.

**Example 7.** In a 5 mH inductor, $i = 2000t$ A for $0 < t < 1$ ms. $v = 5\times10^{-3}\times2000 = 10$ V, so $p = 10\times2000t = 20\,000\,t$ W, growing linearly with time.

Energy stored as the current rises from 0 to $I$:

$$w = \int_0^I L\,i\,di = \tfrac12 LI^2$$

$$\boxed{\,w_L = \tfrac12 L i^2 = \frac{\lambda^2}{2L} = \tfrac12\lambda i\,}$$

**Example 8.** $L = 3$ H carrying 4 A: $w = \tfrac12\times3\times16 = 24$ J. Using flux linkage: $\lambda = Li = 12$ Wb-turn, $\lambda^2/2L = 144/6 = 24$ J. Same.

### Energy taken by a real coil

For a coil with $R$ and $L$, the energy taken from the source in a time interval is

$$w_{abs} = \int i^2R\,dt + \Delta\!\left(\tfrac12 L i^2\right)$$

The first term is the heat in $R$; the second is the change in energy stored in $L$.

**Example 9.** A coil with $R = 3$ Ω and $L = 1$ H. Its current rises linearly from 0 to 2 A in 3 s and then stays at 2 A until 5 s. Stored at 5 s: $\tfrac12\times1\times2^2 = 2$ J. Heat for 0–3 s ($i = \tfrac23 t$): $\int_0^3 3\cdot\tfrac49t^2\,dt = \tfrac43\cdot9 = 12$ J. Heat for 3–5 s: $2^2\times3\times2 = 24$ J. Total energy absorbed $= 2 + 12 + 24 = 38$ J.

## Inductors in series and in parallel (no magnetic coupling)

If the coils do not share flux, they combine **exactly like resistors**:

- Series: $L_{eq} = L_1 + L_2 + \dots$
- Parallel: $\dfrac{1}{L_{eq}} = \dfrac{1}{L_1} + \dfrac{1}{L_2} + \dots$

If the coils are magnetically coupled, mutual inductance must be added or subtracted (next section).

## Capacitor and inductor side by side (duality)

| | Capacitor | Inductor |
|---|---|---|
| Stores | charge $q = Cv$ | flux linkage $\lambda = Li$ |
| Rule | $i = C\,dv/dt$ | $v = L\,di/dt$ |
| Cannot jump | voltage | current |
| DC steady state | open circuit | short circuit |
| Energy | $\tfrac12Cv^2$ | $\tfrac12Li^2$ |
| Series rule | like R in parallel | like R in series |

Swap $v \leftrightarrow i$ and $C \leftrightarrow L$ and every capacitor result becomes an inductor result. This is called **duality**, and it halves what you need to remember.

## Summary of this section

| Idea | Formula / fact |
|---|---|
| Flux linkage | $\lambda = Li$ |
| Voltage | $v = L\,di/dt$ (slope of $i$) |
| Current | $i = i(0) + \frac1L\int v\,dt$ (area of $v$ over $L$) |
| DC steady state | short circuit |
| Continuity | $i_L$ cannot jump; a sudden DC current needs an impulse of voltage |
| Real coil | $v = Ri + L\,di/dt$ |
| Energy | $\tfrac12Li^2 = \lambda^2/2L$ |
| No coupling | combine like resistors |
