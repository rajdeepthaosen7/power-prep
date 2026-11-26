"""Circuit figures for Electric Circuits, Chapter 2 (schemdraw, SVG).
Run: python3 build_circuits.py   (writes the .svg files next to this script)
Each question figure follows the original paper's circuit; polarity marks are added where the paper used symbols only.
"""
import math
import os
import sys

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "..", "..", "tools"))
from svgkit import drawing_svg  # noqa: E402

import schemdraw  # noqa: E402
import schemdraw.elements as elm  # noqa: E402

OUT = os.path.dirname(os.path.abspath(__file__))
ONLY = set(sys.argv[1:])


def save(name, d):
    with open(os.path.join(OUT, name + ".svg"), "w") as f:
        f.write(drawing_svg(d))


def D():
    return schemdraw.Drawing(show=False, unit=2.6)


def R(label, **kw):
    return elm.Resistor(**kw).label(label)


def diag(d, p0, p1, label, near_start=True, loc="top"):
    """Resistor on a diagonal, with its body near one end, so two crossing diagonals cross on plain wire."""
    f0, f1 = (0.0, 0.42) if near_start else (0.58, 1.0)
    a = (p0[0] + (p1[0] - p0[0]) * f0, p0[1] + (p1[1] - p0[1]) * f0)
    b = (p0[0] + (p1[0] - p0[0]) * f1, p0[1] + (p1[1] - p0[1]) * f1)
    if near_start:
        d += elm.Resistor().endpoints(a, b).label(label, loc=loc)
        d += elm.Line().endpoints(b, p1)
    else:
        d += elm.Line().endpoints(p0, a)
        d += elm.Resistor().endpoints(a, b).label(label, loc=loc)


# ---------------- concept figures ----------------

def c2_node_loop():
    d = D()
    d += (v := elm.SourceV().up().label("V", loc="top"))
    d += elm.Resistor().right().length(3.6).label("R1")
    d += (a := elm.Dot().label("node a", loc="top"))
    d += elm.Resistor().down().label("R2", loc="bottom")
    d += (b := elm.Dot())
    d += elm.Line().left().tox(v.start)
    d += elm.Line().at(a.center).right().length(3.4)
    d += elm.Resistor().down().label("R3", loc="bottom")
    d += elm.Line().left().tox(b.center)
    d += elm.Label().at((b.center[0], b.center[1] - 0.55)).label("node b (the whole bottom wire)")
    d += elm.Label().at((v.center[0] + 1.9, v.center[1])).label("mesh 1")
    d += elm.Label().at((a.center[0] + 1.9, v.center[1])).label("mesh 2")
    save("c2-node-loop", d)


def c2_dividers():
    d = D()
    d += elm.Line().up().length(1.3)
    d += (v := elm.SourceV().up().label("V", loc="top"))
    d += elm.Line().up().length(1.3)
    d += elm.Line().right().length(3.0)
    d += elm.Resistor().down().length(2.6).label(("+", "V1", "−"), loc="bottom").label("R1", loc="top")
    d += elm.Resistor().down().length(2.6).label(("+", "V2", "−"), loc="bottom").label("R2", loc="top")
    d += elm.Line().left().tox(0)
    # current divider
    x0 = 7.0
    d += (s := elm.SourceI().at((x0, 0)).up().length(5.2).label("I", loc="top"))
    d += elm.Line().right().length(1.6)
    d += (n1 := elm.Dot())
    d += elm.Resistor().down().length(5.2).label("R1", loc="bottom")
    d += elm.Line().left().tox(s.start)
    d += elm.Line().at(n1.center).right().length(2.2)
    d += elm.Resistor().down().length(5.2).label("R2", loc="bottom")
    d += elm.Line().left().length(2.2)
    d += elm.Label().at((n1.center[0] + 0.55, n1.center[1] - 1.0)).label("I1 ↓")
    d += elm.Label().at((n1.center[0] + 2.75, n1.center[1] - 1.0)).label("I2 ↓")
    save("c2-dividers", d)


def tri(d, a, b, c, lab_ab, lab_bc, lab_ca, names=("a", "b", "c")):
    """Delta: resistors on the three sides of triangle a-b-c."""
    d += elm.Resistor().endpoints(a, b).label(lab_ab)
    d += elm.Resistor().endpoints(b, c).label(lab_bc)
    d += elm.Resistor().endpoints(c, a).label(lab_ca)
    for p, n, loc in zip((a, b, c), names, ("top", "right", "left")):
        d += elm.Dot().at(p).label(n, loc=loc)


def star(d, o, pts, labs, names=("a", "b", "c")):
    for p, lab, n, loc in zip(pts, labs, names, ("top", "right", "left")):
        d += elm.Resistor().endpoints(o, p).label(lab)
        d += elm.Dot().at(p).label(n, loc=loc)
    d += elm.Dot().at(o)


def c2_star_delta():
    d = D()
    a, b, c = (2.6, 4.5), (5.2, 0), (0, 0)
    tri(d, a, b, c, "Rc", "Ra", "Rb")
    o = (10.6, 1.6)
    star(d, o, [(10.6, 4.6), (13.2, 0), (8.0, 0)], ["R1", "R2", "R3"])
    save("c2-star-delta", d)


def bridge(d, labels, mid, nodes=("A", "C", "B", "D"), h=2.6):
    """Diamond A(left) C(top) B(right) D(bottom); labels = (AC, CB, AD, DB); mid = middle C-D label or None."""
    A, C, B, Dn = (0, 0), (h, h), (2 * h, 0), (h, -h)
    d += elm.Resistor().endpoints(A, C).label(labels[0])
    d += elm.Resistor().endpoints(C, B).label(labels[1])
    d += elm.Resistor().endpoints(A, Dn).label(labels[2], loc="bottom")
    d += elm.Resistor().endpoints(Dn, B).label(labels[3], loc="bottom")
    if mid:
        d += elm.Resistor().endpoints(C, Dn).label(mid, loc="bottom")
    for p, n, loc in zip((A, C, B, Dn), nodes, ("left", "top", "right", "bottom")):
        d += elm.Dot().at(p).label(n, loc=loc, ofst=0.3)
    return A, C, B, Dn


def c2_bridge():
    d = D()
    A, C, B, Dn = bridge(d, ("P", "Q", "R", "S"), "G")
    d += elm.Line().at(A).left().length(1.2)
    d += elm.Line().down().toy(Dn[1] - 1.5)
    d += elm.SourceV().right().tox(B[0] + 1.2).label("V", loc="top")
    d += elm.Line().up().toy(B[1])
    d += elm.Line().left().tox(B[0])
    save("c2-bridge", d)


def ladder(d, start, series_lab, shunt_lab, n=3, first_shunt=None, arrow=None):
    x, y = start
    d += elm.Line().at((x, y)).right().length(0.6)
    if arrow:
        d += elm.Label().at((x - 0.2, y + 0.45)).label(arrow)
    if first_shunt:
        d += elm.Resistor().at((x + 0.6, y)).down().length(2.6).label(first_shunt, loc="bottom")
    cx = x + 0.6
    for _ in range(n):
        d += elm.Resistor().at((cx, y)).right().length(2.4).label(series_lab)
        cx += 2.4
        d += elm.Resistor().at((cx, y)).down().length(2.6).label(shunt_lab, loc="bottom")
    d += elm.Line().at((cx, y)).right().length(0.6)
    d += elm.Line().at((x, y - 2.6)).right().tox(cx + 0.6)
    d += elm.Label().at((cx + 1.2, y - 1.3)).label("···")
    d += elm.Dot(open=True).at((x, y))
    d += elm.Dot(open=True).at((x, y - 2.6))


def c2_ladder():
    d = D()
    ladder(d, (0, 0), "Rs", "Rp")
    d += elm.Label().at((-0.6, -1.3)).label("Rin →")
    save("c2-ladder", d)


# ---------------- question figures ----------------

def gate2004_32():
    d = D()
    d += (s := elm.SourceI().up().label("1 A", loc="bottom"))
    d += elm.Resistor().right().label("10 Ω")
    d += (n := elm.Dot())
    d += elm.Resistor().down().label("6 Ω", loc="bottom")
    d += elm.Line().left().tox(s.start)
    d += elm.Resistor().at(n.center).right().label("6 Ω")
    d += (p := elm.Dot().label("P", loc="top"))
    d += elm.Line().right().length(1.4)
    d += elm.SourceV().down().reverse().label("E", loc="bottom")
    d += elm.Line().left().tox(n.center)
    d += elm.Label().at((p.center[0] + 0.7, p.center[1] + 0.45)).label("← 2 A")
    save("q-gate2004-32", d)


def gate2004_33():
    d = D()
    tri(d, (2.6, 4.5), (5.2, 0), (0, 0), "Rc", "Ra", "Rb")
    star(d, (10.6, 1.6), [(10.6, 4.6), (13.2, 0), (8.0, 0)], ["R1", "R2", "R3"])
    save("q-gate2004-33", d)


def gate2004_35():
    d = D()
    d += (v := elm.SourceV().up().label("100 V", loc="bottom"))
    d += elm.Resistor().right().label("10 Ω")
    d += (n := elm.Dot())
    d += elm.Resistor().down().label("10 Ω", loc="bottom")
    d += elm.Line().left().tox(v.start)
    d += elm.Line().at(n.center).right().length(2.4)
    d += elm.Resistor().down().label("R", loc="bottom")
    d += elm.Line().left().tox(n.center)
    d += elm.Label().at((n.center[0] + 1.2, n.center[1] + 0.45)).label("2 A →")
    save("q-gate2004-35", d)


def gate2005_1():
    d = D()
    d += (v := elm.SourceV().up().label("100 V", loc="bottom"))
    d += elm.Resistor().right().label("R")
    d += (n := elm.Dot())
    d += elm.Resistor().down().label("10 Ω", loc="bottom")
    d += elm.Line().left().tox(v.start)
    d += elm.Line().at(n.center).right().length(2.2)
    d += elm.Resistor().down().label("10 Ω", loc="bottom")
    d += elm.Line().left().length(2.2)
    d += elm.Label().at((v.end[0] + 0.6, v.end[1] + 0.45)).label("8 A →")
    save("q-gate2005-1", d)


def gate2009_7():
    d = D()
    A, C, B, Dn = bridge(d, ("1 kΩ", "1 kΩ", "1 kΩ", "1 kΩ"), "2 kΩ")
    d += elm.Line().at(A).left().length(1.0)
    d += elm.Line().down().toy(Dn[1] - 1.4)
    d += elm.SourceV().right().tox(B[0] + 1.0).label("6 V", loc="top")
    d += elm.Line().up().toy(B[1])
    d += elm.Line().left().tox(B[0])
    save("q-gate2009-7", d)


def gate2010_33():
    d = D()
    d += (s := elm.SourceI().up().label("2 A", loc="bottom"))
    d += elm.Resistor().right().label("1 Ω")
    d += (n := elm.Dot())
    d += elm.Resistor().down().label("12 Ω", loc="bottom")
    d += elm.Line().left().tox(s.start)
    d += elm.Resistor().at(n.center).right().label("R")
    d += elm.Line().right().length(0.8)
    d += elm.SourceV().down().reverse().label("6 V", loc="bottom")
    d += elm.Line().left().tox(n.center)
    d += elm.Label().at((n.center[0] - 0.6, n.center[1] - 1.3)).label("1 A ↓")
    save("q-gate2010-33", d)


def gate2012_28():
    d = D()
    TL, A, Bn, TR = (0, 3), (3.4, 3), (7.6, 3), (11, 3)
    BL, C, Dd, BR = (0, 0), (3.4, 0), (7.6, 0), (11, 0)
    d += elm.Resistor().endpoints(TL, A).label("R")
    d += elm.Resistor().endpoints(TL, BL).label("R", loc="bottom")
    d += elm.Resistor().endpoints(BL, A).label("R")
    d += elm.Resistor().endpoints(A, C).label("R", loc="bottom")
    d += elm.SourceV().endpoints(C, BL).label("5 V", loc="bottom")
    d += elm.Resistor().endpoints(A, Bn).label("2 Ω")
    d += elm.Resistor().endpoints(Bn, TR).label("R")
    d += elm.Resistor().endpoints(Bn, Dd).label("R")
    d += elm.Resistor().endpoints(TR, Dd).label("R", loc="bottom")
    d += elm.SourceV().endpoints(TR, BR).label("10 V", loc="bottom")
    d += elm.Resistor().endpoints(Dd, BR).label("R", loc="bottom")
    # 1 ohm in parallel with a 2 A source between C and D (arrow from C to D)
    d += elm.Line().endpoints(C, (4.2, 0))
    d += elm.Line().endpoints((6.8, 0), Dd)
    d += elm.SourceI().endpoints((4.2, 0), (6.8, 0)).label("2 A", loc="bottom")
    d += elm.Line().endpoints((4.2, 0), (4.2, 1.1))
    d += elm.Resistor().endpoints((4.2, 1.1), (6.8, 1.1)).label("1 Ω")
    d += elm.Line().endpoints((6.8, 1.1), (6.8, 0))
    for p, n, loc in ((A, "VA", "top"), (Bn, "VB", "top"), (C, "VC", "bottom"), (Dd, "VD", "bottom")):
        d += elm.Dot().at(p).label(n, loc=loc, ofst=0.35)
    save("q-gate2012-28", d)


def gate2013_19():
    d = D()
    # delta drawn as a pi: Rb (left shunt), Ra (top series), Rc (right shunt)
    d += elm.Dot(open=True).at((0, 2.6))
    d += elm.Line().right().length(0.8)
    d += (t1 := elm.Dot())
    d += elm.Resistor().right().label("Ra")
    d += (t2 := elm.Dot())
    d += elm.Line().right().length(0.8)
    d += elm.Dot(open=True)
    d += elm.Resistor().at(t1.center).down().label("Rb", loc="bottom")
    d += elm.Resistor().at(t2.center).down().label("Rc", loc="bottom")
    d += elm.Line().at((0, 0)).right().length(5.0)
    d += elm.Dot(open=True).at((0, 0))
    d += elm.Dot(open=True).at((5.0, 0))
    # star drawn as a T
    x = 8.0
    d += elm.Dot(open=True).at((x, 2.6))
    d += elm.Resistor().right().length(2.4).label("RC")
    d += (m := elm.Dot())
    d += elm.Resistor().right().length(2.4).label("RB")
    d += elm.Dot(open=True)
    d += elm.Resistor().at(m.center).down().label("RA", loc="bottom")
    d += elm.Line().at((x, 0)).right().length(4.8)
    d += elm.Dot(open=True).at((x, 0))
    d += elm.Dot(open=True).at((x + 4.8, 0))
    save("q-gate2013-19", d)


def gate2014_s3_30():
    d = D()
    d += (v := elm.SourceV().up().label("1 V", loc="bottom"))
    d += (tl := elm.Dot())
    d += elm.Resistor().right().label("1 Ω")
    d += (m := elm.Dot())
    d += elm.Resistor().right().label("1 Ω")
    d += (tr := elm.Dot())
    d += elm.Resistor().down().label("1 Ω", loc="bottom")
    d += elm.Line().left().tox(v.start)
    d += elm.SourceI().at((m.center[0], v.start[1])).up().label("2 A", loc="bottom")
    d += elm.Line().at(tl.center).up().length(1.4)
    d += elm.SourceV().right().reverse().tox(tr.center).label("1 V")
    d += elm.Line().down().toy(tr.center[1])
    save("q-gate2014-s3-30", d)


def gate2015_s2_29():
    d = D()
    TL, TR, BL, BR = (0, 3.2), (4.4, 3.2), (0, 0), (4.4, 0)
    d += elm.SourceV().endpoints(BL, TL).label("5 V", loc="bottom")
    d += elm.Resistor().endpoints(TL, TR).label("1 Ω")
    d += elm.Resistor().endpoints(BL, BR).label("1 Ω", loc="top")
    diag(d, TL, BR, "1 Ω", near_start=True)
    diag(d, BL, TR, "1 Ω", near_start=False, loc="bottom")
    d += elm.Line().endpoints(TR, (5.6, 3.2))
    d += elm.Resistor().endpoints((5.6, 3.2), (5.6, 0)).label("2 Ω", loc="bottom")
    d += elm.Line().endpoints((5.6, 0), BR)
    d += elm.Label().at((6.3, 2.6)).label("i ↓")
    for p in (TL, TR, BL, BR):
        d += elm.Dot().at(p)
    d += elm.Label().at((2.6, -1.0)).label("Diagonals cross without joining.")
    save("q-gate2015-s2-29", d)


def gate2016_s1_15():
    d = D()
    ladder(d, (0, 0), "2 Ω", "1 Ω", arrow="RA →")
    ladder(d, (0, -4.6), "2 Ω", "1 Ω", first_shunt="1 Ω", arrow="RB →")
    save("q-gate2016-s1-15", d)


def gate2016_s1_17():
    d = D()
    d += elm.Line().right().length(1.0)
    d += (l := elm.Dot())
    d += elm.Line().up().length(1.0)
    d += elm.Resistor().right().length(2.6).label("4 Ω")
    d += elm.Resistor().right().length(2.6).label("6 Ω")
    d += elm.Line().down().length(1.0)
    d += (r := elm.Dot())
    d += elm.Line().right().length(1.0)
    d += elm.Line().at(l.center).down().length(1.0)
    d += elm.Resistor().right().length(5.2).label("5 Ω", loc="top")
    d += elm.Line().up().length(1.0)
    save("q-gate2016-s1-17", d)


def gate2017_s1_19():
    d = D()
    A0, P, Q, T = (0, 4), (2.6, 4), (6.2, 4), (9.6, 4)
    B0, S1, S2, S3 = (0, 0), (2.6, 0), (6.2, 0), (9.6, 0)
    d += elm.Resistor().endpoints(A0, P).label("1 Ω")
    d += elm.Resistor().endpoints(P, Q).label("2 Ω")
    d += elm.Resistor().endpoints(Q, T).label("1 Ω")
    d += elm.Resistor().endpoints(T, S3).label("1 Ω", loc="bottom")
    d += elm.Resistor().endpoints(B0, S1).label("0.8 Ω", loc="top")
    d += elm.Line().endpoints(S1, S3)
    d += elm.Resistor().endpoints(P, S1).label("3 Ω", loc="bottom")
    d += elm.Resistor().endpoints(Q, S2).label("6 Ω", loc="bottom")
    diag(d, P, S2, "6 Ω", near_start=True)
    diag(d, S1, Q, "3 Ω", near_start=False, loc="bottom")
    d += elm.Dot(open=True).at(A0).label("A", loc="left")
    d += elm.Dot(open=True).at(B0).label("B", loc="left")
    for p in (P, Q, S1, S2):
        d += elm.Dot().at(p)
    d += elm.Label().at((4.8, -1.0)).label("Diagonals cross without joining.")
    save("q-gate2017-s1-19", d)


def gate2019_42():
    d = D()
    xs = [0, 2.6, 5.2, 7.8, 10.4]
    parts = [("50 Ω", "200 V"), ("40 Ω", "160 V"), ("25 Ω", "100 V"), ("20 Ω", "80 V"), ("20 Ω", None)]
    for x, (r, v) in zip(xs, parts):
        if v:
            d += elm.Resistor().at((x, 5.2)).down().length(2.6).label(r, loc="bottom")
            d += elm.SourceV().at((x, 2.6)).down().length(2.6).reverse().label(v, loc="bottom")
        else:
            d += elm.Resistor().at((x, 5.2)).down().length(5.2).label(r, loc="bottom")
    d += elm.Line().at((0, 5.2)).right().tox(10.4)
    d += elm.Line().at((0, 0)).right().tox(10.4)
    d += elm.Label().at((11.1, 4.4)).label("I ↓")
    save("q-gate2019-42", d)


def ese2017_41():
    d = D()
    d += (v1 := elm.SourceV().up().label("8 V", loc="bottom"))
    d += elm.Resistor().right().label("12 Ω")
    d += (m := elm.Dot().label("M", loc="top"))
    d += elm.Resistor().right().label("8 Ω")
    d += elm.SourceV().down().reverse().label("2 V", loc="bottom")
    d += elm.Line().left().tox(v1.start)
    d += elm.Resistor().at(m.center).down().label("8 Ω", loc="bottom")
    d += elm.Dot()
    d += elm.Label().at((m.center[0], -0.55)).label("N")
    save("q-ese2017-41", d)


def ese2017_50():
    d = D()
    d += elm.Resistor().right().label("80 Ω")
    d += elm.Resistor().right().label("20 Ω")
    d += elm.Line().down().length(2.0)
    d += elm.Line().left().length(1.3)
    d += elm.SourceV().left().reverse().label("25 V", loc="top")
    d += elm.Line().left().tox(0)
    d += elm.Line().up().toy(0)
    save("q-ese2017-50", d)


def ese2017_60():
    d = D()
    d += (s := elm.SourceI().up().label("5 A", loc="bottom"))
    d += elm.Line().right().length(1.6)
    d += (n1 := elm.Dot())
    d += elm.Resistor().down().label("4 Ω", loc="bottom")
    d += elm.Line().left().tox(s.start)
    d += elm.Resistor().at(n1.center).right().label("5 Ω")
    d += (n2 := elm.Dot())
    d += elm.Resistor().down().label("6 Ω", loc="bottom")
    d += elm.Line().left().tox(n1.center)
    d += elm.Line().at(n2.center).right().length(1.6)
    d += elm.SourceI().down().reverse().label("10 A", loc="bottom")
    d += elm.Line().left().length(1.6)
    save("q-ese2017-60", d)


def ese2018_17():
    d = D()
    d += (v := elm.SourceV().up().label("100 V", loc="bottom"))
    d += elm.Resistor().right().label("12 Ω")
    d += (n := elm.Dot())
    d += elm.Resistor().down().label("14 Ω", loc="bottom")
    d += elm.Line().left().tox(v.start)
    d += elm.Line().at(n.center).right().length(2.4)
    d += elm.Resistor().down().label("R", loc="bottom")
    d += elm.Line().left().tox(n.center)
    d += elm.Label().at((n.center[0] - 0.7, n.center[1] - 0.45)).label("Is →")
    d += elm.Label().at((n.center[0] + 1.2, n.center[1] + 0.45)).label("I = 4 A →")
    save("q-ese2018-17", d)


def ese2018_26():
    d = D()
    d += (v := elm.SourceV().up().label("15 V", loc="bottom"))
    d += elm.Resistor().right().label("4 Ω")
    d += (n1 := elm.Dot())
    d += elm.Resistor().down().label("2 Ω", loc="bottom")
    d += elm.Line().left().tox(v.start)
    d += elm.Line().at(n1.center).right().length(2.2)
    d += (n2 := elm.Dot())
    d += elm.SourceI().down().reverse().label("6 A", loc="bottom")
    d += elm.Line().left().tox(n1.center)
    d += elm.Resistor().at(n2.center).right().label("3 Ω")
    d += elm.Resistor().down().label("5 Ω", loc="bottom")
    d += elm.Line().left().tox(n2.center)
    save("q-ese2018-26", d)


def ese2018_30():
    d = D()
    d += (v := elm.SourceV().up().label("2 V", loc="bottom"))
    d += elm.Resistor().right().label("2 Ω")
    d += (n := elm.Dot())
    d += elm.Resistor().down().length(1.0).label("2 Ω", loc="bottom")
    d += elm.SourceV().down().length(1.6).reverse().label("3 V", loc="bottom")
    d += elm.Line().left().tox(v.start)
    d += elm.Line().at(n.center).right().length(2.2)
    d += (n2 := elm.Dot())
    d += elm.Resistor().down().label("1 Ω", loc="bottom")
    d += elm.Line().left().tox(n.center)
    d += elm.Line().at(n2.center).right().length(2.2)
    d += elm.SourceI().down().reverse().label("2 A", loc="bottom")
    d += elm.Line().left().length(2.2)
    save("q-ese2018-30", d)


def ese2022_40():
    d = D()
    xs = [0, 3.0, 6.0, 9.0, 12.0]
    d += elm.SourceI().at((0, 0)).up().label("2.5 A", loc="top")
    d += elm.Resistor().at((3.0, 2.6)).down().label(("+", "v", "−"), loc="bottom").label("5 Ω", loc="top")
    d += elm.SourceI().at((6.0, 2.6)).down().label("2.5 A", loc="bottom")
    d += elm.Resistor().at((9.0, 2.6)).down().label("5 Ω", loc="bottom")
    d += elm.SourceI().at((12.0, 2.6)).down().label("5 A", loc="bottom")
    d += elm.Line().at((0, 2.6)).right().tox(xs[-1])
    d += elm.Line().at((0, 0)).right().tox(xs[-1])
    save("q-ese2022-40", d)


def ese2024_51():
    d = D()
    h = 2.6
    L, T, Rt, Bt = (0, 0), (h, h), (2 * h, 0), (h, -h)
    d += elm.Resistor().endpoints(L, T).label("10 Ω")
    d += elm.Resistor().endpoints(T, Rt).label("30 Ω")
    d += elm.Resistor().endpoints(L, Bt).label("20 Ω", loc="bottom")
    d += elm.Resistor().endpoints(Bt, Rt).label("40 Ω", loc="bottom")
    d += elm.Dot(open=True).at((h - 0.6, 0)).label("A", loc="bottom", ofst=0.3)
    d += elm.Line().endpoints(L, (h - 0.6, 0))
    d += elm.Dot(open=True).at((h + 0.6, 0)).label("B", loc="bottom", ofst=0.3)
    d += elm.Line().endpoints((h + 0.6, 0), Rt)
    d += elm.Line().endpoints(T, (h, h + 0.9))
    d += elm.Line().endpoints((h, h + 0.9), (-2.4, h + 0.9))
    d += elm.SourceV().endpoints((-2.4, -h - 0.9), (-2.4, h + 0.9)).label("10 V", loc="top")
    d += elm.Line().endpoints((-2.4, -h - 0.9), (h, -h - 0.9))
    d += elm.Line().endpoints((h, -h - 0.9), Bt)
    save("q-ese2024-51", d)


def ese2024_52():
    d = D()
    A, C, Dd, B = (0, 0), (2.6, 0), (6.2, 0), (9.4, 0)
    d += elm.Resistor().endpoints((0.6, 0), C).label("10 Ω")
    d += elm.Line().endpoints(A, (0.6, 0))
    d += elm.Resistor().endpoints(C, Dd).label("10 Ω")
    d += elm.Resistor().endpoints(Dd, (8.6, 0)).label("10 Ω")
    d += elm.Line().endpoints((8.6, 0), B)
    d += elm.Line().endpoints((0.6, 0), (0.6, 1.4))
    d += elm.Resistor().endpoints((0.6, 1.4), (6.2, 1.4)).label("10 Ω")
    d += elm.Line().endpoints((6.2, 1.4), Dd)
    d += elm.Line().endpoints(C, (2.6, -1.4))
    d += elm.Resistor().endpoints((2.6, -1.4), (8.6, -1.4)).label("10 Ω", loc="top")
    d += elm.Line().endpoints((8.6, -1.4), (8.6, 0))
    d += elm.Dot(open=True).at(A).label("A", loc="left")
    d += elm.Dot(open=True).at(B).label("B", loc="right")
    d += elm.Dot().at(C).label("C", loc="top")
    d += elm.Dot().at(Dd).label("D", loc="right", ofst=0.15)
    d += elm.Dot().at((0.6, 0))
    d += elm.Dot().at((8.6, 0))
    save("q-ese2024-52", d)


def ese2026_102():
    d = D()
    d += (v1 := elm.SourceV().up().label("12 V", loc="bottom"))
    d += elm.Resistor().right().label("4 Ω")
    d += elm.SourceControlledV().right().reverse().label("2Vo")
    d += elm.SourceV().down().label("4 V", loc="bottom")
    d += elm.Resistor().left().tox(v1.start).label(("−", "Vo", "+"), loc="bottom", ofst=0.35).label("6 Ω", loc="top")
    d += elm.Label().at((v1.end[0] + 0.5, v1.end[1] - 0.5)).label("i →")
    save("q-ese2026-102", d)


def apdcl2023_37():
    d = D()
    A, C, B, Dn = (0, 0), (2.6, 2.6), (5.2, 0), (2.6, -2.6)
    for p, q_ in ((A, C), (C, B), (A, Dn), (Dn, B), (C, Dn)):
        d += elm.Resistor().endpoints(p, q_)
    d += elm.Line().endpoints((-1.2, 0), A)
    d += elm.Line().endpoints(B, (6.4, 0))
    d += elm.Dot(open=True).at((-1.2, 0)).label("A", loc="left")
    d += elm.Dot(open=True).at((6.4, 0)).label("B", loc="right")
    d += elm.Label().at((2.6, -3.5)).label("(every resistor 10 Ω)")
    save("q-apdcl2023-37", d)


def apdcl2023_42():
    d = D()
    d += (s := elm.SourceI().up().label("5 A", loc="bottom"))
    d += elm.Line().right().length(2.0)
    d += (n := elm.Dot())
    d += elm.Resistor().down().length(1.0).label("5 Ω", loc="bottom")
    d += elm.SourceV().down().length(1.6).reverse().label("5 V", loc="bottom")
    d += elm.Line().left().tox(s.start)
    d += elm.Line().at(n.center).right().length(2.4)
    d += elm.SourceV().down().reverse().label("8 V", loc="bottom")
    d += elm.Line().left().length(2.4)
    d += elm.Label().at((n.center[0] + 3.4, n.center[1] - 0.6)).label("I ↑")
    save("q-apdcl2023-42", d)


if __name__ == "__main__":
    for name, fn in list(globals().items()):
        if callable(fn) and (name.startswith(("c2_", "gate", "ese", "apdcl"))) and (not ONLY or name in ONLY):
            fn()
            print("ok", name)
