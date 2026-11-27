## Iron is not a straight line

In the last two topics we took $\mu_r$ as a fixed number, as if $B$ grew in step with $H$ for ever. That is true for air, but **not for iron**. If we keep increasing the current in a coil on an iron ring and measure $B$, the graph of $B$ against $H$ bends over. This graph is the **B-H curve** (also called the magnetisation curve).

## Why iron is magnetic: domains

Inside iron there are millions of tiny regions called **domains**. Each domain is already a small magnet, fully magnetised in some direction. In unmagnetised iron the domains point every which way, so their effects cancel out.

When we apply a magnetising force $H$:

1. **At first** (small H), the domains that point roughly along H grow at the expense of the others. B rises **slowly**.
2. **Then** (medium H), whole domains swing round to line up with H. B rises **steeply**. This is the useful, nearly straight part of the curve.
3. **Finally** (large H), almost all domains already point along H. There is nothing left to line up, so B rises only **very slowly** however much more H we apply. The iron is **saturated**.

## The B-H curve

```remember
Shape of the B-H curve of iron, starting from unmagnetised iron:

1. **Initial part (foot):** small slope; B rises slowly.
2. **Straight (linear) part:** steep and nearly straight; $\mu_r$ is large and roughly constant here.
3. **Knee:** the curve bends. Machines are designed to work near the knee (about 1.0 to 1.5 T for silicon steel): beyond it, extra flux costs a great deal of extra current.
4. **Saturation:** the curve becomes almost flat. Extra H gives very little extra B.

For **air** (and every non-magnetic material) the B-H graph is a **straight line** through the origin, with slope $\mu_0$. It never saturates.
```

```definition
**Saturation** is the state of a magnetic material in which nearly all its domains are already lined up, so a further increase in magnetising force gives almost no increase in flux density.

**Example:** silicon steel saturates at about 1.6 to 2 T. Pushing it from 1.5 T to 1.8 T can need several times the magnetising current.
```

### μr is not constant

Because the curve bends, the ratio $\mu_r = \dfrac{B}{\mu_0 H}$ changes along it. It is small at the foot, largest in the straight part, and falls steadily in saturation. When a question gives one value of $\mu_r$, it means "take the iron as working in its straight part".

## Hysteresis: the iron remembers

Now take the iron to saturation and then **reduce** H back to zero. B does **not** come back down along the same curve. It comes down along a higher path, and when H is zero some flux density is still left in the iron. The domains do not all fall back into their random state.

To remove this left-over flux we must apply H in the **opposite** direction. Carrying on, the iron saturates in the opposite direction; bringing H back again makes B follow yet another path. One full cycle of H traces a closed loop: the **hysteresis loop**. ("Hysteresis" means "lagging behind": B lags behind H.)

```definition
**Residual flux density** (also called **remanence** or **retentivity**, symbol $B_r$): the flux density left in the iron when H is brought back to zero.

**Coercive force** (or **coercivity**, symbol $H_c$): the reverse magnetising force needed to bring B down to zero, that is, to wipe out the residual flux.

**Example:** a soft silicon steel may have $H_c$ of only a few tens of A/m; a permanent-magnet alloy may need tens of thousands of A/m.
```

```remember
On the hysteresis loop:

- Point where the loop cuts the **B-axis** (H = 0): residual flux density $B_r$ (retentivity).
- Point where the loop cuts the **H-axis** (B = 0): coercive force $H_c$ (coercivity).
- The **area** inside the loop is the energy lost as heat in each cubic metre of iron in **every cycle** of magnetisation. More on this in the topic *Core losses*.
```

## Soft and hard magnetic materials

The shape of the hysteresis loop decides what a material is good for.

| | **Soft** magnetic material | **Hard** magnetic material |
|---|---|---|
| Hysteresis loop | **narrow** (thin, small area) | **broad** (fat, large area) |
| Coercivity $H_c$ | low (easy to demagnetise) | high (hard to demagnetise) |
| Permeability | high | low |
| Saturation flux density | high | (not the main concern) |
| Hysteresis loss | small | large |
| Used for | cores of transformers, motors, generators, relays, chokes | **permanent magnets** (meters, loudspeakers, PM motors) |
| Examples | silicon steel, CRGO steel, soft iron, permalloy, soft ferrites | alnico, ferrite magnets (barium/strontium), neodymium-iron-boron, samarium-cobalt |

```remember
**What a transformer core material needs:** high permeability, high saturation flux density, low hysteresis loss (narrow loop) and low eddy current loss (high resistivity, thin laminations).

**What a permanent magnet material needs:** high coercive force and high retentivity (a large, broad loop), and a **high Curie temperature** so that it keeps its magnetism when warm. Its permeability is low.

Both metallic and ceramic (ferrite) materials come in soft and hard grades. The grading into soft or hard is by the hysteresis loop: narrow = soft, broad = hard.
```

### Silicon steel and CRGO

Adding about 3 to 4 % silicon to iron does two good things: it **narrows the hysteresis loop** (less hysteresis loss) and **raises the resistivity** (less eddy current loss, see *Core losses*). Too much silicon makes the steel brittle.

**CRGO** (cold-rolled grain-oriented) steel is silicon steel rolled so that its crystal grains line up along the rolling direction. Along that direction it has a very high permeability and low loss. It is used in power transformer cores, with the flux running along the rolling direction.

### Ferrites

**Ferrites** are ceramic compounds of iron oxide with other metal oxides (for example of manganese, zinc or nickel). They are **ferrimagnetic**: strongly magnetic, though their saturation flux density is lower than that of iron. Their great advantage is a very **high resistivity**, so almost no eddy current flows in them. Soft ferrites are therefore used for cores at **high frequency** (SMPS transformers, radio-frequency coils); hard ferrites make cheap permanent magnets.

## Curie temperature

```definition
**Curie temperature:** the temperature above which a ferromagnetic material loses its ferromagnetism; the domains break up and it behaves as a **paramagnetic** material ($\mu_r$ just above 1).

**Example:** about 770 °C for iron, about 1115 °C for cobalt, about 358 °C for nickel. A permanent magnet heated near its Curie temperature loses its magnetism.
```

## Magnetostriction: why transformers hum

```definition
**Magnetostriction** is the small change in the dimensions of a ferromagnetic material when it is magnetised. A piece of iron stretches or shrinks by a few parts in a million as its domains line up.
```

In a transformer on 50 Hz, the flux goes through its cycle 50 times a second. The core stretches twice in every cycle (once for the positive and once for the negative half; the length change does not depend on the direction of B). So the core vibrates at **100 Hz** and its harmonics. This is the familiar **humming** sound of a transformer.

```remember
Ways to reduce transformer hum (noise from magnetostriction):

- work the core at a **lower flux density** (less stretching);
- **tighten the core** firmly with clamps and bolts, so the laminations cannot rattle;
- use better grades of steel (lower magnetostriction) and mount the tank on anti-vibration pads.

Raising the flux density makes the hum **worse**.
```

## Summary of this topic

- The B-H curve of iron: slow start, steep straight part, knee, saturation. Air: a straight line ($\mu_r = 1$).
- $\mu_r$ of iron is not constant; it falls in saturation.
- Hysteresis: B lags H. $B_r$ = residual flux density (at H = 0); $H_c$ = coercive force (at B = 0). Loop area = energy lost per m³ per cycle.
- Soft materials (narrow loop, low $H_c$, high μ): cores. Hard materials (broad loop, high $H_c$, high $B_r$, high Curie temperature): permanent magnets.
- Silicon steel / CRGO for power transformers; ferrites (high resistivity) for high frequency.
- Above the Curie temperature a ferromagnet becomes paramagnetic. Magnetostriction makes transformers hum at twice the supply frequency.
