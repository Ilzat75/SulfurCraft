import os, re

TMP = os.environ["TEMP"]
TSRG = os.path.join(TMP, "opencode", "mcp", "config", "joined.tsrg")
CLIENT = os.path.join(TMP, "opencode", "client.txt")
OUT = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "maps")
os.makedirs(OUT, exist_ok=True)

PRIM = {"void": "V", "boolean": "Z", "byte": "B", "char": "C", "short": "S",
        "int": "I", "long": "J", "float": "F", "double": "D"}

def t2d(t):
    dims = 0
    while t.endswith("[]"):
        dims += 1
        t = t[:-2]
    if t in PRIM:
        d = PRIM[t]
    else:
        d = "L" + t.replace(".", "/") + ";"
    return "[" * dims + d

def src_to_desc(args, ret):
    ps = []
    for a in args.split(","):
        a = a.strip()
        if a:
            ps.append(t2d(a))
    return "(" + "".join(ps) + ")" + t2d(ret)

# client.txt: single pass (class lines + indented member lines)
cls_re2 = re.compile(r"^(\S+) -> (\S+):$")
cls_re = cls_re2
obf_to_off = {}
moj = {}
cur_obf = None
with open(CLIENT, encoding="utf-8") as f:
    for line in f:
        line = line.rstrip("\n")
        if not line:
            continue
        cm = cls_re.match(line)
        if cm:
            cur_obf = cm.group(2)
            obf_to_off[cur_obf] = cm.group(1).replace(".", "/")
            moj[cur_obf] = []
            continue
        if line[0] != " ":
            continue
        body = line.strip()
        if " -> " not in body:
            continue
        idx = body.rindex(" -> ")
        left = body[:idx].strip()
        obf_m = body[idx + 4:]
        while re.match(r"^\d+:\d+:", left):
            left = left.split(":", 2)[2].strip()
        if "(" in left:
            m2 = re.match(r"^(\S+) (\S+)\((.*)\)$", left)
            if not m2:
                continue
            desc = src_to_desc(m2.group(3), m2.group(1))
            moj.setdefault(cur_obf, []).append((m2.group(2), obf_m, "m", desc))
        else:
            parts = left.rsplit(" ", 1)
            if len(parts) == 2:
                moj.setdefault(cur_obf, []).append((parts[1], obf_m, "f", parts[0]))

print("client classes:", len(moj))

def remap_desc(desc):
    out = []
    i = 0
    while i < len(desc):
        c = desc[i]
        if c == "L":
            j = desc.find(";", i)
            if j < 0:
                return desc
            name = desc[i + 1:j]
            out.append("L" + obf_to_off.get(name, name) + ";")
            i = j + 1
        else:
            out.append(c)
            i += 1
    return "".join(out)

# tsrg2 parse
tsrg_members = {}
cur = None
with open(TSRG, encoding="utf-8") as f:
    for line in f:
        line = line.rstrip("\n")
        if not line:
            continue
        if line[0] != "\t":
            cur = line.split()[0]
        elif line.startswith("\t\t"):
            continue
        else:
            parts = line[1:].split(" ")
            if len(parts) >= 3:
                tsrg_members[(cur, parts[0], parts[1])] = parts[2]
            elif len(parts) == 2:
                tsrg_members[(cur, parts[0], "")] = parts[1]

print("tsrg members:", len(tsrg_members))

out = open(os.path.join(OUT, "members.tsv"), "w", encoding="utf-8")
matched = 0
for (obf_cls, obf_m, desc), srg in tsrg_members.items():
    entries = moj.get(obf_cls)
    if not entries:
        continue
    off_cls = obf_to_off.get(obf_cls, obf_cls)
    if desc:
        key = remap_desc(desc)
        for e in entries:
            if e[1] == obf_m and e[2] == "m" and e[3] == key:
                out.write(off_cls + "\tm\t" + srg + "\t" + e[0] + "\t" + e[3] + "\n")
                matched += 1
    else:
        for e in entries:
            if e[1] == obf_m and e[2] == "f":
                out.write(off_cls + "\tf\t" + srg + "\t" + e[0] + "\t" + e[3] + "\n")
                matched += 1
out.close()
print("matched:", matched)

# sanity checks
import io
with open(os.path.join(OUT, "members.tsv"), encoding="utf-8") as f:
    for line in f:
        if "\tregisterGoals\t" in line or "\tcreateAttributes\t" in line:
            print("CHECK:", line.strip())
