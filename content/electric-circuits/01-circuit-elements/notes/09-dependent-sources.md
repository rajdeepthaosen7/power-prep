## Sources controlled by another voltage or current

A **dependent** (controlled) source is a source whose value is set by a voltage or current **somewhere else in the circuit**. Transistors, op-amps, amplifiers and machines are modelled with them. They are drawn as a **diamond**.

There are four kinds:

| Name | Output | Controlled by | Rule | Unit of the gain |
|---|---|---|---|---|
| VCVS (voltage-controlled voltage source) | voltage | a voltage $v_x$ | $v = \mu\,v_x$ | none |
| CCVS (current-controlled voltage source) | voltage | a current $i_x$ | $v = r\,i_x$ | ohm (transresistance) |
| VCCS (voltage-controlled current source) | current | a voltage $v_x$ | $i = g\,v_x$ | siemens (transconductance) |
| CCCS (current-controlled current source) | current | a current $i_x$ | $i = \beta\,i_x$ | none |

A dependent **voltage** source behaves exactly like an ideal voltage source (it fixes its voltage; its current is set by the circuit), only its value is $\mu v_x$ or $r\,i_x$ instead of a fixed number. A dependent **current** source behaves like an ideal current source.

## Rules that still apply

All the connection rules of ideal sources still hold:

- A dependent voltage source in parallel with another voltage source must have **exactly the same** voltage, with the same polarity.
- A dependent current source in series with another current source must carry **exactly the same** current, in the same direction around the loop.
- A dependent voltage source in parallel with a current source, or a dependent current source in parallel with a voltage source, is always fine.

These rules decide whether a circuit with dependent sources is **realizable** (possible) at all.

**Example 1.** A 5 V independent source (+ at top) is in parallel with a VCVS $v = k\,V_s$ where $V_s$ is the 5 V source's own voltage, also drawn with + at top. Parallel ⇒ same voltage ⇒ $k\times5 = 5$ ⇒ $k = 1$ is the only allowed value. If the VCVS were drawn with + at the **bottom**, we would need $-kV_s = V_s$, i.e. $k = -1$.

**Example 2.** A 2 A independent current source and a CCCS $i = \beta I_s$ form a loop of just two elements (they are in series). Going round the loop, the current goes **up** through one and **down** through the other. If both are drawn with their arrows pointing **up**, then the CCCS current must equal **minus** the independent current: $\beta I_s = -I_s$ ⇒ $\beta = -1$. If the arrows point opposite ways (one up, one down), $\beta = +1$.

**Example 3.** An independent voltage source $V_D$ in parallel with a VCCS $i = g V_D$: always realizable, for **any** $g$. The voltage is fixed by $V_D$ and the voltage source simply absorbs the difference of currents.

## Power of a dependent source

Use the passive sign convention exactly as before. A dependent source can absorb or deliver power. It is an **active** element: it can deliver net energy, which models the energy coming from the power supply of a real transistor or op-amp.

**Example 4.** A CCCS $i = 0.25\,i_x$, with $i_x = 8$ A, pushes its current upward, and the voltage across it is 6 V with + at the top. Its current $= 2$ A leaves the + terminal, so it **delivers** $6\times2 = 12$ W (absorbed power $= -12$ W).

## One warning for later chapters

When you use superposition or find a Thevenin resistance (Chapter 4), you "switch off" **independent** sources only (voltage source → short, current source → open). **Dependent sources are never switched off**, because they are not inputs: they are part of the circuit's behaviour, like a resistor.

## Summary of this section

- Four kinds: VCVS ($\mu$), CCVS ($r$, Ω), VCCS ($g$, S), CCCS ($\beta$). Diamond symbol.
- A dependent voltage source acts like an ideal voltage source; a dependent current source like an ideal current source.
- Realizability: parallel voltage sources must match; series current sources must match (watch the arrow directions around the loop).
- Dependent sources are active elements and are never switched off in superposition or Thevenin.
