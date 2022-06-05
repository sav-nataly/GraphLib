package ru.vsu.savina.graphalgorithms.model;

import java.util.List;
import java.util.UUID;

public interface IGraph {
    List<? extends IVertex> getVertexList();

    List<? extends IEdge> getEdgeList();

    void addVertex(IVertex vertex);
    void addVertex(UUID uid);
    void addVertex();
    void deleteVertex(UUID uid);

    void addEdge(IEdge edge);
    void addEdge(IVertex v1, IVertex v2);
    void addEdge(UUID uid1, UUID uid2);
    void deleteEdge(UUID uid1, UUID uid2);

    IVertex getVertex(UUID uid);
    IEdge getEdge(UUID uid1, UUID uid2);
    IEdge getEdge(IVertex v1, IVertex v2);

    int getGraphSize();
    int getVertexIndex(IVertex vertex);

    List<List<Integer>> getAdjustmentList();
}
