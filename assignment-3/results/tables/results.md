#### Kruskal vs Prim

| Algorithm | Graph | V | E | Avg time (µs) | Operation counters |
|---|---|---|---|---|---|
| Kruskal | Small | 10 | 30 | 29.32 | weight=134, edges_examined=15, comparisons=109, dsu_finds=30, dsu_unions=15 |
| Kruskal | Medium | 100 | 500 | 1133.97 | weight=1153, edges_examined=180, comparisons=3839, dsu_finds=360, dsu_unions=180 |
| Kruskal | Large | 1000 | 5000 | 1951.80 | weight=12900, edges_examined=3271, comparisons=50394, dsu_finds=6542, dsu_unions=3271 |
| Prim | Small | 10 | 30 | 52.44 | weight=134, edges_examined=60, comparisons=204, heap_pushes=30, heap_pops=30 |
| Prim | Medium | 100 | 500 | 447.50 | weight=1153, edges_examined=1000, comparisons=7770, heap_pushes=500, heap_pops=500 |
| Prim | Large | 1000 | 5000 | 4200.72 | weight=12900, edges_examined=10000, comparisons=111028, heap_pushes=5000, heap_pops=5000 |

#### DSU optimisations (inside Kruskal)

| Algorithm | Graph | V | E | Avg time (µs) | Operation counters |
|---|---|---|---|---|---|
| Kruskal-DSU-naive | Small | 10 | 30 | 59.37 | weight=134, dsu_finds=30, dsu_path_steps=22 |
| Kruskal-DSU-naive | Medium | 100 | 500 | 177.93 | weight=1153, dsu_finds=360, dsu_path_steps=2321 |
| Kruskal-DSU-naive | Large | 1000 | 5000 | 5434.72 | weight=12900, dsu_finds=6542, dsu_path_steps=543294 |
| Kruskal-DSU-compression | Small | 10 | 30 | 11.89 | weight=134, dsu_finds=30, dsu_path_steps=19 |
| Kruskal-DSU-compression | Medium | 100 | 500 | 178.39 | weight=1153, dsu_finds=360, dsu_path_steps=489 |
| Kruskal-DSU-compression | Large | 1000 | 5000 | 3380.21 | weight=12900, dsu_finds=6542, dsu_path_steps=12192 |
| Kruskal-DSU-rank | Small | 10 | 30 | 11.11 | weight=134, dsu_finds=30, dsu_path_steps=20 |
| Kruskal-DSU-rank | Medium | 100 | 500 | 144.40 | weight=1153, dsu_finds=360, dsu_path_steps=438 |
| Kruskal-DSU-rank | Large | 1000 | 5000 | 1861.88 | weight=12900, dsu_finds=6542, dsu_path_steps=10625 |
| Kruskal-DSU-both | Small | 10 | 30 | 10.09 | weight=134, dsu_finds=30, dsu_path_steps=17 |
| Kruskal-DSU-both | Medium | 100 | 500 | 92.64 | weight=1153, dsu_finds=360, dsu_path_steps=290 |
| Kruskal-DSU-both | Large | 1000 | 5000 | 2858.06 | weight=12900, dsu_finds=6542, dsu_path_steps=6045 |

#### Topological ordering (DAG vs graph with cycles)

| Algorithm | Graph | V | E | Avg time (µs) | Operation counters |
|---|---|---|---|---|---|
| Topo-DAG | Small | 10 | 30 | 59.35 | dag=true, edge_examinations=30, queue_ops=20 |
| Topo-DAG | Medium | 100 | 500 | 67.34 | dag=true, edge_examinations=500, queue_ops=200 |
| Topo-DAG | Large | 1000 | 5000 | 1743.78 | dag=true, edge_examinations=5000, queue_ops=2000 |
| Topo-Cyclic | Small | 10 | 30 | 4.45 | dag=false, edge_examinations=5, queue_ops=2 |
| Topo-Cyclic | Medium | 100 | 500 | 23.57 | dag=false, edge_examinations=15, queue_ops=4 |
| Topo-Cyclic | Large | 1000 | 5000 | 75.58 | dag=false, edge_examinations=31, queue_ops=16 |

#### SCC (Kosaraju)

| Algorithm | Graph | V | E | Avg time (µs) | Operation counters |
|---|---|---|---|---|---|
| SCC-Directed | Small | 10 | 30 | 21.26 | scc=3, edge_examinations=60 |
| SCC-Directed | Medium | 100 | 500 | 1393.59 | scc=4, edge_examinations=1000 |
| SCC-Directed | Large | 1000 | 5000 | 4501.99 | scc=14, edge_examinations=10000 |
| SCC-DAG | Small | 10 | 30 | 105.10 | scc=10, edge_examinations=60 |
| SCC-DAG | Medium | 100 | 500 | 324.19 | scc=100, edge_examinations=1000 |
| SCC-DAG | Large | 1000 | 5000 | 3497.44 | scc=1000, edge_examinations=10000 |

#### Dijkstra

| Algorithm | Graph | V | E | Avg time (µs) | Operation counters |
|---|---|---|---|---|---|
| Dijkstra | Small | 10 | 30 | 237.67 | reachable=10, edge_examinations=60, relaxations=17, heap_pushes=18, heap_pops=18 |
| Dijkstra | Medium | 100 | 500 | 515.58 | reachable=100, edge_examinations=1000, relaxations=202, heap_pushes=203, heap_pops=203 |
| Dijkstra | Large | 1000 | 5000 | 2013.28 | reachable=1000, edge_examinations=10000, relaxations=1932, heap_pushes=1933, heap_pops=1933 |

