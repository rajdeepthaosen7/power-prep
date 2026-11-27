## Why we need transformers

Power is generated at about 11 to 25 kV, sent over long lines at 132, 220 or 400 kV, and used in homes at 230 V. Raising the voltage for transmission cuts the line current (same power, $P = VI$), and the line loss $I^2R$ falls as the **square** of the current. A transformer is the machine that changes the voltage, and it does it with an efficiency of 98 to 99 %.

```definition
A **transformer** is a static device (no moving parts) that transfers electrical power from one circuit to another by **mutual induction**, changing the voltage and current but keeping the **frequency the same**.

The winding connected to the supply is the **primary**; the winding connected to the load is the **secondary**.

- **Step-up** transformer: secondary voltage higher than primary ($N_2 > N_1$).
- **Step-down** transformer: secondary voltage lower than primary ($N_2 < N_1$).
```

## How it works

1. Two coils are wound on one iron core. They are **not** connected electrically.
2. An alternating voltage on the primary drives an alternating current, which sets up an **alternating flux** in the core.
3. The core carries almost all of this flux through the secondary coil as well.
4. The changing flux induces an EMF in the secondary (mutual induction) and also in the primary itself (self-induction).
5. Both windings link the **same** flux, so each turn has the **same** EMF. The winding with more turns has the bigger total voltage: $\dfrac{E_1}{E_2} = \dfrac{N_1}{N_2}$.

The only link between the two circuits is the magnetic flux. That is why a transformer also **isolates** the two circuits electrically.

```watchout
**A transformer does not work on DC.** A steady current makes a steady flux, and a steady flux induces no EMF (Faraday). Worse, the primary then has nothing to limit its current except its small resistance: a large current flows and the winding **burns out**.

**A transformer does not change the frequency.** The flux alternates at the supply frequency, so the induced EMF has the same frequency.
```

## Parts of a transformer

```remember
1. **Core:** thin sheets (laminations) of silicon steel, about 0.35 to 0.5 mm thick, each coated with varnish or oxide insulation. Large transformers use CRGO steel. The core gives the flux a low-reluctance path.
2. **Windings:** copper (or aluminium) coils, insulated with paper or enamel. The **low-voltage winding is placed next to the core** and the high-voltage winding outside it, because the LV winding needs less insulation from the earthed core.
3. **Tank and transformer oil:** the core and windings sit in a tank of mineral oil. The **oil does two jobs: insulation and cooling.**
4. **Conservator:** a small drum above the tank that lets the oil expand and contract with temperature.
5. **Breather:** lets air in and out of the conservator through silica gel, which dries the air (moisture spoils the oil's insulation).
6. **Bushings:** porcelain insulators that bring the winding leads out through the tank.
7. **Buchholz relay** (in the pipe to the conservator): senses gas from internal faults (studied under Protection).
```

## Why the core is laminated

The alternating flux induces EMFs not only in the windings but also **inside the iron itself**. In a solid core these EMFs drive large circulating currents called **eddy currents**, which heat the core and waste power. Cutting the core into thin sheets insulated from each other breaks up the paths of these currents. Their size and loss fall greatly. (Full detail: topic *Core losses*.)

## Core-type and shell-type transformers

| | **Core type** | **Shell type** |
|---|---|---|
| Picture | the **windings surround the core** | the **core surrounds the windings** |
| Limbs | two limbs (single phase); windings split between both | three limbs; both windings on the **central** limb |
| Flux path | one path | two parallel paths; the central limb is twice as wide as each outer limb |
| Leakage | more leakage flux | less leakage (windings are enclosed) |
| Cooling and repair | windings easier to reach, cool and repair | windings harder to reach |
| Used for | high voltages | low voltages, large currents |

### Joints in the core

The core is built of L-shaped or E-and-I shaped stampings. Where two stampings meet there is a small joint. The joints of **alternate layers are staggered** (placed one over another in overlapping positions) instead of being lined up. This:

- avoids a **continuous air gap** across the core (so the magnetising current stays small), and
- keeps the core **mechanically strong**.

### Stepped core

The windings are circular (round coils are strongest against short-circuit forces). A square core inside a round coil wastes the space at the corners. So the core cross-section is built up in **steps** of different widths, to come close to a circle. For the same iron area, the coil can then be smaller in diameter: **the mean length of each turn is shorter, so less copper is needed and the copper loss is smaller.**

## Air-cored transformers

A transformer can also be wound with **no iron**: the coils share flux through air. Coupling is loose and the magnetising current is large, so this is used only at **high (radio) frequencies**, where iron losses would be too high and a small inductance is enough. Its B-H relation is a straight line, so it never saturates.

## Summary of this topic

- Static device, mutual induction, same frequency, changes V and I; isolates the two circuits. Works only on AC; DC would burn the primary.
- Laminated silicon-steel core (eddy currents), LV winding next to the core, oil for insulation and cooling, conservator, breather, bushings.
- Core type: windings surround the core; shell type: core surrounds the windings (less leakage).
- Staggered joints: no continuous air gap, more strength. Stepped core: shorter mean turn, less copper and copper loss.
- Air-cored transformers: high-frequency use only.
