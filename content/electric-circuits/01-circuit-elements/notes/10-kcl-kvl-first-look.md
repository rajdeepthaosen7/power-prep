## Why we need two more rules

Each element gives one rule between its own $v$ and $i$. To join elements into a circuit we need two more rules about how the elements are **connected**. These are Kirchhoff's laws. Chapter 2 and 3 use them fully (nodal and mesh analysis). Here you need only the simple form, so that you can solve one-node and one-loop questions on elements and sources.

First, two words:

- **Node:** a point where two or more elements meet (all the wire joined to it counts as one node).
- **Loop:** any closed path that does not pass through the same node twice.

## Kirchhoff's Current Law (KCL)

> **The algebraic sum of the currents leaving a node is zero.** Equivalently: total current entering = total current leaving.

$$\sum i_{\text{leaving}} = 0$$

or, the same thing: $\sum i_{\text{in}} = \sum i_{\text{out}}$.

Reason: charge is conserved and a node cannot store charge, so whatever flows in must flow out.

KCL also holds for any **closed surface** (a "supernode" or "cut"): draw a closed boundary around any part of a circuit, even around a whole network. The total current crossing the boundary outward is zero.

**Example 1.** Three wires connect box A to box B. Currents from A to B are 2 A in wire 1, –5 A in wire 2. The current from A to B in wire 3 must be $0 - 2 - (-5) = 3$ A.

**Consequence: series elements carry the same current.** Two elements joined at a node with nothing else connected there: what enters must leave.

## Kirchhoff's Voltage Law (KVL)

> **The algebraic sum of the voltages around any closed loop is zero.**

$$\sum v_{\text{around a loop}} = 0$$

Reason: potential is single-valued. Walk round a loop and come back to the start: you must be back at the same potential (and the energy gained by a charge equals the energy lost).

Practical method: walk round the loop in one direction. Add a voltage when you go from – to + (a rise) and subtract when you go from + to – (a drop), or the other way round, but be consistent.

**Consequence: parallel elements have the same voltage.** Two elements across the same pair of nodes form a loop of two elements, so their voltages must be equal.

## Using the laws with ideal sources

Remember from the sources section:

- The **voltage across a current source** is found by **KVL** round a loop containing it.
- The **current through a voltage source** is found by **KCL** at one of its nodes.

**Example 2.** One loop contains: an ideal 6 V source (+ at the top, on the left), a 3 Ω resistor along the top, and an ideal 2 A current source on the right with its arrow pointing **up**.

- The current source fixes the loop current: 2 A, upward through the current source, then **leftward** along the top through 3 Ω, then **down** through the 6 V source (entering its + terminal).
- Voltage across the 3 Ω resistor $= 2\times3 = 6$ V, higher at its right end (current flows from + to – through a resistor).
- Take the bottom wire as 0 V. Top-left node $= 6$ V (the voltage source). Top-right node $= 6 + 6 = 12$ V.
- So the voltage across the current source (top with respect to bottom) is **12 V**.

Power check: current source: current leaves its + (top) terminal ⇒ delivers $12\times2 = 24$ W. Resistor absorbs $2^2\times3 = 12$ W. 6 V source: current enters its + terminal ⇒ absorbs $6\times2 = 12$ W (it is being charged). $24 = 12 + 12$ ✔.

**Example 3 (KCL with a dependent source).** At one node, a current $I$ arrives through resistor $R_1$, a CCCS injects $0.5\,I$ into the node, and the only path out is a resistor carrying 9 A away. KCL: $I + 0.5I = 9$ ⇒ $I = 6$ A. If $R_1$ is fed from an ideal 40 V source whose current is this same $I$ leaving its + terminal, the source supplies $40\times6 = 240$ W.

**Example 4 (a node voltage that makes a branch current zero).** A node X connects: an ideal 10 V source through a resistor $\alpha$ Ω (to ground), a 2 Ω resistor from X to ground, and an ideal 5 A current source feeding X. KCL at X: $\dfrac{V_X - 10}{\alpha} + \dfrac{V_X}{2} = 5$. Try $V_X = 10$: the first term is 0 and $10/2 = 5$ ✔. So $V_X = 10$ V for **every** $\alpha$, the current through the $\alpha$ branch is zero, and the 10 V source supplies **zero** power. Spot such "balance" cases before doing heavy algebra.

**Example 5 (element laws + KCL at one node).** A node has four branches: a 3 A current source feeding the node; a resistor branch bringing in 1 A; a 0.5 F capacitor leaving the node, with $v_C = 2\sin4t$ V (+ at the node); an inductor of 1 H leaving the node to ground, with $v_L$ + at the node.
- Capacitor current out of the node: $i_C = C\,\dfrac{dv_C}{dt} = 0.5\times8\cos4t = 4\cos4t$ A.
- KCL: $3 + 1 = 4\cos4t + i_L$ ⇒ $i_L = 4 - 4\cos4t$ A.
- Inductor voltage: $v_L = L\,\dfrac{di_L}{dt} = 1\times16\sin4t = 16\sin4t$ V.

**Example 6 (KVL with element laws).** A battery $E$, a resistor $R$ and a capacitor $C$ are in series, carrying current $i$. KVL round the loop:
$$iR + \frac1C\int i\,dt = E$$
Differentiate once (E is constant): $R\,\dfrac{di}{dt} + \dfrac{i}{C} = 0$, i.e. $\dfrac{di}{dt} + \dfrac{1}{RC}\,i = 0$. Both forms are correct descriptions of the same circuit. This is the starting point of Chapter 5 (transients).

## Power balance in a part of a circuit

If you know the voltage across and the current through each element, you can find each element's absorbed power with the passive sign convention, and the total absorbed power of a **part** of a circuit (which need not be zero, since the rest of the circuit exchanges power with it).

**Example 7.** Three batteries meet at one node. 7 A flows **into** the node through a 40 V battery, entering that battery at its + terminal. 4 A flows **out** of the node through a 20 V battery, entering that battery at its – terminal. The remaining 3 A flows out through a 10 V battery, entering at its + terminal.
Absorbed powers: $+40\times7 = 280$ W; $-20\times4 = -80$ W; $+10\times3 = 30$ W. Total absorbed by the three $= 230$ W (the rest of the circuit supplies it).

## Summary of this section

| Law | Statement | Gives |
|---|---|---|
| KCL | $\sum i_{\text{leaving}} = 0$ at a node or closed surface | series ⇒ same current; current in a voltage source |
| KVL | $\sum v = 0$ round a loop | parallel ⇒ same voltage; voltage across a current source |

Always finish with a power-balance check: total absorbed power of a complete circuit = 0.
