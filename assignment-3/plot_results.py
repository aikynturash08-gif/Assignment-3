"""Reads results/tables/results.csv, writes results/tables/results.md and PNG plots to results/plots/."""
import csv
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt

rows = list(csv.DictReader(open("results/tables/results.csv")))
for r in rows:
    r["V"], r["E"] = int(r["V"]), int(r["E"])
    r["t"], r["ops"] = float(r["avg_time_us"]), int(r["total_ops"])

def series(algo):
    return sorted([r for r in rows if r["algorithm"] == algo], key=lambda r: r["V"])

# ---------- markdown tables
groups = [
    ("Kruskal vs Prim", ["Kruskal", "Prim"]),
    ("DSU optimisations (inside Kruskal)", ["Kruskal-DSU-naive", "Kruskal-DSU-compression", "Kruskal-DSU-rank", "Kruskal-DSU-both"]),
    ("Topological ordering (DAG vs graph with cycles)", ["Topo-DAG", "Topo-Cyclic"]),
    ("SCC (Kosaraju)", ["SCC-Directed", "SCC-DAG"]),
    ("Dijkstra", ["Dijkstra"]),
]
with open("results/tables/results.md", "w") as f:
    for title, algos in groups:
        f.write(f"#### {title}\n\n| Algorithm | Graph | V | E | Avg time (µs) | Operation counters |\n|---|---|---|---|---|---|\n")
        for a in algos:
            for r in series(a):
                f.write(f"| {a} | {r['graph']} | {r['V']} | {r['E']} | {r['t']:.2f} | {r['counters'].replace(';', ', ')} |\n")
        f.write("\n")

# ---------- plots
main = ["Kruskal", "Prim", "Topo-DAG", "SCC-Directed", "Dijkstra"]
for key, label, fname in [("V", "Number of vertices (V)", "time_vs_vertices.png"), ("E", "Number of edges (E)", "time_vs_edges.png")]:
    plt.figure(figsize=(7, 4.5))
    for a in main:
        s = series(a)
        plt.plot([r[key] for r in s], [r["t"] for r in s], marker="o", label=a)
    plt.xscale("log"); plt.yscale("log")
    plt.xlabel(label); plt.ylabel("Average time (µs, log scale)")
    plt.title(f"Execution time vs {label.split(' (')[0].lower()}")
    plt.grid(True, which="both", alpha=.3); plt.legend(); plt.tight_layout()
    plt.savefig("results/plots/" + fname, dpi=130); plt.close()

# Kruskal vs Prim: ops and time
fig, ax = plt.subplots(1, 2, figsize=(10, 4))
for a in ["Kruskal", "Prim"]:
    s = series(a)
    ax[0].plot([r["V"] for r in s], [r["t"] for r in s], marker="o", label=a)
    ax[1].plot([r["V"] for r in s], [r["ops"] for r in s], marker="o", label=a)
for x, t in zip(ax, ["Time (µs)", "Total counted operations"]):
    x.set_xscale("log"); x.set_yscale("log"); x.set_xlabel("V"); x.set_ylabel(t); x.grid(True, which="both", alpha=.3); x.legend()
fig.suptitle("Kruskal vs Prim"); fig.tight_layout(); fig.savefig("results/plots/kruskal_vs_prim.png", dpi=130); plt.close()

# DSU variants: path steps on Large
names = ["Kruskal-DSU-naive", "Kruskal-DSU-compression", "Kruskal-DSU-rank", "Kruskal-DSU-both"]
vals = []
for a in names:
    r = [x for x in series(a) if x["graph"] == "Large"][0]
    vals.append(int([kv.split("=")[1] for kv in r["counters"].split(";") if kv.startswith("dsu_path_steps")][0]))
plt.figure(figsize=(7, 4)); plt.bar([n.replace("Kruskal-DSU-", "") for n in names], vals, color=["#c44", "#e90", "#4a9", "#27c"])
plt.yscale("log"); plt.ylabel("Parent-pointer steps in find() (log)"); plt.title("DSU optimisations, Large graph (V=1000, E=5000)")
plt.tight_layout(); plt.savefig("results/plots/dsu_path_steps.png", dpi=130); plt.close()
print("ok")
