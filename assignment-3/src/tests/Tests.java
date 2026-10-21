package tests;

import connectivity.SCC;
import connectivity.TopologicalSort;
import dsu.DSU;
import graph.Edge;
import graph.Graph;
import graph.GraphGenerator;
import mst.Kruskal;
import mst.MSTResult;
import mst.Prim;
import shortestpath.Dijkstra;

import java.util.*;

public class Tests {
    static int passed = 0, failed = 0;

    static void check(boolean cond, String name) {
        if (cond) { passed++; System.out.println("PASS  " + name); }
        else { failed++; System.out.println("FAIL  " + name); }
    }

    static Graph und(int n, int[][] es) {
        Graph g = new Graph(n, false);
        for (int[] e : es) g.addEdge(e[0], e[1], e[2]);
        return g;
    }

    static Graph dir(int n, int[][] es) {
        Graph g = new Graph(n, true);
        for (int[] e : es) g.addEdge(e[0], e[1], e.length > 2 ? e[2] : 1);
        return g;
    }

    static long[] bellmanFord(Graph g, int s) {
        long INF = Long.MAX_VALUE / 4;
        long[] d = new long[g.n];
        Arrays.fill(d, INF);
        d[s] = 0;
        for (int i = 0; i < g.n; i++)
            for (int u = 0; u < g.n; u++)
                if (d[u] < INF) for (Edge e : g.adj.get(u)) d[e.v] = Math.min(d[e.v], d[u] + e.w);
        return d;
    }

    public static void main(String[] args) {
        DSU d = new DSU(6);
        check(d.union(0, 1) && d.union(1, 2) && !d.union(0, 2), "DSU: union merges, repeated union returns false");
        check(d.connected(0, 2) && !d.connected(0, 5) && d.components == 4, "DSU: connected / component count");

        Graph small = und(4, new int[][]{{0, 1, 1}, {1, 2, 2}, {2, 3, 3}, {0, 3, 10}, {0, 2, 5}});
        MSTResult k = Kruskal.run(small), p = Prim.run(small);
        check(k.totalWeight == 6 && p.totalWeight == 6 && k.connected && p.connected, "MST: small graph weight 6, connected");

        Graph disc = und(5, new int[][]{{0, 1, 1}, {1, 2, 2}, {3, 4, 7}});
        k = Kruskal.run(disc); p = Prim.run(disc);
        check(!k.connected && !p.connected && k.totalWeight == p.totalWeight && k.totalWeight == 10, "MST: disconnected graph detected, forest weights equal");

        Graph multi = und(4, new int[][]{{0, 1, 1}, {1, 2, 1}, {2, 3, 1}, {3, 0, 1}}); // 4 valid MSTs, all weight 3
        check(Kruskal.run(multi).totalWeight == 3 && Prim.run(multi).totalWeight == 3, "MST: multiple valid MSTs, same weight");

        Graph dup = und(5, new int[][]{{0, 1, 4}, {1, 2, 4}, {2, 3, 4}, {3, 4, 4}, {0, 4, 4}, {0, 2, 4}});
        check(Kruskal.run(dup).totalWeight == 16 && Prim.run(dup).totalWeight == 16, "MST: all-duplicate weights");

        Graph single = new Graph(1, false);
        check(Kruskal.run(single).totalWeight == 0 && Kruskal.run(single).connected && Prim.run(single).connected, "MST: single vertex");

        Graph cyc = und(3, new int[][]{{0, 1, 1}, {1, 2, 1}, {2, 0, 1}});
        check(Kruskal.run(cyc).edges.size() == 2, "MST: cycle graph -> exactly V-1 edges");

        boolean allEqual = true;
        for (int seed = 0; seed < 300; seed++) {
            int n = 5 + seed % 30;
            Graph g = GraphGenerator.connectedUndirected(n, Math.min(n * (n - 1) / 2, 2 * n), seed, 10);
            MSTResult a = Kruskal.run(g), b = Prim.run(g);
            if (a.totalWeight != b.totalWeight || a.edges.size() != n - 1 || b.edges.size() != n - 1) allEqual = false;
        }
        check(allEqual, "MST: Kruskal == Prim on 300 random connected graphs");

        Graph dag = dir(6, new int[][]{{5, 2}, {5, 0}, {4, 0}, {4, 1}, {2, 3}, {3, 1}});
        TopologicalSort.Result t = TopologicalSort.run(dag);
        check(t.isDag && TopologicalSort.isValidOrder(dag, t.order), "Topo: valid order for DAG");
        Graph cyclic = dir(3, new int[][]{{0, 1}, {1, 2}, {2, 0}});
        check(!TopologicalSort.run(cyclic).isDag, "Topo: cycle detected");
        boolean okDags = true;
        for (int seed = 0; seed < 100; seed++) {
            Graph g = GraphGenerator.randomDag(30, 80, seed, 5);
            TopologicalSort.Result r = TopologicalSort.run(g);
            if (!r.isDag || !TopologicalSort.isValidOrder(g, r.order)) okDags = false;
        }
        check(okDags, "Topo: valid on 100 random DAGs");
        check(TopologicalSort.run(new Graph(1, true)).isDag, "Topo: single vertex");

        Graph scc = dir(8, new int[][]{{0, 1}, {1, 2}, {2, 0}, {2, 3}, {3, 4}, {4, 5}, {5, 3}, {6, 5}, {6, 7}, {7, 6}});
        SCC.Result sr = SCC.run(scc);
        check(sr.components.size() == 3, "SCC: 3 components in sample graph");
        check(sr.componentId[0] == sr.componentId[1] && sr.componentId[1] == sr.componentId[2]
                && sr.componentId[3] == sr.componentId[4] && sr.componentId[4] == sr.componentId[5]
                && sr.componentId[6] == sr.componentId[7] && sr.componentId[0] != sr.componentId[3], "SCC: correct grouping");
        boolean partition = true;
        for (int seed = 0; seed < 100; seed++) {
            Graph g = GraphGenerator.randomDirected(40, 70, seed, 5);
            SCC.Result r = SCC.run(g);
            int total = 0;
            boolean[] seen = new boolean[g.n];
            for (List<Integer> c : r.components) for (int v : c) { total++; if (seen[v]) partition = false; seen[v] = true; }
            if (total != g.n) partition = false;
        }
        check(partition, "SCC: every vertex in exactly one component (100 random graphs)");
        check(SCC.run(dag).components.size() == 6, "SCC: DAG => every vertex is its own SCC");

        Graph w = und(5, new int[][]{{0, 1, 4}, {0, 2, 1}, {2, 1, 2}, {1, 3, 1}, {2, 3, 5}});
        Dijkstra dj = new Dijkstra(w, 0);
        check(dj.dist[1] == 3 && dj.dist[3] == 4 && dj.dist[2] == 1, "Dijkstra: correct distances");
        check(dj.pathTo(3).equals(Arrays.asList(0, 2, 1, 3)), "Dijkstra: correct path");
        check(!dj.reachable(4) && dj.pathTo(4).isEmpty() && dj.reachableCount() == 4, "Dijkstra: unreachable vertex handled");
        check(new Dijkstra(new Graph(1, false), 0).dist[0] == 0, "Dijkstra: single vertex");
        boolean negThrown = false;
        try { new Dijkstra(dir(2, new int[][]{{0, 1, -1}}), 0); } catch (IllegalArgumentException e) { negThrown = true; }
        check(negThrown, "Dijkstra: negative weight rejected");
        boolean matches = true;
        for (int seed = 0; seed < 100; seed++) {
            Graph g = GraphGenerator.randomDirected(25, 60, seed, 20);
            Dijkstra a = new Dijkstra(g, 0);
            long[] ref = bellmanFord(g, 0);
            for (int v = 0; v < g.n; v++) {
                long x = a.dist[v] == Dijkstra.INF ? Long.MAX_VALUE / 4 : a.dist[v];
                if (x != ref[v]) matches = false;
            }
        }
        check(matches, "Dijkstra == Bellman-Ford on 100 random directed graphs");

        System.out.println("\nPassed: " + passed + "  Failed: " + failed);
        if (failed > 0) System.exit(1);
    }
}
