package ru.vsu.savina.graphalgorithms.algorithms.implementation;

import ru.vsu.savina.graphalgorithms.algorithms.ISpanningTreeAlgorithm;
import ru.vsu.savina.graphalgorithms.model.*;

import java.util.*;

public class PrimAlgorithm implements ISpanningTreeAlgorithm {
    private static double getWeight(IEdge edge) {
        if (edge.getMetadata().containsKey("weight")) {
            return (double) edge.getMetadata().get("weight");
        }
        return 0.0;
    }

    private static UndirectedGraph createGraph(IGraph graph, Map<IVertex, IVertex> edges) {
        UndirectedGraph graph1 = new UndirectedGraph();

        for (Map.Entry<IVertex, IVertex> entry : edges.entrySet()) {
            if (entry.getValue() != null) {

                UndirectedVertex v = new UndirectedVertex(entry.getKey().getUid());
                UndirectedVertex v2 = new UndirectedVertex(entry.getValue().getUid());

                v.setMetadata(entry.getKey().getMetadata());
                v2.setMetadata(entry.getValue().getMetadata());

                graph1.addVertex(v);
                graph1.addVertex(v2);

                IEdge e = new UndirectedEdge(new HashMap<>() {
                    {
                        put(VertexType.SOURCE, (UndirectedVertex) graph1.getVertex(v.getUid()));
                        put(VertexType.TARGET, (UndirectedVertex) graph1.getVertex(v2.getUid()));
                    }
                }, graph.getEdge(entry.getKey(), entry.getValue()).getMetadata());
                graph1.addEdge(e);
            }
        }

        return graph1;

    }

    @Override
    public UndirectedGraph execute(UndirectedGraph graph, IVertex root) {
        double INF = Integer.MAX_VALUE;

        Set<IVertex> used = new HashSet<>();
        Map<IVertex, Double> minWeight = new HashMap<>();
        Map<IVertex, IVertex> sel = new HashMap<>();
        Map<IVertex, IVertex> edge = new HashMap<>();
        Map<IVertex, IVertex> edges = new HashMap<>();

        Map<IVertex, Double> key = new HashMap<>();
        Map<IVertex, IVertex> parent = new HashMap<>();

        List<IVertex> vertices = new ArrayList<>();


        for (IVertex v : graph.getVertexList()) {
            minWeight.put(v, INF);
            sel.put(v, null);

            key.put(v, INF);
            parent.put(v, null);
            vertices.add(v);
        }


        key.replace(root, 0.0);

        while (!vertices.isEmpty()) {
            UndirectedVertex v = (UndirectedVertex) vertices.stream().min((v1, v2) -> (int) (key.get(v1) - key.get(v2))).get();
            vertices.remove(v);

            for (Map.Entry<UndirectedEdge, EdgeType> entry : v.getEdgeMap().entrySet()) {
                UndirectedVertex u = (UndirectedVertex) entry.getKey().getOpposite(v);

                if (vertices.contains(u) && key.get(u) > getWeight(entry.getKey())) {
                    parent.replace(u, v);
                    key.replace(u, getWeight(entry.getKey()));

                }
            }

            if (edges.containsKey(v))
                edges.replace(v, parent.get(v));
            else
                edges.put(v, parent.get(v));

        }


        minWeight.replace(graph.getVertexList().get(0), 0.0);
        for (IVertex v : graph.getVertexList()) {
            IVertex v1 = null;

            for (IVertex j : graph.getVertexList()) {
                if (!used.contains(j) && (v1 == null || minWeight.get(j) < minWeight.get(v1)))
                    v1 = j;
            }
            if (minWeight.get(v1) == INF)
                return null;


            used.add(v1);

            if (sel.get(v1) != null) {
                edge.put(v1, sel.get(v1));
            }

            for (IVertex to : graph.getVertexList()) {
                IEdge edge1 = graph.getEdge(v1, to);
                double weight = (edge1 == null) ? INF : getWeight(edge1);
                if (weight < minWeight.get(to)) {
                    minWeight.replace(to, weight);
                    sel.replace(to, v1);
                }
            }
        }

        return createGraph(graph, edges);
    }
}
