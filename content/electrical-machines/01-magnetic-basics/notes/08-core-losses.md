## Where the iron wastes energy

When the flux in iron keeps changing (as in every AC machine and transformer), the iron heats up. This heat is energy taken from the supply and wasted. It is called the **core loss** or **iron loss**, and it has two parts:

```remember
**Core (iron) loss = hysteresis loss + eddy current loss**

$P_i = P_h + P_e$

In a transformer the core flux is almost the same at every load (topic *EMF equation*), so the core loss is a **constant loss**: it does not change with the load current. (The copper loss, $I^2R$ in the windings, is the variable loss.)
```

## 1. Hysteresis loss

Every time the iron is taken round its hysteresis loop, its domains are turned one way and then back again. This "molecular friction" turns energy into heat. The energy lost in one cycle is the **area of the hysteresis loop**.

```remember
**Loop area → energy, by the units of the axes:**

- Axes **B (T) and H (A/m):** loop area is in T × A/m = **joule per m³ per cycle**. Multiply by the core volume (m³) and by the frequency (cycles per second) to get watts.
- Axes **Φ (Wb) and MMF (AT):** loop area is in Wb × AT = **joule per cycle for the whole core**. Multiply only by the frequency.

**Reading a loop drawn on graph paper:** area in cm² × (value of 1 cm on one axis) × (value of 1 cm on the other axis).
```

```example Worked example 1: loss from a B-H loop
A hysteresis loop drawn for a sample of steel has an area of $\given{40\ \text{cm}^2}$. The scales are $\given{1\ \text{cm} = 0.1\ \text{T}}$ and $\given{1\ \text{cm} = 50\ \text{A/m}}$. The core volume is $\given{0.005\ \text{m}^3}$ and the frequency $\given{50\ \text{Hz}}$. Find the hysteresis loss.
:::given area $= 40\ \text{cm}^2$; 1 cm = 0.1 T; 1 cm = 50 A/m; $V = 0.005\ \text{m}^3$; $f = 50\ \text{Hz}$
:::find hysteresis loss $P_h$ in watts
:::step Value of 1 cm² of the graph.
$1\ \text{cm}^2 = 0.1\ \text{T} \times 50\ \text{A/m} = 5\ \text{J/m}^3$
:::step Energy per m³ per cycle (the loop area).
$40 \times 5 = 200\ \text{J/m}^3\ \text{per cycle}$
:::step Energy per cycle for the whole core.
$200 \times 0.005 = 1\ \text{J per cycle}$
:::step Power: cycles per second × energy per cycle.
$P_h = 1 \times 50 = 50\ \text{W}$
:::answer $\ans{P_h = 50\ \text{W}}$
```

```example Worked example 2: loss from a Φ-MMF loop
The hysteresis loop of a core is drawn with $\given{1\ \text{cm} = 5\ \text{AT}}$ and $\given{1\ \text{cm} = 20\ \text{mWb}}$. Its area is $\given{4\ \text{cm}^2}$. Find the hysteresis loss at $\given{50\ \text{Hz}}$.
:::given area $= 4\ \text{cm}^2$; 1 cm = 5 AT; 1 cm = 20 mWb; $f = 50\ \text{Hz}$
:::find hysteresis loss $P_h$
:::step Value of 1 cm² of the graph.
$20\ \text{mWb} = 0.02\ \text{Wb}$
$1\ \text{cm}^2 = 5\ \text{AT} \times 0.02\ \text{Wb} = 0.1\ \text{J}$
:::step Energy per cycle (whole core, because the axes are Φ and MMF).
$4 \times 0.1 = 0.4\ \text{J per cycle}$
:::step Power.
$P_h = 0.4 \times 50 = 20\ \text{W}$
:::answer $\ans{P_h = 20\ \text{W}}$
```

```example Worked example 3: energy lost per hour, given mass and density
An iron specimen of mass $\given{60\ \text{kg}}$ (density $\given{7500\ \text{kg/m}^3}$) is taken through a hysteresis loop of area $\given{300\ \text{J/m}^3}$ at $\given{50\ \text{Hz}}$. Find the energy lost in one hour.
:::given $m = 60\ \text{kg}$, density $= 7500\ \text{kg/m}^3$, loop $= 300\ \text{J/m}^3$, $f = 50\ \text{Hz}$
:::find energy lost per hour
:::step Volume = mass ÷ density.
$V = 60 \div 7500 = 0.008\ \text{m}^3$
:::step Energy per cycle.
$300 \times 0.008 = 2.4\ \text{J}$
:::step Cycles in one hour.
$1\ \text{h} = 3600\ \text{s}$
$50 \times 3600 = 180\,000\ \text{cycles}$
:::step Energy per hour.
$2.4 \times 180\,000 = 432\,000\ \text{J}$
:::answer $\ans{4.32 \times 10^{5}\ \text{J}}$ per hour (that is 120 W).
```

### Steinmetz's formula

Steinmetz found by experiment how the hysteresis loss depends on the peak flux density.

```formula
$$\fx{P_h = \eta\,B_m^{1.6}\,f\,V} \quad \text{watt}$$

- $\eta$ = Steinmetz hysteresis coefficient (depends on the material), $B_m$ = peak flux density (T), $f$ = frequency (Hz), $V$ = volume of the core (m³).
- The power 1.6 is Steinmetz's value; for modern steels it lies between about 1.5 and 2.5.
- **At a fixed $B_m$, the hysteresis loss is directly proportional to frequency** (the same loop, traced $f$ times a second).
```

**To reduce hysteresis loss:** use a material with a narrow loop (silicon steel, CRGO) and do not work it at too high a flux density.

## 2. Eddy current loss

The alternating flux induces EMFs not only in the windings but in the iron itself (iron is a conductor). In a solid core these EMFs drive currents round closed paths inside the iron, like whirlpools ("eddies"). They heat the core: $I^2R$ loss in the iron.

```formula
$$\fx{P_e = K_e\,B_m^{2}\,f^{2}\,t^{2}\,V} \quad \text{watt}$$

- $t$ = thickness of each lamination (m); $K_e$ depends on the resistivity of the iron (the higher the resistivity, the smaller $K_e$).
- Eddy loss goes as the **square** of the flux density, the **square** of the frequency and the **square** of the lamination thickness.
```

```remember
**How to reduce eddy current loss:**

1. **Laminate the core:** thin sheets (0.35 to 0.5 mm) insulated from each other by varnish. Halving the thickness cuts the eddy loss to a quarter.
2. **Raise the resistivity of the iron:** add silicon (silicon steel).
3. At high frequency use **ferrites** (ceramic, very high resistivity) or powdered-iron cores.

A **solid** core gives the **largest** eddy loss.

**Eddy currents are not always bad.** They are used for induction heating and furnaces, for braking the disc of an energy meter, and for damping the pointer of measuring instruments.
```

## How the core loss changes with voltage and frequency

From the EMF equation, $B_m \propto \dfrac{V}{f}$. Put this into the two formulas, one step at a time.

**Hysteresis loss** (using $B_m^{1.6}$):

$P_h \propto B_m^{1.6} f = \left(\dfrac{V}{f}\right)^{1.6} f = V^{1.6} f^{-0.6}$

**Eddy current loss:**

$P_e \propto B_m^{2} f^{2} = \left(\dfrac{V}{f}\right)^{2} f^{2} = \dfrac{V^2}{f^2} \times f^2 = V^{2}$

```remember
**Eddy current loss depends only on the voltage, not on the frequency:** $P_e \propto V^2$.

| What changes | $B_m$ | Hysteresis loss $P_h$ | Eddy loss $P_e$ |
|---|---|---|---|
| $V$ and $f$ change together (V/f constant) | constant | $\propto f$ | $\propto f^2$ |
| $V$ changes, $f$ fixed | $\propto V$ | $\propto V^{1.6}$ | $\propto V^2$ |
| $f$ changes, $V$ fixed | $\propto 1/f$ | $\propto f^{-0.6}$ (rises as $f$ falls) | **unchanged** |
```

```example Worked example 4: eddy loss at a higher voltage
A transformer has an eddy current loss of $\given{80\ \text{W}}$ at $\given{200\ \text{V}}$, 50 Hz. Find the eddy current loss at $\given{250\ \text{V}}$, 50 Hz.
:::given $P_{e1} = 80\ \text{W}$ at $V_1 = 200\ \text{V}$; $V_2 = 250\ \text{V}$; same frequency
:::find $P_{e2}$
:::step Eddy loss goes as $V^2$. Find the voltage ratio.
$V_2 \div V_1 = 250 \div 200 = 1.25$
:::step Square it.
$1.25^2 = 1.5625$
:::step Multiply the old loss.
$P_{e2} = 80 \times 1.5625 = 125\ \text{W}$
:::answer $\ans{P_{e2} = 125\ \text{W}}$
```

```example Worked example 5: V and f both raised by 20 %
For a transformer, the supply voltage and frequency are both raised by $\given{20\ \%}$. By what percentage do the hysteresis and eddy current losses change?
:::given $V$ and $f$ both × 1.2
:::find % change in $P_h$ and in $P_e$
:::step $B_m \propto V/f$, and V/f is unchanged.
$B_m$ stays the same
:::step Hysteresis loss at fixed $B_m$ goes as $f$.
$P_h$ becomes $1.2$ times: a rise of 20 %
:::step Eddy loss at fixed $B_m$ goes as $f^2$.
$1.2^2 = 1.44$: a rise of 44 %
:::answer $\ans{P_h\ \text{up}\ 20\,\%,\ P_e\ \text{up}\ 44\,\%}$
```

## Separating the two losses

The total core loss can be measured (open-circuit test). To split it into its two parts, measure it at **two frequencies, keeping V/f constant** (so $B_m$ is the same in both tests). Then

$P_i = A f + B f^2$, where $A f$ is the hysteresis part and $B f^2$ the eddy part.

Divide by $f$: $\dfrac{P_i}{f} = A + B f$. This is a straight line when $P_i/f$ is plotted against $f$: the line cuts the axis at $A$, and its slope is $B$.

```example Worked example 6: separating hysteresis and eddy loss
At constant V/f, a transformer's core loss is $\given{1000\ \text{W}}$ at $\given{50\ \text{Hz}}$ and $\given{350\ \text{W}}$ at $\given{25\ \text{Hz}}$. Find the hysteresis and eddy current losses at 50 Hz.
:::given $P_i = 1000\ \text{W}$ at 50 Hz; $P_i = 350\ \text{W}$ at 25 Hz; V/f constant
:::find $P_h$ and $P_e$ at 50 Hz
:::step Write $P_i / f = A + B f$ for each test.
$1000 \div 50 = 20$, so $20 = A + 50B$
$350 \div 25 = 14$, so $14 = A + 25B$
:::step Subtract the second from the first.
$20 - 14 = 50B - 25B$
$6 = 25B$
$B = 6 \div 25 = 0.24$
:::step Find A.
$A = 14 - 25 \times 0.24$
$25 \times 0.24 = 6$
$A = 14 - 6 = 8$
:::step Losses at 50 Hz.
$P_h = A f = 8 \times 50 = 400\ \text{W}$
$P_e = B f^2 = 0.24 \times 2500 = 600\ \text{W}$
:::answer $\ans{P_h = 400\ \text{W},\ P_e = 600\ \text{W}}$ at 50 Hz.
**Sense check:** $400 + 600 = 1000$ W ✓. At 25 Hz: $8 \times 25 + 0.24 \times 625 = 200 + 150 = 350$ W ✓.
```

```watchout
The method works only if $B_m$ is the same in both tests, i.e. **V/f is kept constant**. Check this first in every question (for example 230 V at 50 Hz and 138 V at 30 Hz both give 4.6 V/Hz). If V/f is not constant, first scale the losses for the change in $B_m$.
```

## Summary of this topic

- Core loss = hysteresis + eddy; a constant loss in a transformer.
- Hysteresis: energy per cycle = loop area; $P_h = \eta B_m^{1.6} f V$; reduce with narrow-loop steel.
- Eddy: $P_e = K_e B_m^2 f^2 t^2 V$; reduce with thin insulated laminations, silicon (higher resistivity), ferrites at high frequency.
- $B_m \propto V/f$, so $P_e \propto V^2$ (independent of $f$); at constant V/f: $P_h \propto f$, $P_e \propto f^2$.
- Separation: two tests at constant V/f; $P_i/f = A + Bf$.
