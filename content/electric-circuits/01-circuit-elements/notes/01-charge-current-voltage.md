## What is an electric circuit?

Think of the water pipes in your house. A pump pushes water, the water flows through pipes, and somewhere it does useful work (it turns a small wheel, or fills a bucket). An electric circuit is the same idea:

- a **source** (battery, generator) pushes electric charge,
- **wires** carry the charge,
- **elements** (lamp, heater, motor, resistor, capacitor, inductor) use or store the energy that the charge carries.

An electric circuit is a closed path in which charge can flow from the source, through the elements, and back to the source.

In this chapter we study the **building blocks**: the elements and the sources. Every later chapter (KCL, theorems, transients, AC, three-phase) only joins these same blocks in bigger ways. If the blocks are clear, the rest becomes easy.

### Lumped elements: the key assumption

In circuit theory we assume that each element is a **lump**: all its resistance sits in one place, all its capacitance in another place, and the wires between them are perfect (zero resistance, zero length electrically). This is called the **lumped element model**.

It is valid when the circuit is much smaller than the wavelength of the signal. At 50 Hz the wavelength is $\lambda = c/f = 3\times10^8/50 = 6000\ \text{km}$, so a whole substation is "small" and the lumped model works very well. A long transmission line (hundreds of km) is not small, so there we use *distributed* parameters (that is studied in Power Systems).

> **Remember:** All of Electric Circuits (this subject) uses lumped elements. "Distributed" appears only for long lines.

## Charge

Charge is the basic property of matter that causes electric force. Its symbol is $q$ (or $Q$) and its unit is the **coulomb (C)**.

- One electron carries $-1.602\times10^{-19}\ \text{C}$.
- So $1\ \text{C}$ is the charge of about $6.24\times10^{18}$ electrons.

Charge can neither be created nor destroyed. It only moves. This simple fact (conservation of charge) is the real reason behind Kirchhoff's Current Law, which you will meet at the end of this chapter.

## Current

**Current is the rate of flow of charge.**

$$i = \frac{dq}{dt}$$

Unit: ampere (A). $1\ \text{A} = 1\ \text{C/s}$.

If we know the current, we get the charge that has passed by adding up (integrating):

$$q(t) = \int_{t_0}^{t} i\,dt \;+\; q(t_0)$$

Read this as: **charge = area under the current–time graph**. This one idea solves many exam questions on capacitors, because a capacitor stores exactly this charge.

**Example 1.** A current of 2 mA flows for 30 s. Charge passed $= 2\times10^{-3}\times30 = 0.06\ \text{C}$.

**Example 2.** A current rises in a straight line from 0 to 6 mA in 3 µs. The graph is a triangle, so charge $= \tfrac12\times(3\times10^{-6})\times(6\times10^{-3}) = 9\times10^{-9}\ \text{C} = 9\ \text{nC}$.

### Direction of current

By agreement, the direction of current is the direction in which **positive** charge would move. Electrons actually move the opposite way, but we never need that in circuit work.

When you solve a circuit you may not know the real direction. Don't worry: **choose any direction** and mark it with an arrow. If your answer comes out negative, the real current flows opposite to your arrow. That is all. A negative answer is not a mistake.

### DC and AC

- **DC (direct current):** constant with time, $i = I$.
- **AC (alternating current):** changes direction periodically, for example $i = I_m\sin\omega t$.
- Many exam questions use other shapes too: ramps, triangles, steps, pulses. The element laws in this chapter work for **any** shape of current or voltage.

## Voltage

To push charge through an element, energy is needed. **Voltage (potential difference) between two points is the energy needed to move a unit positive charge from one point to the other.**

$$v = \frac{dw}{dq}$$

Unit: volt (V). $1\ \text{V} = 1\ \text{J/C}$.

Voltage is always **between two points**. We mark it with a **+** and a **–** sign. $v_{ab}$ means "potential of $a$ minus potential of $b$":

$$v_{ab} = v_a - v_b$$

$$v_{ba} = -v_{ab}$$

So if $v_{ab} = 5\ \text{V}$, then point $a$ is 5 V higher than point $b$.

> **Remember:** Current flows **through** an element. Voltage appears **across** an element (between its two ends). Never mix the two words.

### Ground (reference)

In a big circuit we pick one point and call it **ground** or the **reference node**, with potential 0 V. Every other "node voltage" is measured from this point. Choosing a different ground changes the node voltages, but it never changes any voltage *between* two points, and never changes any current.

## Summary of this section

| Quantity | Symbol | Unit | Key relation |
|---|---|---|---|
| Charge | $q$ | coulomb (C) | $q = \int i\,dt$ (area under $i$–$t$ graph) |
| Current | $i$ | ampere (A) | $i = dq/dt$ (flows through) |
| Voltage | $v$ | volt (V) | $v = dw/dq$ (appears across) |
