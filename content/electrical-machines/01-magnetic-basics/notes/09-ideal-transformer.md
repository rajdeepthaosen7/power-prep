## Why study an "ideal" transformer

A real transformer has small imperfections: its windings have resistance, a little flux leaks, the core needs some magnetising current and has core loss. These are all small (efficiency is about 98 %). So we first study a transformer **without** them. The ideal transformer gives the main rules (voltage, current and impedance ratios) that we use in every question; the imperfections are added in Chapter 3 (*Single-phase transformer*).

```definition
An **ideal transformer** is an imaginary transformer with:

1. **zero winding resistance** (no copper loss, no resistive drop);
2. **no leakage flux** (all the flux links both windings; coupling $k = 1$);
3. a core of **infinite permeability** (zero reluctance, so **zero magnetising current**), with a **linear** magnetisation characteristic;
4. **no core loss** (no hysteresis or eddy current loss).

It therefore has 100 % efficiency and no voltage drop.
```

## Voltage ratio

Both windings link the same flux, so the EMF per turn is the same in both (topic *EMF equation*). With no resistance and no leakage, the terminal voltages equal the EMFs:

```formula
$$\fx{\dfrac{V_1}{V_2} = \dfrac{E_1}{E_2} = \dfrac{N_1}{N_2} = a}$$

- $a = N_1/N_2$ is the **turns ratio**. $a > 1$: step-down; $a < 1$: step-up.
```

```watchout
Books use two different ratios. Some define the **transformation ratio** $K = \dfrac{N_2}{N_1} = \dfrac{V_2}{V_1}$ (secondary over primary), others the turns ratio $a = \dfrac{N_1}{N_2}$ (primary over secondary). Always read which one a question uses. A ratio written "1 : 100" usually means $N_1 : N_2$.
```

## Current ratio: the ampere-turn balance

In an ideal core the reluctance is zero, so the net MMF on the core must be zero (otherwise an infinite flux would appear). When the secondary carries a current $I_2$, its ampere-turns $N_2 I_2$ try to change the core flux; the primary at once draws a current $I_1$ whose ampere-turns cancel them:

```formula
$$\fx{N_1 I_1 = N_2 I_2} \qquad \Rightarrow \qquad \dfrac{I_1}{I_2} = \dfrac{N_2}{N_1} = \dfrac{1}{a}$$

The current ratio is the **inverse** of the voltage ratio. The high-voltage winding carries the smaller current.
```

Multiply the two ratios: $V_1 I_1 = V_2 I_2$. **Volt-amperes in = volt-amperes out.** The ideal transformer passes the power through unchanged, at the **same power factor** (it adds no resistance and no reactance of its own).

```example Worked example 1: voltages, currents and power
An ideal transformer has $\given{200}$ primary turns and $\given{800}$ secondary turns. The primary is connected to $\given{230\ \text{V}}$, 50 Hz. The secondary feeds a load that draws $\given{2\ \text{A}}$ at a power factor of $\given{0.8}$ lagging. Find the secondary voltage, the primary current and the power to the load.
:::given $N_1 = 200$, $N_2 = 800$, $V_1 = 230\ \text{V}$, $I_2 = 2\ \text{A}$, pf $= 0.8$ lagging
:::find $V_2$, $I_1$ and the power $P$
:::step Secondary voltage. $\fx{V_2 = V_1 \times N_2 / N_1}$
$N_2 \div N_1 = 800 \div 200 = 4$
$V_2 = 230 \times 4 = 920\ \text{V}$
:::step Primary current. $\fx{I_1 = I_2 \times N_2 / N_1}$
$I_1 = 2 \times 4 = 8\ \text{A}$
:::step Power to the load. $\fx{P = V_2 I_2 \cos\phi}$
$V_2 I_2 = 920 \times 2 = 1840\ \text{VA}$
$P = 1840 \times 0.8 = 1472\ \text{W}$
:::answer $\ans{V_2 = 920\ \text{V},\ I_1 = 8\ \text{A},\ P = 1472\ \text{W}}$
**Sense check:** $V_1 I_1 = 230 \times 8 = 1840$ VA $= V_2 I_2$ ✓. Step-up in voltage, step-down in current.
```

### Load impedance from the rating

A transformer rated $S$ volt-amperes at secondary voltage $V_2$ is "fully loaded" when it delivers rated current $I_2 = S/V_2$. The load impedance that draws exactly this current is $Z = V_2 / I_2 = V_2^2 / S$ (whatever the power factor).

**Example:** a 25 kVA, 2400/240 V transformer fully loaded on its 240 V side: $Z = 240^2 \div 25\,000 = 57\,600 \div 25\,000 = 2.304\ \Omega$.

## Referring an impedance to the other side

An impedance $Z_2$ connected across the secondary: what does it "look like" from the primary?

1. $V_2 = I_2 Z_2$ (Ohm's law on the secondary side).
2. $V_1 = a V_2$ and $I_1 = I_2 / a$.
3. Impedance seen at the primary: $Z_1' = \dfrac{V_1}{I_1} = \dfrac{a V_2}{I_2 / a} = a^2 \dfrac{V_2}{I_2} = a^2 Z_2$.

```formula
$$\fx{Z' = a^2 Z = \left(\dfrac{N_1}{N_2}\right)^2 Z} \qquad \text{(secondary impedance seen from the primary)}$$

- Going from the secondary to the primary side: **multiply** by $a^2$. Going the other way: **divide** by $a^2$.
- A resistor $R$ looks like $a^2 R$; an inductor $L$ like $a^2 L$; a **capacitor $C$ looks like $C / a^2$** (because a capacitor's impedance is $1/(\omega C)$).
- Sources move the other way: a voltage source moves across with the **voltage ratio** (×1/a going to the secondary side), a current source with the current ratio.
```

```example Worked example 2: impedance matching
An amplifier has an internal resistance of $\given{800\ \Omega}$. It must drive an $\given{8\ \Omega}$ loudspeaker. Find the turns ratio of the matching transformer for maximum power transfer.
:::given $R_s = 800\ \Omega$, $R_L = 8\ \Omega$
:::find turns ratio $a = N_1/N_2$
:::step For maximum power, the speaker seen from the amplifier must equal the amplifier's resistance.
$a^2 \times 8 = 800$
:::step Solve for $a^2$.
$a^2 = 800 \div 8 = 100$
:::step Take the square root.
$a = \sqrt{100} = 10$
:::answer $\ans{N_1 : N_2 = 10 : 1}$
```

```example Worked example 3: Thevenin equivalent through an ideal transformer
A $\given{10\ \text{V}}$ source with a series resistance of $\given{5\ \Omega}$ feeds the primary of an ideal $\given{1 : 3}$ transformer. Find the Thevenin equivalent seen at the secondary terminals.
:::given $V_s = 10\ \text{V}$, $R_s = 5\ \Omega$, $N_1 : N_2 = 1 : 3$ (so $a = 1/3$)
:::find $V_{th}$ and $R_{th}$ at the secondary
:::step Open-circuit voltage: no current, so no drop in $R_s$; the full 10 V is on the primary.
$V_1 = 10\ \text{V}$
$V_{th} = V_1 \times N_2 / N_1 = 10 \times 3 = 30\ \text{V}$
:::step Resistance seen from the secondary: move $R_s$ across, multiplying by $(N_2/N_1)^2$.
$(N_2/N_1)^2 = 3^2 = 9$
$R_{th} = 5 \times 9 = 45\ \Omega$
:::answer $\ans{V_{th} = 30\ \text{V},\ R_{th} = 45\ \Omega}$
**Sense check:** short-circuit current at the secondary $= 30 \div 45 = 0.667$ A. On the primary that is $0.667 \times 3 = 2$ A, which is exactly $10\ \text{V} \div 5\ \Omega$ with the primary shorted. ✓
```

## Polarity and the dot convention

Each winding has two ends. At any instant, one end of each winding is positive with respect to its other end. The ends that are positive **at the same instant** are marked with a **dot**.

```remember
- **Voltages:** the dotted ends of both windings have the same polarity at every instant. If $V_1$ is "+ at the dot", then $V_2$ is also "+ at the dot".
- **Currents (ideal transformer):** if current **enters** the dotted end of the primary, it **leaves** the dotted end of the secondary (so the two ampere-turns cancel).
- In a loop equation for coupled coils: if both currents enter (or both leave) the dotted ends, the mutual terms have the **same sign** as the self terms; if one enters and the other leaves, they have the **opposite** sign.
- In a transformer the induced EMF $E_1$ in the primary is a "back EMF": it opposes the applied voltage $V_1$ round the primary loop, and in an ideal transformer it is equal to it in size. The secondary EMF $E_2$ drives the load current, so $V_2 = E_2$.
```

## Summary of this topic

- Ideal: no resistance, no leakage, infinite permeability (no magnetising current), no core loss, linear.
- $V_1/V_2 = N_1/N_2 = a$; $I_1/I_2 = N_2/N_1$; $V_1 I_1 = V_2 I_2$; the power factor passes through unchanged.
- Rated-load impedance $= V^2/S$ on that side.
- Impedance seen from the primary $= a^2 Z_2$ (L × $a^2$, C ÷ $a^2$). Matching: $a = \sqrt{R_s/R_L}$.
- Thevenin through a transformer: voltage × turns ratio, impedance × (turns ratio)².
- Dots: same polarity at the same instant; current in at one dot, out at the other.
