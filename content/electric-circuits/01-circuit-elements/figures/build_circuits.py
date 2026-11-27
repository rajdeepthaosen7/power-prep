"""Circuit figures for Electric Circuits, Chapter 1 (schemdraw, SVG).
Run: python3 build_circuits.py   (writes the .svg files next to this script)
Each figure follows the original paper's circuit; polarity marks are added where the paper used symbols only.
"""
import os
import sys

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "..", "..", "tools"))
from svgkit import drawing_svg  # noqa: E402

import schemdraw  # noqa: E402
import schemdraw.elements as elm  # noqa: E402
from schemdraw import flow  # noqa: E402

OUT = os.path.dirname(os.path.abspath(__file__))


def save(name, d):
    with open(os.path.join(OUT, name + ".svg"), "w") as f:
        f.write(drawing_svg(d))


def D():
    return schemdraw.Drawing(show=False, unit=2.6)


# ---------------- concept figures ----------------

def passive_sign():
    d = D()
    d += elm.Line().down().length(0.8)
    d += (e1 := elm.ResistorIEC().down().label(("+", "v", "−"), loc="bottom").label("element", loc="top", ofst=0.25))
    d += elm.Line().down().length(0.8)
    d += elm.CurrentLabelInline(direction="in").at(e1.start).label("i")
    save("c1-passive-sign", d)


def lc_symbols():
    d = D()
    d += (c := elm.Capacitor().right().label(("+", "v", "−"), loc="bottom").label("C", loc="top"))
    d += elm.CurrentLabelInline(direction="in").at(c.start).label("i")
    d += elm.Label().at((c.center[0], c.center[1] - 1.6)).label("i = C dv/dt")
    d.move(1.2, 0)
    d += (l := elm.Inductor2().right().label(("+", "v", "−"), loc="bottom").label("L", loc="top"))
    d += elm.CurrentLabelInline(direction="in").at(l.start).label("i")
    d += elm.Label().at((l.center[0], l.center[1] - 1.6)).label("v = L di/dt")
    save("c1-lc-symbols", d)


def source_symbols():
    d = D()
    x = 0
    items = [
        (elm.SourceV, "Vs", "ideal voltage\nsource"),
        (elm.SourceI, "Is", "ideal current\nsource"),
        (elm.SourceControlledV, "μvx  or  r·ix", "dependent voltage\nsource"),
        (elm.SourceControlledI, "g·vx  or  β·ix", "dependent current\nsource"),
    ]
    for cls, val, name in items:
        d += cls().up().at((x, 0)).label(val, loc="bottom", ofst=0.3)
        d += elm.Label().at((x, -1.0)).label(name)
        x += 4.2
    save("c1-source-symbols", d)


# ---------------- question figures ----------------

def gate2014_s1_6():
    d = D()
    d += elm.Line().right().length(1.2).linestyle(":")
    d += (b1 := elm.BatteryCell().right().label("100 V", loc="bottom").label("+         −", loc="top", ofst=0.05))
    d += elm.Dot()
    j = d.here
    d += (b2 := elm.BatteryCell().right().reverse().label("80 V", loc="bottom").label("−         +", loc="top", ofst=0.05))
    d += elm.Line().right().length(1.2).linestyle(":")
    d += (b3 := elm.BatteryCell().down().at(j).reverse().label("15 V", loc="bottom"))
    d += elm.Label().at((b3.center[0] - 0.45, b3.center[1] + 0.35)).label("−")
    d += elm.Label().at((b3.center[0] - 0.45, b3.center[1] - 0.45)).label("+")
    d += elm.Line().down().length(1.0).linestyle(":")
    d += elm.Label().at((b1.center[0], b1.center[1] + 1.2)).label("10 A →")
    d += elm.Label().at((b2.center[0], b2.center[1] + 1.2)).label("→ 8 A")
    save("q-gate2014-s1-6", d)


def gate2026_23():
    d = D()
    d += (v := elm.SourceV().up().label("VDC", loc="bottom"))
    d += elm.Diode().right().label("D")
    d += elm.Resistor().right().label("R")
    d += elm.Inductor2().right().label("L")
    d += elm.Capacitor().down().label("C", loc="bottom")
    d += elm.Line().left().tox(v.start)
    save("q-gate2026-23", d)


def ese2021_8():
    d = D()
    d += (bat := elm.Battery().up().length(4).label("9 V", loc="bottom").reverse())
    d += elm.Line().right().length(3)
    top_mid = d.here
    d += elm.Lamp().down().length(2).label("15 W", loc="bottom")
    d += elm.Lamp().down().length(2).label("10 W", loc="bottom")
    bot_mid = d.here
    d += elm.Line().left().tox(bat.start)
    d += elm.Line().right().at(top_mid).length(3)
    d += elm.Lamp().down().length(4).label("20 W", loc="bottom")
    d += elm.Line().left().tox(bot_mid[0])
    save("q-ese2021-8", d)


def gate2014_s1_30():
    d = D()
    d += (b := elm.Battery().up().reverse().label("300 V", loc="bottom"))
    d += (t := elm.Line().right().length(3))
    d += elm.CurrentLabel(top=True).at(t).label("I")
    d += elm.ResistorIEC().down().label("R", loc="bottom")
    d += elm.Line().left().tox(b.start)
    save("q-gate2014-s1-30", d)


def gate2013_30():
    d = D()
    d += elm.Dot(open=True)
    d += elm.Line().right().length(1)
    left = d.here
    d += elm.Line().up().length(1)
    d += elm.Capacitor().right().label("C₂")
    d += elm.Capacitor().right().label("C₃")
    d += elm.Line().down().length(1)
    right = d.here
    d += elm.Line().right().length(1)
    d += elm.Dot(open=True)
    d += elm.Line().down().at(left).length(1)
    d += elm.Capacitor().right().length(2 * 2.6).label("C₁", loc="bottom")
    d += elm.Line().up().length(1)
    save("q-gate2013-30", d)


def gate1999_cap_by():
    d = D()
    B = (0, 0)
    d += elm.Dot().at(B).label("B", loc="left")
    d += (c1 := elm.Capacitor().at(B).theta(60).length(3.2).label("Cc", loc="top"))
    R = c1.end
    d += elm.Dot().at(R).label("R", loc="top")
    d += (c2 := elm.Capacitor().at(R).theta(-60).length(3.2).label("Cc", loc="top"))
    Y = c2.end
    d += elm.Dot().at(Y).label("Y", loc="top")
    d += elm.Capacitor().at(B).to(Y).label("Cc", loc="bottom")
    d += elm.Line().at(Y).right().length(1.6)
    d += elm.Dot(open=True)
    # Cs to ground
    d += elm.Capacitor().at(B).down().length(2.4).label("Cs", loc="bottom")
    bl = d.here
    d += elm.Capacitor().at(Y).down().length(2.4).label("Cs", loc="top")
    br = d.here
    d += elm.Line().at(bl).to(br)
    d += elm.Line().at(br).right().length(2.4)
    gr = d.here
    # R tied to ground on the right
    d += elm.Line().at(R).right().tox(gr[0])
    d += elm.Line().down().toy(gr[1])
    d += elm.Line().at(((bl[0] + br[0]) / 2, bl[1])).down().length(0.6)
    d += elm.Ground()
    save("q-gate1999-cap-by", d)


def gate2012_17():
    d = D()
    d += (c1 := elm.Capacitor().up().label("C₁", loc="bottom"))
    d += elm.Line().right().length(1)
    d += elm.Switch(action="close").right().label("S   t = 0")
    d += elm.Line().right().length(1)
    d += elm.Capacitor().down().label("C₂", loc="bottom")
    d += (b := elm.Line().left().tox(c1.start))
    d += elm.Label().at(((c1.start[0] + b.start[0]) / 2, c1.center[1])).label("i(t) →")
    save("q-gate2012-17", d)


def gate2009_22():
    d = D()
    d += (b := elm.Battery().up().reverse().label("3 V", loc="bottom"))
    d += elm.Switch(action="open").right().label("S₁")
    n1 = d.here
    d += elm.Capacitor().down().label("C₁  1 F", loc="bottom")
    d += elm.Line().left().tox(b.start)
    d += elm.Switch(action="close").at(n1).right().label("S₂")
    d += elm.Capacitor().down().label("C₂  2 F", loc="bottom")
    d += elm.Line().left().tox(n1[0])
    save("q-gate2009-22", d)


def gate2017_s2_22():
    d = D()
    d += (v := elm.SourceV().up().label("10 V", loc="bottom"))
    d += elm.Line().up().length(1)
    d += elm.Capacitor().right().label("1 F")
    A = d.here
    d += elm.Resistor().down().length(2.2).label("5 Ω", loc="bottom")
    Bn = d.here
    d += elm.Resistor().down().length(2.2).label("5 Ω", loc="bottom")
    d += elm.Ground()
    d += elm.Line().at(A).right().length(3)
    d += elm.Resistor().down().length(2.2).label("5 Ω", loc="bottom")
    Cn = d.here
    d += elm.Resistor().down().length(2.2).label("5 Ω", loc="bottom")
    d += elm.Ground()
    d += elm.Resistor().at(Bn).to(Cn).label("5 Ω", loc="bottom")
    d += elm.Line().at(v.start).down().length(0.3)
    d += elm.Ground()
    save("q-gate2017s2-22", d)


def gate1998_impulse():
    d = D()
    d += (s := elm.Source().up().label("v(t)", loc="bottom"))
    d += (t := elm.Line().right().length(3))
    d += elm.CurrentLabel(top=True).at(t).label("i(t)")
    d += elm.Inductor2().down().label("L", loc="bottom")
    d += elm.Line().left().tox(s.start)
    save("q-gate1998-impulse", d)


def gate2010_7():
    d = D()
    d += (bx := flow.Box(w=3, h=2.2).label("Source"))
    d += elm.Line().at(bx.E).right().length(0.01)
    top = (bx.E[0], bx.E[1] + 0.8)
    bot = (bx.E[0], bx.E[1] - 0.8)
    d += (t := elm.Line().at(top).right().length(2.5))
    d += elm.CurrentLabel(top=True).at(t).label("i")
    d += elm.ResistorIEC().down().toy(bot[1]).label("1 Ω", loc="bottom")
    d += elm.Line().left().tox(bot[0])
    d += elm.Label().at((top[0] + 0.45, top[1] - 0.25)).label("+")
    d += elm.Label().at((top[0] + 0.45, bx.E[1])).label("v")
    d += elm.Label().at((top[0] + 0.45, bot[1] + 0.25)).label("−")
    save("q-gate2010-7", d)


def gate2025_18():
    d = D()
    d += elm.Dot(open=True).label("X", loc="left")
    d += (w := elm.Line().down().length(1.2))
    d += elm.CurrentLabel(top=False, ofst=0.3).at(w).label("IXY", loc="bottom")
    n = d.here
    d += elm.Line().left().length(1.2)
    d += elm.SourceI().down().label("1 A", loc="top")
    lb = d.here
    d += elm.Line().at(n).right().length(1.2)
    d += elm.Resistor().down().label("1 kΩ", loc="bottom")
    d += elm.Line().left().tox(lb[0])
    d += elm.Line().at(((lb[0] + d.here[0] + 2.4) / 2, lb[1])).down().length(0.01)
    mid = (n[0], lb[1])
    d += elm.Line().at(mid).down().length(1.2)
    d += elm.Dot(open=True).label("Y", loc="left")
    save("q-gate2025-18", d)


def gate2026_25():
    d = D()

    def pair(x, y, left_cls, left_lbl, right_cls, right_lbl):
        d.add(left_cls().up().at((x, y)).label(left_lbl, loc="bottom"))
        d.add(elm.Line().right().at((x, y + 2.6)).length(2.6))
        d.add(right_cls().down().reverse().at((x + 2.6, y + 2.6)).label(right_lbl, loc="bottom"))
        d.add(elm.Line().left().at((x + 2.6, y)).length(2.6))

    pair(0, 4.2, elm.SourceV, "VD", elm.SourceControlledV, "VX = k₁VD")
    pair(8, 4.2, elm.SourceV, "VD", elm.SourceControlledI, "IX = 0.5k₂VD")
    pair(0, 0, elm.SourceI, "ID", elm.SourceControlledV, "VX = k₃ID")
    pair(8, 0, elm.SourceI, "ID", elm.SourceControlledI, "IX = 3k₄ID")
    save("q-gate2026-25", d)


def gate2016_s2_23():
    d = D()
    d += (v := elm.SourceV().up().label("10 V", loc="top"))
    d += elm.Resistor().right().length(3.2).label("2 Ω")
    n = d.here
    d += elm.SourceI().down().reverse().label("5 A", loc="top")
    bn = d.here
    d += elm.Line().left().tox(v.start)
    d += elm.Line().at(n).right().length(2.4)
    d += elm.Gap().down().toy(bn[1]).label(("+", "Vout", "−"), loc="bottom")
    d += elm.Line().left().tox(bn[0])
    save("q-gate2016-s2-23", d)


def gate2008_23():
    d = D()
    d += elm.Dot().label("b", loc="left")
    d += (cs := elm.SourceI().up().label("1 A", loc="top"))
    d += elm.Dot().label("a", loc="left")
    d += (r := elm.Resistor().right().length(4).label("2 Ω"))
    d += elm.SourceV().down().label("5 V", loc="bottom")
    d += (bt := elm.Line().left().tox(cs.start))
    d += elm.Gap().at((cs.start[0] + 1.1, cs.end[1])).down().toy(cs.start[1]).label(("+", "vab", "−"), loc="bottom")
    d += elm.Label().at((r.center[0] + 0.4, cs.center[1] - 0.9)).label("← i")
    save("q-gate2008-23", d)


def gate2015_s1_28():
    d = D()
    d += (n1 := flow.Box(w=2.4, h=5).label("Network\nN1"))
    x0 = n1.E[0]
    d += (n2 := flow.Box(w=2.4, h=5).at((x0 + 6 + 1.2, n1.center[1] + 2.5)).anchor("N").label("Network\nN2"))
    y = n1.center[1]
    d += elm.Line().at((x0, y + 1.6)).right().length(1.5)
    d += elm.Resistor().right().length(3).label("3 Ω", loc="bottom").label(("−", "6 V", "+"), loc="top")
    d += elm.Line().right().length(1.5)
    d += elm.Line().at((x0, y)).right().length(1.5)
    d += elm.Resistor().right().length(3).label("2 Ω", loc="top").label(("+", "2 V", "−"), loc="bottom")
    d += elm.Line().right().length(1.5)
    d += elm.Line().at((x0, y - 1.6)).right().length(1.5)
    d += elm.SourceV().right().length(3).label(("−", "5 V", "+"), loc="bottom")
    d += elm.Line().right().length(1.5)
    save("q-gate2015-s1-28", d)


def gate2017_s1_18():
    d = D()
    d += (v := elm.SourceV().up().label("25 V", loc="top"))
    d += (w := elm.Line().right().length(1.2))
    d += elm.CurrentLabelInline(direction="in").at(w.end).label("I")
    d += elm.Resistor().right().label("R₁").label(("+", "17 V", "−"), loc="bottom")
    n = d.here
    d += (r2 := elm.Resistor().down().label(("+", "R₂", "−"), loc="bottom"))
    d += elm.Label().at((r2.center[0] - 0.9, r2.center[1])).label("14 A ↓")
    bn = d.here
    d += elm.Line().left().tox(v.start)
    d += elm.Line().at(n).right().length(3)
    d += elm.SourceControlledI().down().reverse().label("0.4I", loc="bottom")
    d += elm.Line().left().tox(bn[0])
    save("q-gate2017s1-18", d)


def gate2024_13():
    d = D()
    d += (v := elm.SourceV().up().label("10 V", loc="bottom"))
    d += elm.Resistor().right().label("α Ω")
    n = d.here
    d += elm.Resistor().down().label("1 Ω", loc="bottom")
    bn = d.here
    d += elm.Line().left().tox(v.start)
    d += elm.Resistor().at(n).right().label("2 Ω")
    d += elm.SourceI().down().reverse().label("10 A", loc="bottom")
    d += elm.Line().left().tox(bn[0])
    save("q-gate2024-13", d)


def gate2003_segment():
    d = D()
    N = (0, 0)
    d += elm.Dot().at(N)
    d += elm.SourceI().at(N).left().reverse().label("2 A", loc="top")
    d += elm.Dot().label("P", loc="left")
    d += elm.Resistor().at(N).up().label("5 Ω", loc="bottom").label(("−", "vR", "+"), loc="top")
    d += elm.Dot().label("Q", loc="right")
    d += elm.Capacitor().at(N).right().label("1 F").label(("+", "vC", "−"), loc="bottom")
    d += elm.Dot().label("R", loc="right")
    d += elm.Inductor2().at(N).down().label("2 H", loc="bottom").label(("+", "vL", "−"), loc="top")
    d += elm.Dot().label("S", loc="right")
    save("q-gate2003-segment", d)


def gate2014_s1_8():
    d = D()
    d += (cs := elm.SourceI().up().label("20 mA", loc="top"))
    d += elm.Line().right().length(2.2)
    d += elm.Capacitor().right().label("1 µF").label(("+", "vC", "−"), loc="bottom")
    d += elm.Resistor().down().label("100 Ω", loc="bottom")
    d += elm.Switch(action="close").left().label("t = 0", loc="bottom")
    d += elm.Line().left().tox(cs.start)
    # shorting switch across the current source (opens at t = 0)
    d += elm.Line().at(cs.start).left().length(2.2)
    d += elm.Switch(action="open").up().length(cs.end[1] - cs.start[1]).label("t = 0", loc="top")
    d += elm.Line().right().tox(cs.end[0])
    d += elm.Gap().at((cs.start[0] + 1.3, cs.end[1])).down().toy(cs.start[1]).label(("+", "vs", "−"), loc="bottom")
    save("q-gate2014-s1-8", d)


def ese2026_101():
    d = D()
    d += (v := elm.SourceV().up().label("P₁  20 V", loc="top"))
    d += elm.ResistorIEC().right().length(3.4).label("P₂     I = 5 A →").label(("+", "12 V", "−"), loc="bottom")
    n = d.here
    d += (p3 := elm.ResistorIEC().down().label(("+", "P₃", "−"), loc="bottom"))
    d += elm.Label().at((p3.center[0] - 1.1, p3.center[1])).label("8 V\n6 A ↓")
    bn = d.here
    d += elm.Line().left().tox(v.start)
    d += elm.Line().at(n).right().length(3)
    d += elm.SourceControlledI().down().reverse().label("P₄  0.2I", loc="bottom")
    d += elm.Line().left().tox(bn[0])
    save("q-ese2026-101", d)


def apdcl2023_43():
    """APDCL 2023 Q43: 2 H and 4 H in series, M = 1 H. Dot at the left end of the 2 H coil and at the right end of the 4 H coil
    (current enters one dot and leaves the other: series opposing), as printed in the paper."""
    d = D()
    d += elm.Dot(open=True).label("a", loc="left")
    d += (w1 := elm.Line().right().length(1.0))
    d += (l1 := elm.Inductor2().right().label("2 H", loc="bottom"))
    d += elm.Line().right().length(1.4)
    d += (l2 := elm.Inductor2().right().label("4 H", loc="bottom"))
    d += (w2 := elm.Line().right().length(1.0))
    d += elm.Dot(open=True).label("b", loc="right")
    # marks added after the chain, so they do not move the drawing position
    d += elm.CurrentLabel(top=True, ofst=0.15).at(w1).label("i")
    d += elm.CurrentLabel(top=True, ofst=0.15).at(w2).label("i")
    d += elm.Dot(radius=0.07).at((l1.start[0] + 0.12, l1.start[1] + 0.45))
    d += elm.Dot(radius=0.07).at((l2.end[0] - 0.12, l2.end[1] + 0.45))
    d += elm.Arc2(arrow="<->", k=0.35).at((l1.center[0], l1.center[1] + 0.6)).to((l2.center[0], l2.center[1] + 0.6))
    d += elm.Label().at(((l1.center[0] + l2.center[0]) / 2, l1.center[1] + 1.7)).label("M = 1 H")
    save("q-apdcl2023-43", d)


for f in [passive_sign, lc_symbols, source_symbols, gate2014_s1_6, gate2026_23, ese2021_8, gate2014_s1_30,
          gate2013_30, gate1999_cap_by, gate2012_17, gate2009_22, gate2017_s2_22, gate1998_impulse, gate2010_7,
          gate2025_18, gate2026_25, gate2016_s2_23, gate2008_23, gate2015_s1_28, gate2017_s1_18, gate2024_13,
          gate2003_segment, gate2014_s1_8, ese2026_101, apdcl2023_43]:
    try:
        f()
    except Exception as ex:  # report and continue so one bad figure does not hide the rest
        print("FAILED", f.__name__, type(ex).__name__, ex)
print("circuits written")
