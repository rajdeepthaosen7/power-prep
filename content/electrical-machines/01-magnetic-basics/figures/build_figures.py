"""Figures of Electrical Machines Ch 1 questions (schemdraw + svgkit). Run: python3 build_figures.py
Each question figure follows the original paper's drawing."""
import math
import os
import sys

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "..", "..", "tools"))
from svgkit import arrow_head, drawing_svg, plot, svg, text  # noqa: E402

import schemdraw  # noqa: E402
import schemdraw.elements as elm  # noqa: E402

OUT = os.path.dirname(os.path.abspath(__file__))
L = 'stroke="currentColor" fill="none" stroke-width="1.6"'


def write(name, content):
    with open(os.path.join(OUT, name + ".svg"), "w") as f:
        f.write(content)


def save(name, d):
    write(name, drawing_svg(d))


def D():
    return schemdraw.Drawing(show=False, unit=2.6)


def coil(x, y0, n, side, step=10, r=7):
    """A coil drawn as n half loops down a vertical limb at x, starting at y0; side = -1 (left) or +1 (right)."""
    d = f"M{x},{y0} "
    for k in range(n):
        d += f"a{r},{step / 2} 0 0 {1 if side > 0 else 0} 0,{step} "
    return f'<path d="{d}" {L}/>'


def core_rect(x, y, w, h, t):
    """A rectangular core of limb width t: outer and inner rectangles."""
    return [f'<rect x="{x}" y="{y}" width="{w}" height="{h}" {L}/>', f'<rect x="{x + t}" y="{y + t}" width="{w - 2 * t}" height="{h - 2 * t}" {L}/>']


def gate2009_q14():
    """Single-window core; one winding on each vertical arm."""
    o = core_rect(40, 20, 200, 120, 24)
    o += [coil(40, 45, 7, -1), coil(240, 45, 7, 1)]
    o += [f'<line x1="10" y1="45" x2="40" y2="45" {L}/>', f'<line x1="10" y1="115" x2="40" y2="115" {L}/>',
          f'<line x1="240" y1="45" x2="270" y2="45" {L}/>', f'<line x1="240" y1="115" x2="270" y2="115" {L}/>']
    write("q-gate2009-q14", svg(280, 160, *o))


def gate2019_q44():
    """Square core 10 cm x 10 cm (mean path 40 cm), coil on the left limb carrying I, air gap of 0.2 cm in the right limb."""
    o = [f'<path d="M60,30 H250 V118 M250,128 V230 H60 V30" {L}/>', f'<path d="M90,60 H220 V118 M220,128 V200 H90 V60" {L}/>',
         f'<line x1="220" y1="118" x2="250" y2="118" {L}/>', f'<line x1="220" y1="128" x2="250" y2="128" {L}/>',
         '<path d="M75,45 H235 V215 H75 Z" stroke="currentColor" fill="none" stroke-width="1" stroke-dasharray="4 3" opacity="0.7"/>',
         coil(60, 80, 9, -1), f'<line x1="30" y1="80" x2="60" y2="80" {L}/>', f'<line x1="30" y1="170" x2="60" y2="170" {L}/>',
         arrow_head(52, 80, "right"), text(36, 72, "I", italic=True), text(272, 127, "0.2 cm", size=12, anchor="start"),
         text(155, 18, "10 cm (mean)", size=12), text(26, 135, "10 cm", size=12)]
    write("q-gate2019-q44", svg(330, 245, *o))


def gate2009_q53():
    """(a) current source i(t) on the primary of a 1:1 lossless transformer; secondary to A, switch S, 30 ohm, B.
    (b) i(t): triangle, 0 at t = 0, +10 A at 5 ms, 0 at 10 ms, -10 A at 15 ms, 0 at 20 ms, +10 A at 25 ms."""
    d = D()
    d += (src := elm.SourceI().up().label("i(t)", loc="top"))
    d += elm.Line().right().length(1.0)
    d += elm.Line().down().toy(src.start)
    d += elm.Line().left().tox(src.start)
    tx = src.end[0] + 1.0
    d += elm.Inductor2(loops=4).at((tx + 0.5, src.end[1])).down().toy(src.start).flip()
    d += elm.Inductor2(loops=4).at((tx + 1.1, src.end[1])).down().toy(src.start)
    d += elm.Label().at((tx + 0.8, src.end[1] + 0.4)).label("1 : 1")
    d += elm.Line().at((tx + 1.1, src.end[1])).right().length(1.4)
    d += (a := elm.Dot().label("A", loc="top"))
    d += elm.Line().right().length(1.0)
    d += elm.Switch().down().length(1.2).label("S", loc="bottom")
    d += elm.Resistor().down().toy(src.start).label("30 Ω", loc="bottom")
    d += elm.Line().left().tox(tx + 1.1)
    d += elm.Dot().at((a.center[0], src.start[1])).label("B", loc="bottom")
    save("q-gate2009-q53a", d)
    pts = [(0, 0), (5, 10), (15, -10), (25, 10), (32, -4)]
    g = plot([pts], xmax=33, ymin=-12, ymax=12, w=380, h=170, xlabel="t (ms)", ylabel="i(t) (A)", lw=1.6,
             xticks=[(5, "5"), (10, "10"), (15, "15"), (20, "20"), (25, "25"), (30, "30")], yticks=[(10, "10"), (-10, "−10")],
             guides=[((5, 0), (5, 10)), ((15, 0), (15, -10)), ((25, 0), (25, 10))], left=46)
    write("q-gate2009-q53b", svg(380, 170, g))


def gate2015_s2_q20():
    """A circular turn of radius 1 m in the x-z plane, turning about the x-axis (its diameter); H along z; output Vturn."""
    o = [f'<line x1="40" y1="150" x2="300" y2="150" {L}/>', arrow_head(304, 150, "right"), text(300, 136, "x", italic=True),
         f'<line x1="70" y1="250" x2="70" y2="20" {L}/>', arrow_head(70, 16, "up"), text(56, 22, "z", italic=True),
         '<ellipse cx="170" cy="150" rx="58" ry="90" stroke="currentColor" fill="none" stroke-width="2"/>',
         f'<line x1="228" y1="146" x2="250" y2="146" {L}/>', f'<line x1="228" y1="156" x2="250" y2="156" {L}/>',
         '<circle cx="252" cy="146" r="2.5" fill="currentColor"/>', '<circle cx="252" cy="156" r="2.5" fill="currentColor"/>',
         text(258, 132, "Vturn", italic=True, anchor="start", size=13),
         f'<line x1="170" y1="50" x2="170" y2="22" {L}/>', arrow_head(170, 18, "up"), text(182, 28, "H", italic=True, anchor="start")]
    write("q-gate2015-s2-q20", svg(330, 260, *o))


def waves(name, kind):
    """GATE 2011 Q18 options: magnetising current waveforms."""
    n = 240
    pts = []
    for k in range(n + 1):
        th = 2 * math.pi * k / n
        if kind == "sine":
            y = math.sin(th)
        elif kind == "peaky":
            y = 0.3 * math.sin(th) + 0.7 * math.sin(th) ** 7
        elif kind == "tilt-a":
            y = 0.8 * math.sin(th) - 0.3 * math.sin(3 * th) + 0.25 * math.cos(th) * (1 if th < math.pi else 0.2)
        else:  # tilt-b
            y = 0.75 * math.sin(th - 0.35) - 0.3 * math.sin(3 * (th - 0.35)) + 0.1
        pts.append((th, y))
    g = plot([pts], xmax=2 * math.pi + 0.3, ymin=-1.3, ymax=1.3, w=230, h=120, xlabel="t", ylabel="i", lw=1.6, label_origin=False)
    write(name, svg(230, 120, g))


def gate2011_q18():
    waves("q-gate2011-q18-a", "tilt-a")
    waves("q-gate2011-q18-b", "tilt-b")
    waves("q-gate2011-q18-c", "sine")
    waves("q-gate2011-q18-d", "peaky")


def gate2008_q49():
    """Core with 100-turn winding (p-q) and 200-turn winding (r-s), dots at p and r; flux: 0 to 0.12 Wb in 1 s, flat to 2 s, 0 at 2.5 s.
    Options: four e_rs waveforms."""
    o = core_rect(40, 20, 200, 130, 26)
    o += [coil(40, 48, 7, -1), coil(240, 48, 7, 1), text(85, 90, "100", size=13), text(195, 90, "200", size=13),
          f'<line x1="10" y1="48" x2="40" y2="48" {L}/>', f'<line x1="10" y1="118" x2="40" y2="118" {L}/>',
          f'<line x1="240" y1="48" x2="270" y2="48" {L}/>', f'<line x1="240" y1="118" x2="270" y2="118" {L}/>',
          '<circle cx="50" cy="40" r="3" fill="currentColor"/>', '<circle cx="230" cy="40" r="3" fill="currentColor"/>',
          text(8, 40, "p", italic=True), text(8, 132, "q", italic=True), text(274, 40, "r", italic=True, anchor="start"), text(274, 132, "s", italic=True, anchor="start"),
          text(4, 86, "e_pq", size=12, anchor="start"), text(278, 86, "e_rs", size=12, anchor="start"),
          f'<line x1="120" y1="12" x2="160" y2="12" {L}/>', arrow_head(164, 12, "right"), text(172, 16, "φ", italic=True, anchor="start")]
    core = svg(320, 160, '<g transform="translate(30,0)">', *o, '</g>').replace('width="320"', 'width="350"').replace('viewBox="0 0 320 160"', 'viewBox="0 0 350 160"')
    g = plot([[(0, 0), (1, 0.12), (2, 0.12), (2.5, 0)]], xmax=2.9, ymin=0, ymax=0.15, w=300, h=150, xlabel="t (s)", ylabel="φ (Wb)",
             xticks=[(1, "1"), (2, "2"), (2.5, "2.5")], yticks=[(0.12, "0.12")], guides=[((0, 0.12), (1, 0.12)), ((1, 0), (1, 0.12)), ((2, 0), (2, 0.12))], left=46)
    write("q-gate2008-q49", core)
    write("q-gate2008-q49-flux", svg(300, 150, g))
    opts = {"a": [(0, 24), (1, 24), (1, 0), (2, 0), (2, -48), (2.5, -48), (2.5, 0), (2.8, 0)],
            "b": [(0, -24), (1, -24), (1, 0), (2, 0), (2, 48), (2.5, 48), (2.5, 0), (2.8, 0)],
            "c": [(0, 24), (1, 24), (1, 0), (2, 0), (2, 48), (2.5, 48), (2.5, 0), (2.8, 0)],
            "d": [(0, -24), (1, -24), (1, 0), (2, 0), (2, -48), (2.5, -48), (2.5, 0), (2.8, 0)]}
    for k, pts in opts.items():
        ys = [p[1] for p in pts]
        yt = sorted({v for v in ys if v != 0})
        g = plot([pts], xmax=2.9, ymin=min(-55, min(ys) - 8), ymax=max(55, max(ys) + 8), w=260, h=140, xlabel="t (s)", ylabel="e_rs", lw=1.6,
                 xticks=[(1, "1"), (2, "2"), (2.5, "2.5")], yticks=[(v, f"{v:g} V") for v in yt], left=54)
        write(f"q-gate2008-q49-{k}", svg(260, 140, g))


def gate2018_q26():
    """Toroidal core (mean radius R, cross-section radius r); N_P turns fed by v_P = V cos wt with i_P = I sin wt; N_S turns open (i_S = 0)."""
    o = ['<circle cx="200" cy="120" r="95" stroke="currentColor" fill="none" stroke-width="1.6"/>',
         '<circle cx="200" cy="120" r="55" stroke="currentColor" fill="none" stroke-width="1.6"/>',
         '<circle cx="200" cy="120" r="75" stroke="currentColor" fill="none" stroke-width="1" stroke-dasharray="5 4"/>',
         f'<line x1="200" y1="120" x2="253" y2="67" {L}/>', arrow_head(256, 64, "up"), text(236, 100, "R", italic=True),
         '<circle cx="200" cy="45" r="14" stroke="currentColor" fill="none" stroke-width="1.2"/>', text(222, 40, "r", italic=True, anchor="start")]
    for k in range(6):
        a = math.radians(160 + k * 8)
        x1, y1 = 200 + 52 * math.cos(a), 120 + 52 * math.sin(a)
        x2, y2 = 200 + 98 * math.cos(a), 120 + 98 * math.sin(a)
        o.append(f'<line x1="{x1:.1f}" y1="{y1:.1f}" x2="{x2:.1f}" y2="{y2:.1f}" {L}/>')
        a2 = math.radians(-20 + k * 8)
        x1, y1 = 200 + 52 * math.cos(a2), 120 + 52 * math.sin(a2)
        x2, y2 = 200 + 98 * math.cos(a2), 120 + 98 * math.sin(a2)
        o.append(f'<line x1="{x1:.1f}" y1="{y1:.1f}" x2="{x2:.1f}" y2="{y2:.1f}" {L}/>')
    o += [f'<path d="M107,95 H40 V185 H107" {L}/>', '<circle cx="40" cy="140" r="13" stroke="currentColor" fill="none" stroke-width="1.6"/>',
          '<path d="M31,140 q4.5,-8 9,0 t9,0" stroke="currentColor" fill="none" stroke-width="1.4"/>',
          text(4, 80, "i_P = I sin ωt →", size=12, anchor="start"), text(4, 222, "v_P = V cos ωt", size=12, anchor="start"),
          text(30, 100, "+", size=14), text(30, 196, "−", size=14), text(112, 222, "N_P", size=12),
          f'<path d="M293,95 H360 M293,170 H360" {L}/>', text(300, 82, "i_S = 0", size=12, anchor="start"),
          text(366, 100, "+", size=14, anchor="start"), text(366, 176, "−", size=14, anchor="start"), text(362, 138, "v_S", size=12, anchor="start"),
          text(288, 222, "N_S", size=12)]
    write("q-gate2018-q26", svg(400, 235, *o))


def gate2023_q53():
    """Two-winding transformer: winding a-b (open, V_ab = 200 V, 50 Hz) on the left limb, winding c-d fed by a 50 Hz AC current source."""
    o = core_rect(60, 20, 200, 140, 28)
    o += [coil(88, 50, 8, 1), coil(232, 50, 8, -1), f'<line x1="30" y1="50" x2="88" y2="50" {L}/>', f'<line x1="30" y1="130" x2="88" y2="130" {L}/>',
          '<circle cx="30" cy="50" r="3" fill="none" stroke="currentColor"/>', '<circle cx="30" cy="130" r="3" fill="none" stroke="currentColor"/>',
          text(22, 46, "a", italic=True, anchor="end"), text(22, 136, "b", italic=True, anchor="end"), text(4, 176, "Vab = 200 V, 50 Hz (open circuit)", size=11, anchor="start"),
          f'<path d="M232,50 H300 V72 M300,108 V130 H232" {L}/>', '<circle cx="300" cy="90" r="18" stroke="currentColor" fill="none" stroke-width="1.6"/>',
          f'<line x1="300" y1="102" x2="300" y2="80" {L}/>', arrow_head(300, 77, "up"),
          text(305, 44, "c", italic=True, anchor="start"), text(305, 144, "d", italic=True, anchor="start"), text(324, 90, "50 Hz, AC current source", size=11, anchor="start")]
    write("q-gate2023-q53", svg(470, 185, *o))


def gate2014_s3_q36():
    g = plot([[(0, 10), (50, 15)]], xmax=56, ymin=0, ymax=17, w=320, h=180, xlabel="f (Hz)", ylabel="Pc/f (W/Hz)",
             xticks=[(25, "25"), (50, "50")], yticks=[(5, "5"), (10, "10"), (15, "15")],
             guides=[((0, 15), (50, 15)), ((50, 0), (50, 15)), ((25, 0), (25, 12.5)), ((0, 12.5), (25, 12.5))], left=46)
    write("q-gate2014-s3-q36", svg(320, 180, g))


def gate2014_s2_q11():
    """Source sin(wt) with 1 ohm in series on the primary of an ideal 1:2 transformer; Thevenin at x-y (secondary)."""
    d = D()
    d += (s := elm.SourceSin().up().label("sin(ωt)", loc="bottom"))
    d += elm.Resistor().right().label("1 Ω", loc="top")
    d += elm.Line().right().length(0.4)
    d += (p := elm.Inductor2(loops=4).down().toy(s.start).flip())
    d += elm.Line().left().tox(s.start)
    d += elm.Inductor2(loops=4).at((p.start[0] + 0.6, p.start[1])).down().toy(s.start)
    d += elm.Label().at((p.start[0] + 0.3, s.start[1] - 0.4)).label("1 : 2")
    d += elm.Line().at((p.start[0] + 0.6, p.start[1])).right().length(1.2)
    d += elm.Dot(open=True).label("x", loc="right")
    d += elm.Line().at((p.start[0] + 0.6, s.start[1])).right().length(1.2)
    d += elm.Dot(open=True).label("y", loc="right")
    save("q-gate2014-s2-q11", d)


def gate2013_q43():
    """W-X: primary of an ideal 1:1.25 transformer; the secondary is across a potentiometer whose wiper is Y; Z = bottom end."""
    d = D()
    d += (w := elm.Dot(open=True).label("W", loc="left"))
    d += elm.Line().right().length(0.8)
    d += (p := elm.Inductor2(loops=4).down().length(2.2).flip())
    d += elm.Line().left().length(0.8)
    d += elm.Dot(open=True).label("X", loc="left")
    d += elm.Inductor2(loops=4).at((p.start[0] + 0.6, p.start[1])).down().length(2.2)
    d += elm.Label().at((p.start[0] + 0.3, p.start[1] + 0.4)).label("1 : 1.25")
    d += elm.Line().at((p.start[0] + 0.6, p.start[1])).right().length(1.3)
    d += (pot := elm.Potentiometer().down().length(2.2))
    d += elm.Line().left().tox(p.start[0] + 0.6)
    d += elm.Line().at(pot.tap).right().length(1.2)
    d += elm.Dot(open=True).label("Y", loc="right")
    d += elm.Line().at(pot.end).right().length(1.2)
    d += elm.Dot(open=True).label("Z", loc="right")
    save("q-gate2013-q43", d)


if __name__ == "__main__":
    gate2009_q14()
    gate2019_q44()
    gate2009_q53()
    gate2015_s2_q20()
    gate2011_q18()
    gate2008_q49()
    gate2018_q26()
    gate2023_q53()
    gate2014_s3_q36()
    gate2014_s2_q11()
    gate2013_q43()
    print("ok")
