package ru.vsu.savina.graphalgorithms.algorithms;

import ru.vsu.savina.graphalgorithms.model.IGraph;
import ru.vsu.savina.graphalgorithms.model.IVertex;

import java.util.List;

public interface IGraphTraversalAlgorithm {
    List<? extends IVertex> execute(IGraph graph, IVertex startVertex);
}
