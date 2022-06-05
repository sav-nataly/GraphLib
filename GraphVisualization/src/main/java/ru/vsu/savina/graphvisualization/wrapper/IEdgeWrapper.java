package ru.vsu.savina.graphvisualization.wrapper;

import ru.vsu.savina.graphalgorithms.model.IEdge;
import ru.vsu.savina.graphalgorithms.model.VertexType;
import ru.vsu.savina.graphvisualization.graphics.Point;

import java.util.List;
import java.util.Map;

public interface IEdgeWrapper {
    Map<VertexType, ? extends IVertexWrapper> getVertexMap();

    IEdge getEdge();
    void setEdge(IEdge edge);

    List<Point> getPoints();
    void setPoints(List<Point> points);

    IVertexWrapper getTarget();
    IVertexWrapper getSource();
}
