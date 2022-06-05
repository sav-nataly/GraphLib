package ru.vsu.savina.graphalgorithms.algorithms.implementation;

import ru.vsu.savina.graphalgorithms.algorithms.IGraphTraversalAlgorithm;
import ru.vsu.savina.graphalgorithms.model.*;

import java.util.*;

public class DepthFirstTraversalDirectedAlgorithm implements IGraphTraversalAlgorithm {

    @Override
    public List<DirectedVertex> execute(IGraph graph, IVertex startVertex) {
        List<DirectedVertex> path = new ArrayList<>();

        Deque<DirectedVertex> q = new ArrayDeque<>();
        Set<IVertex> used = new HashSet<>();

        q.addFirst((DirectedVertex) startVertex);

        while (!q.isEmpty()) {
            DirectedVertex vertex = q.removeFirst();

            if (!used.contains(vertex)) {
                path.add(vertex);

                used.add(vertex);

                for (Map.Entry<DirectedEdge, EdgeType> entry : vertex.getEdgeMap().entrySet()) {
                    if (entry.getValue() == EdgeType.OUT)
                        q.addFirst(entry.getKey().getTarget());
                }
            }
        }

        return path;
    }
}
