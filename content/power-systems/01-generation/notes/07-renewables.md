## Renewable and non-renewable sources

A source is **renewable** if nature refills it as fast as we use it: **solar, wind, hydro, geothermal, tidal, wave, biomass**.

A source is **non-renewable** if there is a fixed stock in the earth that will run out: **coal, oil, natural gas** and **nuclear fuel** (uranium, thorium).

> **Trap:** nuclear is *clean* (no CO₂ while running) but it is **not renewable**. Geothermal (heat from inside the earth) **is** renewable.

## Solar photovoltaic (PV) power

### How a solar cell works

A solar cell is a large **p-n junction** (diode) of silicon. When light (photons) falls on it, each photon with enough energy frees an electron and leaves a hole. The electric field of the junction pushes the electrons to the n side and the holes to the p side. If an external load is connected, a current flows. This **light-generated current** $I_L$ is proportional to the light falling on the cell.

### The equivalent circuit

![Equivalent circuit of a PV cell: light-generated current source, diode, shunt resistance and series resistance](fig:c-ps1-pv-circuit)

- **Current source $I_L$:** the light-generated (photo) current.
- **Diode:** the p-n junction itself. Part of $I_L$ flows back through the diode: $I_D = I_0\left(e^{V/V_T}-1\right)$.
- **$I_0$, the reverse saturation current,** is produced by heat (thermally generated carriers). It flows even when there is no light at all, so it is called the **dark current**.
- **Shunt resistance $R_{sh}$:** leakage paths across the junction (edges, defects).
- **Series resistance $R_s$:** resistance of the silicon, contacts and wires.

The output current is

$$I = I_L - I_D - I_{sh}$$

**For more power** we want:

- $I_L$ **as large as possible** (more light, better cell);
- $R_s$ **as small as possible** (less voltage lost inside the cell);
- $R_{sh}$ **as large as possible** (less current leaking back inside the cell).

An ideal cell has $R_s = 0$ and $R_{sh} = \infty$.

### The I-V curve and the key numbers

![I-V curve of a solar cell, with the maximum power point](fig:c-ps1-pv-iv)

- **Short-circuit current $I_{sc}$:** current with the output shorted ($V = 0$). Almost equal to $I_L$.
- **Open-circuit voltage $V_{oc}$:** voltage with no load ($I = 0$). About **0.5–0.6 V** for one silicon cell.
- **Maximum power point (MPP):** the point $(V_{mp}, I_{mp})$ where $V\times I$ is largest. Inverters use **MPPT** (maximum power point tracking) to keep the panel there.

**Fill factor:**

$$FF = \frac{V_{mp}\,I_{mp}}{V_{oc}\,I_{sc}} \qquad\Rightarrow\qquad P_{max} = FF\times V_{oc}\times I_{sc}$$

It shows how "square" the I-V curve is. Good silicon cells have $FF \approx 0.7$–$0.85$.

**Efficiency** $= P_{max}/(\text{irradiance}\times\text{area})$; commercial panels are about 18–22 %.

### Cells in series and parallel

- Cells in **series** add their **voltages** (same current).
- Cells in **parallel** add their **currents** (same voltage).

A **module** (panel) is usually 36, 60 or 72 cells in series; many modules make an **array**.

**Example 1.** A module has 36 cells in series. Each cell: $V_{oc} = 0.6$ V, $I_{sc} = 5$ A. Fill factor = 0.75.
Module $V_{oc} = 36\times0.6 = 21.6$ V, $I_{sc} = 5$ A (series cells carry the same current).
$P_{max} = 0.75\times21.6\times5 = 81$ W.
If $V_{mp} = 0.8\,V_{oc}$, then $I_{mp} = \dfrac{FF\,I_{sc}}{V_{mp}/V_{oc}} = \dfrac{0.75\times5}{0.8} = 4.69$ A.

**Without a calculator:** $I_{mp} = I_{sc}\times\dfrac{FF}{V_{mp}/V_{oc}}$. The number of cells cancels out, so you never need the module voltage to find $I_{mp}$. Here $0.75/0.8 = 15/16 = 0.9375$, and $5\times0.9375 = 4.69$.

## Wind power

### Power in the wind

Air of density $\rho$ moving at speed $V$ through an area $A$ carries kinetic energy at the rate

$$P = \tfrac12\,\rho\,A\,V^3$$

(mass flow $\rho A V$ per second, each kilogram carrying $\tfrac12V^2$). With $A = \pi D^2/4$ for a rotor of diameter $D$:

- $P \propto V^3$: **double the wind speed → 8 times the power**;
- $P \propto D^2$: double the rotor diameter → 4 times the power.

**Example 2.** Air density 1.2 kg/m³, swept area 100 m², wind speed 10 m/s.
Power in the wind $= 0.5\times1.2\times100\times1000 = 60\,000$ W = 60 kW. If the turbine's power coefficient is 0.4, it gives 24 kW.

**Example 3.** Wind speed rises by 10 %. Power rises by the factor $1.1^3 = 1.331$, about 33 %.

### Betz limit

A turbine cannot take all the energy (the air must keep moving to leave). The maximum fraction any turbine can extract is

$$C_{p,\max} = \frac{16}{27} \approx 0.593 \quad (\text{Betz limit, } 59.3\,\%)$$

Real turbines reach about 0.4–0.5.

### Tip speed ratio

$$\lambda = \frac{\text{speed of the blade tip}}{\text{wind speed}} = \frac{\omega R}{V}$$

The **tip speed (tip speed ratio) is the single most important design parameter** of a wind turbine: the power coefficient $C_p$ depends on it, and each rotor design gives its best $C_p$ at one particular tip speed ratio. Modern three-blade turbines work best at $\lambda \approx 6$–$8$; the controller changes the rotor speed to keep near this value.

### Lift type and drag type turbines

- **Lift type** (modern horizontal-axis propeller turbines, Darrieus): blades work like aircraft wings. **High speed, low torque**, high efficiency: used for electricity.
- **Drag type** (Savonius, cup anemometer, old multi-blade water-pumping windmills): the wind simply pushes the blades. The blades can never move faster than the wind, so they have **low speed and high (starting) torque**: good for water pumping.

### Speeds on the power curve

- **Cut-in speed:** the turbine starts producing power (about 3–4 m/s).
- **Rated speed:** the turbine reaches its rated power.
- **Cut-out (furling) speed:** the turbine is stopped to save it from damage (about 25 m/s).

## Other renewable sources (one-liners)

- **Tidal:** uses the rise and fall of the sea. Fully **predictable** (tides follow the moon), but possible only at a few coastal sites and output varies over the day.
- **Geothermal:** heat from hot rocks under the ground makes steam. Runs steadily round the clock (very high capacity factor), so it is the **most reliable** of these renewables, but only in special places.
- **Biomass:** burning plant and animal waste (bagasse in sugar mills, rice husk); can be run like a small steam plant.
- **Wave, ocean thermal (OTEC):** still small.

## Reliability of renewable sources

| Source | Can we predict it? | Reliability |
|---|---|---|
| Geothermal | steady all the time | highest |
| Tidal | yes, exactly (tide tables) | high, but varies in a known way |
| Solar | mostly (day/night known; clouds vary) | medium |
| **Wind** | **hard to predict**; may stop for days | **least reliable** |

## Summary of this topic

- Nuclear is not renewable; geothermal is.
- PV cell: $I_L$ source, diode, $R_{sh}$, $R_s$. More power: $I_L$ max, $R_s$ small, $R_{sh}$ large. Dark current = reverse saturation current (flows without light).
- $FF = V_{mp}I_{mp}/(V_{oc}I_{sc})$; series cells add voltage, parallel add current.
- Wind: $P = \tfrac12\rho AV^3$; Betz limit 59.3 %; tip speed ratio is the key design parameter; drag type = low speed, high torque.
- Wind is the least reliable (least predictable) renewable source.
