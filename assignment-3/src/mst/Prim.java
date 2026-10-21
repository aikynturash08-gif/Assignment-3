package mst;

import graph.Edge;
import graph.Graph;
import java.util.PriorityQueue;


public class Prim {
    public static MSTResult run(Graph g) {
        MSTResult r = new MSTResult();
        final long[] cmp = {0};
        boolean[] vis = new boolean[g.n];
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> { cmp[0]++; return Integer.compare(a[0], b[0]); });
        int components = 0;
        for (int s = 0; s < g.n; s++) {
            if (vis[s]) continue;
            components++;
            vis[s] = true;
            for (Edge e : g.adj.get(s)) { r.edgesExamined++; pq.add(new int[]{e.w, e.v, s}); r.heapPushes++; }
            while (!pq.isEmpty()) {
                int[] t = pq.poll();
                r.heapPops++;
                int v = t[1];
                if (vis[v]) continue;
                vis[v] = true;
                r.edges.add(new Edge(t[2], v, t[0]));
                r.totalWeight += t[0];
                for (Edge e : g.adj.get(v)) {
                    r.edgesExamined++;
                    if (!vis[e.v]) { pq.add(new int[]{e.w, e.v, v}); r.heapPushes++; }
                }
            }
        }
        r.connected = components == 1;
        r.comparisons = cmp[0];
        return r;
    }
}
