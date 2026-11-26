## The idea in one line

Water stored at a height has **potential energy**. In a hydroelectric (hydel) plant the water falls through a pipe, spins a **water turbine**, and the turbine drives the alternator. No fuel is burnt.

![Hydro plant: the path of water from the reservoir to the tail race](fig:c-ps1-hydro)

## How much power? (the one formula of this topic)

Let $Q$ = flow of water (discharge) in m³/s and $H$ = head (the height through which the water falls) in m.

Each second, a mass $\rho Q$ kg of water falls through $H$ metres, so the power given by the water is

$$P = \rho\,g\,Q\,H \ \text{watts}$$

With $\rho = 1000$ kg/m³ and $g = 9.81$ m/s²:

$$P = 9.81\,Q\,H \ \text{kW} \qquad\text{and the electrical output is}\qquad P_e = 9.81\,Q\,H\,\eta \ \text{kW}$$

where $\eta$ is the overall efficiency (turbine × generator, about 0.8–0.9).

> **Remember:** the output of a hydro plant depends on **head, discharge and efficiency**. Not on the dam type or the catchment directly.

**Example 1.** $Q = 100$ m³/s, $H = 50$ m, $\eta = 0.8$.
$P_e = 9.81\times100\times50\times0.8 = 39\,240$ kW $\approx 39$ MW.

**Without a calculator:** take $g \approx 10$: $10\times100\times50\times0.8 = 40\,000$ kW, then take off 2 % (because 9.81 is about 2 % less than 10): about 39 000 kW.

**Example 2.** How much water per second does a 100 MW plant need with a 200 m head and 90 % efficiency?
$Q = \dfrac{100\,000}{9.81\times200\times0.9} \approx \dfrac{100\,000}{1766} \approx 57$ m³/s.

> **Trap:** questions sometimes give extra data such as the **area of the reservoir** or the catchment. The *power* needs only $Q$ and $H$ (and $\eta$). The reservoir area matters for how long the water lasts (energy stored), not for the power.

## The parts of a hydro plant

- **Catchment area:** the whole area whose rain flows into the river at the dam.
- **Reservoir and dam:** store water and create the head. A **spillway** lets extra flood water pass safely.
- **Intake / head works:** with gates and trash racks (screens to stop debris).
- **Penstock:** the large pipe that brings water from the reservoir (or the forebay) down to the turbine.
- **Surge tank:** an open tank on the pipeline just before the penstock. When the load suddenly falls and the turbine gates close, the moving water would give a big pressure rise (**water hammer**) that can burst the penstock. The surge tank takes this rising water. When the load suddenly rises, it supplies extra water at once.
- **Turbine and alternator** in the power house.
- **Draft tube:** a widening pipe from the outlet of a reaction turbine down to the tail race. It lets the turbine be placed above the tail race level without losing head, and recovers some kinetic energy of the leaving water.
- **Tail race:** the channel that takes the used water back to the river.

## Types of hydro plants

### By the head

| Head | Typical range | Turbine |
|---|---|---|
| Low head | below about 30 m | Kaplan (propeller) |
| Medium head | about 30 m to 300 m | Francis |
| High head | above about 300 m | Pelton wheel |

(Books give slightly different limits. What matters is the order: Kaplan for low head, Francis for medium, Pelton for high.)

### By the storage of water

1. **Run-of-river plant without pondage:** uses the river flow as it comes; no storage. Output depends on the day's flow. When the river is low, output falls. Useful as **base load** in the season of good flow.
2. **Run-of-river plant with pondage:** a small **pond** behind a weir stores water for a few hours or days. Water saved during low-load hours is used during peak hours. **Pondage increases the firm capacity** of the plant, and the plant can take part of the peak load.
3. **Storage (reservoir) plant:** a big reservoir stores water from the rainy season for the whole year. Output can be controlled, so these plants are excellent for following load and for **peaks**.
4. **Pumped storage plant:** has an upper and a lower reservoir. During off-peak hours (cheap surplus power at night or solar noon) the machines **pump** water up; during **peak** hours the water runs back down to generate. It is a giant battery and a typical **peak load** plant.

**Firm capacity** = the power that the plant can supply **at all times**, even in the driest period. It is fixed by the minimum river flow. Pondage or storage raises it.

## Water turbines

### Impulse and reaction turbines

- **Impulse turbine (Pelton wheel):** all the head is turned into a high-speed **jet** of water in a nozzle. The jet strikes buckets on the wheel in open air at **atmospheric pressure**. The runner is not full of water. Used for **high head, low flow**.
- **Reaction turbine (Francis, Kaplan):** the runner is **completely filled with (embedded in) water** inside a closed casing. The water pressure drops as it passes through the runner, and this **pressure drop** (plus the change of velocity) drives the runner. A draft tube is used. Used for **medium and low heads, large flows**.

> **Trap:** "reaction turbines are used for high head and low flow" is **false**. High head–low flow is Pelton (impulse). Reaction turbines (Francis, Kaplan) suit lower heads and larger flows.

### Kaplan turbine

An axial-flow reaction turbine (like a ship's propeller) with a **high degree of reaction** and **adjustable runner blades**. The blade angle changes with the load, so it keeps a good efficiency when the head and flow vary. It is the right choice for **low and varying heads** with large flows.

### Francis turbine

A mixed-flow (radial-to-axial) reaction turbine for **medium heads**. The most widely used turbine in the world.

### Specific speed

The **specific speed** $N_s$ of a turbine is the speed of a geometrically similar turbine that would give 1 metric horsepower (or 1 kW) under 1 m head. It tells us the turbine type:

$$N_s = \frac{N\sqrt{P}}{H^{5/4}}$$

where $N$ is in rpm, $P$ is the output and $H$ the head in metres. ($5/4 = 1.25$.)

| Turbine | Specific speed (metric units, approx.) |
|---|---|
| Pelton | about 10–35 (one jet) |
| Francis | about 60–300 |
| Kaplan | about 300–1000 |

Low head → high specific speed (Kaplan); high head → low specific speed (Pelton).

**How to remember the power of $H$:** a bigger head gives a smaller $N_s$, so $H$ is in the **denominator**; its power is **5/4** (not 2.25, not 3/2).

## Advantages and disadvantages of hydro plants

| Advantages | Disadvantages |
|---|---|
| No fuel: very low running cost | Very high capital cost (dam, tunnels) |
| Starts in minutes and picks up load quickly | Output depends on rainfall |
| Long life, simple, reliable | Usually far from the load: long lines needed |
| No smoke; also gives irrigation and flood control | Long construction time; land under water |

In Assam, for example, APGCL runs the Karbi Langpi hydro project in Karbi Anglong.

## Summary of this topic

- $P = 9.81\,Q\,H\,\eta$ kW: output depends on head, discharge and efficiency.
- Surge tank: protects penstock from water hammer. Draft tube: on reaction turbines, recovers head and kinetic energy.
- Pelton (impulse, high head); Francis (reaction, medium head); Kaplan (reaction, low varying head, adjustable blades).
- Reaction turbine: runner fully immersed, driven by pressure drop. Impulse: free jet at atmospheric pressure.
- $N_s = N\sqrt P / H^{5/4}$.
- Pondage increases firm capacity. Pumped storage = peak load plant.
