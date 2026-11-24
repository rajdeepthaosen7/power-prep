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
