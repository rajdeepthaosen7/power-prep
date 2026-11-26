## The idea in one line

A steam (thermal) power station burns coal to boil water, and the high-pressure steam spins a **turbine**, which drives the **alternator**. Most of India's electricity still comes from coal plants, so this is the plant you must know best.

![Steam power plant: the water–steam loop, and the path of the flue gas](fig:c-ps1-thermal)

## The water–steam cycle (Rankine cycle) step by step

The working fluid (water/steam) goes round a **closed loop**:

1. **Feed pump:** pumps water from the condenser into the boiler at **high pressure**. *The pressure of the whole cycle is developed by the feed pump.* Pumping water (a liquid) needs very little work compared with what the turbine gives.
2. **Boiler:** heats the high-pressure water until it becomes steam (at constant pressure).
3. **Superheater:** heats the steam further, **above its saturation temperature**, so it is completely dry.
4. **Turbine:** the steam expands from high pressure to a very low pressure and its heat energy becomes mechanical work.
5. **Condenser:** the exhaust steam is cooled by cooling water and turns back to water. This water goes back to the feed pump. The loop is complete.

> **Remember the jobs:** pump → raises **pressure**; boiler → makes **steam**; superheater → raises **temperature** of steam; turbine → gives **work**; condenser → turns steam back to **water** at low pressure.

## The main parts and what each one does

### Coal handling and the furnace

Coal is crushed and ground to fine powder (**pulverised**) in mills and blown into the furnace with air. Pulverised coal burns quickly and completely, like a gas.

### Boiler

Modern big stations use **water-tube boilers** (water inside the tubes, hot gases outside); they can work at very high pressure. In **fire-tube boilers** the hot gases go inside the tubes; they are small and low-pressure.

**Requirements of a good boiler (exam list):**

- it should give the **maximum steam with the minimum fuel**;
- it must be capable of **quick starting and loading** (to follow changes of load);
- it should have **no joints exposed to flames** (joints weaken in the fire);
- it must **burn the fuel efficiently** (exam lists also say: capable of burning low-ash coal efficiently);
- low first cost, little attention while running, all parts easy to reach for inspection and cleaning, light weight, small floor space.

### Superheater

Raises the steam temperature above saturation. Superheated (dry) steam:

- gives more work per kg of steam (higher efficiency);
- does not condense inside the turbine too early, so the blades are not eroded by water drops.

In **modern high-pressure utility boilers**, the latent heat of water becomes small at high pressure, so a large part of the heat goes into superheating. **More than 40 % of the total heat absorbed in making steam is taken up in the superheaters.**

A **reheater** heats the steam again after it has passed the high-pressure part of the turbine. This also improves efficiency and keeps the steam dry.

### Economiser

The hot **flue gases** (the gases left after burning) still carry a lot of heat. The economiser uses them to **heat the feed water** before it enters the boiler. *The feed water coming to the economiser is heated by the flue gases.* This saves fuel ("economy").

### Air preheater

After the economiser, the flue gases heat the **air** going into the furnace. Hot air helps the coal burn better.

### Electrostatic precipitator (ESP)

Indian coal has a lot of ash. Fine ash ("fly ash") is carried by the flue gases. Before the gases go up the chimney, the ESP gives the dust particles an electric charge and collects them on charged plates. *The electrostatic precipitator collects the dust (ash) particles from the flue gases.*

### Draught system and chimney

Fans move the air and gases: the **forced draught (FD) fan** pushes air into the furnace and the **induced draught (ID) fan** pulls the flue gases out and sends them up the **chimney**.

### Condenser and cooling tower

The condenser keeps a **vacuum** at the turbine exhaust. Low back pressure lets the steam expand further, so the turbine gives more work. It also recovers the pure water for re-use. The cooling water that takes heat from the condenser is cooled in a **cooling tower** (or taken from a river).

### Ash handling, water treatment

Bottom ash and fly ash are removed and disposed of. Feed water is treated to remove salts, which would form scale in the boiler tubes.

## Efficiency, heat rate and coal consumption

- **Thermal efficiency** (heat → mechanical work at the turbine shaft): about 30–40 %. Most of the heat is lost in the condenser.
- **Electrical (generator) efficiency:** about 95–98 %.
- **Overall efficiency** = thermal efficiency × electrical efficiency.

Useful conversions:

$$1\ \text{kWh} = 3600\ \text{kJ} = 860\ \text{kcal}$$

**Coal needed per hour:**

$$\text{coal (kg/h)} = \frac{P\ (\text{kW})\times 860}{\eta_{\text{overall}}\times\text{calorific value (kcal/kg)}}$$

because in one hour the station gives $P$ kWh of electrical energy, which needs $P\times860/\eta$ kcal of heat from the coal.

**Example 1.** A 50 MW station has an overall efficiency of 25 % and burns coal of calorific value 5000 kcal/kg. Coal per hour $= \dfrac{50\,000\times860}{0.25\times5000} = \dfrac{43\,000\,000}{1250} = 34\,400$ kg = 34.4 tonnes.

**Without a calculator:** $860/0.25 = 3440$; $50\,000/5000 = 10$; so $3440\times10 = 34\,400$ kg.

**Heat rate** = heat input per kWh $= 860/\eta$ kcal/kWh. A lower heat rate means a better plant. At 25 % efficiency the heat rate is 3440 kcal/kWh.

## Supercritical plants

Above the **critical point of water** (about 221 bar and 374 °C) water turns to steam **without boiling**: there is no separate water and steam. A **supercritical** boiler works above this pressure. It needs no steam drum (it is a "once-through" boiler).

Advantages:

- **higher efficiency** (about 40 % or more, against about 35 % for subcritical units), so **less coal per kWh**;
- so **less CO₂, SOx and NOx per kWh** and less ash;
- coal burns more completely; exam lists also add that low-grade coal can be used.

> **Trap:** emissions are **reduced**, never "completely eliminated". An option saying NOx is completely eliminated is false.

India's new coal units are mostly supercritical (660 MW and 800 MW units) and ultra-supercritical.

## Choosing the site (in short)

Near the **coal supply** (pit-head plants) or a good rail link; plenty of **water** for the condenser; land for ash disposal; away from towns (pollution) but not too far from the load.

## Advantages and disadvantages

| Advantages | Disadvantages |
|---|---|
| Cheaper to build than hydro or nuclear | Fuel (coal) cost is high and goes on all its life |
| Can be built near the load centre | Pollution (smoke, ash, CO₂) |
| Large unit sizes (500–800 MW) | Low efficiency (35–40 %) |
| Less space than hydro | Takes hours to start from cold: not good for sudden peaks |

Because a big coal unit is slow to start and cheap to run at full load, it is best used as a **base load** plant.

## Summary of this topic

- Feed pump builds the pressure; boiler makes steam; superheater raises its temperature; turbine gives work; condenser turns it back to water (keeps vacuum).
- Economiser: flue gas heats feed water. Air preheater: flue gas heats air. ESP: removes dust (fly ash) from flue gas.
- Coal per hour $= P\times860/(\eta\times\text{CV})$; overall η = thermal η × generator η.
- Supercritical: above 221 bar, once-through, higher efficiency, lower emissions (not zero).
