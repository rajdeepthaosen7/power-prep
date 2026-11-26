## Series connection

Two or more elements are in **series** when they are joined end to end and **no other branch leaves the joints between them**. Then the same current flows through all of them (KCL at each joint).

For resistors in series:

$$R_{eq} = R_1 + R_2 + \dots + R_n$$

Proof: same current $I$; KVL gives $V = IR_1 + IR_2 + \dots = I(R_1 + R_2 + \dots)$.

Facts to remember:

- $R_{eq}$ is **larger than the largest** resistor.
- $n$ equal resistors $R$ in series: $nR$.
- Each one takes a voltage in proportion to its resistance (voltage divider, next topic). With $n$ equal resistors across $V$, each gets $V/n$, **whatever the value of $R$**. Halving or doubling every resistor does not change this; only the current changes.
- Power in series: $P = I^2R \propto R$, so the **larger** resistor takes more power.

**Example 1.** 2 Ω, 3 Ω and 5 Ω in series across 20 V: $R_{eq} = 10$ Ω, $I = 2$ A, voltages 4 V, 6 V, 10 V.

## Parallel connection

Two or more elements are in **parallel** when they are connected across the **same pair of nodes**. Then they all have the same voltage (KVL).

For resistors in parallel, conductances add:

$$\frac{1}{R_{eq}} = \frac{1}{R_1} + \frac{1}{R_2} + \dots \qquad G_{eq} = G_1 + G_2 + \dots$$

Proof: same voltage $V$; KCL gives $I = V/R_1 + V/R_2 + \dots$

Facts to remember:

- $R_{eq}$ is **smaller than the smallest** resistor. This alone often kills two options.
- Two resistors: $R_1 \parallel R_2 = \dfrac{R_1R_2}{R_1 + R_2}$ ("product over sum").
- $n$ equal resistors $R$ in parallel: $R/n$.
- $R \parallel 2R = \tfrac23R$, $R \parallel 3R = \tfrac34R$, $R \parallel R = \tfrac12R$.
- $R \parallel 0 = 0$ (a short across anything shorts it). $R \parallel \infty = R$ (an open branch changes nothing).
- Power in parallel: $P = V^2/R \propto 1/R$, so the **smaller** resistor takes more power.

**Example 2.** $2 \parallel 3 \parallel 6$: $\tfrac12 + \tfrac13 + \tfrac16 = \tfrac{3+2+1}{6} = 1$, so $R_{eq} = 1$ Ω.

**Example 3 (taking out a common factor).** $600 \parallel 900 = 300\times(2 \parallel 3) = 300\times\tfrac{6}{5} = 360$ Ω.

## Finding the equivalent resistance of a network

Work from the end **farthest** from the terminals towards the terminals, combining series and parallel groups step by step. Before you start, simplify the drawing:

1. **Name the nodes.** Put a letter at every node. Remember: wire with no element in it is one node. Give the same letter to all points joined by plain wire.
2. **Remove shorted elements.** An element whose two ends are the same node carries no current and has no effect. Delete it.
3. **Remove dangling elements.** An element that is connected at only one end (an open branch) carries no current. Delete it.
4. **Redraw** with each lettered node as one dot and each element between its two letters. Series and parallel groups now become obvious.
5. **Watch crossings.** Where two wires cross with a small hump (or with no dot), they are **not** connected. A dot means they are.

**Example 4.** Five resistors are drawn in a zig-zag between A and B, but a straight wire joins the middle node to B. Then every element between the middle node and B is shorted (both ends are "B"), and $R_{AB}$ is just what lies between A and the middle node.

**Example 5 (step by step).** From A: 1 Ω to node P. Between P and the bottom node S: 3 Ω and 6 Ω (in parallel, $= 2$ Ω). From P: 2 Ω to node Q. Between Q and S: 3 Ω, 6 Ω, and a path of 1 Ω + 1 Ω (all three in parallel: $3 \parallel 6 \parallel 2 = 1$ Ω). From S: 0.8 Ω to B.

- Q to S: 1 Ω. Add the 2 Ω from P: 3 Ω between P and S through Q.
- P to S: $2 \parallel 3 = 1.2$ Ω.
- A to B: $1 + 1.2 + 0.8 = 3$ Ω.

## Inductors and capacitors (reminder)

- Uncoupled **inductors** combine like resistors: series add, parallel by product over sum.
- **Capacitors** combine the opposite way: parallel add; series by product over sum ($\frac{1}{C} = \frac{1}{C_1} + \frac{1}{C_2}$).

## Sources in series and in parallel

- **Voltage sources in series** add with their signs: 10 V and 4 V in series aiding give 14 V; opposing give 6 V.
- **Current sources in parallel** add with their signs (arrows into the same node add; opposite arrows subtract).
- Voltage sources in parallel are allowed **only if equal** (otherwise KVL fails). Current sources in series are allowed **only if equal** (otherwise KCL fails).
- A resistor in **parallel with an ideal voltage source** does not change the voltage seen outside; for the outside circuit you may remove it (it still takes current from the source).
- A resistor in **series with an ideal current source** does not change the current seen outside; for the outside circuit you may short it.

**Example 6.** A node has a 2.5 A source feeding it, a 2.5 A source and a 5 A source drawing from it, and two 5 Ω resistors to the other node. Net current into the resistors: $2.5 - 2.5 - 5 = -5$ A. $5 \parallel 5 = 2.5$ Ω, so the node voltage is $-5\times2.5 = -12.5$ V (the top is 12.5 V **below** the bottom).

## Without a calculator

- Kill options with bounds: series result > largest; parallel result < smallest.
- Use the ratio shortcuts ($R \parallel R$, $R \parallel 2R$, $R \parallel 3R$) and common factors.
- With many equal resistors, count: $n$ in series is $nR$, $n$ in parallel is $R/n$.
