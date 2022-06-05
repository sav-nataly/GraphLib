package ru.vsu.savina.graphalgorithms.model;

import java.util.HashMap;
import java.util.Map;

public class UndirectedEdge implements IEdge {
    private final Map<VertexType, UndirectedVertex> vertexMap;
    private Map<String, Object> metadata = new HashMap<>();

    public UndirectedEdge(Map<VertexType, UndirectedVertex> vertexMap, Map<String, Object> metadata) {
        this.vertexMap = vertexMap;
        this.metadata = metadata;
    }

    public UndirectedEdge(Map<VertexType, UndirectedVertex> vertexMap) {
        this.vertexMap = vertexMap;
    }

    public UndirectedVertex getSource() {
        return vertexMap.get(VertexType.SOURCE);
    }

    public UndirectedVertex getTarget() {
        return vertexMap.get(VertexType.TARGET);
    }

    @Override
    public Map<VertexType, ? extends IVertex> getVertexMap() {
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
