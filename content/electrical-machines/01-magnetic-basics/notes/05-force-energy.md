## Magnetism makes motion

The last topic showed that motion in a magnetic field makes a voltage (generator action). The reverse is also true: a current in a magnetic field feels a **force** (motor action). With these two facts, a machine can turn electrical energy into mechanical energy and back again. This is called **electromechanical energy conversion**.

## Force on a current-carrying conductor

Place a straight conductor of length $l$ carrying a current $I$ in a uniform field $B$.

```formula
$$\fx{F = B\,I\,l\,\sin\theta}$$

- $\theta$ = angle between the conductor (direction of current) and the field.
- Largest force, $F = BIl$, when the conductor is **at right angles** to the field ($\theta = 90°$).
- **No force** when the conductor lies **along** the field ($\theta = 0$).
- $B$ in tesla, $I$ in ampere, $l$ in metre, $F$ in newton.
```

**Direction: Fleming's left-hand rule** (for motors). Stretch the thumb, first finger and second finger of the **left** hand at right angles. **F**irst finger = **F**ield, se**C**ond finger = **C**urrent, thu**M**b = **M**otion (force).

```remember
Right hand = generator (motion given, find the current). Left hand = motor (current given, find the motion). A way to remember: we usually "write" with the right hand, and a generator "writes" (makes) electricity.
```

```example Worked example 1: force on a conductor
A conductor $\given{0.4\ \text{m}}$ long carries $\given{50\ \text{A}}$ at right angles to a field of $\given{0.6\ \text{T}}$. Find the force. What is the force if the conductor is turned to make $\given{30°}$ with the field?
:::given $l = 0.4\ \text{m}$, $I = 50\ \text{A}$, $B = 0.6\ \text{T}$
:::find $F$ at 90° and at 30°
:::step At right angles, $\sin 90° = 1$. $\fx{F = BIl}$
$F = 0.6 \times 50 \times 0.4$
$0.6 \times 50 = 30$
$30 \times 0.4 = 12\ \text{N}$
:::step At 30°, multiply by $\sin 30° = 0.5$.
$F = 12 \times 0.5 = 6\ \text{N}$
:::answer $\ans{12\ \text{N}}$ at right angles, $\ans{6\ \text{N}}$ at 30°.
```

### Torque on a coil: the principle of a motor

A rectangular coil of $N$ turns, length $l$ and width $b$ (area $A = l\,b$) carries current $I$ in a field $B$, with its two long sides at right angles to the field. Each long side has a force $NBIl$, one up and one down. The two forces are a distance $b$ apart, so they make a **torque** (turning effect):

$T = (NBIl) \times b = N\,B\,I\,(l\,b) = N\,B\,I\,A$ (when the plane of the coil is along the field).

This is how a DC motor and a moving-coil (PMMC) meter turn.

## Electromechanical energy conversion

In every rotating machine there are two magnetic fields: one made by the **stator** (the still part) and one made by the **rotor** (the turning part). Like two bar magnets, the two fields try to line up with each other. The torque they produce depends on three things.

```remember
The torque developed in an electromechanical energy conversion device depends on:

1. the **stator field strength**,
2. the **rotor field strength**, and
3. the **angle between the two fields** (the torque angle, δ): $T \propto F_s\,F_r\,\sin\delta$.

No angle (fields lined up), no torque; largest torque when they are at 90°. Also, **the two fields must be stationary with respect to each other** to give a steady (non-zero average) torque.
```

**Motor and generator action happen together.** In a motor, the turning conductors also cut flux, so an EMF is induced in them that opposes the supply (the **back EMF**). In a generator, the current in the conductors feels a force that opposes the turning (the driving engine must supply this torque). The magnetic field is the go-between that passes energy from one side to the other.

## Energy stored in a magnetic field

We saw $W = \tfrac12 L I^2$ for an inductor. Put $L = N^2/S$ and $\Phi = NI/S$:

$W = \tfrac12 \times \dfrac{N^2}{S} \times I^2 = \tfrac12 \times (NI) \times \dfrac{NI}{S} = \tfrac12 F \Phi$

Per cubic metre of the field (for a uniform field, divide by volume $A\,l$, using $F = Hl$ and $\Phi = BA$):

```formula
$$\fx{w = \tfrac12 B H = \dfrac{B^2}{2\mu}} \qquad \text{(energy density, J/m}^3\text{)}$$

In air, $\mu = \mu_0$ is small, so $B^2/(2\mu_0)$ is **large**: for the same B, an air gap stores about $\mu_r$ times more energy per m³ than iron. **In a machine with an air gap, most of the magnetic energy sits in the gap.**
```

## The pull of an electromagnet

An electromagnet pulls a piece of iron across an air gap. Think of the gap as a "spring" of stored energy: if the gap closes by a small distance $dx$, the volume of the gap falls by $A\,dx$ and the energy $\dfrac{B^2}{2\mu_0} \times A\,dx$ is released as work $F \times dx$. So:

```formula
$$\fx{F = \dfrac{B^2 A}{2\mu_0}} \quad \text{(newton)} \qquad \text{force per m}^2\text{ (pressure)} = \dfrac{B^2}{2\mu_0}\ \ \text{N/m}^2$$

- $A$ = area of the pole face (each face, if there are two gaps, add the forces).
- With **AC**, $B = B_m\sin\omega t$ and the average of $\sin^2$ over a cycle is ½. So the **average** force is $\dfrac{B_m^2 A}{4\mu_0}$ (half the force at the peak).
```

```example Worked example 2: force density on an iron face
The flux density at the face of an iron pole is $\given{1\ \text{T}}$. Find the force per square metre on the face. Take $\mu_0 = 4\pi \times 10^{-7}$ H/m.
:::given $B = 1\ \text{T}$
:::find force density $\find{F/A}$
:::step Square B.
$B^2 = 1 \times 1 = 1$
:::step Bottom of the formula. $\fx{2\mu_0}$
$2\mu_0 = 2 \times 12.566 \times 10^{-7} = 25.13 \times 10^{-7}$
:::step Divide. $\fx{F/A = B^2 \div (2\mu_0)}$
$F/A = 1 \div (25.13 \times 10^{-7})$
$= (1 \div 25.13) \times 10^{7}$
$1 \div 25 = 0.04$, so $1 \div 25.13 \approx 0.0398$
$= 0.0398 \times 10^{7} = 3.98 \times 10^{5}\ \text{N/m}^2$
:::answer $\ans{F/A \approx 3.98 \times 10^{5}\ \text{N/m}^2}$ (about 40 newton per square centimetre).
**Quick scaling:** force density goes as $B^2$. At 1.6 T it is $1.6^2 = 2.56$ times bigger: $2.56 \times 3.98 \times 10^5 \approx 1.02 \times 10^6$ N/m².
```

## Energy and co-energy

For a coil, draw its **flux linkage λ** against its **current i**. For an air-cored coil this is a straight line ($\lambda = Li$); for an iron-cored coil it bends over as the iron saturates.

```definition
**Field energy** $W = \displaystyle\int_0^{\lambda} i\,d\lambda$: the area between the λ-i curve and the **λ-axis**.

**Co-energy** $W' = \displaystyle\int_0^{i} \lambda\,di$: the area between the λ-i curve and the **i-axis**.

Together they fill the rectangle: $W + W' = \lambda\,i$.
```

```remember
- **Linear** magnetic circuit (constant permeability, e.g. an air core, or iron used well below saturation): the λ-i graph is a straight line, the two areas are equal triangles, so **energy = co-energy** $= \tfrac12 L i^2$.
- **Saturating** iron: the curve bends towards the i-axis, so the area under it is bigger: **co-energy > energy**.
```

### Force from co-energy

When the moving part (say, an armature that closes an air gap $x$) moves, the force on it can be found from the co-energy:

```formula
$$\fx{F = \dfrac{\partial W'(i, x)}{\partial x}} \quad \text{(current kept constant)}$$

1. Write λ as a function of $i$ and $x$.
2. Integrate λ with respect to $i$ (treat $x$ as a constant) to get $W'(i, x)$.
3. Differentiate $W'$ with respect to $x$ (treat $i$ as a constant).
4. Put in the numbers. A negative sign means the force acts to **reduce** $x$ (to close the gap).

For a linear system $W' = \tfrac12 L(x)\,i^2$, so $F = \tfrac12\,i^2\,\dfrac{dL}{dx}$.
```

```example Worked example 3: force from co-energy
For an electromagnet, $\lambda = \dfrac{0.02\,i}{x}$ Wb-turn, where $x$ is the air gap in metre. Find the force on the armature when $\given{i = 5\ \text{A}}$ and $\given{x = 4\ \text{mm}}$.
:::given $\lambda = 0.02\,i/x$, $i = 5\ \text{A}$, $x = 4\ \text{mm}$
:::find the force $F$
:::step Integrate λ with respect to i to get the co-energy ($x$ is a constant here).
$W' = \int_0^{i} \dfrac{0.02\,i}{x}\,di$
$= \dfrac{0.02}{x} \times \dfrac{i^2}{2}$
$= \dfrac{0.01\,i^2}{x}$
:::step Differentiate with respect to $x$ ($i$ is a constant here). $\fx{F = \partial W'/\partial x}$
$F = 0.01\,i^2 \times \dfrac{d}{dx}\left(\dfrac{1}{x}\right)$
$= 0.01\,i^2 \times \left(-\dfrac{1}{x^2}\right)$
$= -\dfrac{0.01\,i^2}{x^2}$
:::step Put in the numbers.
$i^2 = 25$
$x = 0.004\ \text{m}$, so $x^2 = 0.000016 = 1.6 \times 10^{-5}$
$0.01 \times 25 = 0.25$
$F = -0.25 \div (1.6 \times 10^{-5})$
$= -(0.25 \div 1.6) \times 10^{5}$
$0.25 \div 1.6 = 0.15625$
$= -0.15625 \times 10^{5} = -15\,625\ \text{N}$
:::answer $\ans{|F| \approx 15\,600\ \text{N}}$; the minus sign says the force acts to close the gap.
```

## Summary of this topic

- $F = BIl\sin\theta$ (left-hand rule); torque on a coil $T = NBIA$.
- Torque of a machine $\propto F_s F_r \sin\delta$: stator field, rotor field and the angle between them.
- Motor and generator actions always occur together (back EMF; opposing torque).
- Energy density $B^2/(2\mu)$: most of the stored energy is in the air gap. Pull across a gap $F = B^2A/(2\mu_0)$; with AC the average is half the peak.
- Energy $= \int i\,d\lambda$, co-energy $= \int \lambda\,di$; equal for a linear system, co-energy larger when the iron saturates. $F = \partial W'/\partial x$ at constant current.
