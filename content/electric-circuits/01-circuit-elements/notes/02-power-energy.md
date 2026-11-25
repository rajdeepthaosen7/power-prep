## Power: the rate of using energy

Voltage tells us the energy per coulomb. Current tells us coulombs per second. Multiply them and we get energy per second, which is **power**:

$$p = \frac{dw}{dt} = \frac{dw}{dq}\cdot\frac{dq}{dt} = v\,i$$

Unit: watt (W). $1\ \text{W} = 1\ \text{J/s}$.

Here $p$ is the **instantaneous power**: the power at one instant of time. If $v$ and $i$ change with time, $p$ also changes with time.

## The passive sign convention (the most important rule of this chapter)

An element has two terminals. We mark a voltage $v$ across it (with + and –) and a current $i$ through it (with an arrow). There are only two ways the arrow can sit:

![Passive sign convention: the current arrow enters the terminal marked +](fig:c1-passive-sign)

**Passive sign convention:** the current arrow **enters the + terminal**. When we mark $v$ and $i$ this way:

$$p = v\,i$$

is the power **absorbed** by the element.

- If $p > 0$: the element **absorbs** (takes in) power. A resistor, a charging battery, a capacitor being charged.
- If $p < 0$: the element **delivers** (gives out) power. A battery driving a lamp.

If the current arrow **leaves** the + terminal (enters the – terminal), then $v\,i$ is the power **delivered** by the element, so the absorbed power is $-v\,i$.

> **Remember (two-line rule):**
> Current enters + → absorbed power $= +vi$.
> Current enters – → absorbed power $= -vi$.

Think of it like money. "Absorbed $= -100$ W" means exactly the same as "delivered $= +100$ W", in the same way that "spent ₹ –100" means "earned ₹ 100".

**Example 1.** A 12 V battery has 3 A entering its + terminal (it is being charged). Absorbed power $= 12\times3 = 36$ W. The battery takes in 36 W.

**Example 2.** A 12 V battery has 3 A leaving its + terminal (it is driving a load). Current enters the – terminal, so absorbed power $= -12\times3 = -36$ W, i.e. it **delivers** 36 W.

**Example 3.** An element has $v = 8$ V (+ at top) and a current of 2 A flowing **upward** through it (entering at the bottom, the – terminal). Absorbed power $= -8\times2 = -16$ W. This element is a source: it delivers 16 W.

> **Trap:** Some questions say "power supplied" but write the options as *absorbed* power (a source shown as a negative number), because textbooks such as Sadiku list absorbed power. Work out both sets: absorbed powers, and supplied powers (the same numbers with the opposite sign). If only one set appears among the options, that is the intended one. If both appear, go by the exact words of the question.

## Power balance (conservation of energy)

Energy cannot be created or destroyed. So, at every instant, in any complete circuit:

$$\sum p_{\text{absorbed}} = 0$$

In words: the total power delivered by some elements = the total power absorbed by the others.

This is called **Tellegen's balance** or simply power balance. It is a powerful check. After solving any circuit, add up the absorbed powers of all elements: the total must be zero.

**Example 4.** A 10 V source drives 2 A through a resistor. Source absorbs $-20$ W (delivers 20 W). Resistor absorbs $+20$ W. Sum $= 0$. ✔

If you are given only **part** of a circuit (a few elements), the sum of their absorbed powers need not be zero, because the rest of the circuit can give or take power.

## Energy

Energy is power added up over time:

$$w = \int_{t_1}^{t_2} p\,dt$$

- For constant power: $w = p\times t$.
- Unit: joule (J). The electricity bill uses kilowatt-hour: $1\ \text{kWh} = 1000\times3600 = 3.6\times10^6\ \text{J}$.

**Example 5.** A constant current of 3 A flows for 20 s through a heater, and 1.8 kJ of heat is produced. Power $= 1800/20 = 90$ W. Voltage across the heater $= p/i = 90/3 = 30$ V.

**Example 6.** A 100 W lamp is used for 5 hours a day for 30 days. Energy $= 100\times5\times30 = 15\,000$ Wh $= 15$ kWh (units).

## Summary of this section

| Idea | Formula / rule |
|---|---|
| Instantaneous power | $p = v\,i$ |
| Passive sign convention | current enters + ⇒ $vi$ = absorbed power |
| Absorbed vs delivered | $p_{\text{abs}} = -p_{\text{del}}$ |
| Power balance | $\sum p_{\text{abs}} = 0$ for a complete circuit |
| Energy | $w = \int p\,dt$; $1\ \text{kWh} = 3.6\ \text{MJ}$ |
