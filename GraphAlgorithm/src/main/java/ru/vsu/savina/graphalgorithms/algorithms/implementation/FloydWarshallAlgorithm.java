package ru.vsu.savina.graphalgorithms.algorithms.implementation;

import ru.vsu.savina.graphalgorithms.algorithms.IShortestPathAlgorithm;
import ru.vsu.savina.graphalgorithms.model.DirectedGraph;
import ru.vsu.savina.graphalgorithms.model.IEdge;
import ru.vsu.savina.graphalgorithms.model.IGraph;
import ru.vsu.savina.graphalgorithms.model.IVertex;

import java.util.ArrayList;
import java.util.List;

public class FloydWarshallAlgorithm implements IShortestPathAlgorithm {
    private final double INF = Integer.MAX_VALUE;

    @Override
    public List<? extends IVertex> execute(IGraph graph, IVertex startVertex, IVertex targetVertex) {
        if (!graph.getClass().isAssignableFrom(DirectedGraph.class))
            return null;

        double[][] distance = new double[graph.getGraphSize()][graph.getGraphSize()];
        int[][] next = new int[graph.getGraphSize()][graph.getGraphSize()];

        initWeightMatrix(distance, next, graph);
        int n = graph.getGraphSize();
        for (int i = 0; i < n; i++) {
            for (int u = 0; u < n; u++) {
                for (int v = 0; v < n; v++) {
                    if (distance[u][i] + distance[i][v] < distance[u][v]) {
                        distance[u][v] = distance[u][i] + distance[i][v];
                        next[u][v] = next[u][i];
                    }
                }
            }
        }
        return getPath(graph, startVertex, targetVertex, distance, next);
    }

    private List<IVertex> getPath(IGraph graph, IVertex startVertex, IVertex targetVertex, double[][] distance, int[][] next) {
        int u = graph.getVertexList().indexOf(startVertex);
        int v = graph.getVertexList().indexOf(targetVertex);

        if (distance[u][v] == INF) {
            return null;
        }
        int c = u;
        List<IVertex> path = new ArrayList<>();

        while (c != v) {
            path.add(graph.getVertexList().get(c));
            c = next[c][v];
        }
        path.add(targetVertex);
        return path;
    }

    private void initWeightMatrix(double[][] distance, int[][] next, IGraph graph) {

        for (int i = 0; i < distance.length; i++) {
            for (int j = 0; j < distance.length; j++) {
                if (i == j)
                    distance[i][j] = 0;
                else {
                    IEdge edge = graph.getEdge(graph.getVertexList().get(i), graph.getVertexList().get(j));
                    if (edge == null) {
                        distance[i][j] = INF;
                    } else {
                        double weight;
                        if (edge.getMetadata().containsKey("weight"))
                            weight = (double) edge.getMetadata().get("weight");
                        else
                            weight = 0;
                        next[i][j] = j;
                        distance[i][j] = weight;
                    }
                }
            }
        }
    }
}
