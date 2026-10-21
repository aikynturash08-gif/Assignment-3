package shortestpath;

import graph.Edge;
import graph.Graph;
import java.util.*;


public class Dijkstra {
    public static final long INF = Long.MAX_VALUE;
    public final long[] dist;
    public final int[] prev;
    public long edgeExaminations, successfulRelaxations, heapPushes, heapPops;

    public Dijkstra(Graph g, int source) {
        for (Edge e : g.edges) if (e.w < 0) throw new IllegalArgumentException("Dijkstra requires non-negative weights");
        dist = new long[g.n];
        prev = new int[g.n];
        Arrays.fill(dist, INF);
        Arrays.fill(prev, -1);
        dist[source] = 0;
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
        pq.add(new long[]{0, source});
        heapPushes++;
        while (!pq.isEmpty()) {
            long[] t = pq.poll();
            heapPops++;
            int u = (int) t[1];
            if (t[0] > dist[u]) continue;
            for (Edge e : g.adj.get(u)) {
                edgeExaminations++;
                long nd = dist[u] + e.w;
                if (nd < dist[e.v]) {
                    dist[e.v] = nd;
                    prev[e.v] = u;
                    successfulRelaxations++;
                    pq.add(new long[]{nd, e.v});
                    heapPushes++;
                }
            }
        }
    }

    public boolean reachable(int v) { return dist[v] != INF; }

    public int reachableCount() { int c = 0; for (long d : dist) if (d != INF) c++; return c; }

    public List<Integer> pathTo(int target) {
        if (!reachable(target)) return Collections.emptyList();
        LinkedList<Integer> p = new LinkedList<>();
        for (int v = target; v != -1; v = prev[v]) p.addFirst(v);
        return p;
    }
}
