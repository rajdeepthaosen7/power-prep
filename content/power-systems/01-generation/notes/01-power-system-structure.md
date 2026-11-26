## What is a power system?

A **power system** is the whole network that makes electrical energy and brings it to our homes, shops and factories. It has three main parts:

1. **Generation:** power stations turn some natural energy (coal, falling water, uranium, sunlight, wind) into electrical energy.
2. **Transmission:** high-voltage lines carry this energy over long distances, from the power stations to the load areas.
3. **Distribution:** lower-voltage networks spread the energy to every consumer.

![The chain from the power station to the consumer, with typical Indian voltages](fig:c-ps1-structure)

So the **main function of a power system** is to **convert energy** from a natural form into electrical form and **transmit (deliver) it to the consumers**, with the **highest possible efficiency**, at a steady voltage and frequency, at low cost and without interruption.

> **Remember:** generating stations + transmission system + distribution network = the three main parts of a power system. A question listing these three wants "all three".

In Assam, these three jobs are even done by three separate companies: **APGCL** generates, **AEGCL** transmits (the grid company) and **APDCL** distributes. These are exactly the three companies of your exam.

## Why do we transmit at high voltage?

The power carried by a three-phase line is

$$P = \sqrt3\,V_L I_L\cos\phi$$

For the same power $P$ (and the same power factor), the current is

$$I \propto \frac{1}{V}$$

The loss in the line is $I^2R$, so

$$\text{line loss} \propto I^2 \propto \frac{1}{V^2}$$

This is the whole reason for high-voltage transmission.

- Double the voltage → current becomes half → loss becomes **one quarter**.
- For the same loss, the conductor can be much thinner: the conductor volume needed is also $\propto 1/V^2$.
- Less current also means less voltage drop, so better voltage regulation.

The price we pay: more insulation, taller towers and costly transformers and switchgear. So a very high voltage is worth it only for long lines and large power.

**Example 1.** A line carries 100 MW at 220 kV with a loss of 4 MW. The same power is now sent at 440 kV on the same line. Loss = $4 \times (220/440)^2 = 4\times\tfrac14 = 1$ MW.

**Example 2.** If the voltage is raised by 10 %, the loss falls to $1/1.1^2 \approx 1/1.21 \approx 0.83$ of the old value (about 17 % less).

## Voltage levels used in India

| Part | Typical voltages |
|---|---|
| Generation (alternator terminals) | 11 kV to about 25 kV (11, 13.8, 15.75, 21 kV are common) |
| Transmission | 765 kV, 400 kV, 220 kV (and ±500 kV, ±800 kV HVDC) |
| Sub-transmission | 132 kV, 66 kV, 33 kV |
| Primary distribution | 11 kV (also 33 kV) |
| Secondary distribution | 415 V three-phase, 230 V single-phase |

The frequency is **50 Hz** all over India.

A **substation** changes the voltage from one level to another (step-up at the power station, step-down near the loads) and also holds the switchgear and protection.

## The grid: why we interconnect

In the early days every city had its own power station. Today the power stations and loads of a whole region (even the whole country) are joined by transmission lines into one **interconnected system** called the **grid**. India's five regional grids were joined step by step; since the end of 2013 the whole country runs as **one national grid at one frequency** ("One Nation, One Grid, One Frequency").

**Advantages of a large interconnected grid:**

1. **Less reserve capacity is needed.** Each station keeps some spare capacity for breakdowns. In a grid, the stations share their reserves, so the total spare capacity can be smaller.
2. **Diversity of loads.** Peak demands of different areas come at different times. So the maximum demand of the whole grid is less than the sum of the separate maximum demands. A **smaller total installed capacity can meet the same demand** (or the same installed capacity can meet a **higher** demand).
3. **Economy (cheapest plants run most).** The most efficient, lowest-cost plants can be run at full load all the time and their energy sent wherever needed.
4. **Better co-ordination of hydro, thermal, nuclear and renewable plants.** For example, hydro power in the rainy season can be used to save coal; surplus solar at noon can be sent to other regions. This also saves (conserves) energy.
5. **Better frequency control.** A large system has huge rotating mass (inertia). A sudden change of load in one place changes the frequency only a little, and many generators share the correction (load frequency control works better).
6. **Higher reliability.** If one plant or line fails, power comes through other routes.

**Disadvantages:** a big fault can spread across the system (cascade tripping, blackouts like July 2012 in North India); fault currents (fault levels) become higher, so circuit breakers must be stronger; operation needs central co-ordination (load despatch centres).

> **Trap:** "Same total installed capacity can meet **lower** demands" is the wrong way round. Interconnection lets the same capacity meet **higher** demand. Read such statements slowly.

## Reliability, reserve and quality: three words that get mixed up

- **Reliability:** the ability of the power system to supply all the demand **without failure over long periods of time**. Interconnection, a grid and redundant (extra) lines are what make a system reliable.
- **Reserve generating capacity:** the spare generating capacity kept above the maximum demand. It is one of the *means* of reliability, not the name of the ability itself.
- **Power quality:** how "clean" the supply is at any instant: steady voltage, steady 50 Hz, a pure sine wave (no harmonics), no flicker.

**Example 3.** "The ability to serve all demands without failure over long periods, due to interconnection and line redundancy, is called …" → **reliability**.

## Summary of this topic

- Power system = generation + transmission + distribution.
- Main function: convert energy to electrical form and transmit it to consumers with maximum efficiency.
- Line loss $\propto 1/V^2$ for the same power: that is why we transmit at high voltage.
- Grid advantages: less reserve, diversity (less installed capacity for the same demand), economy, hydro-thermal-nuclear-renewable co-ordination, better frequency control, reliability.
- Reliability = supply without failure over long periods.
