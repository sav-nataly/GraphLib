package ru.vsu.savina.graphalgorithms.model;

import java.util.Map;

public interface IEdge {
    Map<VertexType, ? extends IVertex> getVertexMap();
    Map<String, Object> getMetadata();
    void setMetadata(Map<String, Object> metadata);

    IVertex getSource();
    IVertex getTarget();

    IVertex getOpposite(IVertex vertex);
}
