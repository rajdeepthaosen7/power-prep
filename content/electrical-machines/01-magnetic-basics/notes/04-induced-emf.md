## Magnetism makes electricity

A current makes a magnetic field. In 1831 Michael Faraday showed the reverse: a magnetic field can make a current, but **only while it is changing**. Push a bar magnet into a coil connected to a galvanometer: the needle kicks. Hold the magnet still: the needle returns to zero. Pull the magnet out: the needle kicks the other way.

This is **electromagnetic induction**. Every transformer and every generator works on it.

## Flux linkage

A coil of $N$ turns with flux $\Phi$ passing through all of it "links" that flux $N$ times.

```definition
**Flux linkage** (symbol **λ**, read "lambda", some books write $\psi$) is the flux multiplied by the number of turns it passes through: $\lambda = N\Phi$.

**Unit:** weber-turn (Wb-turn).

**Example:** 2 mWb through a coil of 500 turns gives $\lambda = 500 \times 0.002 = 1$ Wb-turn.
```

## Faraday's laws and Lenz's law

```remember
**Faraday's first law:** whenever the flux linking a coil changes, an EMF is induced in it. (If the coil is part of a closed circuit, a current flows.)

**Faraday's second law:** the size of the induced EMF equals the rate of change of flux linkage:

$$e = N\dfrac{d\Phi}{dt} = \dfrac{d\lambda}{dt}$$

**Lenz's law:** the induced EMF always acts in a direction that **opposes the change** that produces it. (If the flux is growing, the induced current tries to make flux in the opposite direction; if the flux is falling, it tries to keep it up.)

Faraday's and Lenz's laws together are written with a minus sign:

$$\fx{e = -N\dfrac{d\Phi}{dt} = -\dfrac{d\lambda}{dt}}$$

The minus sign is Lenz's law. In numericals we usually work out the size and fix the direction by Lenz's law.
```

Lenz's law is really the law of **conservation of energy**. If the induced current helped the change instead of opposing it, the flux would grow by itself and give energy for nothing.

```example Worked example 1: average induced EMF
The flux linking a coil of $\given{400}$ turns falls steadily from $\given{3\ \text{mWb}}$ to $\given{1\ \text{mWb}}$ in $\given{0.02\ \text{s}}$. Find the EMF induced.
:::given $N = 400$, $\Phi_1 = 3\ \text{mWb}$, $\Phi_2 = 1\ \text{mWb}$, $t = 0.02\ \text{s}$
:::find the induced EMF $e$
:::step Find the change in flux.
$\Delta\Phi = 3 - 1 = 2\ \text{mWb} = 2 \times 10^{-3}\ \text{Wb}$
:::step Find the rate of change of flux. $\fx{\Delta\Phi \div \Delta t}$
$\Delta\Phi \div \Delta t = (2 \times 10^{-3}) \div 0.02$
$= 0.002 \div 0.02$
$= 0.1\ \text{Wb/s}$
:::step Multiply by the turns. $\fx{e = N\,\Delta\Phi/\Delta t}$
$e = 400 \times 0.1$
$= 40\ \text{V}$
:::answer $\ans{e = 40\ \text{V}}$
**Direction (Lenz):** the flux is falling, so the induced current flows the way that would keep the flux up.
```

## Two ways to change the flux linkage

```remember
**1. Statically induced EMF:** the coil and the core do not move; the **flux itself changes** because the current making it changes (AC). This is how a **transformer** works.

- **Self-induced EMF:** the changing current of a coil induces EMF in the **same** coil.
- **Mutually induced EMF:** the changing current of one coil induces EMF in a **second** coil that shares its flux.

**2. Dynamically induced EMF:** the flux is steady but a **conductor moves** through it and "cuts" the lines of force. This is how a **generator** works.
```

### Dynamically induced EMF: e = Blv

A straight conductor of length $l$ moves with speed $v$ through a uniform field $B$. If $B$, $l$ and $v$ are all at right angles to one another, the conductor sweeps an area $l \times v$ every second, so the flux it cuts per second is $B \times l \times v$. That is the EMF.

```formula
$$\fx{e = B\,l\,v} \qquad \text{(B, l and v mutually perpendicular)}$$

If the conductor moves at an angle $\theta$ to the field lines: $e = B\,l\,v\sin\theta$. Moving **along** the lines ($\theta = 0$) cuts no flux and gives no EMF.

- $B$ in tesla, $l$ in metre, $v$ in metre per second, $e$ in volt.
```

**Direction: Fleming's right-hand rule** (for generators). Stretch the thumb, first finger and second finger of the right hand at right angles to one another. **F**irst finger = **F**ield (N to S), thu**M**b = **M**otion of the conductor, se**C**ond finger = induced **C**urrent (EMF).

```example Worked example 2: conductor moving in a field
A conductor of length $\given{0.5\ \text{m}}$ moves at $\given{20\ \text{m/s}}$ at right angles to a uniform field of $\given{0.8\ \text{T}}$. Find the EMF.
:::given $l = 0.5\ \text{m}$, $v = 20\ \text{m/s}$, $B = 0.8\ \text{T}$
:::find EMF $e$
:::step All three are at right angles, so use $\fx{e = Blv}$.
$e = 0.8 \times 0.5 \times 20$
$0.8 \times 0.5 = 0.4$
$0.4 \times 20 = 8$
:::answer $\ans{e = 8\ \text{V}}$
```

```example Worked example 3: a conductor going round in a circle
A conductor $\given{0.5\ \text{m}}$ long is held parallel to an axis at a distance $\given{0.2\ \text{m}}$ from it and is turned round the axis at $\given{300\ \text{rpm}}$. The field is radial (pointing straight out from the axis) with $\given{B = 0.5\ \text{T}}$ where the conductor is. Find the EMF.
:::given $l = 0.5\ \text{m}$, $r = 0.2\ \text{m}$, $N = 300\ \text{rpm}$, $B = 0.5\ \text{T}$ (radial)
:::find EMF $e$
:::step The conductor moves along a circle, so its velocity is along the circle: at right angles to the radial field and to its own length. So $e = Blv$ applies directly. First change rpm to revolutions per second.
$300\ \text{rpm} = 300 \div 60 = 5\ \text{rev/s}$
:::step Angular speed. $\fx{\omega = 2\pi \times \text{rev/s}}$
$\omega = 2\pi \times 5 = 10\pi\ \text{rad/s}$
:::step Linear speed of the conductor. $\fx{v = \omega r}$
$v = 10\pi \times 0.2 = 2\pi\ \text{m/s}$
$2\pi = 6.283\ \text{m/s}$
:::step EMF. $\fx{e = Blv}$
$e = 0.5 \times 0.5 \times 6.283$
$0.5 \times 0.5 = 0.25$
$0.25 \times 6.283 = 1.571$
:::answer $\ans{e \approx 1.57\ \text{V}}$
**Turned the other way round:** to get a given EMF, find $v = e/(Bl)$, then $\omega = v/r$, then rpm $= 60\,\omega/(2\pi)$.
```

### A coil turning in a uniform field gives a sine wave

Turn a flat coil of $N$ turns and area $A$ at a steady angular speed $\omega$ in a uniform field $B$. The flux through it is $\Phi = BA\cos\omega t$ (biggest when the coil faces the field, zero when it is edge-on). By Faraday's law:

$e = -N\dfrac{d}{dt}(BA\cos\omega t) = N B A \omega \sin\omega t$

```formula
$$\fx{e = E_m \sin\omega t, \qquad E_m = N\,B\,A\,\omega = N\,\Phi_m\,\omega}$$

where $\Phi_m = BA$ is the largest flux through the coil and $\omega = 2\pi f$. **This is how an AC generator makes a sinusoidal voltage.** The same idea, turned round, gives the EMF equation of a transformer (topic *EMF equation*).
```

```example Worked example 4: peak EMF of a rotating loop
A single-turn circular loop of radius $\given{0.5\ \text{m}}$ turns at $\given{120\ \text{rpm}}$ about a diameter, in a uniform field $\given{B = 0.2\ \text{T}}$ at right angles to the axis of turning. Find the peak EMF.
:::given $N = 1$, $r = 0.5\ \text{m}$, speed $= 120\ \text{rpm}$, $B = 0.2\ \text{T}$
:::find peak EMF $E_m$
:::step Area of the loop. $\fx{A = \pi r^2}$
$A = \pi \times 0.5^2 = \pi \times 0.25 = 0.785\ \text{m}^2$
:::step Angular speed.
$120\ \text{rpm} = 120 \div 60 = 2\ \text{rev/s}$
$\omega = 2\pi \times 2 = 4\pi = 12.57\ \text{rad/s}$
:::step Peak EMF. $\fx{E_m = NBA\omega}$
$E_m = 1 \times 0.2 \times 0.785 \times 12.57$
$0.2 \times 0.785 = 0.157$
$0.157 \times 12.57 = 1.97$
:::answer $\ans{E_m \approx 1.97\ \text{V}}$
**Without a calculator:** $E_m = 0.2 \times \pi \times 0.25 \times 4\pi = 0.2\pi^2 \approx 0.2 \times 9.87 \approx 1.97$ V.
**Watch out:** if the field is given as H (A/m) in air, first find $B = \mu_0 H$.
```

## Self-inductance

When the current in a coil changes, its own flux changes, so an EMF is induced **in the same coil**, opposing the change of current (Lenz). A coil "resists" any change in its current, just as a heavy body resists any change in its speed. This property is **self-inductance**.

```definition
**Self-inductance** (symbol **L**) of a coil is the flux linkage per ampere of its own current: $L = \dfrac{N\Phi}{I}$.

**Unit:** henry (H). 1 H = 1 Wb-turn per ampere.

The induced EMF is $e = L\dfrac{di}{dt}$ (in size). **Example:** a 2 H coil whose current rises at 5 A/s has $e = 2 \times 5 = 10$ V across it.
```

### Inductance from the magnetic circuit

Put the magnetic circuit into the definition, step by step:

$\Phi = \dfrac{NI}{S}$ (magnetic Ohm's law)

$N\Phi = \dfrac{N \times NI}{S} = \dfrac{N^2 I}{S}$

$L = \dfrac{N\Phi}{I} = \dfrac{N^2}{S}$

```formula
$$\fx{L = \dfrac{N^2}{S} = \dfrac{\mu_0\,\mu_r\,N^2\,A}{l}}$$

- Inductance grows as the **square** of the turns: double the turns, four times the inductance.
- An iron core multiplies L by $\mu_r$. But as iron saturates, $\mu_r$ falls, so the inductance of an iron-cored coil **falls at large currents**. With a non-magnetic core ($\mu_r = 1$), L does not depend on the current at all.
- A **long solenoid** (length much bigger than its diameter) in air: $L = \dfrac{\mu_0 N^2 A}{l}$, with $A = \pi d^2/4$.
```

```example Worked example 5: inductance of an air-cored coil
A coil of $\given{500}$ turns is wound uniformly on a ring of non-magnetic material. The ring has a mean circumference of $\given{400\ \text{mm}}$ and a cross-section of $\given{400\ \text{mm}^2}$. Find the inductance.
:::given $N = 500$, $l = 400\ \text{mm}$, $A = 400\ \text{mm}^2$, $\mu_r = 1$
:::find inductance $L$
:::step Change to SI units.
$l = 400\ \text{mm} = 0.4\ \text{m}$
$A = 400\ \text{mm}^2 = 400 \times 10^{-6} = 4 \times 10^{-4}\ \text{m}^2$
:::step Square the turns.
$N^2 = 500 \times 500 = 250\,000 = 2.5 \times 10^{5}$
:::step Top of the formula: $\mu_0 N^2 A$.
$\mu_0 N^2 A = 4\pi \times 10^{-7} \times 2.5 \times 10^{5} \times 4 \times 10^{-4}$
$4 \times 2.5 \times 4 = 40$
$10^{-7} \times 10^{5} \times 10^{-4} = 10^{-6}$
$\mu_0 N^2 A = 40\pi \times 10^{-6}$
:::step Divide by the length. $\fx{L = \mu_0 N^2 A \div l}$
$L = 40\pi \times 10^{-6} \div 0.4$
$= 100\pi \times 10^{-6}$
$= 314 \times 10^{-6}\ \text{H}$
:::answer $\ans{L \approx 314\ \mu\text{H}}$
**Note:** with a non-magnetic core the inductance is the same whatever the current. A question that gives "the magnetising current" for such a coil is giving you an unneeded number.
```

## Energy stored in an inductor

To build up the current in a coil from 0 to $I$, the source must push against the self-induced EMF. The work it does is stored in the magnetic field and comes back when the current falls.

```formula
$$\fx{W = \tfrac{1}{2} L I^2} \qquad \text{(joule)}$$

**Example:** a 0.5 H coil carrying 4 A stores $W = 0.5 \times 0.5 \times 4^2 = 0.25 \times 16 = 4$ J.
```

## Mutual inductance

Two coils on the same core share flux. A changing current $i_1$ in coil 1 changes the flux through coil 2 and induces an EMF in it.

```definition
**Mutual inductance** (symbol **M**) is the flux linkage in coil 2 per ampere of current in coil 1: $M = \dfrac{N_2 \Phi_{12}}{I_1}$, where $\Phi_{12}$ is the part of coil 1's flux that passes through coil 2.

**Unit:** henry (H). The EMF in coil 2 is $e_2 = M\dfrac{di_1}{dt}$.

**Coefficient of coupling:** $k = \dfrac{M}{\sqrt{L_1 L_2}}$, between 0 and 1. $k = 1$ when all the flux of each coil passes through the other (no leakage); $k = 0$ when they share no flux.
```

If both coils are on one closed core of reluctance $S$ with no leakage, coil 1 makes $\Phi = N_1 I_1 / S$ and all of it passes through coil 2:

$M = \dfrac{N_2 \Phi}{I_1} = \dfrac{N_2 \times N_1 I_1 / S}{I_1} = \dfrac{N_1 N_2}{S}$

```formula
$$\fx{M = \dfrac{N_1 N_2}{S}} \qquad \text{(both coils on one core, no leakage)}$$

**Where on the core the coils sit does not matter**, as long as the flux goes round the same single closed path: the reluctance $S$ of that path is the same, so $M$ is the same.
```

```example Worked example 6: L and M of two coils on a ring
Two coils of $\given{200}$ and $\given{400}$ turns are wound on an iron ring of reluctance $\given{4 \times 10^{5}\ \text{AT/Wb}}$. Assume no leakage. Find $L_1$, $L_2$, $M$ and $k$.
:::given $N_1 = 200$, $N_2 = 400$, $S = 4 \times 10^{5}\ \text{AT/Wb}$
:::find $L_1$, $L_2$, $M$, $k$
:::step $\fx{L_1 = N_1^2 / S}$
$N_1^2 = 200^2 = 40\,000 = 4 \times 10^{4}$
$L_1 = (4 \times 10^{4}) \div (4 \times 10^{5}) = 0.1\ \text{H}$
:::step $\fx{L_2 = N_2^2 / S}$
$N_2^2 = 400^2 = 160\,000 = 16 \times 10^{4}$
$L_2 = (16 \times 10^{4}) \div (4 \times 10^{5}) = 0.4\ \text{H}$
:::step $\fx{M = N_1 N_2 / S}$
$N_1 N_2 = 200 \times 400 = 80\,000 = 8 \times 10^{4}$
$M = (8 \times 10^{4}) \div (4 \times 10^{5}) = 0.2\ \text{H}$
:::step $\fx{k = M / \sqrt{L_1 L_2}}$
$L_1 L_2 = 0.1 \times 0.4 = 0.04$
$\sqrt{0.04} = 0.2$
$k = 0.2 \div 0.2 = 1$
:::answer $\ans{L_1 = 0.1\ \text{H},\ L_2 = 0.4\ \text{H},\ M = 0.2\ \text{H},\ k = 1}$
**Sense check:** with no leakage $k$ must be 1, so $M = \sqrt{L_1 L_2}$. ✓
```

## Summary of this topic

- $e = -N\,d\Phi/dt = -d\lambda/dt$ (Faraday + Lenz). The minus sign = Lenz = opposition = conservation of energy.
- Statically induced EMF (transformer): self and mutual. Dynamically induced EMF (generator): $e = Blv\sin\theta$, direction by Fleming's right-hand rule.
- A coil turning in a uniform field: $e = NBA\omega\sin\omega t$, peak $N\Phi_m\omega$.
- $L = N\Phi/I = N^2/S = \mu_0\mu_r N^2 A/l$; $e = L\,di/dt$; energy $\tfrac12 LI^2$.
- $M = N_1N_2/S$ on a common core (position of the coils on one closed core does not matter); $k = M/\sqrt{L_1L_2} \le 1$.
