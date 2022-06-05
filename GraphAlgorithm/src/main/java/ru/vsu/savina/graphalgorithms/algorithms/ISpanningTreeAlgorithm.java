package ru.vsu.savina.graphalgorithms.algorithms;

import ru.vsu.savina.graphalgorithms.model.IVertex;
import ru.vsu.savina.graphalgorithms.model.UndirectedGraph;

public interface ISpanningTreeAlgorithm {
    UndirectedGraph execute(UndirectedGraph graph, IVertex root);
}
