"""Graph figures (waveforms, v-i lines) for Electric Circuits, Chapter 1.
Run: python3 build_graphs.py   (writes the .svg files next to this script)
"""
import math
import os
import sys

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "..", "..", "tools"))
from svgkit import plot, svg, text  # noqa: E402

OUT = os.path.dirname(os.path.abspath(__file__))


def save(name, content):
    with open(os.path.join(OUT, name + ".svg"), "w") as f:
        f.write(content)


def curve(f, a, b, n=60):
    return [(a + (b - a) * k / n, f(a + (b - a) * k / n)) for k in range(n + 1)]


# ---------- questions ----------

# ESE 2017 Q38 / GATE 1997: i ramps to 2 A in 2 s; v = 2 V pulse for 2 s
save("q-ese2017-38", svg(
    520, 150,
    plot([[(0, 0), (2, 2), (3.3, 2)]], xmax=3.5, ymin=0, ymax=2.6, ylabel="i(t)",
         xticks=[(2, "2 s")], yticks=[(2, "2 A")], guides=[((0, 2), (2, 2)), ((2, 0), (2, 2))]),
    plot([[(0, 2), (2, 2), (2, 0)]], xmax=3.5, ymin=0, ymax=2.6, ylabel="v(t)", ox=270,
         xticks=[(2, "2 s")], yticks=[(2, "2 V")], guides=[((2, 0), (2, 2))])))

# ESE 2018 Q16: v = 10 t (20 V at 2 s); i = 2 t^2 (2 A at 1 s, 8 A at 2 s)
save("q-ese2018-16", svg(
    520, 170,
    plot([[(0, 0), (2.6, 26)]], xmax=3, ymin=0, ymax=30, h=150, ylabel="v(t)",
         xticks=[(2, "2 s")], yticks=[(20, "20 V")], guides=[((0, 20), (2, 20)), ((2, 0), (2, 20))]),
    plot([curve(lambda t: 2 * t * t, 0, 2)], xmax=2.6, ymin=0, ymax=9.5, h=150, ylabel="i(t)", ox=270,
         xticks=[(1, "1 s"), (2, "2 s")], yticks=[(2, "2 A"), (8, "8 A")],
         guides=[((0, 2), (1, 2)), ((1, 0), (1, 2)), ((0, 8), (2, 8)), ((2, 0), (2, 8))])))

# ESE 2018 Q24: triangular voltage across 0.5 F, and four current options
tri_v = [(0, 10), (2, -10), (4, 10), (6, -10), (7, 0)]
save("q-ese2018-24", svg(
    300, 150,
    plot([tri_v], xmax=7.4, ymin=-12, ymax=12, w=300, h=150, ylabel="v",
         xticks=[(k, str(k)) for k in range(1, 8)], yticks=[(10, "10 V"), (-10, "−10 V")],
         xlabel="t (s)", label_origin=False)))


def square(levels, xmax=7):
    pts = []
    for k, lv in enumerate(levels):
        a, b = 2 * k, min(2 * k + 2, xmax)
        pts += [(a, lv), (b, lv)]
    return [pts]


opts = {
    "a": square([5, -5, 5, -5]),
    "b": square([-5, 5, -5, 5]),
    "c": [[(0, 5), (2, -5), (4, 5), (6, -5), (7, 0)]],
    "d": [[(0, -5), (2, 5), (4, -5), (6, 5), (7, 0)]],
}
for k, s in opts.items():
    save(f"q-ese2018-24-{k}", svg(
        280, 120,
        plot(s, xmax=7.4, ymin=-7, ymax=7, w=280, h=120, ylabel="i", xlabel="t (s)",
             xticks=[(k2, str(k2)) for k2 in range(1, 8)], yticks=[(5, "5 A"), (-5, "−5 A")],
             label_origin=False)))

# ESE 2024 Q60: current into 1 mF
save("q-ese2024-60", svg(
    300, 160,
    plot([[(0, 0), (2, 100), (6.2, 100)]], xmax=6.6, ymin=0, ymax=120, w=300, h=160, ylabel="i (mA)",
         xlabel="t (ms)", xticks=[(2, "2"), (4, "4"), (6, "6")], yticks=[(100, "100")])))

# GATE 2008 Q76: i(t) in mA vs t in µs
grid = [((x, 0), (x, 6)) for x in range(1, 10)] + [((0, y), (9, y)) for y in range(1, 7)]
save("q-gate2008-76", svg(
    340, 190,
    plot([[(0, 0), (2, 4), (8, 0), (9, 0)]], xmax=9.4, ymin=0, ymax=6.6, w=340, h=190, ylabel="i(t) mA",
         xlabel="t (µs)", xticks=[(k, str(k)) for k in range(1, 10)], yticks=[(k, str(k)) for k in range(1, 7)],
         guides=grid)))

# GATE 2003: current through a coil (R = 1 Ω, L = 2 H)
save("q-gate2003-ind-energy", svg(
    280, 150,
    plot([[(0, 0), (2, 6), (4.8, 6)]], xmax=5, ymin=0, ymax=7.5, w=280, h=150, ylabel="i",
         xticks=[(2, "2 s"), (4, "4 s")], yticks=[(6, "6 A")], guides=[((0, 6), (2, 6))])))

# AEGCL 2023 Q83, Fig. 4
save("q-aegcl2023-83", svg(
    300, 150,
    plot([[(0, 0), (2, 10), (4, 10), (6, 0), (6.8, 0)]], xmax=7, ymin=0, ymax=12.5, w=300, h=150,
         ylabel="i (A)", xlabel="t (ms)", xticks=[(2, "2"), (4, "4"), (6, "6")], yticks=[(10, "10")],
         guides=[((0, 10), (2, 10))])))

# GATE 1999: four current waveforms (no scales in the original)
def tri(t):
    """triangle of period 2: -1 at t=0, +1 at t=1, -1 at t=2"""
    t = t % 2
    return -1 + 2 * t if t <= 1 else 3 - 2 * t


def seg_wave(f_up, f_down, n=3):
    pts = []
    for k in range(n):
        a = k * 1.0
        up = (k % 2 == 0)
        f = f_up if up else f_down
        pts += [(a + 0.001, f(a + 0.001)), (a + 0.999, f(a + 0.999))]
    return [pts]


rc = {
    "a": seg_wave(lambda t: 2, lambda t: -2, 4),
    "b": [[(0, -2), (1, 2), (1, -2), (2, 2), (2, -2), (3, 2), (3, -2), (4, 2)]],
    "c": seg_wave(lambda t: 2 - tri(t), lambda t: -2 - tri(t), 4),
    "d": seg_wave(lambda t: 2 + tri(t), lambda t: -2 + tri(t), 4),
}
for k, s in rc.items():
    save(f"q-gate1999-rc-{k}", svg(
        260, 120,
        plot(s, xmin=-0.1, xmax=4.1, ymin=-3.6, ymax=3.6, w=260, h=120, xlabel="t", ylabel="i",
             label_origin=False)))

# GATE 2025 Q18: four I-V options
iv = {
    "a": [[(0, 1), (3, 1)]],
    "b": [[(0, 1), (3, 2.2)]],
    "c": [[(0, 0), (2.6, 2.6)]],
    "d": [[(0, 1), (3, 0.3)]],
}
for k, s in iv.items():
    save(f"q-gate2025-18-{k}", svg(
        250, 120,
        plot(s, xmax=3.2, ymin=0, ymax=2.8, w=250, h=120, xlabel="VXY", ylabel="IXY",
             yticks=[(1, "1 A")])))

# GATE 2014 S1 Q8: four vs(t) options
vs_opts = {
    "a": [[(0, -2), (3.2, 4)]],
    "b": [curve(lambda t: 4 - 6 * math.exp(-t), 0, 3.2)],
    "c": [[(0, 0), (3.2, 4.5)]],
    "d": [curve(lambda t: 4.5 * (1 - math.exp(-1.3 * t)), 0, 3.2)],
}
for k, s in vs_opts.items():
    save(f"q-gate2014-s1-8-{k}", svg(
        220, 130,
        plot(s, xmax=3.4, ymin=-3, ymax=5, w=220, h=130, xlabel="t", ylabel="vs",
             yticks=[(-2, "−2")], label_origin=False)))

# ---------- concept figures ----------

# practical source load line with a load line and operating point
E, r, RL = 10, 2, 3
I_op = E / (r + RL)
V_op = I_op * RL
g = plot([[(0, E), (E / r, 0)], [(0, 0), (4.2, 4.2 * RL)]], xmax=5.6, ymin=0, ymax=12.5, w=360, h=220,
         xlabel="I", ylabel="V", xticks=[(E / r, "Isc = E/r")], yticks=[(E, "Voc = E")], left=70,
         guides=[((0, V_op), (I_op, V_op)), ((I_op, 0), (I_op, V_op))],
         notes=[(3.4, 3.3, "source: V = E − I·r", "start"), (3.05, 11.2, "load: V = I·RL", "start"),
                (I_op + 0.12, V_op + 0.9, "operating point", "start")])
# dot at operating point: recompute pixel position with the same mapping as plot()
L, R_, T, B = 70, 22 + 6.5, 18, 26
px = L + I_op / 5.6 * (360 - L - R_)
py = T + (12.5 - V_op) / 12.5 * (220 - T - B)
save("c1-load-line", svg(360, 220, g, f'<circle cx="{px:.1f}" cy="{py:.1f}" r="4" fill="currentColor"/>'))

print("graphs written")
