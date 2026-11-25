## Chapter 1 at a glance

Use this page for quick revision. Every line was explained in the sections before.

### The basic quantities

- $i = dq/dt$, $q = \int i\,dt$ (area under the current graph).
- Voltage is **across**, current is **through**. $v_{ab} = v_a - v_b$.
- $p = vi$. Current enters + ⇒ $vi$ is **absorbed**. Sum of absorbed powers in a complete circuit $= 0$.
- $w = \int p\,dt$; $1$ kWh $= 3.6$ MJ.

### Elements

| | Resistor | Capacitor | Inductor |
|---|---|---|---|
| Rule | $v = Ri$ | $i = C\,dv/dt$ | $v = L\,di/dt$ |
| Integral form | – | $v = v(0) + \frac1C\int i\,dt$ | $i = i(0) + \frac1L\int v\,dt$ |
| Energy | dissipates $i^2R$ | stores $\tfrac12Cv^2$ | stores $\tfrac12Li^2 = \lambda^2/2L$ |
| DC steady state | $R$ | open | short |
| Cannot jump | – | voltage | current |
| Series | add | like R in parallel | add (no coupling) |
| Parallel | reciprocal | add | reciprocal |

- Linear ⇔ superposition (additivity + homogeneity) ⇔ straight line **through the origin**. $v = ai + b$ with $b \ne 0$ is **not** linear.
- Non-linear can still be bilateral. R, L, C: linear, bilateral, passive, lumped. Diode: non-linear, unilateral.
- Pure L and C never dissipate; net energy over one AC cycle is zero.
- $R_T = R_0[1 + \alpha_0(T - T_0)]$. Rated device: $R = V^2/P$. Series lamps: the lower-watt lamp is brighter.
- Cable: conductor resistances add in series; insulation resistances combine in parallel.
- Charging C from DC: the source gives $CV^2$, C stores $\tfrac12CV^2$, half is lost. Charge sharing: $V = \dfrac{C_1V_1 + C_2V_2}{C_1 + C_2}$.
- Series capacitors: same charge; safe voltage is set by the smallest $Q_{max} = CV_{rated}$.
- Mutual: $M = k\sqrt{L_1L_2}$; series $L_1 + L_2 \pm 2M$; $M = (L_{aid} - L_{opp})/4$.

### Sources

| | Ideal voltage source | Ideal current source |
|---|---|---|
| Fixes | voltage | current |
| Set by the circuit | its current | its voltage |
| Internal r | 0 | ∞ |
| Value zero | short | open |
| Forbidden | unequal ones in parallel | unequal ones in series |

- Practical: $V = E - Ir$; $E = I_s r$; equivalent ⇔ same $V_{oc}$ and $I_{sc}$. Ideal sources cannot be converted.
- $r \ll R_L$ ⇒ constant voltage; $r \gg R_L$ ⇒ constant current.
- Dependent sources: VCVS, CCVS, CCCS, VCCS; active; never switched off.

### Kirchhoff

- KCL: $\sum i = 0$ at a node or a closed surface. KVL: $\sum v = 0$ round a loop.
- Voltage across a current source ⇒ KVL. Current through a voltage source ⇒ KCL.

### Common traps

1. Mixing up "supplied" and "absorbed" power. Work out both sets (they differ only in sign) and match the options and the exact wording.
2. Calling $v = 5i + 2$ linear.
3. Thinking a lossless inductor's current falls back to zero after a voltage pulse. It stays.
4. Using $\tfrac12CV^2$ for the energy **given by the source** while charging. It is $CV^2$.
5. Adding voltage ratings of series capacitors. Use the common charge.
6. In a two-element loop, two arrows that both point "up" are **opposite** currents around the loop.
7. Forgetting the starting value $v(0)$ or $i(0)$ in the integral forms.

## Working without a calculator

APSC does not allow a calculator, and it asks only MCQs. These habits save time in every chapter.

### 1. Read the options before you calculate

Often one rough estimate is enough. If the options are 4.05, 6.02, 7.67 and 8.35, and your first step gives "about 4", stop: the answer is the first one. Options also tell you the form of the answer (a number, a $\sin$ or $\cos$, a ratio), so you know which steps you can skip.

### 2. Test the options instead of solving

For a quadratic or a non-linear element, put each option back into the equation. One option usually fits in a few seconds. Example: $V = 3 - 2I$ and $V = I^2$. Try $I = 1$: $3 - 2 = 1 = 1^2$ ✔.

### 3. Use ratios, not full values

- Same voltage (parallel, or same rated voltage): $P \propto \dfrac1R$, so a 200 W lamp has half the resistance of a 100 W lamp.
- Same current (series): $P \propto R$, so powers split in the same ratio as resistances.
- Series capacitors: voltage $\propto \dfrac1C$.

### 4. Pick units that cancel

| Multiply / divide | Gives |
|---|---|
| mA × kΩ | V |
| mA × ms | µC |
| µC ÷ µF | V |
| µC ÷ mF | mV |
| mH × (A/ms) | V |
| µF × (V/ms) | mA |

Write the numbers in these units and the powers of ten disappear.

### 5. Parallel resistors (and series capacitors) quickly

- $n$ equal ones: $R/n$.
- $R$ and $2R$: $\tfrac23R$. $R$ and $3R$: $\tfrac34R$. $R$ and $R$: $\tfrac12R$.
- Take out a common factor: $600 \parallel 900 = 300\times(2\parallel3) = 300\times\tfrac65 = 360$.

### 6. Numbers worth remembering

- Squares: $11^2 = 121$ … $25^2 = 625$; $110^2 = 12100$, $220^2 = 48400$, $230^2 = 52900$, $240^2 = 57600$.
- $\sqrt2 \approx 1.414$, $\sqrt3 \approx 1.732$, $\tfrac1{\sqrt2} \approx 0.707$, $\pi \approx 3.14$, $\tfrac1\pi \approx 0.318$.
- $\tfrac13 \approx 0.333$, $\tfrac16 \approx 0.167$, $\tfrac17 \approx 0.143$, $\tfrac19 \approx 0.111$, $\tfrac1{4.5} \approx 0.222$.

### 7. Areas instead of integrals

- Charge, flux change, current change: areas of rectangles, triangles and trapeziums.
- Heat in $R$ from a linear ramp of current $0 \to I$ in time $T$: $\dfrac{I^2RT}{3}$.

### 8. Check before you mark

- **Units:** energy must come out in joules, power in watts. An option with the wrong units is wrong.
- **Limits:** if $R \to 0$ or $R \to \infty$, does your answer behave sensibly?
- **Sign:** decide the sign from the physics (absorbing or delivering, rising or falling) before doing any arithmetic.
