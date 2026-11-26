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

### What happens, in plain words

Heat a copper wire and its resistance goes up. Measure it at a few temperatures and plot $R$ against $T$: over the normal working range the points lie on a **straight line**. So the resistance rises by the **same number of ohms for every 1 °C**.

Look at one copper coil:

| Temperature | 20 °C | 30 °C | 40 °C | 70 °C |
|---|---|---|---|---|
| Resistance | 10.0 Ω | 10.4 Ω | 10.8 Ω | 12.0 Ω |

Every 1 °C adds **0.04 Ω**. That is the whole idea. Everything below just writes it as a formula.

### The temperature coefficient $\alpha$

We do not quote "0.04 Ω per °C", because that depends on the size of the coil (a coil of 100 Ω would rise 0.4 Ω per °C). Instead we compare the rise with the resistance at a chosen starting (reference) temperature:

$$\alpha_{ref} = \frac{\text{rise in resistance per °C}}{\text{resistance at the reference temperature}}$$

For the coil above, with 20 °C as reference: $\alpha_{20} = \dfrac{0.04}{10} = 0.004$ /°C. In words: **each 1 °C rise adds 0.4 % of the 20 °C resistance**.

Now the formula follows directly. Rise from $T_{ref}$ to $T$ is $(T - T_{ref})$ degrees; each degree adds $\alpha_{ref}R_{ref}$ ohms:

$$R_T = R_{ref} + \alpha_{ref}R_{ref}(T - T_{ref}) = R_{ref}\,[\,1 + \alpha_{ref}\,(T - T_{ref})\,]$$

Check with the table: $R_{70} = 10[1 + 0.004 \times 50] = 10 \times 1.2 = 12$ Ω. ✓

Sign of $\alpha$:

- Metals: $\alpha > 0$ (copper ≈ 0.0039 /°C at 20 °C).
- Carbon, semiconductors, electrolytes, insulators: $\alpha < 0$ (resistance falls when hot).
- Alloys like manganin and constantan: $\alpha \approx 0$. That is why standard resistors are made from them: **high resistivity** (so a small coil gives a big resistance) and **very low temperature coefficient** (so the value stays fixed).

### Why $\alpha$ depends on the reference temperature

The rise per °C (0.04 Ω for our coil) is fixed. But $\alpha$ divides it by the resistance **at the reference**, and that resistance is different at different references:

- reference 20 °C: $\alpha_{20} = 0.04/10 = 0.004$
- reference 70 °C: $\alpha_{70} = 0.04/12 = 0.00333$

Same coil, same line, two different $\alpha$s. A higher reference temperature means a bigger resistance underneath, so a **smaller** $\alpha$. So $\alpha$ is always stated **at** a temperature: $\alpha_0$ at 0 °C, $\alpha_{20}$ at 20 °C, and so on. When an exam gives two readings and asks for "the temperature coefficient" without naming a reference, give $\alpha_0$ (at 0 °C).

### The no-calculator trick: the "zero point" of the line

Extend the straight line downwards. Our coil loses 0.04 Ω per °C going down, so from 10 Ω at 20 °C it reaches **zero** after $10/0.04 = 250$ °C, i.e. at $20 - 250 = -230$ °C. Call this temperature $-T_z$ (here $T_z = 230$).

Now look at $\alpha$ again: $\alpha_{ref}$ = (rise per °C) ÷ (resistance at ref), and (resistance at ref) = (rise per °C) × (degrees from the zero point to ref). So

$$\alpha_{ref} = \frac{1}{T_{ref} + T_z} \qquad\text{or}\qquad \frac{1}{\alpha_{ref}} = T_{ref} + T_z$$

Check: $1/\alpha_{20} = 20 + 230 = 250$ ⇒ $\alpha_{20} = 1/250 = 0.004$ ✓; $1/\alpha_{70} = 300$ ⇒ $\alpha_{70} = 0.00333$ ✓.

So **$1/\alpha$ simply goes up by 1 for every 1 °C you move the reference up**:

$$\frac{1}{\alpha_{T_1}} = \frac{1}{\alpha_0} + T_1$$

(This is the same as the textbook form $\alpha_{T_1} = \dfrac{\alpha_0}{1 + \alpha_0T_1}$, but much easier to use without a calculator.) For copper, $1/\alpha_0 \approx 234.5$, so copper's line hits zero at about −234.5 °C.

### Hot and cold resistance of a lamp

A lamp filament (tungsten) runs at about 2500 °C. Its hot resistance is roughly 10–15 times its cold (room-temperature) resistance. This is why a lamp draws a big current for an instant when switched on.

- The rating "60 W, 240 V" tells us the **hot** resistance: $R_{hot} = V^2/P$.
- If you know the cold resistance and $\alpha$, you can find the filament temperature from $R_T = R_{ref}[1 + \alpha_{ref}(T - T_{ref})]$.

**Example 2.** A lamp is rated 60 W, 240 V. Its resistance at 20 °C is 80 Ω and $\alpha_{20} = 5\times10^{-3}$ /°C. Find the filament temperature.

1. Hot resistance from the rating: $R_{hot} = \dfrac{240^2}{60} = \dfrac{57600}{60} = 960$ Ω.
2. Put it in the formula: $960 = 80\,[1 + 0.005(T - 20)]$.
3. Divide both sides by 80: $12 = 1 + 0.005(T - 20)$.
4. Take 1 to the left: $11 = 0.005(T - 20)$.
5. Divide by 0.005 (same as multiplying by 200): $T - 20 = 11 \times 200 = 2200$.
6. $T = 2220$ °C.

Quick sense check: the resistance became 12 times, i.e. it **rose by 11 times** $R_{20}$. Each °C adds 0.5 % = 1/200 of $R_{20}$, so we need $11 \times 200 = 2200$ °C of rise.

**Example 3.** A resistor reads 50 Ω at 25 °C and 57.2 Ω at 70 °C. Find its temperature coefficient (a) at 25 °C, (b) at 0 °C.

*Step 1: the rise per °C.* From 25 °C to 70 °C is a 45 °C rise, and the resistance rose by $57.2 - 50 = 7.2$ Ω. So each °C adds

$$\frac{7.2}{45} = 0.16 \text{ Ω per °C}$$

(Without a calculator: $7.2/45 = 72/450 = 8/50 = 0.16$.)

*(a) Reference 25 °C.* Divide the rise per °C by the resistance at 25 °C:

$$\alpha_{25} = \frac{0.16}{50} = 0.0032 = \frac{1}{312.5} \text{ /°C}$$

*(b) Reference 0 °C.* First we need the resistance at 0 °C. Going **down** from 25 °C to 0 °C is 25 degrees, and each degree removes 0.16 Ω:

$$R_0 = 50 - 25 \times 0.16 = 50 - 4 = 46 \text{ Ω}$$

Now divide the same rise per °C by this resistance:

$$\alpha_0 = \frac{0.16}{46} = \frac{16}{4600} = \frac{1}{287.5} \text{ /°C}$$

*Check with the trick:* $1/\alpha_{25} = 1/\alpha_0 + 25$ ⇒ $312.5 = 287.5 + 25$ ✓. (Also: $R_{70} = 46[1 + 70/287.5] = 46 + 11.2 = 57.2$ Ω ✓.)

*Why the two answers differ:* both use the same 0.16 Ω per °C, but (a) divides by 50 Ω and (b) by 46 Ω. A smaller resistance underneath gives a bigger $\alpha$, so $\alpha_0 > \alpha_{25}$.

*The same answer by the formula only (the way most books show it).* Write both readings with 0 °C as reference: $50 = R_0(1 + 25\alpha_0)$ and $57.2 = R_0(1 + 70\alpha_0)$. Divide the second by the first ($R_0$ cancels):

$$\frac{57.2}{50} = \frac{1 + 70\alpha_0}{1 + 25\alpha_0}$$

Cross-multiply: $57.2\,(1 + 25\alpha_0) = 50\,(1 + 70\alpha_0)$.
Open the brackets: $57.2 + 1430\,\alpha_0 = 50 + 3500\,\alpha_0$ (since $57.2 \times 25 = 1430$ and $50 \times 70 = 3500$).
Collect: $57.2 - 50 = 3500\,\alpha_0 - 1430\,\alpha_0$ ⇒ $7.2 = 2070\,\alpha_0$.
So $\alpha_0 = \dfrac{7.2}{2070} = \dfrac{1}{287.5}$ (because $2070 \div 7.2 = 287.5$).

Both ways give the same answer; the step-by-step way (find the ohms per °C first) is quicker and safer in the exam.

### Resistance from field and current density

Inside a conductor of length $l$ and area $A$: $V = El$ and $I = JA$. So

$$R = \frac{V}{I} = \frac{El}{JA}$$

and since $J = \sigma E$, this is the same as $R = \dfrac{l}{\sigma A} = \rho\dfrac{l}{A}$.

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

**Example 4.** A 25 W, 230 V lamp and a 100 W, 230 V lamp are in series across 230 V. $R_1 = 230^2/25 = 2116$ Ω, $R_2 = 230^2/100 = 529$ Ω. Current $= 230/2645 = 0.087$ A. $P_1 = 0.087^2\times2116 = 16$ W, $P_2 = 4$ W. The **25 W lamp glows brighter**. Total $= 20$ W, less than either rating.

**Example 5.** How many 120 W, 240 V heaters in series take the same total power from 240 V as one 40 W, 240 V heater?
Equal power from the same supply needs equal total resistance. One 40 W heater: $240^2/40 = 1440$ Ω. Each 120 W heater: $240^2/120 = 480$ Ω. So $n\times480 = 1440$ ⇒ $n = 3$.

### Power when the current changes with time

The formula $p = i^2R$ works at every instant. If $i = I_m\sin\omega t$:

$$v = R I_m \sin\omega t$$

$$p = R I_m^2 \sin^2\omega t$$

The power is never negative; it pulses between 0 and $RI_m^2$ at twice the supply frequency. Its average is $\tfrac12 RI_m^2$ (you will see this again as the RMS idea in the AC power chapter).

**Example 6.** $i = 3\sin 100t$ A in a 4 Ω resistor: $v = 12\sin 100t$ V, $p = 36\sin^2 100t$ W.

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

**Example 7.** A 6 V source with internal resistance 1 Ω feeds a non-linear resistor with $V = 2I^2$.
$6 - I\times1 = 2I^2$ ⇒ $2I^2 + I - 6 = 0$ ⇒ $I = \dfrac{-1 \pm \sqrt{1+48}}{4} = \dfrac{-1\pm7}{4}$.
So $I = 1.5$ A (the other root, $-2$ A, would make the passive element give power: reject).
$V = 2(1.5)^2 = 4.5$ V, power in the resistor $= 4.5\times1.5 = 6.75$ W.

**Example 8.** A resistor has $R = (10 + I)$ Ω, where $I$ is its current in amperes. It is connected to 24 V.
$24 = I(10 + I)$ ⇒ $I^2 + 10I - 24 = 0$ ⇒ $(I + 12)(I - 2) = 0$ ⇒ $I = 2$ A (reject $-12$).

**Graphical solution:** draw the load line $V = E - Ir$ and the curve of the non-linear element on the same axes; the crossing point is the answer. This works even when the curve has no formula (only a measured graph).

## Summary of this section

| Idea | Formula / fact |
|---|---|
| Ohm's law | $v = Ri$, $G = 1/R$ |
| Resistance | $R = \rho l/A$ |
| Temperature | $R_T = R_{ref}[1 + \alpha_{ref}(T - T_{ref})]$; $1/\alpha_{T} = 1/\alpha_0 + T$; metals $\alpha>0$ |
| Power | $p = i^2R = v^2/R \ge 0$ (always absorbs) |
| Rated device | $R = V_r^2/P_r$; lower watts ⇒ higher R |
| Series lamps | lower-watt lamp glows brighter; total power < each rating |
| Cable | conductor R adds in series; insulation R combines in parallel |
| Non-linear | load line + element rule ⇒ solve; reject the impossible root |
