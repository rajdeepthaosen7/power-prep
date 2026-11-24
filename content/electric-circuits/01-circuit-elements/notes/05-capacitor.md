## What a capacitor is

A capacitor is two conductors (plates) separated by an insulator (the **dielectric**). When a voltage $v$ is applied, charge $+q$ collects on one plate and $-q$ on the other. The charge is proportional to the voltage:

$$q = C\,v$$

$C$ is the **capacitance**, unit farad (F). Practical values are in µF ($10^{-6}$), nF ($10^{-9}$) and pF ($10^{-12}$). For parallel plates, $C = \varepsilon A/d$ (bigger plates, thinner gap, better dielectric ⇒ more $C$). In circuits we just use the value $C$.

Symbol: two parallel lines.

![Capacitor and inductor symbols with passive sign convention](fig:c1-lc-symbols)

## The current–voltage rule

Current is the rate of change of charge: $i = dq/dt = d(Cv)/dt$. With constant $C$:

$$\boxed{\,i = C\,\frac{dv}{dt}\,}$$

Read it slowly, it says a lot:

1. **Current flows only while the voltage is changing.** If $v$ is constant (DC), $dv/dt = 0$ and $i = 0$.
2. So in **DC steady state, a capacitor behaves as an open circuit.**
3. The current is decided by the **slope** of the voltage graph, not by its height. A big but steady voltage gives zero current; a small but fast-changing voltage gives a large current.

## Voltage from current (the integral form)

Turn the rule around:

$$\boxed{\,v(t) = v(t_0) + \frac{1}{C}\int_{t_0}^{t} i\,dt\,}$$

In words: **change in capacitor voltage = (area under the current graph) / C**, and we must add the starting voltage $v(t_0)$. Because the present voltage depends on all past current, the capacitor has **memory**.

**Example 1.** A 5 µF capacitor's voltage rises in a straight line from 0 to 20 V in 4 ms. Slope $= 20/0.004 = 5000$ V/s. Current $= 5\times10^{-6}\times5000 = 25$ mA, constant during the rise, and zero after the voltage stops changing.

**Example 2.** An uncharged 0.02 F capacitor is charged by 3 mA for 20 s and then by 1 mA for 40 s. Charge $= 3\times10^{-3}\times20 + 1\times10^{-3}\times40 = 0.06 + 0.04 = 0.1$ C. Voltage $= q/C = 0.1/0.02 = 5$ V.

**Example 3.** An uncharged 2 mF capacitor carries a current that rises linearly from 0 to 50 mA in 4 ms. Area (triangle) $= \tfrac12\times0.004\times0.05 = 1\times10^{-4}$ C. So $v = 10^{-4}/(2\times10^{-3}) = 0.05$ V $= 50$ mV at 4 ms.

### Standard waveform pairs (learn these shapes)

Because $i = C\,dv/dt$, the current graph is the **slope graph** of the voltage graph:

| Voltage across C | Current through C |
|---|---|
| constant (DC) | zero |
| ramp (straight line rising) | constant |
| triangular wave | square wave |
| step (sudden jump) | impulse (infinite spike) |
| $V_m\sin\omega t$ | $\omega C V_m\cos\omega t$ (current leads by 90°) |

**Example 4.** A triangular voltage swings between +5 V and –5 V, taking 2 s to go from top to bottom and 2 s to come back, and it starts at +5 V at $t = 0$. It is applied to a 0.2 F capacitor. Falling part: slope $= -10/2 = -5$ V/s ⇒ $i = 0.2\times(-5) = -1$ A. Rising part: $i = +1$ A. So the current is a square wave: –1 A for 0–2 s, +1 A for 2–4 s, and so on. Notice the **sign**: falling voltage ⇒ negative current.

## The capacitor voltage cannot jump

From $i = C\,dv/dt$: to change $v$ by a finite amount in **zero** time, $dv/dt$ would be infinite, so the current would be infinite. In any circuit with finite currents this is impossible. Hence:

> **Rule:** The voltage across a capacitor cannot change instantly: $v_C(0^+) = v_C(0^-)$, as long as the currents are finite.

This rule is the starting point of all transient problems (Chapter 5).

**What if the circuit is ideal, with no resistance anywhere?** For example, an ideal voltage source connected directly to an uncharged ideal capacitor by an ideal switch. KVL forces the capacitor voltage to equal the source voltage **at the very instant** the switch closes. The voltage does jump, and the current is an **impulse**: a spike of zero width carrying a finite charge $q = C\,\Delta V$. So:

- an ideal voltage source charges an ideal capacitor **immediately** (instantly), with an impulse of current;
- two ideal capacitors at different voltages, joined by an ideal switch, share charge **instantly**, again by an impulse of current.

In a real circuit there is always some resistance, so the charging takes a little time (exponential, studied in Chapter 5).

## Energy stored in a capacitor

Power into the capacitor: $p = vi = Cv\,\dfrac{dv}{dt}$. Energy stored as the voltage rises from 0 to $V$:

$$w = \int p\,dt = \int_0^V Cv\,dv = \frac12 CV^2$$

$$\boxed{\,w_C = \tfrac12 C v^2 = \frac{q^2}{2C} = \tfrac12 q v\,}$$

- The energy depends only on the **present** voltage, not on how it got there.
- It is stored in the electric field; an ideal capacitor does not dissipate it.
- Over one full cycle of AC the capacitor takes energy and gives all of it back: **net energy over a cycle is zero**.

**Example 5.** A 100 µF capacitor at 200 V stores $\tfrac12\times10^{-4}\times200^2 = 2$ J.

### Charging a capacitor from a DC source: half the energy is always lost

A DC source $V$ charges an uncharged capacitor $C$ through a resistor $R$ until it is full.

- Charge delivered by the source $= Q = CV$ (it all goes into the capacitor).
- The source voltage is constant, so **energy given by the source** $= Q\times V = CV^2$.
- **Energy stored** in the capacitor $= \tfrac12 CV^2$.
- So **energy lost in the resistor** $= \tfrac12 CV^2$, exactly half, **whatever the value of $R$**.

**Example 6.** A 10 µF capacitor is charged to 100 V from a 100 V battery through a resistor. Source gives $CV^2 = 0.1$ J; capacitor stores 0.05 J; resistor turns 0.05 J into heat.

> **Remember:** "Energy transferred from the source" while charging a capacitor to the source voltage $V$ is $QV = CV^2$, not $\tfrac12CV^2$.

### Charge sharing between two capacitors

A charged capacitor $C_1$ (at $V_1$) is connected across $C_2$ (at $V_2$). Charge moves until both have the same voltage. **Total charge is conserved** (no charge can leave the closed pair):

$$V_{final} = \frac{C_1V_1 + C_2V_2}{C_1 + C_2}$$

Energy is **not** conserved: some is always lost (in the connecting resistance or as radiation), even when the switch is ideal.

**Example 7.** A 4 µF capacitor at 50 V is connected to an uncharged 1 µF capacitor. $V = \dfrac{4\times50}{5} = 40$ V. Energy before $= \tfrac12\times4\times10^{-6}\times2500 = 5$ mJ; after $= \tfrac12\times5\times10^{-6}\times1600 = 4$ mJ; lost $= 1$ mJ.

## Capacitors in parallel and in series

**Parallel:** same voltage on all, charges add: $Q = (C_1 + C_2 + \dots)V$, so

$$C_{eq} = C_1 + C_2 + \dots \quad(\text{like resistors in series})$$

**Series:** the same charge $Q$ sits on every capacitor (the inner plates are isolated, so charge only shifts along). Voltages add: $V = Q/C_1 + Q/C_2 + \dots$, so

$$\frac{1}{C_{eq}} = \frac{1}{C_1} + \frac{1}{C_2} + \dots \quad(\text{like resistors in parallel})$$

So the rules for capacitors are the **opposite** of the rules for resistors.

In series, each capacitor's voltage is $Q/C$: the **smallest capacitor takes the largest voltage**.

**Example 8.** 3 µF and 6 µF in series across 90 V. $C_{eq} = \dfrac{3\times6}{9} = 2$ µF, $Q = 2\times90 = 180$ µC, $V_{3\mu F} = 180/3 = 60$ V, $V_{6\mu F} = 30$ V.

### Maximum safe voltage of a capacitor group

Each capacitor has a voltage rating (breakdown voltage). For a **series** group, the common charge must not exceed the **smallest** "maximum charge" $Q_{max} = C\times V_{rated}$ of the capacitors in that series string.

**Example 9.** 4 µF (rated 20 V) in series with 2 µF (rated 15 V). Maximum charges: $4\times20 = 80$ µC and $2\times15 = 30$ µC. The string can carry only 30 µC. Then $V = 30/4 + 30/2 = 7.5 + 15 = 22.5$ V maximum. (Not $20 + 15 = 35$ V!)

If this series string is in parallel with another capacitor, the parallel capacitor also sees this same voltage; check that it is within its own rating, and add its charge to get the total charge.

## Two useful circuit cases

**Capacitor fed by an ideal current source $I$:** the voltage rises in a straight line:
$$v(t) = v(0) + \frac{I}{C}\,t$$
**Example 10.** A 0.5 µF capacitor with initial voltage +3 V is fed by an ideal 10 mA source (current entering its + terminal): $v = 3 + \dfrac{0.01}{0.5\times10^{-6}}t = 3 + 20\,000\,t$ V.

**Capacitor in parallel with a resistor, driven by a voltage $v(t)$:** the source current is the sum of both branch currents:
$$i = \frac{v}{R} + C\frac{dv}{dt}$$
For a triangular $v$, the first term is a triangle and the second is a square wave, so the total is a square wave with sloping tops.

## Summary of this section

| Idea | Formula / fact |
|---|---|
| Charge | $q = Cv$ |
| Current | $i = C\,dv/dt$ (slope of $v$) |
| Voltage | $v = v(0) + \frac1C\int i\,dt$ (area of $i$ over $C$) |
| DC steady state | open circuit |
| Continuity | $v_C$ cannot jump (finite current); ideal source ⇒ impulse current |
| Energy | $\tfrac12Cv^2 = q^2/2C$; net zero over an AC cycle |
| Charging from DC | source gives $CV^2$, C stores $\tfrac12CV^2$, half is lost |
| Parallel / series | $C_1 + C_2$ / $\dfrac{C_1C_2}{C_1+C_2}$ (opposite of R) |
| Series string | same $Q$; smallest $C$ takes most voltage |
