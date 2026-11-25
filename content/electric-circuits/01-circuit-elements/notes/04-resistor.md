## Ohm's law

A resistor opposes the flow of current and turns electrical energy into heat. For a linear resistor (passive sign convention):

$$v = R\,i$$

$$i = G\,v, \quad G = \frac{1}{R}$$

- $R$ = resistance, unit ohm (Ω).
- $G$ = conductance, unit siemens (S). (Old name: mho, ℧.)

Two extreme cases:

- **Short circuit:** $R = 0$. Voltage across it is 0 whatever the current.
- **Open circuit:** $R = \infty$ (or $G = 0$). Current through it is 0 whatever the voltage.

The v–i graph of a linear resistor is a straight line through the origin with slope $R$ (on a $v$ vs $i$ plot).

## What decides the resistance

$$R = \rho\,\frac{l}{A}$$

- $l$ = length of the conductor, $A$ = area of cross-section, $\rho$ = resistivity (Ω·m), a property of the material.
- Longer wire ⇒ more resistance. Thicker wire ⇒ less resistance.
- Copper $\rho \approx 1.7\times10^{-8}$ Ω·m (good conductor). Insulators have $\rho$ around $10^{10}$ to $10^{16}$ Ω·m.

### Conductor resistance and insulation resistance of a cable

A cable has two very different resistances:

1. **Conductor resistance**: along the length of the core. It is in **series** along the length, so it **increases** with length ($R \propto l$).
2. **Insulation resistance**: the leakage path from core to sheath, **across** the insulation (radially). Every small piece of length gives its own leakage path, and all these paths are side by side, i.e. in **parallel**. So a longer cable has **more parallel paths** and **less** insulation resistance ($R_{ins} \propto 1/l$).

So when two cables are joined end to end:

- conductor resistances **add** (series): $R_c = R_{c1} + R_{c2}$
- insulation resistances combine **in parallel**: $R_{ins} = \dfrac{R_{i1}R_{i2}}{R_{i1}+R_{i2}}$

**Example 1.** Two cables: conductor resistances 0.4 Ω and 0.6 Ω, insulation resistances 300 MΩ and 600 MΩ. Joined: $R_c = 1.0$ Ω, $R_{ins} = \dfrac{300\times600}{900} = 200$ MΩ.

## Resistance changes with temperature

For metals, resistance rises with temperature. Over a normal range:

$$R_T = R_0\,[\,1 + \alpha_0\,(T - T_0)\,]$$

where $R_0$ is the resistance at the reference temperature $T_0$ and $\alpha_0$ is the **temperature coefficient of resistance** at $T_0$ (per °C).

- Metals: $\alpha > 0$ (copper ≈ 0.0039 /°C at 20 °C).
- Carbon, semiconductors, electrolytes, insulators: $\alpha < 0$ (resistance falls when hot).
- Alloys like manganin and constantan: $\alpha \approx 0$. That is why standard resistors are made from them: **high resistivity** (so a small coil gives a big resistance) and **very low temperature coefficient** (so the value stays fixed).

### Hot and cold resistance of a lamp

A lamp filament (tungsten) runs at about 2500 °C. Its hot resistance is roughly 10–15 times its cold (room-temperature) resistance. This is why a lamp draws a big current for an instant when switched on.

- The rating "60 W, 240 V" tells us the **hot** resistance: $R_{hot} = V^2/P$.
- If you know the cold resistance and $\alpha$, you can find the filament temperature from $R_T = R_0[1 + \alpha(T - T_0)]$.

**Example 2.** A lamp is rated 60 W, 240 V. Its resistance at 20 °C is 80 Ω and $\alpha_{20} = 5\times10^{-3}$ /°C. Hot resistance $= 240^2/60 = 960$ Ω. Then $960 = 80[1 + 0.005(T-20)]$ ⇒ $12 - 1 = 0.005(T-20)$ ⇒ $T - 20 = 2200$ ⇒ $T = 2220$ °C.

## Power in a resistor

$$p = v\,i = i^2R = \frac{v^2}{R}$$

All three forms give the same answer. Choose the one that uses what you know:

- same **current** (series) ⇒ use $i^2R$ ⇒ power ∝ $R$
- same **voltage** (parallel) ⇒ use $v^2/R$ ⇒ power ∝ $1/R$

Since $i^2R \ge 0$ always, a resistor **always absorbs** power. It can never deliver.

### Ratings of lamps and heaters

A rating "$P_r$ watts, $V_r$ volts" means: at $V_r$ the device takes $P_r$. So its resistance is

$$R = \frac{V_r^2}{P_r}$$

Unless told otherwise, we take this resistance as constant (we ignore the change with temperature).

- **Lower** wattage rating ⇒ **higher** resistance. A 40 W, 230 V lamp has $R = 1322$ Ω; a 100 W, 230 V lamp has $R = 529$ Ω.

**Lamps in series** (same current, power $= i^2R$): the lamp with the **higher resistance** (the **lower** wattage rating) gets more power and **glows brighter**. Also, the total power drawn is **less** than either rating, because the total resistance is larger.

**Lamps in parallel** (same voltage, power $= v^2/R$): each gets its rated voltage, so each gives its rated power; the higher wattage lamp is brighter.

**Example 3.** A 25 W, 230 V lamp and a 100 W, 230 V lamp are in series across 230 V. $R_1 = 230^2/25 = 2116$ Ω, $R_2 = 230^2/100 = 529$ Ω. Current $= 230/2645 = 0.087$ A. $P_1 = 0.087^2\times2116 = 16$ W, $P_2 = 4$ W. The **25 W lamp glows brighter**. Total $= 20$ W, less than either rating.

**Example 4.** How many 120 W, 240 V heaters in series take the same total power from 240 V as one 40 W, 240 V heater?
Equal power from the same supply needs equal total resistance. One 40 W heater: $240^2/40 = 1440$ Ω. Each 120 W heater: $240^2/120 = 480$ Ω. So $n\times480 = 1440$ ⇒ $n = 3$.

### Power when the current changes with time

The formula $p = i^2R$ works at every instant. If $i = I_m\sin\omega t$:

$$v = R I_m \sin\omega t$$

$$p = R I_m^2 \sin^2\omega t$$

The power is never negative; it pulses between 0 and $RI_m^2$ at twice the supply frequency. Its average is $\tfrac12 RI_m^2$ (you will see this again as the RMS idea in the AC power chapter).

**Example 5.** $i = 3\sin 100t$ A in a 4 Ω resistor: $v = 12\sin 100t$ V, $p = 36\sin^2 100t$ W.

## Resistors in series and in parallel (only what you need now)

You will study network reduction fully in Chapter 2. For now:

- **Series** (same current): $R_{eq} = R_1 + R_2 + \dots$
- **Parallel** (same voltage): $\dfrac{1}{R_{eq}} = \dfrac{1}{R_1} + \dfrac{1}{R_2} + \dots$; for two, $R_{eq} = \dfrac{R_1R_2}{R_1+R_2}$

## Non-linear resistors: how to solve

A non-linear resistor has a curved v–i rule, for example $V = kI^2$, or a resistance that depends on current, $R = a + bI$. Ohm's law with a fixed $R$ cannot be used. The method:

1. Write the equation of the **rest of the circuit**: for a source $E$ with internal resistance $r$, the terminal voltage is $V = E - Ir$ (the **load line**).
2. Put the non-linear rule into it.
3. Solve the equation (often a quadratic).
4. **Throw away the impossible root**: a passive element must have $V$ and $I$ of the same sign (it absorbs power), and the current cannot exceed what the circuit can drive.

**Example 6.** A 6 V source with internal resistance 1 Ω feeds a non-linear resistor with $V = 2I^2$.
$6 - I\times1 = 2I^2$ ⇒ $2I^2 + I - 6 = 0$ ⇒ $I = \dfrac{-1 \pm \sqrt{1+48}}{4} = \dfrac{-1\pm7}{4}$.
So $I = 1.5$ A (the other root, $-2$ A, would make the passive element give power: reject).
$V = 2(1.5)^2 = 4.5$ V, power in the resistor $= 4.5\times1.5 = 6.75$ W.

**Example 7.** A resistor has $R = (10 + I)$ Ω, where $I$ is its current in amperes. It is connected to 24 V.
$24 = I(10 + I)$ ⇒ $I^2 + 10I - 24 = 0$ ⇒ $(I + 12)(I - 2) = 0$ ⇒ $I = 2$ A (reject $-12$).

**Graphical solution:** draw the load line $V = E - Ir$ and the curve of the non-linear element on the same axes; the crossing point is the answer. This works even when the curve has no formula (only a measured graph).

## Summary of this section

| Idea | Formula / fact |
|---|---|
| Ohm's law | $v = Ri$, $G = 1/R$ |
| Resistance | $R = \rho l/A$ |
| Temperature | $R_T = R_0[1 + \alpha_0(T - T_0)]$; metals $\alpha>0$ |
| Power | $p = i^2R = v^2/R \ge 0$ (always absorbs) |
| Rated device | $R = V_r^2/P_r$; lower watts ⇒ higher R |
| Series lamps | lower-watt lamp glows brighter; total power < each rating |
| Cable | conductor R adds in series; insulation R combines in parallel |
| Non-linear | load line + element rule ⇒ solve; reject the impossible root |
