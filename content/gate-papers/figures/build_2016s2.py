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


def q27():
    """Non-inverting summer: V3 via 1 ohm to the (-) input, 9 ohm feedback; V1 via 1 ohm and V2 via 4 ohm to the (+) input."""
    d = D()
    op = elm.Opamp(leads=True).right()
    d += op
    d += elm.Line().left().at(op.in1).length(0.6)
    d += (nm := elm.Dot())
    d += elm.Resistor().left().label("1 Ω", loc="top")
    d += elm.Line().left().length(0.3).label("V3", loc="left")
    d += elm.Line().up().at(nm.center).length(1.4)
    d += elm.Resistor().right().tox(op.out[0] + 0.6).label("9 Ω", loc="top")
    d += elm.Line().down().toy(op.out)
    d += elm.Line().left().tox(op.out)
    d += elm.Dot().at((op.out[0] + 0.6, op.out[1]))
    d += elm.Line().right().at((op.out[0] + 0.6, op.out[1])).length(0.6).label("Vout", loc="right")
    d += elm.Line().left().at(op.in2).length(1.2)
    d += (np_ := elm.Dot())
    d += elm.Resistor().left().label("1 Ω", loc="top")
    d += elm.Line().left().length(0.3).label("V1", loc="left")
    d += elm.Line().down().at(np_.center).length(1.3)
    d += elm.Resistor().left().label("4 Ω", loc="bottom")
    d += elm.Line().left().length(0.3).label("V2", loc="left")
    save("q-gate2016-s2-q27", d)


def q28():
    """Spectra X1 (two-step staircase: low level from -B1 to B1, higher level from -B1/2 to B1/2) and X2 (triangle from -B2 to B2);
    then x1(t) and x2(t) into a multiplier, then a block h(t) = e^(-2|t|), output y(t)."""
    from svgkit import arrow_head as ah
    L = 'stroke="currentColor" fill="none" stroke-width="1.5"'
    o = []
    # X1
    o += [f'<line x1="10" y1="90" x2="210" y2="90" {L}/>', ah(210, 90, "right"), f'<line x1="110" y1="95" x2="110" y2="15" {L}/>',
          ah(110, 15, "up"), f'<path d="M40,90 V65 H75 V45 H145 V65 H180 V90" {L}/>',
          text(110, 10, "X1(ω)", size=13), text(214, 94, "ω", size=13, anchor="start"),
          text(40, 106, "−B1", size=12), text(75, 106, "−B1/2", size=12), text(145, 106, "B1/2", size=12), text(180, 106, "B1", size=12)]
    # X2
    o += [f'<line x1="250" y1="90" x2="420" y2="90" {L}/>', ah(420, 90, "right"), f'<line x1="335" y1="95" x2="335" y2="15" {L}/>',
          ah(335, 15, "up"), f'<path d="M290,90 L335,35 L380,90" {L}/>', text(335, 10, "X2(ω)", size=13),
          text(424, 94, "ω", size=13, anchor="start"), text(290, 106, "−B2", size=12), text(380, 106, "B2", size=12)]
    # block diagram
    y = 175
    o += [text(20, 140, "x1(t)", size=13, anchor="start"), f'<path d="M58,136 H150 V{y-16}" {L}/>', ah(150, y - 16, "down"),
          text(20, 222, "x2(t)", size=13, anchor="start"), f'<path d="M58,218 H150 V{y+16}" {L}/>', ah(150, y + 16, "up"),
          f'<circle cx="150" cy="{y}" r="16" {L}/>', f'<path d="M139,{y-11} L161,{y+11} M161,{y-11} L139,{y+11}" {L}/>',
          f'<line x1="166" y1="{y}" x2="228" y2="{y}" {L}/>', ah(228, y, "right"),
          f'<rect x="230" y="{y-25}" width="120" height="50" {L}/>', text(290, y + 5, "h(t) = e^(−2|t|)", size=13),
          f'<line x1="350" y1="{y}" x2="415" y2="{y}" {L}/>', ah(415, y, "right"), text(382, y - 8, "y(t)", size=13)]
    write("q-gate2016-s2-q28", svg(440, 235, *o))


def q34():
    """Vs, series 2 ohm (V1 across it, + on the source side, current Is), node N; from N to ground: 3 ohm, a dependent current
    source 4V1 pointing up (into N), and 2 ohm in series with 4 ohm."""
    d = D()
    d += (vs := elm.SourceV().up().label("Vs", loc="bottom"))
    d += (lw := elm.Line().right().length(1.0))
    d += elm.CurrentLabelInline(direction="in", ofst=-0.5).at(lw).label("Is")
    d += elm.Resistor().right().label(("+", "V1", "−"), loc="top").label("2 Ω", loc="bottom")
    d += (n := elm.Dot())
    d += elm.Line().right().length(1.4)
    d += (n2 := elm.Dot())
    d += elm.Resistor().right().label("2 Ω", loc="top")
    d += elm.Resistor().down().toy(vs.start).label("4 Ω", loc="bottom")
    d += elm.Line().left().tox(vs.start)
    d += elm.Resistor().down().at(n.center).toy(vs.start).label("3 Ω", loc="top")
    d += elm.SourceControlledI().up().at((n2.center[0], vs.start[1])).toy(n2.center).label("4V1", loc="bottom")
    d += elm.Dot().at((n.center[0], vs.start[1]))
    d += elm.Dot().at((n2.center[0], vs.start[1]))
    save("q-gate2016-s2-q34", d)


def q35():
    """20 V source with 10 ohm in series feeding port 1 (V1, I1 in) of a two-port [Z]; port 2 (V2, I2 in) loaded by RL."""
    d = D()
    d += (src := elm.SourceV().up().label("20 V", loc="bottom"))
    d += elm.Resistor().right().label("10 Ω", loc="top")
    d += (w1 := elm.Line().right().length(1.0))
    d += elm.CurrentLabelInline(direction="in", ofst=-0.3).at(w1).label("I1")
    x0, x1 = w1.end[0], w1.end[0] + 2.6
    yt, yb = src.end[1], src.start[1]
    d += elm.Line().at((x0, yt + 0.3)).to((x1, yt + 0.3))
    d += elm.Line().at((x1, yt + 0.3)).to((x1, yb - 0.3))
    d += elm.Line().at((x1, yb - 0.3)).to((x0, yb - 0.3))
    d += elm.Line().at((x0, yb - 0.3)).to((x0, yt + 0.3))
    d += elm.Label().at(((x0 + x1) / 2, (yt + yb) / 2)).label("[Z]", fontsize=18)
    d += elm.Line().left().at((x0, yb)).tox(src.start)
    ym = (yt + yb) / 2
    for x, nm in ((x0 + 0.4, "V1"), (x1 - 0.4, "V2")):
        d += elm.Label().at((x, yt - 0.35)).label("+")
        d += elm.Label().at((x, ym)).label(nm)
        d += elm.Label().at((x, yb + 0.35)).label("−")
    d += (w2 := elm.Line().at((x1 + 1.2, yt)).to((x1, yt)))
    d += elm.CurrentLabelInline(direction="in", ofst=-0.4).at(w2).label("I2")
    d += elm.Line().right().at((x1 + 1.2, yt)).length(0.4)
    d += elm.Resistor().down().toy(yb).label("RL", loc="bottom")
    d += elm.Line().left().tox(x1)
    save("q-gate2016-s2-q35", d)


def q36():
    """Delta of three inductors L; inside it a star of three capacitors C from the centre to the three corners; three supply lines."""
    import math
    d = D()
    top, bl, br = (0.0, 4.2), (-2.4, 0.0), (2.4, 0.0)
    cen = (0.0, 1.4)
    d += elm.Inductor2(loops=3).endpoints(bl, top).label("L", loc="top")
    d += elm.Inductor2(loops=3).endpoints(top, br).label("L", loc="top")
    d += elm.Inductor2(loops=3).endpoints(br, bl).label("L", loc="top")
    for p_, lab in ((top, "C"), (bl, "C"), (br, "C")):
        d += elm.Capacitor().endpoints(cen, p_).label(lab, loc="bottom")
    d += elm.Dot().at(cen)
    for p_ in (top, bl, br):
        d += elm.Dot().at(p_)
    d += elm.Line().at(top).to((-5.0, top[1]))
    d += elm.Line().at(bl).to((-5.0, bl[1]))
    d += elm.Line().at(br).to((br[0], -0.9))
    d += elm.Line().at((br[0], -0.9)).to((-5.0, -0.9))
    save("q-gate2016-s2-q36", d)


def q37():
    """5 uF capacitor charged to 4 V, switch S1 (closes at t = 0) and 5 ohm resistor in a single loop."""
    d = D()
    d += (c := elm.Capacitor().up().label("5 μF", loc="bottom").label("4 V", loc="top"))
    d += elm.Switch(action="close").right().label("S1", loc="top")
    d += elm.Resistor().down().toy(c.start).label("5 Ω", loc="bottom")
    d += elm.Line().left().tox(c.start)
    save("q-gate2016-s2-q37", d)


def q38():
    """Single line diagram: generator (internal bus) - j0.2 - HV bus; two parallel paths, each a transformer j0.1 and a line j0.5,
    to the infinite bus (Line 1 on top, Line 2 below)."""
    L = 'stroke="currentColor" fill="none" stroke-width="1.5"'
    o = []
    o += [f'<line x1="70" y1="30" x2="70" y2="170" {L}/>', text(8, 22, "Generator internal bus", size=12, anchor="start"),
          f'<circle cx="40" cy="100" r="15" {L}/>', '<path d="M30,100 q5,-9 10,0 t10,0" stroke="currentColor" fill="none" stroke-width="1.4"/>',
          f'<line x1="55" y1="100" x2="70" y2="100" {L}/>', f'<line x1="70" y1="100" x2="105" y2="100" {L}/>']
    coil = "".join(f"a6,6 0 0 1 12,0 " for _ in range(4))
    o += [f'<path d="M105,100 {coil}" {L}/>', text(129, 88, "j0.2", size=12), f'<line x1="153" y1="100" x2="190" y2="100" {L}/>',
          '<line x1="190" y1="30" x2="190" y2="170" stroke="currentColor" stroke-width="4"/>']
    for y, nm in ((60, "Line 1"), (140, "Line 2")):
        o += [f'<line x1="190" y1="{y}" x2="215" y2="{y}" {L}/>', f'<circle cx="227" cy="{y}" r="12" {L}/>',
              f'<circle cx="243" cy="{y}" r="12" {L}/>', text(235, y - 18 if y < 100 else y + 28, "j0.1", size=12),
              f'<line x1="255" y1="{y}" x2="400" y2="{y}" {L}/>', text(328, y - 7, "j0.5", size=12), text(328, y + 16, nm, size=12)]
    o += [f'<line x1="400" y1="30" x2="400" y2="170" stroke="currentColor" stroke-width="3"/>', text(432, 22, "Infinite bus", size=12, anchor="end")]
    o += [f'<line x1="400" y1="{y}" x2="410" y2="{y-8}" stroke="currentColor" stroke-width="1"/>' for y in range(38, 175, 10)]
    write("q-gate2016-s2-q38", svg(440, 185, *o))


def q45():
    """Single-phase full thyristor bridge: left leg T1 (top) / T4 (bottom), right leg T3 (top) / T2 (bottom), source vm between the
    leg midpoints. Output: top rail through L, R = 20 ohm and E = 800 V (minus at the top, plus at the bottom) back to the bottom
    rail; IL flows from the top rail into the load."""
    d = D()
    xl, xr, yt, yb = 0.0, 3.0, 4.0, 0.0
    ym = (yt + yb) / 2
    for x, lo, hi in ((xl, "T4", "T1"), (xr, "T2", "T3")):
        d += elm.SCR().endpoints((x, yb), (x, ym)).label(lo, loc="top" if x == xl else "bottom")
        d += elm.SCR().endpoints((x, ym), (x, yt)).label(hi, loc="top" if x == xl else "bottom")
        d += elm.Dot().at((x, ym))
    d += elm.Line().endpoints((xl, yt), (xr, yt))
    d += elm.Line().endpoints((xl, yb), (xr, yb))
    d += elm.SourceSin().endpoints((xl, ym), (xr, ym)).label("vm", loc="top")
    d += elm.Dot().at((xr, yt))
    d += elm.Dot().at((xr, yb))
    d += elm.Line().endpoints((xr, yt), (xr, yt + 1.0))
    d += elm.Line().right().length(3.4)
    d += elm.Inductor2().down().length(1.6).label("L", loc="bottom")
    d += elm.Resistor().down().length(2.0).label("R = 20 Ω", loc="bottom")
    d += elm.Battery().down().reverse().length(1.4).label("E = 800 V", loc="bottom").label(("−", "", "+"), loc="top")
    d += elm.Line().down().toy(yb - 1.0)
    d += elm.Line().left().tox(xr)
    d += elm.Line().up().toy(yb)
    d += elm.Arrow().at((xr + 0.9, yt + 1.4)).to((xr + 2.3, yt + 1.4)).label("IL", loc="top")
    save("q-gate2016-s2-q45", d)


def q46():
    """Three-phase VSI: 600 V battery, three legs of IGBT + anti-parallel diode (upper and lower); the leg midpoints a, b, c feed a
    delta of three 30 ohm resistors."""
    d = D()
    yt, yb = 5.2, 0.0
    ym = (yt + yb) / 2
    d += elm.Battery().up().at((0, yb)).to((0, yt)).reverse().label("600 V", loc="bottom")
    legs = (2.2, 5.0, 7.8)
    d += elm.Line().at((0, yt)).to((legs[-1] + 1.0, yt))
    d += elm.Line().at((0, yb)).to((legs[-1] + 1.0, yb))
    for x in legs:
        for y_hi, y_lo in ((yt, ym), (ym, yb)):
            q = elm.IgbtN().theta(0).anchor("collector").at((x, y_hi - 0.5))
            d += q
            d += elm.Line().at((x, y_hi)).to(q.collector)
            d += elm.Line().at(q.emitter).to((x, y_lo))
            d += elm.Line().at((x, y_hi)).to((x + 1.0, y_hi))
            d += elm.Diode().at((x + 1.0, y_lo)).to((x + 1.0, y_hi))
            d += elm.Line().at((x + 1.0, y_lo)).to((x, y_lo))
        d += elm.Dot().at((x, ym))
    xa, xb, xc = legs
    p1, p2, p3 = (14.4, ym + 2.4), (12.4, ym - 0.9), (16.4, ym - 0.9)
    d += elm.Line().at((xa, ym)).to((xa, ym + 0.35))
    d += elm.Line().at((xa, ym + 0.35)).to((10.4, ym + 0.35))
    d += elm.Line().to((10.4, p1[1]))
    d += elm.Line().to(p1)
    d += elm.Line().at((xb, ym)).to((p2[0], ym))
    d += elm.Line().to(p2)
    d += elm.Line().at((xc, ym)).to((xc, ym - 0.35))
    d += elm.Line().at((xc, ym - 0.35)).to((11.0, ym - 0.35))
    d += elm.Line().to((11.0, ym - 2.4))
    d += elm.Line().to((p3[0], ym - 2.4))
    d += elm.Line().to(p3)
    d += elm.Resistor().endpoints(p2, p1).label("30 Ω", loc="top")
    d += elm.Resistor().endpoints(p1, p3).label("30 Ω", loc="top")
    d += elm.Resistor().endpoints(p3, p2).label("30 Ω", loc="top")
    for p_ in (p1, p2, p3):
        d += elm.Dot().at(p_)
    save("q-gate2016-s2-q46", d)


def _sw(d, x, y_hi, y_lo, name=None):
    """IGBT with anti-parallel diode between (x, y_hi) and (x, y_lo)."""
    q = elm.IgbtN().theta(0).anchor("collector").at((x, y_hi - 0.5))
    if name:
        q = q.label(name, loc="left")
    d += q
    d += elm.Line().at((x, y_hi)).to(q.collector)
    d += elm.Line().at(q.emitter).to((x, y_lo))
    d += elm.Line().at((x, y_hi)).to((x + 1.0, y_hi))
    d += elm.Diode().at((x + 1.0, y_lo)).to((x + 1.0, y_hi))
    d += elm.Line().at((x + 1.0, y_lo)).to((x, y_lo))


def q47():
    """Boost: 360 V source, 10 mH inductor, switch S (IGBT + anti-parallel diode) to ground, diode, 1 mF capacitor, load 400 V."""
    d = D()
    yt, yb = 3.0, 0.0
    d += elm.Battery().endpoints((0, yb), (0, yt)).reverse().label("360 V", loc="bottom")
    d += elm.Inductor2().endpoints((0, yt), (2.6, yt)).label("10 mH", loc="top")
    d += elm.Dot().at((2.6, yt))
    _sw(d, 2.6, yt, yb, "S")
    d += elm.Diode().endpoints((2.6, yt), (5.6, yt))
    d += elm.Capacitor().endpoints((5.6, yt), (5.6, yb)).label("1 mF", loc="bottom")
    d += elm.Dot().at((5.6, yt))
    d += elm.Line().endpoints((5.6, yt), (7.4, yt))
    d += elm.Resistor().endpoints((7.4, yt), (7.4, yb)).label("Load 400 V", loc="bottom").label(("+", "", "−"), loc="top")
    d += elm.Line().endpoints((7.4, yb), (0, yb))
    d += elm.Dot().at((2.6, yb))
    d += elm.Dot().at((5.6, yb))
    save("q-gate2016-s2-q47", d)


def q48():
    """Single-phase VSC: 220 V ac source, 5 mH (Xs) inductor, current Is; full bridge of four IGBTs with anti-parallel diodes;
    dc side 1 mF capacitor and a 400 V battery. Beside it, the phasor diagram: Is along Vs, IsXs drawn down from the tip of Vs,
    VC1 from the origin to the tip of IsXs, angle delta between Vs and VC1."""
    d = D()
    yt, yb = 5.2, 0.0
    ym = (yt + yb) / 2
    xa, xb = 3.0, 5.6
    for x in (xa, xb):
        _sw(d, x, yt, ym)
        _sw(d, x, ym, yb)
        d += elm.Dot().at((x, ym))
    d += elm.Line().endpoints((xa, yt), (9.6, yt))
    d += elm.Line().endpoints((xa, yb), (9.6, yb))
    d += elm.Capacitor().endpoints((7.4, yt), (7.4, yb)).label("1 mF", loc="bottom")
    d += elm.Battery().endpoints((9.6, yb), (9.6, yt)).reverse().label("400 V", loc="bottom")
    d += elm.SourceSin().endpoints((-1.6, ym - 1.6), (-1.6, ym + 0.6)).label("220 V ac", loc="top")
    d += elm.Line().endpoints((-1.6, ym + 0.6), (-1.6, ym + 1.0))
    d += (w := elm.Line().endpoints((-1.6, ym + 1.0), (-0.4, ym + 1.0)))
    d += elm.CurrentLabelInline(direction="in", ofst=-0.2).at(w).label("Is")
    d += elm.Inductor2().endpoints((-0.4, ym + 1.0), (1.8, ym + 1.0)).label("5 mH (Xs)", loc="top")
    d += elm.Line().endpoints((1.8, ym + 1.0), (1.8, ym))
    d += elm.Line().endpoints((1.8, ym), (xa, ym))
    d += elm.Line().endpoints((-1.6, ym - 1.6), (-1.6, ym - 2.0))
    d += elm.Line().endpoints((-1.6, ym - 2.0), (4.2, ym - 2.0))
    d += elm.Line().endpoints((4.2, ym - 2.0), (4.2, ym - 0.35))
    d += elm.Line().endpoints((4.2, ym - 0.35), (xb, ym - 0.35))
    d += elm.Line().endpoints((xb, ym - 0.35), (xb, ym))
    save("q-gate2016-s2-q48a", d)
    L = 'stroke="currentColor" fill="none" stroke-width="1.6"'
    from svgkit import arrow_head as ah
    o = [f'<line x1="20" y1="40" x2="250" y2="40" {L}/>', ah(250, 40, "right"), text(258, 45, "Vs", size=15, anchor="start"),
         f'<line x1="20" y1="40" x2="120" y2="40" stroke="currentColor" stroke-width="3"/>', ah(124, 40, "right", size=9),
         text(110, 30, "Is", size=15),
         f'<line x1="250" y1="40" x2="250" y2="100" {L}/>', ah(250, 100, "down"), text(258, 80, "IsXs", size=15, anchor="start"),
         f'<line x1="20" y1="40" x2="250" y2="100" {L}/>', ah(250, 100, "right"), text(130, 100, "VC1", size=15),
         '<path d="M75,40 A55,55 0 0 1 73.2,54" stroke="currentColor" fill="none" stroke-width="1"/>', text(88, 56, "δ", size=14)]
    write("q-gate2016-s2-q48b", svg(310, 115, *o))


def q53():
    """Two generators in parallel on one bus: the upper one star-connected with its neutral not grounded, the lower one star with
    its neutral grounded."""
    L = 'stroke="currentColor" fill="none" stroke-width="1.6"'
    o = []
    for y, grounded in ((52, False), (122, True)):
        o += [f'<path d="M30,{y-14} L40,{y-2} L50,{y-14} M40,{y-2} V{y+12}" {L}/>',
              f'<circle cx="90" cy="{y}" r="20" {L}/>', f'<path d="M77,{y} q6.5,-12 13,0 t13,0" {L}/>',
              f'<line x1="110" y1="{y}" x2="190" y2="{y}" {L}/>']
        if grounded:
            o += [f'<path d="M40,{y+12} V{y+22} M31,{y+22} H49 M34,{y+26} H46 M37,{y+30} H43" {L}/>']
    o += [f'<line x1="190" y1="28" x2="190" y2="150" stroke="currentColor" stroke-width="2.5"/>',
          text(62, 20, "neutral not grounded", size=11, anchor="start"), text(62, 166, "neutral grounded", size=11, anchor="start")]
    write("q-gate2016-s2-q53", svg(260, 175, *o))


def q55():
    """Radial field B = 1 T pointing outward from the z-axis (shown as two dotted rings of outward arrows); a 1 m conductor parallel
    to the z-axis at 1 m from it."""
    import math
    L = 'stroke="currentColor" fill="none" stroke-width="1.4"'
    from svgkit import arrow_head as ah
    o = [f'<line x1="160" y1="210" x2="160" y2="18" {L}/>', ah(160, 14, "up"), text(172, 20, "z", size=14, anchor="start")]
    for cy in (75, 160):
        o.append(f'<ellipse cx="160" cy="{cy}" rx="110" ry="28" fill="none" stroke="currentColor" stroke-width="1.2" stroke-dasharray="3 3"/>')
        for k in range(8):
            a = 2 * math.pi * k / 8 + math.pi / 8
            x1, y1 = 160 + 45 * math.cos(a), cy + 11 * math.sin(a)
            x2, y2 = 160 + 85 * math.cos(a), cy + 21 * math.sin(a)
            o.append(f'<line x1="{x1:.1f}" y1="{y1:.1f}" x2="{x2:.1f}" y2="{y2:.1f}" {L} marker-end="url(#ar)"/>')
    o.insert(0, '<defs><marker id="ar" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="6" markerHeight="6" orient="auto">'
                '<path d="M0,0 L10,5 L0,10 z" fill="currentColor"/></marker></defs>')
    o += [text(175, 72, "B", size=15, anchor="start"),
          '<line x1="262" y1="88" x2="262" y2="170" stroke="currentColor" stroke-width="5"/>',
          f'<line x1="160" y1="170" x2="258" y2="170" {L}/>', text(210, 188, "1 m", size=13),
          f'<line x1="285" y1="88" x2="285" y2="170" {L}/>', ah(285, 88, "up"), ah(285, 170, "down"), text(292, 134, "1 m", size=13, anchor="start")]
    write("q-gate2016-s2-q55", svg(330, 220, *o))


if __name__ == "__main__":
    q53()
    q55()
    q47()
    q48()
    q45()
    q46()
    q38()
    q35()
    q36()
    q37()
    q34()
    q27()
    q28()
    q21()
    q25()
    q16()
    q20()
    q11()
    q13()
    q2()
    q3()
    print("ok")
