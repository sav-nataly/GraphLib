package ru.vsu.savina.graphalgorithms.algorithms.implementation;

import ru.vsu.savina.graphalgorithms.algorithms.IVertexSortingAlgorithm;
import ru.vsu.savina.graphalgorithms.model.EdgeType;
import ru.vsu.savina.graphalgorithms.model.IEdge;
import ru.vsu.savina.graphalgorithms.model.IGraph;
import ru.vsu.savina.graphalgorithms.model.IVertex;
import ru.vsu.savina.graphalgorithms.model.implementation.DirectedGraph;

import java.util.*;

public class TopologicalSortingAlgorithm implements IVertexSortingAlgorithm {

    @Override
    public IGraph execute(IGraph graph) {
        if (!graph.getClass().isAssignableFrom(DirectedGraph.class))
            return null;

        Set<IVertex> visited = new HashSet<>();
        Stack<IVertex> stack = new Stack<>();

        Map<IVertex, Integer> color = new HashMap<>();

        for (IVertex v : graph.getVertexList()) {
            color.put(v, 0);
        }

        for (IVertex v : graph.getVertexList())
            if (color.get(v) == 0)
                topologicalSort(v, visited, stack, color);
        fillVertexList(graph, stack);

        return graph;
    }

    private void topologicalSort(IVertex v, Set<IVertex> visited, Stack<IVertex> stack, Map<IVertex, Integer> color) {
        color.replace(v, 1);

        visited.add(v);
        for (Map.Entry<? extends IEdge, EdgeType> entry : v.getEdgeMap().entrySet()) {
            IVertex v1 = entry.getKey().getTarget();
            if (color.get(v1) == 0) {
                topologicalSort(v1, visited, stack, color);
            }
        }
        color.replace(v, 2);
        stack.push(v);
    }

    private void fillVertexList(IGraph graph, Stack<IVertex> stack) {
        graph.getVertexList().clear();
        while (!stack.empty())
            graph.addVertex(stack.pop());
    }
}
