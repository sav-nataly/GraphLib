package ru.vsu.savina.graphvisualization.wrapper;

import ru.vsu.savina.graphalgorithms.model.IEdge;
import ru.vsu.savina.graphalgorithms.model.IGraph;
import ru.vsu.savina.graphalgorithms.model.IVertex;
import ru.vsu.savina.graphalgorithms.model.UndirectedGraph;

import java.util.ArrayList;
import java.util.List;

public class UndirectedGraphWrapper implements IGraphWrapper{
    private UndirectedGraph graph;
    private List<UndirectedVertexWrapper> vertexList = new ArrayList<>();
    private List<UndirectedEdgeWrapper> edgeList = new ArrayList<>();

    public UndirectedGraphWrapper(UndirectedGraph graph) {
        this.graph = graph;
    }

    @Override
    public IGraph getGraph() {
        return graph;
    }

    @Override
    public IEdgeWrapper getEdge(IEdge edge) {
        return edgeList.stream().filter(e -> e.getEdge().equals(edge)).findFirst().orElse(null);
    }

    @Override
    public List<UndirectedVertexWrapper> getVertices() {
        return vertexList;
    }


    @Override
    public List<UndirectedEdgeWrapper> getEdges() {
        return edgeList;
    }

    @Override
    public UndirectedVertexWrapper getVertex(IVertex vertex) {
        return vertexList.stream().filter(v -> v.getVertex().equals(vertex)).findFirst().orElse(null);
    }
}
