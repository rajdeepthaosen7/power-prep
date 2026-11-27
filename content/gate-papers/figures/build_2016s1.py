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


def q30():
    """Asymptotic Bode magnitude: +20 dB/dec crossing 0 dB at 0.5 rad/s, flat 12 dB, then -40 dB/dec crossing 0 dB at 8 rad/s."""
    import math
    u = lambda w: math.log10(w) + 0.8          # shift so the axis sits left of 0.5 rad/s, as in the paper
    pts = [(u(0.25), -6), (u(2), 12), (u(4), 12), (u(16), -12)]
    g = plot([pts], xmin=0, xmax=u(20), ymin=-14, ymax=20, w=380, h=210, xlabel="ω (rad/s)", ylabel="magnitude (dB)",
             xticks=[(u(0.5), "0.5"), (u(8), "8")], yticks=[(0, "0 dB")], label_origin=False,
             notes=[(u(0.7), 9, "20 dB/dec", "end"), (u(2.8), 14.5, "12 dB", "middle"), (u(8.5), 7, "−40 dB/dec", "start")], left=50)
    write("q-gate2016-s1-q30", svg(380, 210, g))


def q34():
    """Two real-valued spectra made of triangles; X2(w) is X1(-w)."""
    t1 = [(-1.3, 0), (-1, 0), (-0.5, 0.3), (0, 0), (0.5, 1), (1, 0), (1.5, 0.5), (2, 0), (2.4, 0)]
    t2 = [(-x, y) for x, y in reversed(t1)]
    g1 = plot([t1], xmin=-1.3, xmax=2.4, ymin=0, ymax=1.2, w=260, h=170, xlabel="ω", ylabel="X1(jω)",
              xticks=[(-1, "−1"), (1, "1"), (2, "2")], notes=[(-0.5, 0.42, "0.3", "middle"), (0.5, 1.08, "1", "middle"), (1.5, 0.62, "0.5", "middle")],
              left=20)
    g2 = plot([t2], xmin=-2.4, xmax=1.3, ymin=0, ymax=1.2, w=260, h=170, xlabel="ω", ylabel="X2(jω)",
              xticks=[(-2, "−2"), (-1, "−1"), (1, "1")], notes=[(0.5, 0.42, "0.3", "middle"), (-0.5, 1.08, "1", "middle"), (-1.5, 0.62, "0.5", "middle")],
              left=20, ox=280)
    write("q-gate2016-s1-q34", svg(570, 170, g1, g2))


def q36():
    """Two JK flip-flops on one clock: FF A has J = K = 1 (5 V); FF B has J = K = Q-bar of A."""
    d = D()
    pins = [elm.IcPin(name="J", side="left", slot="3/3"), elm.IcPin(name=">", side="left", slot="2/3", anchorname="clk"),
            elm.IcPin(name="K", side="left", slot="1/3"), elm.IcPin(name="Q", side="right", slot="2/2", anchorname="q"),
            elm.IcPin(name="Q'", side="right", slot="1/2", anchorname="qb")]
    d += (a := elm.Ic(pins=pins, size=(2, 3), label="A"))
    d += (b := elm.Ic(pins=pins, size=(2, 3), label="B").at((a.q[0] + 3.6, a.J[1])).anchor("J"))
    d += elm.Line().right().at(a.q).length(0.5).label("QA", loc="top")
    d += elm.Line().right().at(b.q).length(0.6).label("QB", loc="right")
    # FF A: J = K = 5 V
    d += elm.Line().left().at(a.J).length(0.8)
    d += (v := elm.Dot())
    d += elm.Line().up().length(0.8).label("5 V", loc="right")
    d += elm.Line().down().at(v.center).toy(a.K)
    d += elm.Line().right().tox(a.K)
    # FF B: J = K = Q-bar A
    d += elm.Line().right().at(a.qb).length(1.7)
    d += elm.Line().up().toy(b.J)
    d += (j := elm.Dot())
    d += elm.Line().right().tox(b.J)
    d += elm.Line().down().at(j.center).toy(b.K)
    d += elm.Line().right().tox(b.K)
    # common clock
    y = a.K[1] - 1.2
    d += elm.Line().left().at(a.clk).length(1.4)
    d += elm.Line().down().toy(y)
    d += (c := elm.Dot())
    d += elm.Line().left().length(0.8).label("CLK", loc="left")
    d += elm.Line().right().at(c.center).tox(b.clk[0] - 1.0)
    d += elm.Line().up().toy(b.clk)
    d += elm.Line().right().tox(b.clk)
    save("q-gate2016-s1-q36", d)


def q37():
    """2-bit flash ADC: 3 V across 100-200-200-100 ohm; taps at 2.5, 1.5, 0.5 V to the (-) inputs of three comparators;
    VIN to all (+) inputs; outputs X2, X1, X0 to a digital circuit giving B1, B0."""
    d = D()
    d += elm.Dot(open=True).label("3 V", loc="top")
    d += elm.Resistor().down().label("100 Ω", loc="top")
    d += (t2 := elm.Dot())
    d += elm.Resistor().down().label("200 Ω", loc="top")
    d += (t1 := elm.Dot())
    d += elm.Resistor().down().label("200 Ω", loc="top")
    d += (t0 := elm.Dot())
    d += elm.Resistor().down().label("100 Ω", loc="top")
    d += elm.Ground()
    xin = t2.center[0] + 2.2
    outs = []
    for tap, name in [(t2, "X2"), (t1, "X1"), (t0, "X0")]:
        op = elm.Opamp(leads=True).right().anchor("in1").at((xin + 1.0, tap.center[1]))
        d += op
        d += elm.Line().at(tap.center).to(op.in1)
        d += elm.Line().at(op.in2).to((xin, op.in2[1]))
        d += elm.Dot().at((xin, op.in2[1]))
        outs.append((op, name))
    top_in = outs[0][0].in2[1]
    d += elm.Line().at((xin, top_in)).to((xin, t0.center[1] - 1.6))
    d += elm.Label().at((xin, t0.center[1] - 1.9)).label("VIN")
    box_x = outs[0][0].out[0] + 1.4
    for op, name in outs:
        d += elm.Line().at(op.out).to((box_x, op.out[1])).label(name, loc="top")
    ytop, ybot = outs[0][0].out[1] + 0.8, outs[2][0].out[1] - 0.8
    d += elm.Line().at((box_x, ytop)).to((box_x + 2.2, ytop))
    d += elm.Line().at((box_x + 2.2, ytop)).to((box_x + 2.2, ybot))
    d += elm.Line().at((box_x + 2.2, ybot)).to((box_x, ybot))
    d += elm.Line().at((box_x, ybot)).to((box_x, ytop))
    ym = (ytop + ybot) / 2
    d += elm.Label().at((box_x + 1.1, ym + 0.3)).label("Digital")
    d += elm.Label().at((box_x + 1.1, ym - 0.3)).label("circuit")
    d += elm.Line().at((box_x + 2.2, ym + 0.5)).to((box_x + 3.0, ym + 0.5)).label("B1", loc="right")
    d += elm.Line().at((box_x + 2.2, ym - 0.5)).to((box_x + 3.0, ym - 0.5)).label("B0", loc="right")
    save("q-gate2016-s1-q37", d)


def q39():
    """3 V source, switch S1, 1 ohm to the top node; 1 H inductor from the top node to the bottom; switch S2 (closed long) to
    a 2 ohm in series with another 3 V source (+ at top)."""
    d = D()
    d += (v1 := elm.Battery().up().reverse().label("3 V", loc="bottom"))
    d += (s1 := elm.Switch(action="close").right().label("S1", loc="top"))
    d += elm.Resistor().right().label("1 Ω", loc="top")
    d += (n := elm.Dot())
    d += elm.Line().right().length(2.0).label("S2 (closed for a long time)", loc="top")
    d += elm.Resistor().down().length(1.6).label("2 Ω", loc="bottom")
    d += elm.Battery().down().length(1.4).toy(v1.start).label("3 V", loc="bottom")
    d += elm.Line().left().tox(v1.start)
    d += elm.Inductor2().down().at(n.center).toy(v1.start).label("1 H", loc="bottom")
    d += elm.Dot().at((n.center[0], v1.start[1]))
    save("q-gate2016-s1-q39", d)


def q42():
    """Star-delta transformer with dots: star windings A-N (dot at A end), B-N (dot at B end), C-N (dot at C end);
    delta windings a-b (dot at a end, parallel to A), b-c (dot at b end, parallel to B), c-a (dot at c end, parallel to C)."""
    import math
    d = D()
    L = 1.7
    n = (0, 0)
    a_end = (L * math.cos(math.radians(60)), L * math.sin(math.radians(60)))
    c_end = (L * math.cos(math.radians(-60)), L * math.sin(math.radians(-60)))
    b_end = (-L, 0)
    d += elm.Inductor2(loops=3).at(a_end).to(n)
    d += elm.Inductor2(loops=3).at(b_end).to(n)
    d += elm.Inductor2(loops=3).at(c_end).to(n)
    d += elm.Label().at((0.5, 0.05)).label("N")
    for end, name in [(a_end, "A"), (c_end, "C")]:
        d += elm.Line().at(end).to((-2.8, end[1]))
        d += elm.Label().at((-3.05, end[1])).label(name)
    d += elm.Line().at(b_end).to((-2.8, 0))
    d += elm.Label().at((-3.05, 0)).label("B")
    d += elm.Dot(radius=0.07).at((a_end[0] - 0.45, a_end[1] - 0.15))
    d += elm.Dot(radius=0.07).at((b_end[0] + 0.25, 0.25))
    d += elm.Dot(radius=0.07).at((c_end[0] - 0.45, c_end[1] + 0.15))
    # delta
    x0 = 2.4
    T = (x0 + 1.0, a_end[1])
    Lv = (x0, c_end[1] + 0.6)
    Rv = (x0 + 2.0, c_end[1] + 0.6)
    d += elm.Inductor2(loops=3).at(T).to(Lv)
    d += elm.Inductor2(loops=3).at(T).to(Rv)
    d += elm.Inductor2(loops=3).at(Lv).to(Rv)
    d += elm.Line().at(T).to((x0 + 3.2, T[1])).label("a", loc="right")
    d += elm.Line().at(Rv).to((x0 + 3.2, Rv[1])).label("c", loc="right")
    d += elm.Line().at(Lv).to((x0, c_end[1]))
    d += elm.Line().to((x0 + 3.2, c_end[1])).label("b", loc="right")
    d += elm.Dot(radius=0.07).at((T[0] - 0.45, T[1] - 0.25))
    d += elm.Dot(radius=0.07).at((Rv[0] + 0.05, Rv[1] + 0.45))
    d += elm.Dot(radius=0.07).at((Lv[0] + 0.45, Lv[1] + 0.25))
    save("q-gate2016-s1-q42", d)


if __name__ == "__main__":
    q42()
    q37()
    q39()
    q34()
    q36()
    q30()
    q25()
    q11()
    q12()
    q18()
    q22()
    q23()
    q24()
    print("ok")
