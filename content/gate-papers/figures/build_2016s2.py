"""Figures of GATE EE 2016, Set 2 (schemdraw, SVG). Run: python3 build_2016s2.py (writes .svg files next to this script).
Each figure follows the original paper's drawing."""
import os
import sys

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "..", "tools"))
from svgkit import drawing_svg, plot, svg, text  # noqa: E402

import schemdraw  # noqa: E402
import schemdraw.elements as elm  # noqa: E402
import schemdraw.logic as logic  # noqa: E402

OUT = os.path.dirname(os.path.abspath(__file__))


def save(name, d):
    with open(os.path.join(OUT, name + ".svg"), "w") as f:
        f.write(drawing_svg(d))


def D():
    return schemdraw.Drawing(show=False, unit=2.6)


def write(name, content):
    with open(os.path.join(OUT, name + ".svg"), "w") as f:
        f.write(content)


def q2():
    """Inverting op-amp: R1 from Vin to (-); R2 parallel C from output back to (-); (+) grounded; supplies +15 V / -15 V."""
    d = D()
    d += (op := elm.Opamp(leads=True).label("+15 V", loc="top", ofst=(0.2, 0.1)).label("−15 V", loc="bottom", ofst=(0.2, -0.1)))
    d += elm.Line().left().at(op.in1).length(0.4)
    d += (n := elm.Dot())
    d += elm.Resistor().left().label("R1", loc="top")
    d += elm.Dot(open=True).label("Vin", loc="left")
    d += elm.Line().left().at(op.in2).length(0.4)
    d += elm.Ground()
    d += elm.Line().right().at(op.out).length(0.6)
    d += (o := elm.Dot())
    d += elm.Line().right().length(0.6).label("Vout", loc="right")
    d += elm.Line().up().at(n.center).length(1.6)
    d += (c1 := elm.Dot())
    d += elm.Capacitor().right().tox(o.center).label("C", loc="top")
    d += (c2 := elm.Dot())
    d += elm.Line().down().toy(o.center)
    d += elm.Line().up().at(c1.center).length(1.4)
    d += elm.Resistor().right().tox(o.center).label("R2", loc="top")
    d += elm.Line().down().toy(c2.center)
    save("q-gate2016-s2-q2", d)


def q3():
    """100 V source, XL = 10 ohm, current I, ideal transformer 1:100, secondary R = 80 k-ohm in series with XC = 40 k-ohm."""
    d = D()
    d += (src := elm.SourceSin().up().length(1.6).label("100 V", loc="bottom"))
    d += (lx := elm.Inductor2().right().label("XL = 10 Ω", loc="top"))
    d += elm.CurrentLabelInline(direction="in").at(lx)
    d += elm.Label().at((0.45, 1.25)).label("I")
    d += (t := elm.Transformer(t1=4, t2=4, core=True).anchor("p1").label("1 : 100", loc="top"))
    d += elm.Line().at(t.p2).to(src.start)
    d += elm.Resistor().right().at(t.s1).label("R = 80 kΩ", loc="top")
    d += elm.Capacitor().down().toy(t.s2).label("XC = 40 kΩ", loc="bottom")
    d += elm.Line().left().tox(t.s2)
    save("q-gate2016-s2-q3", d)


if __name__ == "__main__":
    q2()
    q3()
    print("ok")
