package experiments;

import connectivity.SCC;
import connectivity.TopologicalSort;
import graph.Graph;
import graph.GraphGenerator;
import mst.Kruskal;
import mst.MSTResult;
import mst.Prim;
import shortestpath.Dijkstra;

import java.io.PrintWriter;
import java.util.function.Supplier;


public class Benchmark {
    static final int RUNS = 5, WARMUP = 3, MAX_W = 100;
    static final long SEED_UNDIRECTED = 42, SEED_DIRECTED = 43, SEED_DAG = 44;
    static final String[] NAMES = {"Small", "Medium", "Large"};
    static final int[] V = {10, 100, 1000}, E = {30, 500, 5000};

    static PrintWriter out;

    static void bench(String algo, String graphName, int v, int e, Supplier<String> task) {
        for (int i = 0; i < WARMUP; i++) task.get();
        long total = 0;
        String counters = "";
        for (int i = 0; i < RUNS; i++) {
            long t0 = System.nanoTime();
            counters = task.get();
            total += System.nanoTime() - t0;
        }
        double avgUs = total / (double) RUNS / 1000.0;
        long ops = 0;
        for (String kv : counters.split(";")) {
            String[] p = kv.split("=");
            if (!p[0].equals("weight") && !p[0].equals("scc") && !p[0].equals("dag") && !p[0].equals("reachable")) ops += Long.parseLong(p[1]);
        }
        out.printf("%s,%s,%d,%d,%.3f,%d,%s%n", algo, graphName, v, e, avgUs, ops, counters);
        System.out.printf("%-26s %-7s V=%-5d E=%-5d %10.2f us  ops=%d  [%s]%n", algo, graphName, v, e, avgUs, ops, counters);
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "results/tables/results.csv";
        out = new PrintWriter(path);
        out.println("algorithm,graph,V,E,avg_time_us,total_ops,counters");
        for (int i = 0; i < 3; i++) {
            int v = V[i], e = E[i];
            String name = NAMES[i];
            Graph und = GraphGenerator.connectedUndirected(v, e, SEED_UNDIRECTED, MAX_W);
            Graph dir = GraphGenerator.randomDirected(v, e, SEED_DIRECTED, MAX_W);
            Graph dag = GraphGenerator.randomDag(v, e, SEED_DAG, MAX_W);

            bench("Kruskal", name, v, e, () -> {
                MSTResult r = Kruskal.run(und);
                return "weight=" + r.totalWeight + ";edges_examined=" + r.edgesExamined + ";comparisons=" + r.comparisons
                        + ";dsu_finds=" + r.dsuFinds + ";dsu_unions=" + r.dsuUnions;
            });
            bench("Prim", name, v, e, () -> {
                MSTResult r = Prim.run(und);
                return "weight=" + r.totalWeight + ";edges_examined=" + r.edgesExamined + ";comparisons=" + r.comparisons
                        + ";heap_pushes=" + r.heapPushes + ";heap_pops=" + r.heapPops;
            });
            boolean[][] variants = {{false, false}, {true, false}, {false, true}, {true, true}};
            String[] vnames = {"Kruskal-DSU-naive", "Kruskal-DSU-compression", "Kruskal-DSU-rank", "Kruskal-DSU-both"};
            for (int k = 0; k < 4; k++) {
                final boolean c = variants[k][0], rk = variants[k][1];
                bench(vnames[k], name, v, e, () -> {
                    MSTResult r = Kruskal.run(und, c, rk);
                    return "weight=" + r.totalWeight + ";dsu_finds=" + r.dsuFinds + ";dsu_path_steps=" + r.dsuPathSteps;
                });
            }
            bench("Topo-DAG", name, v, e, () -> {
                TopologicalSort.Result r = TopologicalSort.run(dag);
                return "dag=" + r.isDag + ";edge_examinations=" + r.edgeExaminations + ";queue_ops=" + r.queueOps;
            });
            bench("Topo-Cyclic", name, v, e, () -> {
                TopologicalSort.Result r = TopologicalSort.run(dir);
                return "dag=" + r.isDag + ";edge_examinations=" + r.edgeExaminations + ";queue_ops=" + r.queueOps;
            });
            bench("SCC-Directed", name, v, e, () -> {
                SCC.Result r = SCC.run(dir);
                return "scc=" + r.components.size() + ";edge_examinations=" + r.edgeExaminations;
            });
            bench("SCC-DAG", name, v, e, () -> {
                SCC.Result r = SCC.run(dag);
                return "scc=" + r.components.size() + ";edge_examinations=" + r.edgeExaminations;
            });
            bench("Dijkstra", name, v, e, () -> {
                Dijkstra d = new Dijkstra(und, 0);
                return "reachable=" + d.reachableCount() + ";edge_examinations=" + d.edgeExaminations + ";relaxations="
                        + d.successfulRelaxations + ";heap_pushes=" + d.heapPushes + ";heap_pops=" + d.heapPops;
            });
        }
        out.close();
        System.out.println("Wrote " + path);
    }
}
