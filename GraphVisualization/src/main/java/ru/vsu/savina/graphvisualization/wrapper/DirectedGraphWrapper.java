package ru.vsu.savina.graphvisualization.wrapper;

import ru.vsu.savina.graphalgorithms.model.DirectedGraph;
import ru.vsu.savina.graphalgorithms.model.IEdge;
import ru.vsu.savina.graphalgorithms.model.IGraph;
import ru.vsu.savina.graphalgorithms.model.IVertex;

import java.util.ArrayList;
import java.util.List;

public class DirectedGraphWrapper implements IGraphWrapper {
    private DirectedGraph graph;
    private List<DirectedVertexWrapper> vertexList = new ArrayList<>();
    private List<DirectedEdgeWrapper> edgeList = new ArrayList<>();

    public DirectedGraphWrapper(DirectedGraph graph) {
        this.graph = graph;
    }

    @Override
    public IGraph getGraph() {
        return graph;
    }

    @Override
    public List<DirectedVertexWrapper> getVertices() {
        return vertexList;
    }

    @Override
    public List<DirectedEdgeWrapper> getEdges() {
        return edgeList;
    }

    @Override
    public IEdgeWrapper getEdge(IEdge edge) {
        return edgeList.stream().filter(e -> e.getEdge().equals(edge)).findFirst().orElse(null);
    }

    @Override
    public DirectedVertexWrapper getVertex(IVertex vertex) {
        return vertexList.stream().filter(v -> v.getVertex().equals(vertex)).findFirst().orElse(null);
    }
}
