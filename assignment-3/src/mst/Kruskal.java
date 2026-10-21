package mst;

import dsu.DSU;
import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.List;

public class Kruskal {
    public static MSTResult run(Graph g) { return run(g, true, true); }

    public static MSTResult run(Graph g, boolean compression, boolean byRank) {
        MSTResult r = new MSTResult();
        List<Edge> sorted = new ArrayList<>(g.edges);
        final long[] cmp = {0};
        sorted.sort((a, b) -> { cmp[0]++; return Integer.compare(a.w, b.w); });
        DSU dsu = new DSU(g.n, compression, byRank);
        for (Edge e : sorted) {
            r.edgesExamined++;
            if (dsu.union(e.u, e.v)) {
                r.edges.add(e);
                r.totalWeight += e.w;
                if (r.edges.size() == g.n - 1) break;
            }
        }
        r.comparisons = cmp[0];
        r.dsuFinds = dsu.findCalls;
        r.dsuUnions = dsu.unionCalls;
        r.dsuPathSteps = dsu.pathSteps;
        r.connected = dsu.components == 1;
        return r;
    }
}
