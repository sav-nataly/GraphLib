package ru.vsu.savina.graphvisualization.wrapper;

import ru.vsu.savina.graphalgorithms.model.IVertex;

public interface IVertexWrapper {
    IVertex getVertex();
    void setVertex(IVertex vertex);

    double getX();
    void setX(double x);

    double getY();
    void setY(double y);
}
