## The whole chapter on one page

### Formulas

| What | Formula |
|---|---|
| Flux density | $B = \Phi/A$ (T = Wb/m²) |
| Magnetising force | $H = NI/l$ (A/m) |
| Permeability | $B = \mu_0\mu_r H$, $\mu_0 = 4\pi\times10^{-7}$ H/m; susceptibility $\chi = \mu_r - 1$ |
| MMF | $F = NI$ (AT) |
| Reluctance | $S = l/(\mu_0\mu_r A)$ (AT/Wb); $\Phi = F/S$; series: $S = S_1 + S_2 + \dots$ |
| Iron + air gap | "air length" $= l_g + l_i/\mu_r$; ideal iron: $NI = B l_g/\mu_0$ |
| Faraday + Lenz | $e = -N\,d\Phi/dt = -d\lambda/dt$ |
| Moving conductor | $e = Blv\sin\theta$; rotating coil $E_m = NBA\omega$ |
| Self-inductance | $L = N\Phi/I = N^2/S = \mu_0\mu_r N^2A/l$; energy $\tfrac12LI^2$ |
| Mutual inductance | $M = N_1N_2/S$; $k = M/\sqrt{L_1L_2}$ |
| Force on conductor | $F = BIl\sin\theta$; torque on coil $NBIA$ |
| Energy density | $B^2/(2\mu)$ J/m³; pull across a gap $B^2A/(2\mu_0)$ (AC average: half) |
| Force from co-energy | $F = \partial W'/\partial x$ at constant $i$; linear: $\tfrac12 i^2\,dL/dx$ |
| EMF equation | $E = 4.44fN\Phi_m = 4.44fNB_mA$ (rms) |
| Flux from the supply | $\Phi_m \approx V/(4.44fN)$, $B_m \propto V/f$ |
| Hysteresis loss | loop area per cycle; $P_h = \eta B_m^{1.6} f V$ |
| Eddy loss | $P_e = K_e B_m^2 f^2 t^2 V$; $P_e \propto V^2$ (not $f$) |
| Separation | at constant V/f: $P_i/f = A + Bf$ |
| Ideal transformer | $V_1/V_2 = N_1/N_2 = a$; $I_1/I_2 = 1/a$; $Z' = a^2Z$ |
| Rated-load impedance | $V^2/S$ |

### One-line facts that are asked again and again

- Iron, nickel, cobalt are ferromagnetic. Diamagnetic: $\chi$ small and negative (copper, bismuth). Paramagnetic: $\chi$ small and positive (aluminium). Free space: $\mu_r = 1$.
- Above the **Curie temperature** a ferromagnet becomes **paramagnetic** (iron about 770 °C).
- **Soft** material: narrow loop, low coercivity, high permeability: cores. **Hard** material: broad loop, high coercivity, high retentivity, high Curie temperature: permanent magnets.
- Transformer core needs: high permeability, low hysteresis and low eddy losses (silicon steel / CRGO, thin laminations).
- **Ferrites** have very high resistivity: cores at high frequency (low eddy loss).
- **Magnetostriction** (small change of size on magnetising) causes transformer **hum** at twice supply frequency; reduce by lower flux density and tight clamping.
- KCL-type law holds for magnetic circuits (fluxes at a junction); no magnetic insulator, so leakage flux always exists.
- A short air gap can need more MMF than a long iron path; in a gapped core most stored energy is in the gap.
- Energy = co-energy for a linear (constant-permeability) circuit; co-energy > energy when the iron saturates.
- Torque in a machine depends on stator field, rotor field and the angle between them.
- Transformer: static, mutual induction, same frequency; does not work on DC (primary burns out).
- Oil in a transformer: insulation and cooling. LV winding next to the core.
- Core type: windings surround the core. Shell type: core surrounds the windings (less leakage).
- Staggered joints: no continuous air gap, more strength. Stepped core: less copper, less copper loss.
- Laminations reduce **eddy** loss (loss ∝ thickness²); silicon raises resistivity and narrows the loop.
- Hysteresis loss ∝ $f$, eddy loss ∝ $f^2$ (at constant $B_m$). Eddy loss ∝ $V^2$ whatever the frequency.
- Core loss is a constant loss: the core flux is almost the same at all loads.
- Magnetising current of an iron core is peaky, rich in **3rd harmonic**; of an air core, sinusoidal.
- Least inrush when switched on at the **peak** of voltage; worst at a voltage zero (flux up to about $2\Phi_m$).
- Ideal transformer: zero winding resistance, no leakage, infinite permeability (no magnetising current), no core loss, linear.

### Traps

- cm² → m²: × $10^{-4}$ (not $10^{-2}$); mm² → m²: × $10^{-6}$.
- 4.44 gives **rms** EMF from **peak** flux.
- Lower frequency at the same voltage **raises** the flux density (and the magnetising current).
- Capacitance referred across a transformer is **divided** by $a^2$, not multiplied.
- Check V/f is constant before separating hysteresis and eddy losses.
