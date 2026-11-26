## The voltage divider

![Voltage divider (left) and current divider (right)](fig:c2-dividers)

Two resistors $R_1$ and $R_2$ in **series** across a voltage $V$ carry the same current $I = \dfrac{V}{R_1 + R_2}$. So

$$V_1 = V\,\frac{R_1}{R_1 + R_2}, \qquad V_2 = V\,\frac{R_2}{R_1 + R_2}$$

In words: **the voltage divides in the ratio of the resistances.** The bigger resistor takes the bigger share.

For $n$ resistors in series, $V_k = V\,\dfrac{R_k}{R_1 + R_2 + \dots + R_n}$.

**Example 1.** 80 Ω and 20 Ω in series across 25 V. $V_{80} = 25\times\dfrac{80}{100} = 20$ V, $V_{20} = 5$ V.

**Example 2.** $n$ identical resistors in series across $V$: each gets $V/n$. If all are halved, each still gets $V/n$ (the ratio did not change).

### Capacitors divide the other way

Capacitors in series carry the same **charge**, so $V \propto 1/C$:

$$V_1 = V\,\frac{C_2}{C_1 + C_2}$$

The **smaller** capacitor takes the **larger** voltage.

### A loaded divider

If a load $R_L$ is connected across $R_2$, the divider is changed: replace $R_2$ by $R_2 \parallel R_L$ first, then divide. A load always **lowers** the output voltage.

**Example 3.** 10 kΩ and 10 kΩ across 12 V give 6 V. Put a 10 kΩ load across the lower one: $10 \parallel 10 = 5$ kΩ, so the output becomes $12\times\dfrac{5}{15} = 4$ V.

## The current divider

Two resistors in **parallel** with total current $I$ entering share the same voltage $V = I\,(R_1 \parallel R_2)$. So

$$I_1 = I\,\frac{R_2}{R_1 + R_2}, \qquad I_2 = I\,\frac{R_1}{R_1 + R_2}$$

Note the **cross-over**: the current in $R_1$ has $R_2$ on top. **The current divides in the inverse ratio of the resistances**: the smaller resistor takes the larger current.

With conductances it looks like the voltage divider: $I_k = I\,\dfrac{G_k}{G_1 + G_2 + \dots}$. Use this form for three or more branches.

**Example 4.** 12 A into 4 Ω ∥ 2 Ω. $I_4 = 12\times\dfrac{2}{6} = 4$ A, $I_2 = 8$ A.

**Example 5.** 9.75 A into three branches of 4 Ω, 2 Ω and 8 Ω. Conductances 0.25, 0.5, 0.125 S, total 0.875 S. Current in the 8 Ω branch $= 9.75\times\dfrac{0.125}{0.875} = \dfrac{9.75}{7} = \dfrac{39}{28}$ A.

## Using power ratios (very common in APSC and GATE)

- **Series** (same current): $P \propto R$. If one resistor's power is known, the other's is $P\,\dfrac{R_2}{R_1}$.
- **Parallel** (same voltage): $P \propto 1/R$.

**Example 6.** A 4 Ω and a 6 Ω in series, and this pair in parallel with a 5 Ω. The 5 Ω produces 10 units of heat per second. Common voltage: $V^2 = 10\times5 = 50$. The 4 + 6 branch takes $V^2/10 = 5$ units, split in the ratio 4 : 6, so the 4 Ω produces 2 units.

## Finding an unknown resistor

Many questions give one current and ask for a resistor. Work **backwards** with Ohm's law, KCL and KVL, one element at a time.

**Example 7.** 100 V source, then 12 Ω in series, then 14 Ω in parallel with $R$. The current in $R$ is 4 A.

- Voltage across the parallel pair $= 4R$.
- Current in 14 Ω $= 4R/14$. Source current $= 4 + 4R/14$.
- KVL: $100 = 12\,(4 + 4R/14) + 4R$ ⇒ $52 = R\,(48/14 + 4) = R\times\dfrac{104}{14}$ ⇒ $R = 7$ Ω.

Faster: **test the options.** With $R = 7$: pair voltage 28 V, current in 14 Ω is 2 A, total 6 A, drop in 12 Ω is 72 V, $72 + 28 = 100$ ✔.

## Without a calculator

- Divider answers are fractions of the supply: write the ratio first (80 : 20 = 4 : 1, so 4/5 of 25 V).
- For unknown resistors, put each option back in; one fits in a few seconds.
- Check: in a voltage divider each part is less than $V$; in a current divider each part is less than $I$.
