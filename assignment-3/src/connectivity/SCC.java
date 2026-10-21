package connectivity;

import graph.Edge;
import graph.Graph;
import java.util.*;

public class SCC {
    public static class Result {
        public final List<List<Integer>> components = new ArrayList<>();
        public int[] componentId;
        public long edgeExaminations;
    }

    public static Result run(Graph g) {
        Result r = new Result();
        int n = g.n;
        boolean[] vis = new boolean[n];
        int[] idx = new int[n];
        List<Integer> finish = new ArrayList<>();
        Deque<Integer> st = new ArrayDeque<>();
        for (int s = 0; s < n; s++) {
            if (vis[s]) continue;
            vis[s] = true;
            st.push(s);
            while (!st.isEmpty()) {
                int u = st.peek();
                if (idx[u] < g.adj.get(u).size()) {
                    Edge e = g.adj.get(u).get(idx[u]++);
                    r.edgeExaminations++;
                    if (!vis[e.v]) { vis[e.v] = true; st.push(e.v); }
                } else { st.pop(); finish.add(u); }
            }
        }
        List<List<Integer>> rev = new ArrayList<>();
        for (int i = 0; i < n; i++) rev.add(new ArrayList<>());
        for (Edge e : g.edges) rev.get(e.v).add(e.u);
        r.componentId = new int[n];
        Arrays.fill(r.componentId, -1);
        for (int i = n - 1; i >= 0; i--) {
            int s = finish.get(i);
            if (r.componentId[s] != -1) continue;
            int id = r.components.size();
            List<Integer> comp = new ArrayList<>();
            r.componentId[s] = id;
            st.push(s);
            while (!st.isEmpty()) {
                int u = st.pop();
                comp.add(u);
                for (int v : rev.get(u)) {
                    r.edgeExaminations++;
                    if (r.componentId[v] == -1) { r.componentId[v] = id; st.push(v); }
                }
            }
            r.components.add(comp);
        }
        return r;
    }
}
