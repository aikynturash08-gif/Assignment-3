package connectivity;

import graph.Edge;
import graph.Graph;
import java.util.*;

public class TopologicalSort {
    public static class Result {
        public final List<Integer> order = new ArrayList<>();
        public boolean isDag;
        public long edgeExaminations, queueOps;
    }

    public static Result run(Graph g) {
        Result r = new Result();
        int[] indeg = new int[g.n];
        for (Edge e : g.edges) indeg[e.v]++;
        ArrayDeque<Integer> q = new ArrayDeque<>();
        for (int v = 0; v < g.n; v++) if (indeg[v] == 0) { q.add(v); r.queueOps++; }
        while (!q.isEmpty()) {
            int u = q.poll();
            r.queueOps++;
            r.order.add(u);
            for (Edge e : g.adj.get(u)) {
                r.edgeExaminations++;
                if (--indeg[e.v] == 0) { q.add(e.v); r.queueOps++; }
            }
        }
        r.isDag = r.order.size() == g.n;
        return r;
    }
    public static boolean isValidOrder(Graph g, List<Integer> order) {
        if (order.size() != g.n) return false;
        int[] pos = new int[g.n];
        Arrays.fill(pos, -1);
        for (int i = 0; i < order.size(); i++) {
            int v = order.get(i);
            if (pos[v] != -1) return false;
            pos[v] = i;
        }
        for (Edge e : g.edges) if (pos[e.u] >= pos[e.v]) return false;
        return true;
    }
}
