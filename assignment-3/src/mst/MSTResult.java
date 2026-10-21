package mst;

import graph.Edge;
import java.util.ArrayList;
import java.util.List;

public class MSTResult {
    public final List<Edge> edges = new ArrayList<>();
    public long totalWeight;
    public boolean connected;
    public long edgesExamined, comparisons;
    public long dsuFinds, dsuUnions, dsuPathSteps;
    public long heapPushes, heapPops;
}
