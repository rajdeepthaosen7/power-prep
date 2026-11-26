## Chapter 2 at a glance

### Kirchhoff's laws

- **KCL:** at a node (or across any closed boundary), current in = current out. Use the arrows as drawn and keep the signs of the values.
- **KVL:** around any loop, rises = drops. Voltage between two points = sum of rises along **any** path from one to the other.
- One wire with no element in it = **one node**, however it is drawn.
- Counts: $n - 1$ KCL equations, $b - n + 1$ KVL equations.
- Current through a voltage source: by KCL. Voltage across a current source: by KVL.

### Series and parallel

| | Series | Parallel |
|---|---|---|
| Same quantity | current | voltage |
| Resistors | $R_1 + R_2$ | $\dfrac{R_1R_2}{R_1 + R_2}$ |
| Result is | > largest | < smallest |
| Power share | $\propto R$ | $\propto 1/R$ |
| Capacitors | $\dfrac{C_1C_2}{C_1 + C_2}$ | $C_1 + C_2$ |

- $n$ equal: $nR$ in series, $R/n$ in parallel. $R \parallel 2R = \tfrac23R$, $R \parallel 3R = \tfrac34R$.
- Delete shorted elements (both ends on one node) and dangling elements (one end free).

### Dividers

- Voltage divider: $V_1 = V\dfrac{R_1}{R_1 + R_2}$ (bigger $R$, bigger share).
- Current divider: $I_1 = I\dfrac{R_2}{R_1 + R_2}$ (cross-over; smaller $R$, bigger share). For many branches use $I_k = I\,G_k/\sum G$.
- A load across a divider lowers its output: replace the lower resistor by its parallel combination with the load.

### Source transformation and Millman

- $E$ in series with $R$ ⇔ $E/R$ in parallel with $R$ (arrow towards the old + terminal).
- Millman: $V = \dfrac{\sum E_kG_k + \sum I_k}{\sum G_k}$ for branches across the same two nodes.

### Star–delta

- Δ → Y: arm = (product of the two sides at that node) ÷ (sum of the three sides).
- Y → Δ: side = (sum of products two at a time) ÷ (opposite arm).
- Equal: $R_Y = R_\Delta/3$. Scaling by $k$ scales the other form by $k$.

### Bridges, symmetry, ladders

- Balance: $PS = QR$ (products of opposite arms). Then the middle branch carries zero current; open it or short it.
- Points that symmetry forces to the same potential may be joined; any element between them carries nothing.
- Infinite ladder: $R = R_s + R_p \parallel R$ ⇒ $R^2 - R_sR - R_sR_p = 0$.
- Finite ladder: combine from the far end; for voltages assume 1 V (or 1 A) at the end and scale.

## Common traps

- Treating a long drawn wire as several nodes.
- Missing a crossover hump (no connection) or a dot (connection).
- Using the voltage-divider formula for a current divider (forgetting the cross-over).
- Keeping a resistor in parallel with an ideal voltage source when finding what the outside circuit sees (it changes the source current only).
- In a balanced bridge, computing with the middle resistor: it is a distractor.
- Sign of a current given as negative: it flows against its arrow.

## Working without a calculator

- **Bounds first:** series > largest, parallel < smallest. Two options usually fall at once.
- **Ratios:** 80 : 20 = 4 : 1, so 4/5 of the voltage. Power ratios need no current at all.
- **Test options** for unknown resistors and infinite ladders: put the option back in and check one line.
- **Use conductances** for three or more parallel branches; pick a common denominator (for 4, 2 and 8 Ω use eighths: 2 + 4 + 1 = 7 eighths).
- **Check balance and symmetry** before any algebra.
