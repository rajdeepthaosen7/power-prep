"""Figures of GATE EE 2016, Set 1 (schemdraw, SVG). Run: python3 build_2016s1.py (writes .svg files next to this script).
Each figure follows the original paper's drawing."""
import os
import sys

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "..", "tools"))
from svgkit import drawing_svg  # noqa: E402

import schemdraw  # noqa: E402
import schemdraw.elements as elm  # noqa: E402
import schemdraw.logic as logic  # noqa: E402

OUT = os.path.dirname(os.path.abspath(__file__))


def save(name, d):
    with open(os.path.join(OUT, name + ".svg"), "w") as f:
        f.write(drawing_svg(d))


def D():
    return schemdraw.Drawing(show=False, unit=2.6)


def q11():
    """2-to-1 MUX: input 0 = NOT A, input 1 = A, select = B."""
    d = D()
    d += (m := elm.Multiplexer(pins=[elm.IcPin(name="0", side="left", slot="2/2", anchorname="in0"), elm.IcPin(name="1", side="left", slot="1/2", anchorname="in1"),
                                     elm.IcPin(name="S", side="bot"), elm.IcPin(name="Y", side="right")],
                               edgepadW=0.6, edgepadH=0.8, pinspacing=1.6).anchor("center"))
    d += elm.Line().right().at(m.Y).length(1.5).label("F", loc="right")
    d += elm.Line().down().at(m.S).length(1.0).label("B", loc="bottom")
    d += elm.Line().left().at(m.in1).length(2.6)
    d += (a := elm.Dot())
    d += elm.Line().down().at(a.center).length(1.0).label("A", loc="bottom")
    d += elm.Line().up().at(a.center).toy(m.in0)
    d += (n := logic.Not().right().length(1.4))
    d += elm.Line().right().at(n.out).tox(m.in0)
    save("q-gate2016-s1-q11", d)


def q12():
    """BJT with emitter resistor; base held at 5.3 V by a Zener fed through 4.7 kOhm from 10 V."""
    d = D()
    d += (top := elm.Dot(open=True).label("10 V", loc="top"))
    d += elm.Line().left().length(1.6)
    d += (r1 := elm.Resistor().down().label("4.7 kΩ", loc="top"))
    d += (b := elm.Dot())
    d += elm.Line().down().length(0.25)
    d += elm.Arrow().down().length(0.6).label("0.5 mA", loc="top", ofst=0.15)
    d += elm.Line().down().length(0.25)
    d += (z := elm.Zener().down().reverse().length(1.6).label("5.3 V", loc="top"))
    d += (g := elm.Dot())
    d += elm.Line().right().at(b.center).length(1.0)
    d += (q := elm.BjtNpn(circle=False).right().anchor("base"))
    d += elm.Line().up().at(q.collector).toy(top.center)
    d += elm.Line().left().tox(top.center)
    d += elm.Resistor().down().at((q.collector[0], top.center[1])).toy(q.collector).label("220 Ω", loc="bottom")
    d += elm.Resistor().down().at(q.emitter).toy(g.center).label("470 Ω", loc="bottom")
    d += elm.Line().left().tox(g.center)
    d += elm.Ground().at(g.center)
    save("q-gate2016-s1-q12", d)


def q18():
    """1 V battery, 1 ohm (I1), node with a CCCS of value I2 to ground, then 1 ohm + 1 ohm carrying I2."""
    d = D()
    d += (bat := elm.Battery().up().label("1 V", loc="bottom").reverse())
    d += elm.Line().up().length(0.4)
    d += (r1 := elm.Resistor().right().label("1 Ω", loc="bottom"))
    d += elm.CurrentLabelInline(direction="in").at(r1).label("$I_1$", loc="top")
    d += (n := elm.Dot())
    d += (r2 := elm.Resistor().right().label("1 Ω", loc="bottom"))
    d += elm.CurrentLabelInline(direction="in").at(r2).label("$I_2$", loc="top")
    d += elm.Line().right().length(0.5)
    d += (r3 := elm.Resistor().down().toy(bat.start).label("1 Ω", loc="bottom"))
    d += elm.Line().left().tox(bat.start)
    d += elm.SourceControlledI().down().at(n.center).toy(bat.start).label("$I_2$", loc="bottom")
    d += elm.Dot().at((n.center[0], bat.start[1]))
    save("q-gate2016-s1-q18", d)


if __name__ == "__main__":
    q11()
    q12()
    q18()
    print("ok")
