package ru.vsu.savina.graphvisualization.wrapper;

import ru.vsu.savina.graphalgorithms.model.IEdge;
import ru.vsu.savina.graphalgorithms.model.UndirectedEdge;
import ru.vsu.savina.graphalgorithms.model.VertexType;
import ru.vsu.savina.graphvisualization.graphics.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UndirectedEdgeWrapper implements IEdgeWrapper {
    private UndirectedEdge edge;
    private List<Point> points = new ArrayList<>();
    private Map<VertexType, UndirectedVertexWrapper> vertexMap = new HashMap<>();

    public UndirectedEdgeWrapper(UndirectedEdge edge, List<Point> points) {
        this.edge = edge;
        this.points = points;
    }

    public UndirectedEdgeWrapper(UndirectedEdge edge) {
        this.edge = edge;
    }

    @Override
    public Map<VertexType, UndirectedVertexWrapper> getVertexMap() {
        return vertexMap;
    }

    @Override
    public IEdge getEdge() {
        return edge;
    }

    @Override
    public void setEdge(IEdge edge) {
        this.edge = (UndirectedEdge) edge;
    }

    @Override
    public List<Point> getPoints() {
        return points;
    }

    @Override
    public void setPoints(List<Point> points) {
        this.points = points;
    }

    @Override
    public IVertexWrapper getTarget() {
        return vertexMap.get(VertexType.TARGET);
    }

    @Override
    public IVertexWrapper getSource() {
        return vertexMap.get(VertexType.SOURCE);
    }
}
