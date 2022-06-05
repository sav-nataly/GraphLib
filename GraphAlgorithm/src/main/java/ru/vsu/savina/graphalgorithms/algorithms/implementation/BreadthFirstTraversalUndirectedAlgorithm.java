package ru.vsu.savina.graphalgorithms.algorithms.implementation;

import ru.vsu.savina.graphalgorithms.algorithms.IGraphTraversalAlgorithm;
import ru.vsu.savina.graphalgorithms.model.EdgeType;
import ru.vsu.savina.graphalgorithms.model.IGraph;
import ru.vsu.savina.graphalgorithms.model.IVertex;
import ru.vsu.savina.graphalgorithms.model.implementation.UndirectedEdge;
import ru.vsu.savina.graphalgorithms.model.implementation.UndirectedVertex;

import java.util.*;

public class BreadthFirstTraversalUndirectedAlgorithm implements IGraphTraversalAlgorithm {
    @Override
    public List<UndirectedVertex> execute(IGraph graph, IVertex startVertex) {
        List<UndirectedVertex> path = new ArrayList<>();

        Deque<UndirectedVertex> q = new ArrayDeque<>();
        Set<IVertex> used = new HashSet<>();

        q.addFirst((UndirectedVertex) startVertex);

        while (!q.isEmpty()) {
            UndirectedVertex vertex = q.pop();

            if (!used.contains(vertex)) {
                path.add(vertex);

                used.add(vertex);

                for (Map.Entry<UndirectedEdge, EdgeType> entry : vertex.getEdgeMap().entrySet()) {
                    if (!used.contains(entry.getKey().getTarget()))
                        q.add(entry.getKey().getTarget());

                    if (!used.contains(entry.getKey().getSource()))
                        q.add(entry.getKey().getSource());
                }
            }
        }

        return path;
    }
}
