## Why loads matter to a power station

Electrical energy cannot be stored in large amounts. So a power station must produce, **at every moment**, exactly what the consumers are using. But the consumers' demand keeps changing: low at night, high in the evening. The size of the plant is fixed by the **highest** demand, while the money earned comes from the **energy** sold. The "load factors" in this topic measure how well a plant is used. They decide the cost of each unit of electricity.

## The words we need

**Connected load:** the sum of the ratings (in kW) of all the equipment connected by a consumer. A house with ten 60 W lamps, a 1 kW heater and a 2 kW geyser has a connected load of $0.6 + 1 + 2 = 3.6$ kW.

**Maximum demand (MD):** the greatest load actually drawn at any time during a given period (day, month, year). All the equipment is never ON together, so the maximum demand is **less than** the connected load.

**Average load:** the steady load that would give the same energy in the same time:

$$\text{Average load} = \frac{\text{energy (kWh) in a period}}{\text{hours in the period}}$$

- Daily average load = units in the day ÷ 24
- Annual average load = units in the year ÷ 8760 (= 365 × 24)

## The load curve and the load duration curve

A **load curve** is a graph of load (kW or MW) against time, usually over 24 hours.

![A daily load curve: the highest point is the maximum demand; the dashed line is the average load](fig:c-ps1-load-curve)

From the load curve we read:

- the **maximum demand** (highest point),
- the **energy** used in the day = **area under the curve** (kWh),
- the **average load** = area ÷ 24,
- the hours of peak load, which decide the plants needed (see *Base load and peak load plants*).

A **load duration curve** is made from the same data by arranging the loads in **decreasing order**, the largest first. The time axis now shows "for how many hours the load was at least this much". The area under it is still the same energy.

![Load duration curve: the same loads arranged from largest to smallest](fig:c-ps1-load-duration)

The load duration curve is useful for choosing how much base-load and peak-load capacity to install.

## The factors (learn the definitions exactly)

### 1. Demand factor

$$\text{Demand factor} = \frac{\text{maximum demand}}{\text{connected load}} \qquad (\le 1)$$

**Example 1.** A factory has a connected load of 400 kW and a maximum demand of 300 kW. Demand factor $= 300/400 = 0.75$.

### 2. Load factor (the most important one)

$$\text{Load factor} = \frac{\text{average load}}{\text{maximum demand}} \qquad (\le 1)$$

Multiplying top and bottom by the hours $T$:

$$\text{Load factor} = \frac{\text{energy in } T}{\text{maximum demand}\times T}$$

So **annual load factor** $= \dfrac{\text{units generated per year}}{\text{MD}\times 8760}$.

A load factor of 1 means the load is flat (always at its maximum). A low load factor means a sharp peak with long idle hours.

**Example 2.** A consumer takes 300 kW for 8 hours and 100 kW for the other 16 hours of the day.
Energy $= 300\times8 + 100\times16 = 2400 + 1600 = 4000$ kWh. Average $= 4000/24 = 166.7$ kW. Load factor $= 166.7/300 = 0.556$.

**Without a calculator:** load factor $= \dfrac{4000}{300\times24} = \dfrac{4000}{7200} = \dfrac{5}{9} \approx 0.56$.

**Example 3 (time-weighted shortcut).** Load = 2 units for half the day and 1 unit for the other half. Average = 1.5 units, maximum = 2 units, load factor = 0.75. When the load stays at each level for **equal times**, the average is just the plain average of the levels.

> **Trap:** the load factor uses **kW (real power)**. A power factor given in the question (for example "1000 kW at 0.8 lag") does **not** enter the load factor. It is a distractor.

### 3. Diversity factor

The maximum demands of different consumers do not occur at the same time. So

$$\text{Diversity factor} = \frac{\text{sum of the individual maximum demands}}{\text{maximum demand of the whole group (station)}} \qquad (\ge 1)$$

**Example 4.** Three consumers have maximum demands of 50, 40 and 30 kW. The maximum demand of the three together (at the feeder) is 80 kW. Diversity factor $= (50+40+30)/80 = 120/80 = 1.5$.

A higher diversity factor means the station needs a **smaller** capacity to serve the same consumers. Domestic lighting loads (all peak at night) have low diversity; a mix of domestic, commercial, industrial and agricultural loads has high diversity.

### 4. Plant capacity factor

$$\text{Plant capacity factor} = \frac{\text{average load}}{\text{rated capacity of the plant}} = \frac{\text{actual energy produced}}{\text{maximum possible energy (capacity}\times\text{hours)}}$$

Since the capacity is at least the maximum demand, the capacity factor is **less than or equal to** the load factor. They are equal only if the plant capacity equals the maximum demand (no reserve).

### 5. Utilisation factor

$$\text{Utilisation factor} = \frac{\text{maximum demand}}{\text{rated capacity}}$$

and so: **capacity factor = load factor × utilisation factor.**

### 6. Reserve capacity

$$\text{Reserve capacity} = \text{plant capacity} - \text{maximum demand}$$

### 7. Plant use factor

$$\text{Plant use factor} = \frac{\text{actual energy produced}}{\text{capacity}\times\text{hours the plant actually ran}}$$

It is like the capacity factor, but only the running hours count.

**Example 5 (all factors together).** A station of 125 MW capacity has a maximum demand of 100 MW and produces 438 000 MWh in a year.

- Average load $= 438\,000/8760 = 50$ MW.
- Load factor $= 50/100 = 0.5$.
- Capacity factor $= 50/125 = 0.4$.
- Utilisation factor $= 100/125 = 0.8$; check: $0.5\times0.8 = 0.4$ ✓.
- Reserve capacity $= 125 - 100 = 25$ MW.

**Without a calculator:** remember $8760 = 365\times24$ and $438\,000 = 50\times8760$. Many exam numbers are built this way, so try dividing by 8760 in your head first.

**Example 6.** A station supplies four feeders with maximum demands 10, 8, 6 and 6 MW. The diversity factor is 1.5 and the annual load factor is 0.6. Find the station's maximum demand and its annual energy.

Station MD $= (10+8+6+6)/1.5 = 30/1.5 = 20$ MW. Energy $= 20\times0.6\times8760 = 12\times8760 = 105\,120$ MWh.

## Why high load factor and high diversity mean cheap power

The cost of a station has two parts (see *Cost of generation and tariffs*):

- a **fixed** part (interest, depreciation) that depends on the **size** of the plant (its kW capacity), and
- a **running** part (fuel) that depends on the **energy** (kWh).

- A **high load factor** means more kWh are produced from the same plant, so the fixed cost is spread over more units: the cost per unit falls.
- A **high diversity factor** means a smaller maximum demand for the same consumers, so a smaller (cheaper) plant is enough: again the cost per unit falls.

> **Remember:** for cheap energy, **both** the load factor and the diversity factor should be **high**.

## Summary of this topic

| Factor | Formula | Range |
|---|---|---|
| Demand factor | MD ÷ connected load | ≤ 1 |
| Load factor | average load ÷ MD | ≤ 1 |
| Diversity factor | Σ individual MDs ÷ MD of group | ≥ 1 |
| Capacity factor | average load ÷ plant capacity | ≤ load factor |
| Utilisation factor | MD ÷ plant capacity | ≤ 1 |

- Energy = area under the load curve; average load = energy ÷ hours.
- Capacity factor = load factor × utilisation factor.
- Power factor does not enter the load factor (kW basis).
