package ru.vsu.savina.graphalgorithms.algorithms.implementation;

import ru.vsu.savina.graphalgorithms.algorithms.IGraphTraversalAlgorithm;
import ru.vsu.savina.graphalgorithms.model.EdgeType;
import ru.vsu.savina.graphalgorithms.model.IGraph;
import ru.vsu.savina.graphalgorithms.model.IVertex;
import ru.vsu.savina.graphalgorithms.model.implementation.DirectedEdge;
import ru.vsu.savina.graphalgorithms.model.implementation.DirectedVertex;

import java.util.*;

public class BreadthFirstTraversalDirectedAlgorithm implements IGraphTraversalAlgorithm {
    @Override
    public List<DirectedVertex> execute(IGraph graph, IVertex startVertex) {
        List<DirectedVertex> path = new ArrayList<>();

        Deque<DirectedVertex> q = new ArrayDeque<>();
        Set<IVertex> used = new HashSet<>();

        q.addFirst((DirectedVertex) startVertex);

        while (!q.isEmpty()) {
            DirectedVertex vertex = q.pop();

            if (!used.contains(vertex))
                path.add(vertex);

            used.add(vertex);

            for (Map.Entry<DirectedEdge, EdgeType> entry : vertex.getEdgeMap().entrySet()) {
                if (!used.contains(entry.getKey().getTarget()))
                    q.add(entry.getKey().getTarget());

                if (!used.contains(entry.getKey().getSource()))
                    q.add(entry.getKey().getSource());
            }
        }

        return path;
    }
}
