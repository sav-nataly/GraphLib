package ru.vsu.savina.graphalgorithms.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DirectedVertex implements IVertex {
    private final UUID uid;
    private Map<String, Object> metadata = new HashMap<>();
    private Map<DirectedEdge, EdgeType> edgeMap = new HashMap<>();

    public DirectedVertex(UUID uid) {
        this.uid = uid;
    }

    public DirectedVertex() {
        this.uid = UUID.randomUUID();
    }

    @Override
    public UUID getUid() {
        return uid;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    @Override
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public Map<DirectedEdge, EdgeType> getEdgeMap() {
        return edgeMap;
    }

    @Override
    public void setEdgeMap(Map<? extends IEdge, EdgeType> edgeMap) {
        for (Map.Entry<? extends IEdge, EdgeType> entry : edgeMap.entrySet()) {
            this.edgeMap.put((DirectedEdge) entry.getKey(), entry.getValue());
        }
    }
}
