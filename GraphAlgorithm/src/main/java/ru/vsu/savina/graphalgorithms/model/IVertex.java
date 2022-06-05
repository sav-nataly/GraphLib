package ru.vsu.savina.graphalgorithms.model;

import java.util.Map;
import java.util.UUID;

public interface IVertex {
    UUID getUid();

    Map<String, Object> getMetadata();

    void setMetadata(Map<String, Object> metadata);

    Map<? extends IEdge, EdgeType> getEdgeMap();

    void setEdgeMap(Map<? extends IEdge, EdgeType> edgeMap);
}
