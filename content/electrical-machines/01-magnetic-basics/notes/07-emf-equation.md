## What we want to find

A sinusoidal voltage at frequency $f$ is applied to the primary. The core flux then goes up and down as a sine wave between $+\Phi_m$ and $-\Phi_m$. How big is the EMF this flux induces in a winding of $N$ turns? The answer, $E = 4.44\,f\,N\,\Phi_m$, is the most used formula in the whole of transformers.

## Deriving the EMF equation

Let the flux be $\phi = \Phi_m \sin\omega t$, with $\omega = 2\pi f$.

```definition
- $\phi$ = the flux at any instant, in Wb; $\Phi_m$ = its **maximum** (peak) value.
- $f$ = supply frequency in Hz; $\omega = 2\pi f$ = angular frequency in rad/s.
- $N$ = number of turns of the winding.
```

By Faraday's law, the size of the induced EMF is $e = N\dfrac{d\phi}{dt}$. Take it one step at a time:

1. $\dfrac{d\phi}{dt} = \dfrac{d}{dt}(\Phi_m \sin\omega t) = \Phi_m\,\omega\cos\omega t$
2. $e = N\,\Phi_m\,\omega\cos\omega t$
3. The largest value of $\cos\omega t$ is 1, so the **peak EMF** is $E_m = N\,\Phi_m\,\omega = 2\pi f\,N\,\Phi_m$.
4. For a sine wave, rms = peak ÷ $\sqrt2$, so $E = \dfrac{2\pi f N \Phi_m}{\sqrt2}$.
5. Simplify the number: $\dfrac{2\pi}{\sqrt2} = \sqrt2\,\pi = 1.4142 \times 3.1416 = 4.443$.

```formula
$$\fx{E = 4.44\,f\,N\,\Phi_m = 4.44\,f\,N\,B_m\,A}$$

- $E$ = **rms** EMF (volt); $\Phi_m$ = **peak** flux (Wb); $B_m$ = peak flux density (T); $A$ = net core area (m²).
- For the two windings: $E_1 = 4.44 f N_1 \Phi_m$ and $E_2 = 4.44 f N_2 \Phi_m$.
- **EMF per turn** $= E/N = 4.44\,f\,\Phi_m$ is the **same in both windings**.
- Since $\cos$ is a sine shifted by 90°, the induced EMF is **90° out of phase with the flux** (the flux lags the applied voltage by 90°).
```

```remember
**Where the 4.44 comes from, another way:** in each half cycle the flux changes from $-\Phi_m$ to $+\Phi_m$, a change of $2\Phi_m$, in a time of $\dfrac{1}{2f}$. So the average EMF $= N \times 2\Phi_m \div \dfrac{1}{2f} = 4 f N \Phi_m$. For a sine wave, rms = 1.11 × average (form factor 1.11), and $4 \times 1.11 = 4.44$.
```

```example Worked example 1: EMF from the flux
A sinusoidal flux of peak value $\given{0.02\ \text{Wb}}$ links a winding of $\given{100}$ turns at $\given{50\ \text{Hz}}$. Find the rms EMF.
:::given $\Phi_m = 0.02\ \text{Wb}$, $N = 100$, $f = 50\ \text{Hz}$
:::find rms EMF $E$
:::step Use $\fx{E = 4.44 f N \Phi_m}$. First multiply 4.44 by f.
$4.44 \times 50 = 222$
:::step Multiply by N.
$222 \times 100 = 22\,200$
:::step Multiply by the flux.
$22\,200 \times 0.02 = 444$
:::answer $\ans{E = 444\ \text{V}}$
**Without a calculator:** $4.44 \times 50 \times 100 \times 0.02 = 4.44 \times 100 = 444$ (because $50 \times 0.02 = 1$).
```

```example Worked example 2: number of turns for a design
A $\given{2200/220\ \text{V}}$, $\given{50\ \text{Hz}}$ transformer has a net core area of $\given{250\ \text{cm}^2}$ and is to work at a peak flux density of $\given{1.2\ \text{T}}$. Find the turns on each winding.
:::given $V_1 = 2200\ \text{V}$, $V_2 = 220\ \text{V}$, $f = 50\ \text{Hz}$, $A = 250\ \text{cm}^2$, $B_m = 1.2\ \text{T}$
:::find $N_1$ and $N_2$
:::step Peak flux. $\fx{\Phi_m = B_m A}$
$A = 250 \times 10^{-4} = 0.025\ \text{m}^2$
$\Phi_m = 1.2 \times 0.025 = 0.03\ \text{Wb}$
:::step EMF per turn. $\fx{4.44 f \Phi_m}$
$4.44 \times 50 = 222$
$222 \times 0.03 = 6.66\ \text{V per turn}$
:::step Primary turns: total EMF ÷ EMF per turn.
$N_1 = 2200 \div 6.66$
$6.66 \times 330 = 2197.8$
$N_1 \approx 330$
:::step Secondary turns.
$N_2 = 220 \div 6.66 \approx 33$
:::answer $\ans{N_1 \approx 330,\ N_2 \approx 33}$
**Sense check:** $330 \div 33 = 10 = 2200 \div 220$. ✓
```

## The core flux is set by the supply, not by the load

Turn the EMF equation round: $\Phi_m = \dfrac{E_1}{4.44\,f\,N_1}$. In a practical transformer the applied voltage $V_1$ is almost equal to $E_1$ (the drop in the winding is small). So

$$\Phi_m \approx \dfrac{V_1}{4.44\,f\,N_1}, \qquad B_m \propto \dfrac{V}{f}$$

```remember
- With $V_1$ and $f$ fixed, the core flux is **almost constant from no load to full load**. (It falls very slightly with load, because a little more of $V_1$ is used up in the primary's resistance and leakage reactance.) This is why the core loss of a transformer is a **constant loss**.
- **Raise the voltage** at the same frequency: $B_m$ rises; the core goes towards saturation and the magnetising current rises sharply.
- **Lower the frequency** at the same voltage: $B_m$ **rises** in proportion. A 50 Hz transformer on 40 Hz at full voltage runs at 25 % more flux density.
- To use a transformer at another frequency **without changing its flux**, change the voltage in the **same ratio** as the frequency (keep V/f constant). Example: a winding rated 230 V at 50 Hz should get 115 V at 25 Hz.
```

## When the flux is not a sine wave

Faraday's law works for any shape: **the EMF at each instant is N × (slope of the flux curve at that instant)**.

```remember
- Flux rising in a straight line: constant positive EMF.
- Flux flat (constant): **zero** EMF.
- Flux falling in a straight line: constant negative EMF.
- So a **triangular** flux wave gives a **square** EMF wave, and a **trapezoidal** flux wave gives rectangular pulses of EMF with gaps of zero where the flux is flat.

**Turned round:** the flux is the area under the voltage curve, divided by N. A square voltage gives a triangular flux.
```

```example Worked example 3: EMF from a trapezoidal flux
The flux in a core rises in a straight line from $\given{0}$ to $\given{0.01\ \text{Wb}}$ in $\given{2\ \text{ms}}$, stays at 0.01 Wb for 2 ms, then falls to 0 in 2 ms. A winding of $\given{50}$ turns is on the core. Find the EMF in each part.
:::given $N = 50$; rise 0.01 Wb in 2 ms; flat 2 ms; fall 0.01 Wb in 2 ms
:::find the EMF in each of the three parts
:::step Rising part: slope of the flux.
$\Delta\phi \div \Delta t = 0.01 \div 0.002 = 5\ \text{Wb/s}$
$e = N \times 5 = 50 \times 5 = 250\ \text{V}$
:::step Flat part: the flux does not change.
$\Delta\phi = 0$, so $e = 0$
:::step Falling part: same size of slope, opposite sign.
$\Delta\phi \div \Delta t = -0.01 \div 0.002 = -5\ \text{Wb/s}$
$e = 50 \times (-5) = -250\ \text{V}$
:::answer $\ans{+250\ \text{V},\ 0,\ -250\ \text{V}}$: rectangular pulses with a gap.
```

## Why the magnetising current is not a sine wave

Because the applied voltage is a sine wave, the flux **must** also be a sine wave (Faraday's law fixes the flux from the voltage). But the iron's B-H curve is not straight: near the peaks of the flux the iron saturates, and much **more** current is needed for the last bit of flux.

So to make a sinusoidal flux, the coil must draw a current that is **peaky**: flat near zero and sharp near the peaks. Such a wave is a sine wave plus extra sine waves of 3, 5, 7… times the frequency (harmonics).

```remember
- The magnetising current of an iron-cored transformer is **non-sinusoidal**; its main harmonic is the **third** (it is "rich in third harmonic"). The more the iron is saturated, the more distorted the current.
- Hysteresis also tilts the current wave a little, and adds a small in-phase part that supplies the core loss.
- With an **air core** (straight-line B-H, no saturation, no hysteresis), the magnetising current is a **pure sine wave**, in step with the flux.
- When a saturated transformer feeds a resistive load from a sinusoidal supply: the flux, the induced voltages and the load current all stay sinusoidal. Only the current drawn **from the source** (load current + magnetising current) is distorted.
```

## Inrush current when switching on

The flux is the area under the voltage wave. In steady running the flux lags the voltage by 90°: when the voltage passes through zero, the flux is at its peak $\pm\Phi_m$.

- **Switched on at a voltage zero:** the flux has to start from zero (there is no flux yet in the core), but the voltage area over the next half cycle builds up $2\Phi_m$. The flux therefore swings up to about **$2\Phi_m$** (more, if residual flux adds to it). The core saturates hard and the transformer draws a huge current, many times rated current, for a few cycles. This is the **inrush current**.
- **Switched on at a voltage peak:** at that instant the steady flux should be zero, which is exactly where it starts. So the flux follows its normal path from the first moment: **no inrush**.

So inrush is **least** when the transformer is switched on at the **peak** of the voltage, and worst at a voltage zero. It comes from the non-linear (saturating) magnetising inductance.

## Summary of this topic

- $E = 4.44 f N \Phi_m = 4.44 f N B_m A$ (rms); EMF per turn is the same in both windings; EMF is 90° from the flux.
- Core flux $\Phi_m \approx V/(4.44 f N)$: fixed by the supply, nearly the same at all loads; $B_m \propto V/f$.
- Keep V/f constant to keep the flux constant at another frequency.
- For any flux wave, EMF = N × slope. Triangle flux → square EMF.
- Magnetising current of an iron core: peaky, rich in 3rd harmonic; of an air core: sinusoidal.
- Inrush is least when switched on at a voltage peak and worst at a voltage zero (flux can reach about $2\Phi_m$).
