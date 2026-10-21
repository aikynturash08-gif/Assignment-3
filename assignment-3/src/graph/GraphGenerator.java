package graph;

import java.util.*;

public class GraphGenerator {

    public static Graph connectedUndirected(int n, int m, long seed, int maxW) {
        if (m < n - 1 || (long) m > (long) n * (n - 1) / 2) throw new IllegalArgumentException("bad m");
        Random r = new Random(seed);
        Graph g = new Graph(n, false);
        Set<Long> used = new HashSet<>();
        List<Integer> perm = new ArrayList<>();
        for (int i = 0; i < n; i++) perm.add(i);
        Collections.shuffle(perm, r);
        for (int i = 1; i < n; i++) {
            int u = perm.get(i), v = perm.get(r.nextInt(i));
            used.add(key(u, v, false, n));
            g.addEdge(u, v, 1 + r.nextInt(maxW));
        }
        while (g.edgeCount() < m) {
            int u = r.nextInt(n), v = r.nextInt(n);
            if (u == v || !used.add(key(u, v, false, n))) continue;
            g.addEdge(u, v, 1 + r.nextInt(maxW));
        }
        return g;
    }

    public static Graph randomDirected(int n, int m, long seed, int maxW) {
        Random r = new Random(seed);
        Graph g = new Graph(n, true);
        Set<Long> used = new HashSet<>();
        while (g.edgeCount() < m) {
            int u = r.nextInt(n), v = r.nextInt(n);
            if (u == v || !used.add(key(u, v, true, n))) continue;
            g.addEdge(u, v, 1 + r.nextInt(maxW));
        }
        return g;
    }

    public static Graph randomDag(int n, int m, long seed, int maxW) {
        Random r = new Random(seed);
        List<Integer> rank = new ArrayList<>();
        for (int i = 0; i < n; i++) rank.add(i);
        Collections.shuffle(rank, r);
        Graph g = new Graph(n, true);
        Set<Long> used = new HashSet<>();
        while (g.edgeCount() < m) {
            int a = r.nextInt(n), b = r.nextInt(n);
            if (a == b) continue;
            int u = rank.get(a) < rank.get(b) ? a : b, v = u == a ? b : a;
            if (!used.add(key(u, v, true, n))) continue;
            g.addEdge(u, v, 1 + r.nextInt(maxW));
        }
        return g;
    }

    private static long key(int u, int v, boolean directed, int n) {
        if (!directed && u > v) { int t = u; u = v; v = t; }
        return (long) u * n + v;
    }
}
