package ru.vsu.savina.graphvisualization.wrapper;

import ru.vsu.savina.graphalgorithms.model.IVertex;
import ru.vsu.savina.graphalgorithms.model.UndirectedVertex;

public class UndirectedVertexWrapper implements IVertexWrapper{
    private UndirectedVertex vertex;
    private double x;
    private double y;

    public UndirectedVertexWrapper(UndirectedVertex vertex, double x, double y) {
        this.vertex = vertex;
        this.x = x;
        this.y = y;
    }

    public UndirectedVertexWrapper(UndirectedVertex vertex) {
        this.vertex = vertex;
    }

    public UndirectedVertexWrapper() {
        vertex = null;
    }

    @Override
    public IVertex getVertex() {
        return vertex;
    }

    @Override
    public void setVertex(IVertex vertex) {
        this.vertex = (UndirectedVertex) vertex;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }
}
