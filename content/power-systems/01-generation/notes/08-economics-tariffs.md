## Why the cost of each unit is not fixed

A power station costs a lot of money to build, and that money has to be paid back every year whether the plant runs or not. On top of that, fuel is burnt only when energy is produced. So the cost of one unit (kWh) depends on **how much the plant is used**. This topic shows how.

## The costs of a power station

### 1. Fixed costs (capital costs)

Money spent once to build the station: land, buildings, machines, lines. Every year we must pay:

- **interest** on the capital,
- **depreciation** (the fall in value of the plant as it wears out; money kept aside to replace it),
- taxes and insurance.

These depend on the **size of the plant** (its kW capacity), not on the energy produced.

### 2. Semi-fixed costs

Costs that depend on the **maximum demand** (the size of plant and network that must be kept ready), for example part of the staff salaries, maintenance of the network, interest on the distribution system.

### 3. Running (operating) costs

Costs that depend on the **energy (kWh) generated**: fuel, lubricating oil, cooling water, wages of operating staff, repairs from wear.

> **Remember:** **fixed + semi-fixed costs are independent of the energy generated; together they are called the standing charges (standing costs).** Running costs depend on the kWh produced.

So the annual cost can be written in the **three-part form**

$$\text{Annual cost} = a + b\times(\text{kW of maximum demand}) + c\times(\text{kWh generated})$$

## Cost per unit and the load factor

Let the fixed annual cost be $F$ (₹/year), the maximum demand $P_m$ (kW) and the running cost $c$ (₹/kWh). The energy in a year is

$$E = P_m\times LF\times8760\ \text{kWh}$$

$$\text{cost per kWh} = \frac{F}{P_m\times LF\times8760} + c$$

The first term (fixed cost per unit) is **inversely proportional to the load factor**.

**Example 1.** A station has maximum demand 100 MW, fixed cost ₹87.6 crore per year and running cost ₹3 per kWh.
Fixed cost per unit $= \dfrac{87.6\times10^7}{100\,000\times8760\times LF} = \dfrac{1}{LF}$ ₹.

- At LF = 0.5: total = 2 + 3 = ₹5 per kWh.
- At LF = 0.8: total = 1.25 + 3 = ₹4.25 per kWh.

The higher load factor saves 75 paise on every unit.

**Without a calculator:** $100\,000\ \text{kW}\times8760\ \text{h} = 87.6\times10^7$ kWh, so the numbers were chosen to cancel. In the exam, write the energy as $P_m\times8760\times LF$ and look for such cancellations before multiplying.

So, as said in *Load curves and load factors*: **high load factor and high diversity factor both give cheaper energy.**

## Depreciation

The plant loses value every year. Two common ways to set money aside:

- **Straight-line method:** the same amount every year.

$$\text{Annual depreciation} = \frac{\text{first cost} - \text{salvage value}}{\text{useful life (years)}}$$

- **Sinking fund method:** a fixed amount is put aside every year and earns compound interest, so that at the end of the life the fund equals (first cost − salvage value). The yearly amount is smaller than in the straight-line method because interest helps.

**Example 2.** A plant costs ₹50 crore, will last 25 years and will be sold for ₹5 crore as scrap. Straight-line depreciation $= (50-5)/25 = 1.8$ crore per year.

## Tariffs: how consumers are charged

A **tariff** is the rate (method) by which consumers pay for electricity. A good tariff should:

- recover all the costs (fixed and running) plus a fair profit;
- be fair to different classes of consumers;
- **encourage a high load factor** (use more off-peak) and a **high power factor**;
- be simple to understand.

### Types of tariff

1. **Simple tariff:** one fixed rate per kWh for all units. Easy, but does not reflect the fixed cost.
2. **Flat-rate tariff:** different fixed rates per kWh for different types of consumers (e.g. lighting load and power load).
3. **Block-rate tariff:** the first block of units at one rate, the next block at another rate, and so on. (Indian domestic tariffs use rising blocks to save energy; for industry, falling blocks once encouraged more use.)
4. **Two-part tariff:** a charge on the **maximum demand (₹ per kW)** + a charge on the **energy (₹ per kWh)**. The first part recovers the fixed cost, the second the running cost. Industrial consumers pay this way.
5. **Maximum demand tariff:** like two-part, with the maximum demand actually measured by a maximum-demand meter.
6. **Power factor tariff:** penalises low power factor: the demand is charged in **kVA** instead of kW, or there is a sliding scale (discount above a target pf, extra charge below it).
7. **Three-part tariff:** fixed charge (₹ per month) + ₹ per kW of MD + ₹ per kWh.
8. **Time-of-day (ToD) tariff:** higher rate in peak hours, lower rate in off-peak hours. It shifts load away from the peak and improves the load factor.

**Example 3 (two-part tariff).** Tariff: ₹200 per kW of maximum demand per month + ₹5 per kWh. A consumer has MD 10 kW and uses 1500 kWh in the month.
Bill $= 200\times10 + 5\times1500 = 2000 + 7500 = ₹9500$.
Average cost per unit $= 9500/1500 = ₹6.33$. If the same 1500 kWh were used with an MD of only 5 kW (higher load factor), the bill would be 1000 + 7500 = ₹8500.

**Example 4 (why kVA charging pushes up power factor).** A load takes 80 kW. At pf 0.8 its demand is 100 kVA; at pf 1.0 it is 80 kVA. With a kVA demand charge, improving the pf to 1 cuts this part of the bill by 20 %.

## Summary of this topic

- Fixed (interest, depreciation, taxes) and semi-fixed costs are independent of energy: **standing charges**. Running costs (fuel) depend on energy.
- Cost per kWh = fixed cost ÷ ($P_m\times LF\times8760$) + running cost: falls as the load factor rises.
- Straight-line depreciation = (cost − salvage) ÷ life.
- Tariffs: simple, flat, block, **two-part (₹/kW + ₹/kWh)**, maximum demand, power factor (kVA), three-part, time-of-day.
