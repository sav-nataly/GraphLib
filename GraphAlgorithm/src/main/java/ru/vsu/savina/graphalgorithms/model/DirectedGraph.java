package ru.vsu.savina.graphalgorithms.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DirectedGraph implements IGraph {
    private List<DirectedVertex> vertexList = new ArrayList<>();
    private List<DirectedEdge> edgeList = new ArrayList<>();

    public DirectedGraph(List<DirectedVertex> vertexList, List<DirectedEdge> edgeList) {
        this.vertexList = vertexList;
        this.edgeList = edgeList;
    }

    public DirectedGraph() {
    }

    @Override
    public List<List<Integer>> getAdjustmentList() {
        List<List<Integer>> adjList = new ArrayList<>();

        for (int i = 0; i < getVertexList().size(); i++) {
            adjList.add(new ArrayList<>());
        }

        for (IEdge e : getEdgeList()) {
            adjList.get(getVertexIndex(((DirectedEdge) e).getSource()))
                    .add(getVertexIndex(((DirectedEdge) e).getTarget()));
        }

        return adjList;
    }

    @Override
    public List<? extends IVertex> getVertexList() {
        return vertexList;
    }

    @Override
    public List<? extends IEdge> getEdgeList() {
        return edgeList;
    }

    @Override
    public void addVertex(IVertex vertex) {
        DirectedVertex v = (DirectedVertex) vertex;

        if (!vertexList.contains(v) && vertexList.stream().noneMatch(v1 -> v1.getUid().equals(v.getUid())))
            vertexList.add((DirectedVertex) vertex);
    }

    @Override
    public void addVertex(UUID uid) {
        DirectedVertex v = (DirectedVertex) getVertex(uid);
        if (v == null)
            vertexList.add(new DirectedVertex(uid));
    }

    @Override
    public void addVertex() {
        vertexList.add(new DirectedVertex());
    }

    @Override
    public void deleteVertex(UUID uid) {
        vertexList.remove((DirectedVertex) getVertex(uid));
    }

    @Override
    public void addEdge(IEdge edge) {
        DirectedEdge e = (DirectedEdge) edge;

        if (!edgeList.contains(e)) {
            edgeList.add(e);

            ((DirectedVertex) edge.getSource()).getEdgeMap().put(e, EdgeType.OUT);
            ((DirectedVertex) edge.getTarget()).getEdgeMap().put(e, EdgeType.IN);
        }
    }

    @Override
    public void addEdge(UUID uid1, UUID uid2) {
        addEdge(getVertex(uid1), getVertex(uid2));
    }

    @Override
    public void addEdge(IVertex v1, IVertex v2) {
        DirectedEdge e = (DirectedEdge) getEdge(v1, v2);

        if (e == null) {
            DirectedEdge edge = new DirectedEdge(new HashMap<>() {{
                put(VertexType.SOURCE, (DirectedVertex) v1);
                put(VertexType.TARGET, (DirectedVertex) v2);
            }});

            edgeList.add(edge);

            edge.getSource().getEdgeMap().put(edge, EdgeType.OUT);
            edge.getTarget().getEdgeMap().put(edge, EdgeType.IN);
        }

    }

    @Override
    public void deleteEdge(UUID uid1, UUID uid2) {
        edgeList.remove((DirectedEdge) getEdge(uid1, uid2));
    }

    @Override
    public IVertex getVertex(UUID uid) {
        return vertexList.stream().filter(v -> v.getUid().equals(uid)).findFirst().orElse(null);
    }

    @Override
    public IEdge getEdge(UUID uid1, UUID uid2) {
        return getEdge(getVertex(uid1), getVertex(uid2));
    }

    @Override
    public IEdge getEdge(IVertex v1, IVertex v2) {
        return edgeList.stream().filter(e -> e.getSource().equals(v1)
                && e.getTarget().equals(v2)).findFirst().orElse(null);
    }

    @Override
    public int getGraphSize() {
        return vertexList.size();
    }

    @Override
    public int getVertexIndex(IVertex vertex) {
        return vertexList.indexOf((DirectedVertex) vertex);
    }
}
