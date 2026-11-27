## Why machines start with magnetism

Every machine in this subject (transformer, DC machine, induction motor, alternator) works with **magnetism**. A current makes a magnetic field; a changing magnetic field makes a voltage; a magnetic field pushes on a current. These three facts are the whole of Electrical Machines. So before any machine, we must be clear about the magnetic words: field, flux, flux density, magnetising force and permeability.

## The magnetic field and field lines

Bring a bar magnet near some iron filings. The filings arrange themselves in curved lines from one end of the magnet to the other. The space around the magnet where this force acts is called the **magnetic field**. The curved lines we see are called **magnetic lines of force** (or field lines).

A current also makes a magnetic field. Put a compass near a wire carrying direct current: the needle turns. (This was Oersted's discovery in 1820.) Wind the wire into a coil, and the coil behaves just like a bar magnet with a North end and a South end. This is how every machine makes its magnetism: **with current in a coil**.

```remember
Properties of magnetic lines of force:

1. Outside a magnet they go from the N-pole to the S-pole; inside the magnet they go from S back to N. So each line is a **closed loop**.
2. Two lines never cross each other.
3. Where lines are crowded, the field is strong; where they are spread out, the field is weak.
4. Lines prefer to pass through iron rather than air (iron is "easier" for them, as we will see).
```

**Direction of the field of a coil (right-hand grip rule):** hold the coil in your right hand with the fingers curled in the direction of the current. Your thumb points to the N-pole of the coil.

## Magnetic flux, Φ

```definition
**Magnetic flux** (symbol **Φ**, read "phi") is the total amount of magnetic field passing through a surface. Think of it as "the total number of lines of force".

**Unit:** weber (Wb). Smaller units: 1 mWb = $10^{-3}$ Wb, 1 µWb = $10^{-6}$ Wb.

**Example:** the core of a small 1 kVA transformer carries about 1 to 2 mWb.
```

Flux in a magnetic path plays the same part as **current** in an electric circuit: it is the thing that "flows" round the path.

## Flux density, B

The same flux can be spread over a big area or squeezed into a small one. What matters for the iron (how hard it is working) is the flux **per unit area**.

```definition
**Flux density** (symbol **B**) is the flux passing through one square metre of area, taken at right angles to the flux.

**Unit:** tesla (T). 1 T = 1 Wb/m².

**Example:** the Earth's field is about 0.00005 T (50 µT). The iron core of a power transformer works at about 1 to 1.7 T.
```

```formula
$$\fx{B = \dfrac{\Phi}{A}}$$

- $B$ = flux density, in tesla (T)
- $\Phi$ = flux, in weber (Wb)
- $A$ = area at right angles to the flux, in square metre (m²)
```

```watchout
Areas in questions are often given in cm² or mm². Change them to m² first.

- 1 cm = 0.01 m, so 1 cm² = 0.01 m × 0.01 m = **$10^{-4}$ m²**
- 1 mm = 0.001 m, so 1 mm² = 0.001 m × 0.001 m = **$10^{-6}$ m²**

Using $10^{-2}$ for cm² (a common slip) makes the answer 100 times wrong.
```

```example Worked example 1: flux density in a core
A transformer core has a cross-section of $\given{40\ \text{cm}^2}$ and carries a flux of $\given{5\ \text{mWb}}$. Find the flux density in the core.
:::given $A = 40\ \text{cm}^2$, $\Phi = 5\ \text{mWb}$
:::find flux density $B$
:::step Change both values to SI units.
$A = 40\ \text{cm}^2 = 40 \times 10^{-4}\ \text{m}^2$
$\Phi = 5\ \text{mWb} = 5 \times 10^{-3}\ \text{Wb}$
:::step Divide the flux by the area. $\fx{B = \Phi \div A}$
$B = (5 \times 10^{-3}) \div (40 \times 10^{-4})$
$= (5 \div 40) \times 10^{-3+4}$
$5 \div 40 = 0.125$
$= 0.125 \times 10^{1}$
$= 1.25\ \text{T}$
:::answer $\ans{B = 1.25\ \text{T}}$
**Sense check:** 1.25 T is inside the normal working range of transformer iron (1 to 1.7 T). ✓
```

## Magnetising force (field intensity), H

What **makes** the flux? The current in the coil. But the same current does more work if it has more turns, and less if the path it must drive the flux round is longer.

So we measure the "magnetic push per metre of path":

```definition
**Magnetising force** or **magnetic field intensity** (symbol **H**) is the ampere-turns per metre length of the magnetic path.

**Unit:** ampere-turn per metre (AT/m), usually written simply as A/m (a "turn" has no unit).

**Example:** a coil of 100 turns carrying 2 A, wound on a ring of mean length 0.5 m, gives $H = (100 \times 2) \div 0.5 = 400$ A/m.
```

```formula
For a coil wound uniformly on a ring (or on any closed path of one material):

$$\fx{H = \dfrac{N\,I}{l}}$$

- $N$ = number of turns, $I$ = current in ampere (A)
- $l$ = mean length of the magnetic path, in metre (m). For a ring of mean radius $r$: $l = 2\pi r$.
```

Think of the difference between H and B like this: **H is the cause** (how hard the coil is pushing), **B is the effect** (how much flux per m² actually appears). The link between them is the material.

## Permeability, μ

Put the same coil, with the same current (same H), first on a wooden ring and then on an iron ring. The iron ring carries hundreds or thousands of times more flux. The iron "lets the flux through" far more easily. This property is called **permeability**.

```definition
**Permeability** (symbol **μ**, read "mew") tells how much flux density a material gives for each unit of magnetising force: $B = \mu H$.

**Unit:** henry per metre (H/m).

**Permeability of free space (air, vacuum):** $\mu_0 = 4\pi \times 10^{-7}$ H/m $\approx 12.566 \times 10^{-7}$ H/m. This is a fixed number; learn it.
```

```definition
**Relative permeability** (symbol **μr**) is how many times better a material is than air:

$\mu_r = \dfrac{\mu}{\mu_0}$, so $\mu = \mu_0 \mu_r$.

It is a pure number (no unit). Air: $\mu_r = 1$. Silicon steel in a transformer: a few thousand.
```

```formula
$$\fx{B = \mu_0\,\mu_r\,H}$$
```

```example Worked example 2: flux density in an iron ring
An iron ring has a mean length of $\given{60\ \text{cm}}$. A coil of $\given{300}$ turns on it carries $\given{0.5\ \text{A}}$. The relative permeability of the iron is $\given{2000}$. Find H and B.
:::given $l = 60\ \text{cm}$, $N = 300$, $I = 0.5\ \text{A}$, $\mu_r = 2000$
:::find $H$ and $B$
:::step Change the length to metre.
$l = 60\ \text{cm} = 60 \div 100 = 0.6\ \text{m}$
:::step Find the ampere-turns. $\fx{NI}$
$NI = 300 \times 0.5 = 150\ \text{AT}$
:::step Find H. $\fx{H = NI \div l}$
$H = 150 \div 0.6$
$= 1500 \div 6$
$= 250\ \text{A/m}$
:::step Find the permeability of this iron. $\fx{\mu = \mu_0 \mu_r}$
$4\pi = 4 \times 3.1416 = 12.566$
$\mu = 12.566 \times 10^{-7} \times 2000$
$= 25\,132 \times 10^{-7}$
$= 2.5132 \times 10^{-3}\ \text{H/m}$
:::step Find B. $\fx{B = \mu H}$
$B = 2.5132 \times 10^{-3} \times 250$
$2.5132 \times 250 = 628.3$
$B = 628.3 \times 10^{-3}$
$= 0.628\ \text{T}$
:::answer $\ans{H = 250\ \text{A/m}}$ and $\ans{B \approx 0.63\ \text{T}}$
**Sense check:** with air instead of iron ($\mu_r = 1$) the same coil would give only $0.628 \div 2000 = 0.000314$ T. Iron multiplies the flux 2000 times. That is exactly why every machine has an iron core.
```

## Kinds of magnetic materials

All materials respond a little to a magnetic field. They are grouped by their relative permeability.

| Kind | $\mu_r$ | Susceptibility $\chi = \mu_r - 1$ | Examples |
|---|---|---|---|
| **Diamagnetic** | slightly **less** than 1 | small and **negative** | copper, silver, bismuth, water |
| **Paramagnetic** | slightly **more** than 1 | small and **positive** | aluminium, platinum, air |
| **Ferromagnetic** | very large (hundreds to many thousands) | very large and positive | iron, nickel, cobalt and their alloys |
| **Ferrimagnetic (ferrites)** | large | large and positive | ferrites (iron oxide mixed with other metal oxides) |

```definition
**Magnetic susceptibility** (symbol **χ**, read "kai") tells how much a material is magnetised by a field, compared with free space: $\chi = \mu_r - 1$, so $\mu_r = 1 + \chi$.

**Example:** aluminium has $\chi = 2.1 \times 10^{-5}$, so $\mu_r = 1 + 0.000021 = 1.000021$. Aluminium is "almost air" magnetically.
```

```remember
- **Ferromagnetic properties are found almost only in iron, nickel and cobalt** (and their alloys). These are the materials used for machine cores.
- **Curie temperature:** if a ferromagnetic material is heated above a certain temperature (about 770 °C for iron), it loses its ferromagnetism and behaves like a **paramagnetic** material.
- **Ferrites** have very high electrical **resistivity** (they are ceramics). Little current can circulate inside them, so they are used as cores at **high frequency** (SMPS transformers, radio coils), where an iron core would heat up.
- The susceptibility of diamagnetic materials is small and negative; that of paramagnetic materials is small and positive; that of free space (air, vacuum) is zero ($\mu_r = 1$ exactly).
```

## Why machine cores are made of iron

1. **Large flux from a small current.** Iron's $\mu_r$ is in the thousands, so a modest current gives a flux density of about 1 T or more. With air, the same flux would need thousands of times more ampere-turns.
2. **The flux stays where we want it.** Flux prefers iron to air, so the iron core guides it from one winding to the other (in a transformer) or across the air gap (in a motor).
3. **Choke coils** (inductors for AC) use iron cores for the same reason: large flux densities can be produced in iron, so a large inductance is obtained in a small size.

## Summary of this topic

| Quantity | Symbol | Meaning | Unit |
|---|---|---|---|
| Flux | Φ | total magnetic field through an area | weber (Wb) |
| Flux density | B | flux per m², $B = \Phi / A$ | tesla (T) = Wb/m² |
| Magnetising force | H | ampere-turns per metre of path, $H = NI/l$ | A/m (AT/m) |
| Permeability | μ | flux density per unit H, $B = \mu H$ | H/m |
| Relative permeability | μr | $\mu / \mu_0$ | no unit |
| Permeability of free space | μ0 | $4\pi \times 10^{-7}$ | H/m |
