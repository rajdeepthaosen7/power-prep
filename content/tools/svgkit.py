"""Small helpers to make clean, theme-aware SVG figures for the notes and questions.

All strokes and text use `currentColor`, so a figure follows the page colour in day and night mode.
Circuits are drawn with schemdraw (SVG backend); graphs with `plot()` below.
"""
import re

import schemdraw

schemdraw.use("svg")
schemdraw.config(fontsize=13, lw=1.5, font="sans-serif")

FONT = 13


def themed(svg: str) -> str:
    """Make a schemdraw / hand-made SVG follow the page colour and scale freely."""
    svg = svg.replace("stroke:black", "stroke:currentColor").replace("fill:black", "fill:currentColor")
    svg = re.sub(r'(stroke|fill)="black"', r'\1="currentColor"', svg)
    svg = svg.replace("font-family:sans-serif;", "")
    # drop the fixed size in pt; keep viewBox, give a pixel width hint
    m = re.search(r'viewBox="([-\d.]+) ([-\d.]+) ([\d.]+) ([\d.]+)"', svg)
    w = float(m.group(3)) if m else 300
    svg = re.sub(r'\s(height|width)="[\d.]+pt"', "", svg, count=2)
    svg = svg.replace("<svg ", f'<svg width="{round(w * 1.15)}" ', 1)
    svg = re.sub(r"<\?xml[^>]*>\s*", "", svg)
    return svg.strip() + "\n"


def drawing_svg(d) -> str:
    return themed(d.get_imagedata("svg").decode())


def _esc(s: str) -> str:
    return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")


def text(x, y, s, size=FONT, anchor="middle", italic=False, weight=None):
    st = ' font-style="italic"' if italic else ""
    wt = f' font-weight="{weight}"' if weight else ""
    return (f'<text x="{x:.1f}" y="{y:.1f}" font-size="{size}" text-anchor="{anchor}" '
            f'dominant-baseline="middle" fill="currentColor"{st}{wt}>{_esc(s)}</text>')


def arrow_head(x, y, direction, size=6):
    if direction == "right":
        pts = [(x, y), (x - size, y - size / 2), (x - size, y + size / 2)]
    elif direction == "up":
        pts = [(x, y), (x - size / 2, y + size), (x + size / 2, y + size)]
    elif direction == "left":
        pts = [(x, y), (x + size, y - size / 2), (x + size, y + size / 2)]
    else:  # down
        pts = [(x, y), (x - size / 2, y - size), (x + size / 2, y - size)]
    return '<polygon points="' + " ".join(f"{a:.1f},{b:.1f}" for a, b in pts) + '" fill="currentColor"/>'


def plot(series, *, xmax, ymin, ymax, w=240, h=130, xlabel="t", ylabel="", xticks=(), yticks=(),
         guides=(), xmin=0.0, ox=0, oy=0, lw=2.0, label_origin=True, notes=(), left=40):
    """Return an SVG <g> for a small hand-drawn style graph.

    series: list of point lists [(x, y), ...] in data units (each drawn as one polyline).
    xticks / yticks: [(value, "label"), ...]; guides: [((x1, y1), (x2, y2)), ...] dashed lines.
    notes: [(x, y, "text", anchor)] in data units.
    """
    L, R, T, B = left, 22 + 6.5 * len(xlabel or ""), 18, 26  # margins inside the group
    pw, ph = w - L - R, h - T - B

    def X(x):
        return ox + L + (x - xmin) / (xmax - xmin) * pw

    def Y(y):
        return oy + T + (ymax - y) / (ymax - ymin) * ph

    out = []
    y0 = Y(0) if ymin <= 0 <= ymax else Y(ymin)
    x0 = X(max(xmin, 0))
    # axes
    out.append(f'<line x1="{x0:.1f}" y1="{y0:.1f}" x2="{X(xmax) + 14:.1f}" y2="{y0:.1f}" stroke="currentColor" stroke-width="1.2"/>')
    out.append(arrow_head(X(xmax) + 16, y0, "right"))
    out.append(f'<line x1="{x0:.1f}" y1="{Y(ymin) + 4:.1f}" x2="{x0:.1f}" y2="{Y(ymax) - 10:.1f}" stroke="currentColor" stroke-width="1.2"/>')
    out.append(arrow_head(x0, Y(ymax) - 12, "up"))
    if xlabel:
        out.append(text(X(xmax) + 22, y0, xlabel, italic=True, anchor="start"))
    if ylabel:
        out.append(text(x0 + 6, Y(ymax) - 10, ylabel, italic=True, anchor="start"))
    if label_origin:
        out.append(text(x0 - 7, y0 + 11, "0", size=12))
    for v, lab in xticks:
        out.append(f'<line x1="{X(v):.1f}" y1="{y0 - 3:.1f}" x2="{X(v):.1f}" y2="{y0 + 3:.1f}" stroke="currentColor" stroke-width="1.2"/>')
        out.append(text(X(v), y0 + 13, lab, size=12))
    for v, lab in yticks:
        out.append(f'<line x1="{x0 - 3:.1f}" y1="{Y(v):.1f}" x2="{x0 + 3:.1f}" y2="{Y(v):.1f}" stroke="currentColor" stroke-width="1.2"/>')
        out.append(text(x0 - 6, Y(v), lab, size=12, anchor="end"))
    for (a, b), (c, d) in guides:
        out.append(f'<line x1="{X(a):.1f}" y1="{Y(b):.1f}" x2="{X(c):.1f}" y2="{Y(d):.1f}" stroke="currentColor" stroke-width="1" stroke-dasharray="4 3" opacity="0.7"/>')
    for pts in series:
        p = " ".join(f"{X(a):.1f},{Y(b):.1f}" for a, b in pts)
        out.append(f'<polyline points="{p}" fill="none" stroke="currentColor" stroke-width="{lw}" stroke-linejoin="round"/>')
    for x, y, s, anc in notes:
        out.append(text(X(x), Y(y), s, size=12, anchor=anc))
    return "\n".join(out)


def svg(width, height, *groups):
    body = "\n".join(groups)
    return (f'<svg xmlns="http://www.w3.org/2000/svg" width="{width}" viewBox="0 0 {width} {height}">\n'
            f"{body}\n</svg>\n")
