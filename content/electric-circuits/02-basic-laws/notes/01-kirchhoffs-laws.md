## What this chapter does

Chapter 1 gave the rule of each element ($v = iR$, $i = C\,dv/dt$, $v = L\,di/dt$, sources) and a first look at Kirchhoff's laws. In this chapter we use the two laws fully and learn the quick methods that turn a big network into a small one: series and parallel, voltage and current dividers, source conversion, star–delta, bridges, symmetry and ladders. Most APSC circuit questions are solved with exactly these tools, in under a minute, without a calculator.

## The words we need

![Nodes, branches and loops in a small circuit](fig:c2-node-loop)

- **Branch:** one element (or one series string of elements treated together) between two nodes.
- **Node:** a point where two or more branches meet. **All wire joined together without any element in between is one node**, however long it is drawn. This is the most common trap in "find the equivalent resistance" questions.
- **Loop:** any closed path that starts at a node and comes back to it without passing through any node twice.
- **Mesh:** a loop that has no other loop inside it (a "window" of the drawing).
- **Essential node:** a node where three or more branches meet. Currents change only at essential nodes.

**Example 1.** A circuit has a source and two resistors in parallel with it, all drawn with long wires. However many corners the drawing has, there are only **2 nodes**: the top wire and the bottom wire. There are 3 branches and 2 meshes.

## Kirchhoff's Current Law (KCL) in full

> **At any node, the sum of the currents entering equals the sum of the currents leaving.** Written with signs: $\sum i_{\text{leaving}} = 0$.

Why it is true: charge cannot pile up at a node (a node has no capacitance), so charge flowing in each second must flow out in the same second.

### Working with arrows and signs

Each branch current has a **reference arrow**. The value written next to it may be negative.

- A current of $-4$ A with its arrow **into** the node is really 4 A **leaving** the node.
- Write KCL using the arrows as drawn: (sum of values with arrows in) = (sum of values with arrows out). Keep the signs of the values.

**Example 2.** At a node: $i_1 = 2$ A (arrow in), $i_2 = 3$ A (arrow out), $i_3 = -4$ A (arrow in), $i_4$ (arrow out). Then $2 + (-4) = 3 + i_4$, so $i_4 = -5$ A. The real current in branch 4 is 5 A **into** the node.

### KCL for a closed surface (cut set)

Draw any closed boundary around part of a circuit: one node, several nodes, or a whole box. **The total current leaving the boundary is zero.** This "generalised KCL" often finds a current in one line, with no node equations.

**Example 3.** Two networks are joined by only three wires. Currents from the left network to the right one are 3 A and $-1$ A in two of the wires. Current in the third wire from left to right $= 0 - 3 - (-1) = -2$ A, i.e. 2 A from right to left.

**Example 4 (a classic GATE idea).** Two networks are joined by only two paths: a 2 Ω resistor at the top, and at the bottom a 1 Ω resistor in parallel with a 2 A current source. If 3 A flows left to right through the top 2 Ω, then 3 A must flow **right to left** through the bottom paths together. Since the current source pushes 2 A left to right, the 1 Ω must carry $3 + 2 = 5$ A right to left, and the voltage across it is 5 V. No other part of either network matters.

## Kirchhoff's Voltage Law (KVL) in full

> **Around any closed loop, the algebraic sum of the voltages is zero.** Sum of rises = sum of drops.

Why it is true: the potential at a point has one value. If you walk round a loop and return to the start, you must be back at the same potential.

### The walking rule

Walk round the loop in one direction (say clockwise).

- Going from − to + across an element is a **rise** (+).
- Going from + to − is a **drop** (−).
- Through a resistor **in the direction of its current**, you go from + to −: a drop of $IR$. Against the current: a rise of $IR$.

Add everything up and set the sum to zero.

**Example 5.** One loop: a 24 V source and three elements in series. Drops across two of them are 10 V and 6 V. The third must drop $24 - 10 - 6 = 8$ V.

### KVL along an open path (finding the voltage between two points)

KVL also gives the voltage between any two points A and B: **start at B, walk to A along any path, and add the rises (subtract the drops).** The answer is $V_A - V_B$. Every path gives the same answer, so choose the path with the fewest unknowns.

**Example 6.** Node a is at 12 V and node c is at −3 V, both measured from ground. $V_{ac} = V_a - V_c = 12 - (-3) = 15$ V.

**Example 7.** From B to A the path has: a 5 V source (− to +, a rise), then a 2 Ω resistor carrying 1 A from A towards B (walking from B to A is against the current: a rise of 2 V). $V_A - V_B = 5 + 2 = 7$ V.

## How many independent equations?

For a connected circuit with $b$ branches and $n$ nodes:

- independent **KCL** equations: $n - 1$ (the last node's equation is the sum of the others);
- independent **KVL** equations: $b - n + 1$ (this equals the number of meshes in a flat drawing);
- element (v–i) equations: one per branch, $b$.

Together: $2b$ equations for $2b$ unknowns (a current and a voltage in every branch). Chapter 3 (node and mesh analysis) builds on these counts.

## Ideal sources and the two laws

From Chapter 1, keep these two tools ready:

- The **voltage across a current source** is not fixed by the source. Find it by **KVL** around a loop that contains it.
- The **current through a voltage source** is not fixed by the source. Find it by **KCL** at one of its nodes.

**Example 8 (single loop with a dependent source).** A loop has a 12 V source, a 4 Ω resistor, a dependent voltage source $2V_o$, a 4 V source and a 6 Ω resistor, with $V_o$ the voltage across the 6 Ω resistor. Write KVL in terms of the loop current $i$, express $V_o$ in terms of $i$ (watch its marked polarity against the current direction), and solve one equation. A negative answer for $i$ just means the current flows against the chosen arrow.

## Power balance as a check

In every circuit, total power delivered = total power absorbed (Chapter 1). After solving, check it: it catches sign mistakes quickly.

## Without a calculator

- Find a current by KCL on a **big boundary** before writing node equations.
- For $V_{AB}$, pick the path through **sources and known currents** only.
- Signs first, numbers later: decide which way current must flow from the physics, then do the arithmetic.
