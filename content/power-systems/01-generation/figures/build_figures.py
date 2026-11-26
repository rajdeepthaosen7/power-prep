"""Figures for Power Systems, Chapter 1 (generation and economics).
Run: python3 build_figures.py   (writes the .svg files next to this script)
Block diagrams are hand-made SVG; graphs use svgkit.plot; the PV circuit uses schemdraw.
"""
import math
import os
import sys

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "..", "..", "tools"))
from svgkit import arrow_head, drawing_svg, plot, svg, text  # noqa: E402

import schemdraw  # noqa: E402
import schemdraw.elements as elm  # noqa: E402

OUT = os.path.dirname(os.path.abspath(__file__))


def save(name, content):
    with open(os.path.join(OUT, name + ".svg"), "w") as f:
        f.write(content)


def box(x, y, w, h, lines, dashed=False):
    da = ' stroke-dasharray="5 3"' if dashed else ""
    out = [f'<rect x="{x}" y="{y}" width="{w}" height="{h}" rx="6" fill="none" stroke="currentColor" stroke-width="1.4"{da}/>']
    n = len(lines)
    for k, s in enumerate(lines):
        out.append(text(x + w / 2, y + h / 2 + (k - (n - 1) / 2) * 15, s, size=12.5))
    return "\n".join(out)


def arrow(x1, y1, x2, y2, label=None, lx=0, ly=-9):
    out = [f'<line x1="{x1}" y1="{y1}" x2="{x2}" y2="{y2}" stroke="currentColor" stroke-width="1.4"/>']
    if abs(x2 - x1) >= abs(y2 - y1):
        out.append(arrow_head(x2, y2, "right" if x2 > x1 else "left"))
    else:
        out.append(arrow_head(x2, y2, "down" if y2 > y1 else "up"))
    if label:
        out.append(text((x1 + x2) / 2 + lx, (y1 + y2) / 2 + ly, label, size=11.5))
    return "\n".join(out)


def poly(points, dashed=False):
    da = ' stroke-dasharray="5 3"' if dashed else ""
    p = " ".join(f"{a},{b}" for a, b in points)
    return f'<polyline points="{p}" fill="none" stroke="currentColor" stroke-width="1.4"{da}/>'


# ---------------- concept figures ----------------

# The chain from power station to consumer (Indian voltages); drawn tall so it reads well on a phone
_rows = [["Generating station", "11-25 kV"], ["Step-up transformer"], ["Transmission", "765 / 400 / 220 kV"],
         ["Sub-transmission", "132 / 66 / 33 kV"], ["Distribution", "11 kV, then 415 / 230 V"]]
_side = ["generation", "", "transmission", "(substations)", "distribution"]
_g = []
for k, (r, side) in enumerate(zip(_rows, _side)):
    y = 8 + k * 70
    _g.append(box(20, y, 190, 46, r))
    if side:
        _g.append(text(225, y + 23, side, size=12, italic=True, anchor="start"))
    if k < len(_rows) - 1:
        _g.append(arrow(115, y + 46, 115, y + 70))
save("c-ps1-structure", svg(380, 8 + 5 * 70 - 14, *_g))

# A daily load curve with maximum demand and average load
hours = [0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24]
mw = [30, 25, 25, 40, 60, 70, 65, 60, 65, 90, 80, 50, 30]
pts = []
for k in range(len(hours) - 1):  # draw as steps (each value held for 2 h)
    pts += [(hours[k], mw[k]), (hours[k + 1], mw[k])]
avg = sum(mw[:-1]) / 12
save("c-ps1-load-curve", svg(
    340, 200,
    plot([pts], xmax=24, ymin=0, ymax=100, w=330, h=190, xlabel="hour", ylabel="load (MW)",
         xticks=[(0, ""), (6, "6"), (12, "12"), (18, "18"), (24, "24")],
         yticks=[(90, "90"), (round(avg), str(round(avg)))],
         guides=[((0, 90), (18, 90)), ((0, avg), (24, avg))],
         notes=[(19, 96, "maximum demand", "middle"), (0.5, avg + 6, "average load", "start")], label_origin=True)))

# Load duration curve made from the same day: the loads sorted from largest to smallest
srt = sorted(mw[:-1], reverse=True)
pts2 = []
for k, v in enumerate(srt):
    pts2 += [(2 * k, v), (2 * k + 2, v)]
save("c-ps1-load-duration", svg(
    340, 200,
    plot([pts2], xmax=24, ymin=0, ymax=100, w=330, h=190, xlabel="hours", ylabel="load (MW)",
         xticks=[(6, "6"), (12, "12"), (18, "18"), (24, "24")], yticks=[(90, "90"), (25, "25")],
         notes=[(9, 88, "area = energy of the day", "start")])))

# Base load and peak load: the same load curve split by a horizontal line
base = 40
save("c-ps1-base-peak", svg(
    340, 200,
    plot([pts], xmax=24, ymin=0, ymax=100, w=330, h=190, xlabel="hour", ylabel="load (MW)",
         xticks=[(6, "6"), (12, "12"), (18, "18"), (24, "24")], yticks=[(40, "40"), (90, "90")],
         guides=[((0, base), (24, base))],
         notes=[(12, 20, "base load: runs all 24 h", "middle"), (13, 80, "peak load", "middle")])))

# Steam power plant: the water-steam loop (boxes) and the flue gas path (note at the bottom); phone-friendly width
_L, _R, _W, _Hh = 10, 210, 160, 46
_y = [10, 90, 170, 250]
save("c-ps1-thermal", svg(
    380, 385,
    box(_L, _y[0], _W, _Hh, ["Coal + air"]),
    box(_R, _y[0], _W, _Hh, ["Alternator"]),
    box(_L, _y[1], _W, _Hh, ["Boiler", "(with superheater)"]),
    box(_R, _y[1], _W, _Hh, ["Turbine"]),
    box(_L, _y[2], _W, _Hh, ["Economiser"]),
    box(_R, _y[2], _W, _Hh, ["Condenser"]),
    box(_L, _y[3], _W, _Hh, ["Feed pump"]),
    box(_R, _y[3], _W, _Hh, ["Cooling tower"]),
    arrow(_L + 80, _y[0] + _Hh, _L + 80, _y[1]),
    arrow(_L + _W, _y[1] + 23, _R, _y[1] + 23, "steam"),
    arrow(_R + 80, _y[1], _R + 80, _y[0] + _Hh),
    arrow(_R + 80, _y[1] + _Hh, _R + 80, _y[2], "exhaust", lx=30, ly=0),
    poly([(_R, _y[2] + 23), (190, _y[2] + 23), (190, _y[3] + 23)]),
    arrow(190, _y[3] + 23, _L + _W, _y[3] + 23),
    arrow(_L + 80, _y[3], _L + 80, _y[2] + _Hh, "water", lx=26, ly=0),
    arrow(_L + 80, _y[2], _L + 80, _y[1] + _Hh),
    arrow(_R + 60, _y[3], _R + 60, _y[2] + _Hh),
    arrow(_R + 100, _y[2] + _Hh, _R + 100, _y[3]),
    text(190, 322, "Flue gas: furnace → superheater → economiser", size=12),
    text(190, 342, "→ air preheater → electrostatic precipitator", size=12),
    text(190, 362, "(ESP) → chimney", size=12),
))

# Hydro plant: water path from the reservoir to the tail race; phone-friendly
_hy = [["Reservoir (behind dam)"], ["Penstock (pipe)"], ["Turbine → alternator"], ["Draft tube"], ["Tail race (river below)"]]
_g = []
for k, r in enumerate(_hy):
    y = 8 + k * 64
    _g.append(box(20, y, 190, 42, r))
    if k < len(_hy) - 1:
        _g.append(arrow(115, y + 42, 115, y + 64))
_g.append(box(250, 50, 110, 42, ["Surge tank"], dashed=True))
_g.append(poly([(250, 71), (230, 71), (230, 93), (210, 93)]))
_g.append(text(190, 8 + 5 * 64 + 6, "H = head: height of the water level above the turbine", size=12))
save("c-ps1-hydro", svg(380, 8 + 5 * 64 + 22, *_g))

# Typical I-V curve of a PV cell with the maximum power point
def iv(v, isc=8.0, voc=0.6):
    return isc * (1 - (math.exp(v / 0.07) - 1) / (math.exp(voc / 0.07) - 1))


curve = [(0.6 * k / 80, max(0.0, iv(0.6 * k / 80))) for k in range(81)]
vm = max(curve, key=lambda p: p[0] * p[1])
save("c-ps1-pv-iv", svg(
    340, 200,
    plot([curve], xmax=0.7, ymin=0, ymax=10, w=330, h=190, xlabel="V", ylabel="I",
         xticks=[(vm[0], "Vmp"), (0.6, "Voc")], yticks=[(8, "Isc"), (vm[1], "Imp")],
         guides=[((0, vm[1]), (vm[0], vm[1])), ((vm[0], 0), (vm[0], vm[1]))],
         notes=[(vm[0] + 0.02, vm[1] + 0.9, "MPP", "start")])))


# ---------------- question figures ----------------

def pv_equivalent():
    """ESE 2021 Q104: photovoltaic cell equivalent circuit (current source, diode, Rsh, series Rs)."""
    d = schemdraw.Drawing(show=False, unit=2.4)
    d += (src := elm.SourceI().up().label("IL", loc="bottom"))
    d += elm.Line().right().length(2)
    d += (top1 := elm.Dot())
    d += elm.Diode().down().label("ID", loc="bottom")
    d += elm.Line().left().length(2)
    d += elm.Line().at(top1.center).right().length(2)
    d += (top2 := elm.Dot())
    d += elm.Resistor().down().label("Rsh", loc="bottom")
    d += (bot2 := elm.Dot())
    d += elm.Line().left().length(2)
    d += elm.Resistor().at(top2.center).right().label("Rs")
    d += (p := elm.Dot(open=True).label("+", loc="right"))
    d += elm.Line().at(bot2.center).right().length(2.4)
    d += (n := elm.Dot(open=True).label("−", loc="right"))
    d += elm.Label().at(((p.center[0] + 0.9), (p.center[1] + n.center[1]) / 2)).label("V (cell output)")
    save("q-ese2021-104", drawing_svg(d))
    save("c-ps1-pv-circuit", drawing_svg(d))  # same drawing for the notes (figure keys must be unique)


pv_equivalent()
print("figures written")
