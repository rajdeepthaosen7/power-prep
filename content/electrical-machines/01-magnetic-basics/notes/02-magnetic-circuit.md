## The idea: a "circuit" for flux

Wind a coil on an iron ring and pass a current. Flux appears and goes round and round inside the ring. This closed path of flux is called a **magnetic circuit**.

It behaves so much like an electric circuit that we can solve it with the same steps:

| Electric circuit | Magnetic circuit |
|---|---|
| A battery pushes current round a wire | A coil (current × turns) pushes flux round the iron |
| EMF, $E$ (volt) | MMF, $F = NI$ (ampere-turn) |
| Current, $I$ (ampere) | Flux, $\Phi$ (weber) |
| Resistance, $R = \dfrac{l}{\sigma A}$ (ohm) | Reluctance, $S = \dfrac{l}{\mu A}$ (AT/Wb) |
| Conductivity, $\sigma$ | Permeability, $\mu$ |
| Ohm's law: $I = \dfrac{E}{R}$ | "Ohm's law of magnetic circuits": $\Phi = \dfrac{F}{S}$ |

If you know DC circuits from Electric Circuits Chapters 1 and 2, you already know how to solve magnetic circuits.

## Magnetomotive force (MMF)

```definition
**Magnetomotive force** (MMF, symbol **F**) is the "push" that drives flux round a magnetic circuit. It equals the current in the coil multiplied by the number of turns: $F = NI$.

**Unit:** ampere-turn (AT). (A turn is only a count, so some books write simply "A".)

**Example:** 500 turns carrying 2 A give $F = 500 \times 2 = 1000$ AT. So do 1000 turns carrying 1 A: the iron cannot tell the difference.
```

## Reluctance: where the formula comes from

We already have three facts from the last topic:

1. $H = \dfrac{NI}{l}$ (ampere-turns per metre of path)
2. $B = \mu H$, with $\mu = \mu_0 \mu_r$
3. $\Phi = B \times A$

Put them together, one step at a time:

$\Phi = B \times A$

$\quad = (\mu H) \times A$

$\quad = \mu \times \dfrac{NI}{l} \times A$

$\quad = \dfrac{NI}{\;\dfrac{l}{\mu A}\;}$

The bottom part, $\dfrac{l}{\mu A}$, depends only on the shape and material of the path. It is the opposition of the path to flux. We call it **reluctance**.

```definition
**Reluctance** (symbol **S**, some books use $\mathcal{R}$) is the opposition offered by a magnetic path to flux.

**Unit:** ampere-turn per weber (AT/Wb). (This is the same as 1/henry, H⁻¹.)

**It grows** with the length of the path, and **falls** with a bigger cross-section and a higher permeability, exactly like resistance.
```

```formula
$$\fx{S = \dfrac{l}{\mu_0\,\mu_r\,A}} \qquad\qquad \fx{\Phi = \dfrac{F}{S} = \dfrac{NI}{S}}$$

- $l$ = mean length of the path (m), $A$ = cross-section (m²)
- $\mu_0 = 4\pi \times 10^{-7}$ H/m, $\mu_r$ = relative permeability
- **Permeance** $= 1/S$ (Wb/AT): how easily flux passes. It is to reluctance what conductance is to resistance.
```

```example Worked example 1: reluctance of a core
An iron core has a mean length of $\given{80\ \text{cm}}$, a cross-section of $\given{10\ \text{cm}^2}$ and a relative permeability of $\given{1600}$. Find its reluctance.
:::given $l = 80\ \text{cm}$, $A = 10\ \text{cm}^2$, $\mu_r = 1600$
:::find reluctance $S$
:::step Change to SI units.
$l = 80\ \text{cm} = 0.8\ \text{m}$
$A = 10\ \text{cm}^2 = 10 \times 10^{-4} = 10^{-3}\ \text{m}^2$
:::step Work out the bottom of the formula, $\mu_0 \mu_r A$.
$\mu_0 = 4\pi \times 10^{-7} = 12.566 \times 10^{-7}$
$\mu_0 \times \mu_r = 12.566 \times 10^{-7} \times 1600$
$12.566 \times 1600 = 20\,106$
$\mu_0 \mu_r = 20\,106 \times 10^{-7} = 2.0106 \times 10^{-3}$
$\mu_0 \mu_r A = 2.0106 \times 10^{-3} \times 10^{-3} = 2.0106 \times 10^{-6}$
:::step Divide the length by it. $\fx{S = l \div (\mu_0 \mu_r A)}$
$S = 0.8 \div (2.0106 \times 10^{-6})$
$= (0.8 \div 2.0106) \times 10^{6}$
$0.8 \div 2.0106 = 0.3979$ (because $2 \times 0.4 = 0.8$, a little less than 0.4)
$S = 0.398 \times 10^{6}\ \text{AT/Wb}$
:::answer $\ans{S \approx 3.98 \times 10^{5}\ \text{AT/Wb}}$
**Without a calculator:** $4\pi \times 1600 \approx 12.57 \times 1600 \approx 20\,000$. So the bottom is about $2 \times 10^{-3} \times 10^{-3} = 2 \times 10^{-6}$, and $0.8 \div (2 \times 10^{-6}) = 4 \times 10^{5}$. Pick the option nearest to $4 \times 10^5$.
```

```example Worked example 2: current needed for a given flux
An iron ring has a mean length of $\given{50\ \text{cm}}$ and a cross-section of $\given{4\ \text{cm}^2}$. Its relative permeability is $\given{1000}$. A coil of $\given{200}$ turns is wound on it. What $\find{\text{current}}$ is needed to set up a flux of $\given{0.5\ \text{mWb}}$?
:::given $l = 50\ \text{cm}$, $A = 4\ \text{cm}^2$, $\mu_r = 1000$, $N = 200$, $\Phi = 0.5\ \text{mWb}$
:::find current $I$
:::step Change every value to SI units.
$l = 50\ \text{cm} = 0.5\ \text{m}$
$A = 4\ \text{cm}^2 = 4 \times 10^{-4}\ \text{m}^2$
$\Phi = 0.5\ \text{mWb} = 0.5 \times 10^{-3}\ \text{Wb}$
:::step Find the flux density. $\fx{B = \Phi \div A}$
$B = (0.5 \times 10^{-3}) \div (4 \times 10^{-4})$
$= (0.5 \div 4) \times 10^{-3+4}$
$= 0.125 \times 10$
$= 1.25\ \text{T}$
:::step Find the permeability of the iron. $\fx{\mu = \mu_0 \mu_r}$
$\mu = 12.566 \times 10^{-7} \times 1000 = 12.566 \times 10^{-4}\ \text{H/m}$
:::step Find H, the ampere-turns needed per metre. $\fx{H = B \div \mu}$
$H = 1.25 \div (12.566 \times 10^{-4})$
$= (1.25 \div 12.566) \times 10^{4}$
$12.566 \times 0.1 = 1.2566$, just above 1.25, so $1.25 \div 12.566 = 0.0995$
$H = 0.0995 \times 10^{4} = 995\ \text{A/m}$
:::step Ampere-turns for the whole ring. $\fx{F = H \times l}$
$F = 995 \times 0.5 = 497.5\ \text{AT}$
:::step Divide by the number of turns. $\fx{I = F \div N}$
$I = 497.5 \div 200$
$= 2.49\ \text{A}$
:::answer $\ans{I \approx 2.49\ \text{A}}$
**Sense check:** 200 turns × 2.49 A = 498 AT, the MMF we needed. ✓
**Second way (reluctance):** $S = l \div (\mu A) = 0.5 \div (12.566 \times 10^{-4} \times 4 \times 10^{-4}) \approx 9.95 \times 10^{5}$ AT/Wb, so $F = \Phi S = 0.5 \times 10^{-3} \times 9.95 \times 10^{5} \approx 497$ AT. Same answer.
```

## Series magnetic circuit: parts one after another

Often the flux passes through several parts one after the other: iron of one size, iron of another size, an **air gap**. The same flux passes through every part (like the same current through resistors in series).

```remember
**Series magnetic circuit**

- Total reluctance = sum of the parts: $S = S_1 + S_2 + S_3 + \dots$
- Total MMF needed = sum of the MMFs of the parts: $NI = H_1 l_1 + H_2 l_2 + \dots$ (this is "KVL" for magnetic circuits).
- The flux $\Phi$ is the same in every part. If the areas differ, $B$ differs from part to part ($B = \Phi / A$).
```

### The air gap needs most of the MMF

Air has $\mu_r = 1$, iron has $\mu_r$ in the thousands. So even a very short air gap can have **more** reluctance than a long iron path.

```example Worked example 3: a ring with an air gap
The ring of Example 2 ($\given{l_i = 0.5\ \text{m}}$ of iron, $\given{\mu_r = 1000}$, $\given{A = 4\ \text{cm}^2}$) is cut to make an air gap of $\given{1\ \text{mm}}$. How many ampere-turns are now needed for the same flux of $\given{0.5\ \text{mWb}}$? (Ignore fringing: take the gap area equal to the iron area.)
:::given $l_i = 0.5\ \text{m}$, $\mu_r = 1000$, $l_g = 1\ \text{mm}$, $A = 4 \times 10^{-4}\ \text{m}^2$, $\Phi = 0.5\ \text{mWb}$
:::find total MMF $F$
:::step The flux density is the same as before (same flux, same area).
$B = 1.25\ \text{T}$ (from Example 2)
:::step Iron part: H and MMF are the same as before (the iron is only 1 mm shorter, which we can ignore).
$F_{iron} = 497.5\ \text{AT}$
:::step Air gap: find H in air. $\fx{H_g = B \div \mu_0}$
$H_g = 1.25 \div (12.566 \times 10^{-7})$
$= (1.25 \div 12.566) \times 10^{7}$
$= 0.0995 \times 10^{7}$
$= 9.95 \times 10^{5}\ \text{A/m}$
:::step MMF for the gap. $\fx{F_g = H_g \times l_g}$
$l_g = 1\ \text{mm} = 10^{-3}\ \text{m}$
$F_g = 9.95 \times 10^{5} \times 10^{-3}$
$= 995\ \text{AT}$
:::step Add the two parts (series circuit). $\fx{F = F_{iron} + F_g}$
$F = 497.5 + 995$
$= 1492.5\ \text{AT}$
:::answer $\ans{F \approx 1490\ \text{AT}}$, three times what the ring needed without the gap.
**What it shows:** 1 mm of air needs twice the ampere-turns of 500 mm of iron. Reason: 1 mm of air is "worth" $1 \times 1000 = 1000$ mm of this iron, and the iron path is only 500 mm.
```

```remember
**Quick rule for gap questions:** an air gap of length $l_g$ in series with iron of length $l_i$ behaves like a total "air length" of

$$l_g + \dfrac{l_i}{\mu_r}$$

If the iron is **ideal** ($\mu_r \to \infty$), the iron needs no MMF at all and **only the air gap counts**: $NI = \dfrac{B\,l_g}{\mu_0}$.
```

```example Worked example 4: what a finite μr does to B in a gapped core
A core has an iron path of $\given{40\ \text{cm}}$ and an air gap of $\given{0.2\ \text{cm}}$. With ideal iron ($\mu_r = \infty$) the coil gives $\given{B = 1\ \text{T}}$ in the gap. What is $\find{B}$ with the same coil current if $\given{\mu_r = 1000}$?
:::given $l_i = 40\ \text{cm}$, $l_g = 0.2\ \text{cm}$, $B_{ideal} = 1\ \text{T}$, $\mu_r = 1000$
:::find the new flux density $B$
:::step Same MMF both times, and $B$ is MMF divided by the total "air length" (times $\mu_0$). So B falls in the ratio of the air lengths.
$B_{new} \div B_{ideal} = l_g \div (l_g + l_i/\mu_r)$
:::step Find the iron's "air length". $\fx{l_i \div \mu_r}$
$l_i \div \mu_r = 40\ \text{cm} \div 1000 = 0.04\ \text{cm}$
:::step Total air length with real iron.
$l_g + l_i/\mu_r = 0.2 + 0.04 = 0.24\ \text{cm}$
:::step The ratio.
$B_{new} = 1 \times (0.2 \div 0.24)$
$0.2 \div 0.24 = 20 \div 24 = 5 \div 6$
$= 0.833\ \text{T}$
:::answer $\ans{B \approx 0.83\ \text{T}}$
**Sense check:** real iron adds a little reluctance, so B must be a bit less than 1 T, but not much less because the gap still dominates. ✓
```

## Parallel magnetic circuit

In a **shell-type** core, the flux made by a coil on the central limb splits into two outer limbs and joins again, like current splitting into two parallel branches.

```remember
**Kirchhoff's laws hold for magnetic circuits too:**

- **Junction rule ("KCL"):** the flux entering a junction equals the flux leaving it. Central limb flux $\Phi = \Phi_1 + \Phi_2$.
- **Loop rule ("KVL"):** round any closed path, the MMF of the coils equals the sum of $H \times l$ of the parts.
- For two equal outer limbs, each carries half the central flux. The central limb is therefore usually made twice as wide as each outer limb, so that $B$ is the same everywhere.
```

## Leakage and fringing

```definition
**Leakage flux** is the part of the flux that does not follow the intended path. For example, in a transformer some flux of the primary coil closes through the air around the coil and never reaches the secondary.

There is no magnetic "insulator" (even air carries flux), so some leakage is always present. Iron cores and coils wound one over the other keep it small.

**Leakage coefficient** = total flux produced ÷ useful flux (always a little more than 1).
```

```definition
**Fringing** is the bulging outwards of flux lines as they cross an air gap. It makes the effective area of the gap a little **larger** than the iron face, so the gap flux density is a little smaller. Unless told, ignore fringing in calculations.
```

## How magnetic circuits differ from electric circuits

The analogy is very useful, but it is not perfect. These differences are often asked:

| Electric circuit | Magnetic circuit |
|---|---|
| Current really flows (electrons move). | Flux does not "flow"; it is only set up. |
| Energy is used all the time a current flows ($I^2R$ loss). | Energy is needed only to **set up** the flux; a steady flux needs no continuous energy (core losses come only when the flux **changes**). |
| Resistance does not depend on the current (at constant temperature). | Reluctance of iron **changes** with the flux (μr changes as iron saturates). |
| Good insulators exist (air, rubber), so current stays in the wire. | No magnetic insulator exists, so leakage flux is always present. |

## Summary of this topic

- MMF $F = NI$ (AT) drives flux; reluctance $S = \dfrac{l}{\mu_0 \mu_r A}$ (AT/Wb) opposes it; $\Phi = F/S$.
- Series parts: reluctances add; the same flux passes through each. Parallel limbs: fluxes add at a junction.
- A short air gap can need more MMF than a long iron path. With ideal iron, only the gap counts.
- Leakage flux is unavoidable (no magnetic insulator); fringing widens the gap area.
