package ru.vsu.savina.graphalgorithms.algorithms.implementation;

import ru.vsu.savina.graphalgorithms.algorithms.IShortestPathAlgorithm;
import ru.vsu.savina.graphalgorithms.model.EdgeType;
import ru.vsu.savina.graphalgorithms.model.IEdge;
import ru.vsu.savina.graphalgorithms.model.IGraph;
import ru.vsu.savina.graphalgorithms.model.IVertex;

import java.util.*;

public class DijkstraAlgorithm implements IShortestPathAlgorithm {
    private static List<IVertex> getPath(Map<IVertex, IVertex> parent, IVertex startVertex, IVertex target) {
        List<IVertex> path = new ArrayList<>();
        IVertex curr = target;

        while (!curr.equals(startVertex)) {
            path.add(curr);
            curr = parent.get(curr);
        }

        path.add(startVertex);
        Collections.reverse(path);

        return path;
    }

    private static void initializeMap(Map<IVertex, Double> distance, Map<IVertex, IVertex> parent, IGraph graph, IVertex startVertex) {
        double INF = (double) Integer.MAX_VALUE / 2;

        for (IVertex v : graph.getVertexList()) {
            if (v.equals(startVertex))
                distance.put(v, 0.0);
            else
                distance.put(v, INF);

            parent.put(v, v);
        }
    }

    @Override
    public List<? extends IVertex> execute(IGraph graph, IVertex startVertex, IVertex targetVertex) {
        int INF = Integer.MAX_VALUE / 2;

        Set<IVertex> used = new HashSet<>();
        Map<IVertex, Double> distance = new HashMap<>();
        Map<IVertex, IVertex> parent = new HashMap<>();

        initializeMap(distance, parent, graph, startVertex);

        for (IVertex v : graph.getVertexList()) {
            IVertex v1 = null;

            for (IVertex u : graph.getVertexList()) {
                if (!used.contains(u) && (v1 == null || distance.get(u) < distance.get(v1)))
                    v1 = u;
            }

            if (distance.get(v1) == INF)
                break;

            used.add(v1);

            for (Map.Entry<? extends IEdge, EdgeType> entry : v.getEdgeMap().entrySet()) {
                IVertex opposite = entry.getKey().getOpposite(v);

                double weight;
                if (entry.getKey().getMetadata().containsKey("weight"))
                    weight = (double) entry.getKey().getMetadata().get("weight");
                else
                    weight = 0;


                if (distance.get(v) + weight < distance.get(opposite)) {
                    distance.replace(opposite, distance.get(v) + weight);
                    parent.replace(opposite, v);
                }
            }
        }
        return getPath(parent, startVertex, targetVertex);
    }
}
