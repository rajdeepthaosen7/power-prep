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


def q11():
    """Source VS, series resistance (voltage VR) and a coil drawn as resistance + inductance inside a dashed box (voltage VC)."""
    d = D()
    d += (src := elm.SourceSin().up().label("VS", loc="bottom"))
    d += elm.Resistor().right().label("VR", loc="bottom")
    d += elm.Line().right().length(0.8)
    d += (r := elm.Resistor().down().length(1.3))
    d += (l := elm.Inductor2().down().length(1.3))
    d += elm.Line().left().tox(src.start)
    d += elm.Label().at((r.start[0] - 0.8, (r.start[1] + l.end[1]) / 2)).label("VC")
    x = r.start[0]
    for (a, b) in [((x - 0.45, r.start[1] - 0.05), (x + 0.45, r.start[1] - 0.05)), ((x + 0.45, r.start[1] - 0.05), (x + 0.45, l.end[1] + 0.05)),
                   ((x + 0.45, l.end[1] + 0.05), (x - 0.45, l.end[1] + 0.05)), ((x - 0.45, l.end[1] + 0.05), (x - 0.45, r.start[1] - 0.05))]:
        d += elm.Line().at(a).to(b).linestyle("--").linewidth(1)
    save("q-gate2016-s2-q11", d)


def q13():
    """One-line: S1 on bus 1; two parallel lines to bus 2 (upper: R1 at bus 1, R2 at bus 2; lower: R3 at bus 1, R4 at bus 2);
    bus 2 to bus 3 line with R5 at bus 2 and R6 at bus 3; S2 on bus 3."""
    from svgkit import svg as _svg, text as _t
    W, H = 560, 150
    out = []
    def line(x1, y1, x2, y2, w=1.6):
        out.append(f'<line x1="{x1}" y1="{y1}" x2="{x2}" y2="{y2}" stroke="currentColor" stroke-width="{w}"/>')
    def relay(x, y, name):
        out.append(f'<rect x="{x - 14}" y="{y - 6}" width="28" height="12" fill="currentColor"/>')
        out.append(_t(x, y - 14, name, size=13))
    def gen(x, y, name):
        out.append(f'<circle cx="{x}" cy="{y}" r="11" fill="none" stroke="currentColor" stroke-width="1.6"/>')
        out.append(f'<path d="M{x - 6},{y} q3,-6 6,0 t6,0" fill="none" stroke="currentColor" stroke-width="1.4"/>')
        out.append(_t(x, y - 20, name, size=13))
    y1, y2 = 55, 95
    b1, b2, b3 = 110, 290, 470
    gen(40, 75, "S1"); line(51, 75, b1, 75)
    gen(520, 75, "S2"); line(b3, 75, 509, 75)
    for x in (b1, b2, b3):
        line(x, 35, x, 115, 4)
    out.append(_t(b1, 132, "bus 1", size=12)); out.append(_t(b2, 132, "bus 2", size=12)); out.append(_t(b3, 132, "bus 3", size=12))
    line(b1, y1, b2, y1); line(b1, y2, b2, y2); line(b2, 75, b3, 75)
    relay(b1 + 35, y1, "R1"); relay(b2 - 35, y1, "R2"); relay(b1 + 35, y2, "R3"); relay(b2 - 35, y2, "R4")
    relay(b2 + 35, 75, "R5"); relay(b3 - 35, 75, "R6")
    write("q-gate2016-s2-q13", _svg(W, H, *out))


def q16():
    """Parallel plates 2 cm apart; the gap is split side by side into region A (er = 1) and region B (er = 4)."""
    from svgkit import svg as _svg, text as _t
    out = ['<line x1="20" y1="20" x2="220" y2="20" stroke="currentColor" stroke-width="2"/>',
           '<line x1="20" y1="100" x2="220" y2="100" stroke="currentColor" stroke-width="2"/>',
           '<rect x="120" y="21" width="100" height="78" fill="currentColor" opacity="0.25"/>',
           '<line x1="120" y1="20" x2="120" y2="100" stroke="currentColor" stroke-width="1" stroke-dasharray="4 3"/>',
           _t(70, 45, "A", size=15), _t(70, 72, "εr = 1", size=14), _t(170, 45, "B", size=15), _t(170, 72, "εr = 4", size=14),
           '<line x1="245" y1="22" x2="245" y2="98" stroke="currentColor" stroke-width="1.2"/>',
           '<path d="M245,20 l-4,8 h8 z M245,100 l-4,-8 h8 z" fill="currentColor"/>', _t(275, 62, "2 cm", size=14)]
    write("q-gate2016-s2-q16", _svg(310, 120, *out))


def q20():
    """Buck-boost: 24 V battery, switch S, 2 mH inductor to ground, diode (pointing back to the switch node) to the output;
    1000 uF capacitor and 36 V load, output + at the bottom."""
    d = D()
    d += (b := elm.Battery().up().reverse().label("24 V", loc="bottom"))
    d += elm.Line().up().length(0.3)
    d += elm.Switch().right().label("S", loc="top")
    d += (n := elm.Dot())
    d += elm.Diode().right().reverse().label("D", loc="top")
    d += (o := elm.Dot())
    d += elm.Line().right().length(2.4)
    d += elm.Line().down().toy(b.start)
    d += elm.Line().left().tox(b.start)
    d += elm.Inductor2().down().at(n.center).toy(b.start).label("2 mH", loc="bottom")
    d += elm.Capacitor(polar=True).up().at((o.center[0], b.start[1])).toy(o.center).label("1000 μF", loc="bottom")
    d += elm.Dot().at((n.center[0], b.start[1]))
    d += elm.Dot().at((o.center[0], b.start[1]))
    d += elm.Resistor().down().at((o.center[0] + 2.4, o.center[1])).toy(b.start).label("Load 36 V, 72 W", loc="bottom").label(("−", "", "+"), loc="top")
    save("q-gate2016-s2-q20", d)


def q21():
    """Lag network: 9 ohm series; shunt branch of 1 ohm in series with 1 F; vin on the left, vo on the right."""
    d = D()
    d += (a := elm.Dot(open=True))
    d += elm.Resistor().right().label("9 Ω", loc="top")
    d += (m := elm.Dot())
    d += elm.Line().right().length(1.5)
    d += (o := elm.Dot(open=True))
    d += elm.Resistor().down().at(m.center).label("1 Ω", loc="bottom")
    d += elm.Capacitor().down().label("1 F", loc="bottom")
    d += (g := elm.Dot())
    d += elm.Line().left().at(g.center).tox(a.center)
    d += (b := elm.Dot(open=True))
    d += elm.Line().right().at(g.center).tox(o.center)
    d += (c := elm.Dot(open=True))
    d += elm.Gap().down().at(a.center).to(b.center).label(("+", "vin", "−"), loc="top")
    d += elm.Gap().down().at(o.center).to(c.center).label(("+", "vo", "−"), loc="bottom")
    save("q-gate2016-s2-q21", d)


def q25():
    """Two elliptical electrodes side by side. On the left one: A at the far-left tip, C at the right tip (facing the other
    electrode), B at the bottom, D at the centre (inside)."""
    from svgkit import svg as _svg, text as _t
    out = ['<ellipse cx="110" cy="60" rx="85" ry="40" fill="none" stroke="currentColor" stroke-width="1.6"/>',
           '<ellipse cx="300" cy="60" rx="80" ry="40" fill="none" stroke="currentColor" stroke-width="1.6"/>']
    for x, y in [(25, 60), (195, 60), (110, 100), (110, 60)]:
        out.append(f'<circle cx="{x}" cy="{y}" r="3.2" fill="currentColor"/>')
    out += [_t(14, 65, "A", size=14, anchor="end"), _t(205, 50, "C", size=14, anchor="start"),
            _t(110, 120, "B", size=14), _t(122, 65, "D", size=14, anchor="start")]
    write("q-gate2016-s2-q25", _svg(390, 130, *out))


if __name__ == "__main__":
    q21()
    q25()
    q16()
    q20()
    q11()
    q13()
    q2()
    q3()
    print("ok")
