package dsu;

public class DSU {
    private final int[] parent, rank;
    private final boolean compression, byRank;
    public int components;
    public long findCalls, unionCalls, pathSteps;

    public DSU(int n) { this(n, true, true); }

    public DSU(int n, boolean compression, boolean byRank) {
        this.compression = compression;
        this.byRank = byRank;
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
        components = n;
    }

    public int find(int x) {
        findCalls++;
        int root = x;
        while (parent[root] != root) { root = parent[root]; pathSteps++; }
        if (compression) {
            while (parent[x] != root) { int next = parent[x]; parent[x] = root; x = next; }
        }
        return root;
    }

    public boolean union(int a, int b) {
        unionCalls++;
        int ra = find(a), rb = find(b);
        if (ra == rb) return false;
        if (byRank) {
            if (rank[ra] < rank[rb]) { int t = ra; ra = rb; rb = t; }
            parent[rb] = ra;
            if (rank[ra] == rank[rb]) rank[ra]++;
        } else {
            parent[rb] = ra;
        }
        components--;
        return true;
    }

    public boolean connected(int a, int b) { return find(a) == find(b); }
}
