package ru.vsu.savina.graphalgorithms.model;

import java.util.HashMap;
import java.util.Map;

public class DirectedEdge implements IEdge {
    private final Map<VertexType, DirectedVertex> vertexMap;
    private Map<String, Object> metadata = new HashMap<>();

    public DirectedEdge(Map<VertexType, DirectedVertex> vertexMap, Map<String, Object> metadata) {
        this.vertexMap = vertexMap;
        this.metadata = metadata;
    }

    public DirectedEdge(Map<VertexType, DirectedVertex> vertexMap) {
        this.vertexMap = vertexMap;
    }

    @Override
    public DirectedVertex getSource() {
        return vertexMap.get(VertexType.SOURCE);
    }

    @Override
    public DirectedVertex getTarget() {
        return vertexMap.get(VertexType.TARGET);
    }

    @Override
    public Map<VertexType, DirectedVertex> getVertexMap() {
        return vertexMap;
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
    public IVertex getOpposite(IVertex vertex) {
        VertexType t = vertexMap.entrySet().stream().filter(entry -> vertex.equals(entry.getValue())).map(Map.Entry::getKey).findFirst().orElse(null);

        if (t == VertexType.SOURCE)
            return getTarget();
        return getSource();
    }
}
