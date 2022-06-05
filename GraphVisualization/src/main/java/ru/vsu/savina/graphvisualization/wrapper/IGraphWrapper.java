package ru.vsu.savina.graphvisualization.wrapper;

import ru.vsu.savina.graphalgorithms.model.IEdge;
import ru.vsu.savina.graphalgorithms.model.IGraph;
import ru.vsu.savina.graphalgorithms.model.IVertex;

import java.util.List;

public interface IGraphWrapper {
    IGraph getGraph();

    List<? extends IVertexWrapper> getVertices();

    List<? extends IEdgeWrapper> getEdges();

    IEdgeWrapper getEdge(IEdge edge);
    IVertexWrapper getVertex(IVertex vertex);
}
