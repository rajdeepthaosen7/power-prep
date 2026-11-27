"""Figures of GATE EE 2016, Set 1 (schemdraw, SVG). Run: python3 build_2016s1.py (writes .svg files next to this script).
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


def write(name, content):
    with open(os.path.join(OUT, name + ".svg"), "w") as f:
        f.write(content)


def q22():
    """(a) buck converter: Vg, switch M, diode D, L (vL marked + to -), C, R with Vo. (b) inductor voltage waveform."""
    d = D()
    d += (vg := elm.SourceV().up().label("Vg", loc="bottom"))
    d += elm.Line().up().length(0.3)
    d += (m := elm.Switch().right().label("M", loc="top"))
    d += (n := elm.Dot())
    d += (lx := elm.Inductor2().right().label(("+", "vL", "−"), loc="top"))
    d += (o := elm.Dot())
    d += elm.Line().right().length(1.2)
    d += (c := elm.Capacitor().down().toy(vg.start).label("C", loc="top"))
    d += elm.Line().right().at(o.center).length(2.6)
    d += elm.Resistor().down().toy(vg.start).label("R", loc="top").label(("+", "Vo", "−"), loc="bottom", ofst=0.5)
    d += elm.Line().left().tox(vg.start)
    d += elm.Diode().up().at((n.center[0], vg.start[1])).toy(n.center).label("D", loc="bottom")
    save("q-gate2016-s1-q22a", d)
    g = plot([[(0, 30), (2, 30), (2, -20), (3.4, -20), (3.4, 30), (4.4, 30)]], xmax=4.6, ymin=-26, ymax=36, w=300, h=190,
             ylabel="vL", xticks=[], yticks=[(30, "30 V"), (-20, "−20 V")],
             guides=[((0, -20), (2, -20)), ((3.4, 0), (3.4, -24))],
             notes=[(1.0, 8, "switch ON", "middle"), (2.7, 8, "switch OFF", "middle"), (1.7, -24, "one switching period Ts", "middle")], left=56)
    write("q-gate2016-s1-q22b", svg(300, 190, g))


def q23():
    """(a) IGBT S with anti-parallel diode D, 100 A entering at the bottom (emitter) terminal. (b), (c) V-I lines."""
    d = D()
    d += (s_ := elm.IgbtN().label("S", loc="left"))
    c, e = s_.collector, s_.emitter
    x = c[0] + 1.6
    d += elm.Line().at(c).to((x, c[1]))
    d += elm.Line().at(e).to((x, e[1]))
    d += elm.Diode().at((x, e[1])).to((x, c[1])).label("D", loc="bottom")
    d += elm.Line().at(c).to((c[0], c[1] + 1.0))
    d += elm.Dot(open=True).at((c[0], c[1] + 1.0))
    d += elm.Line().at(e).to((e[0], e[1] - 1.4))
    d += elm.Dot(open=True).at((e[0], e[1] - 1.4))
    d += elm.Arrow().at((e[0] - 0.55, e[1] - 1.5)).to((e[0] - 0.55, e[1] - 0.5)).label("100 A", loc="top")
    save("q-gate2016-s1-q23a", d)
    gb = plot([[(0, 0), (1, 0), (2.6, 80)]], xmax=3, ymin=0, ymax=90, w=230, h=160, xlabel="Vs (V)", ylabel="Is (A)",
              xticks=[(1, "1 V")], notes=[(2.0, 40, "slope dV/dI = 0.02 Ω", "start")])
    gc = plot([[(0, 0), (0.7, 0), (1.6, 80)]], xmax=2, ymin=0, ymax=90, w=230, h=160, xlabel="VD (V)", ylabel="ID (A)",
              xticks=[(0.7, "0.7 V")], notes=[(1.25, 40, "slope dV/dI = 0.01 Ω", "start")], ox=250)
    write("q-gate2016-s1-q23b", svg(500, 185, gb, gc, text(115, 178, "IGBT (S)", size=12), text(365, 178, "Diode (D)", size=12)))


def q24():
    """Options of Q24: armature-conductor current shapes."""
    for lab, series, yt, notes in [
        ("a", [[(0, 10), (4, 10)]], [(10, "10 A")], []),
        ("b", [[(0, 40), (4, 40)]], [(40, "40 A")], []),
        ("c", [[(0, 0), (0.25, 10), (1.75, 10), (2.25, -10), (3.75, -10), (4, 0)]], [(10, "10 A"), (-10, "−10 A")],
         []),
        ("d", [[(i / 20, 10 * __import__("math").sin(3.14159 * i / 40)) for i in range(81)]], [(10, "10 A"), (-10, "−10 A")],
         []),
    ]:
        ymin = 0 if lab in "ab" else -13
        ymax = 46 if lab == "b" else 13
        xt = [] if lab in "ab" else [(2, "25 ms"), (4, "50 ms")]
        g = plot(series, xmax=4.3, ymin=ymin, ymax=ymax, w=230, h=120, ylabel="I", yticks=yt, xticks=xt, notes=notes, left=48)
        write(f"q-gate2016-s1-q24{lab}", svg(230, 120, g))


def q25():
    """Ideal transformer n:1, inductor L on port 2; port 1 on the left."""
    d = D()
    d += (t := elm.Transformer(t1=4, t2=4, core=True).label("n : 1", loc="top"))
    d += elm.Line().left().at(t.p1).length(1.4)
    d += elm.Dot(open=True)
    d += elm.Line().left().at(t.p2).length(1.4)
    d += elm.Dot(open=True)
    d += elm.Label().at(((t.p1[0] + t.p2[0]) / 2 - 1.6, (t.p1[1] + t.p2[1]) / 2)).label("Port 1")
    d += elm.Line().right().at(t.s1).length(2.4)
    d += elm.Inductor2().down().toy(t.s2).label("L", loc="bottom")
    d += elm.Line().left().tox(t.s2)
    d += elm.Label().at(((t.s1[0] + t.s2[0]) / 2 + 1.05, (t.s1[1] + t.s2[1]) / 2)).label("Port 2")
    save("q-gate2016-s1-q25", d)


if __name__ == "__main__":
    q25()
    q11()
    q12()
    q18()
    q22()
    q23()
    q24()
    print("ok")
