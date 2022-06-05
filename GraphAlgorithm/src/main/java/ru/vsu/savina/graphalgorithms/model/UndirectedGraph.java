package ru.vsu.savina.graphalgorithms.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UndirectedGraph implements IGraph {
    private List<UndirectedVertex> vertexList = new ArrayList<>();
    private List<UndirectedEdge> edgeList = new ArrayList<>();

    public UndirectedGraph(List<UndirectedVertex> vertexList, List<UndirectedEdge> edgeList) {
        this.vertexList = vertexList;
        this.edgeList = edgeList;
    }

    public UndirectedGraph() {
    }

    @Override
    public List<List<Integer>> getAdjustmentList() {
        List<List<Integer>> adjList = new ArrayList<>();

        for (int i = 0; i < getVertexList().size(); i++) {
            adjList.add(new ArrayList<>());
        }

        for (IEdge e : getEdgeList()) {
            UndirectedEdge edge = (UndirectedEdge) e;
            adjList.get(getVertexIndex(edge.getTarget())).add(getVertexIndex(edge.getSource()));
            adjList.get(getVertexIndex(edge.getSource())).add(getVertexIndex(edge.getTarget()));
        }

        return adjList;
    }


    @Override
    public List<UndirectedVertex> getVertexList() {
        return vertexList;
    }


    @Override
    public List<? extends IEdge> getEdgeList() {
        return edgeList;
    }


    @Override
    public void addVertex(IVertex vertex) {
        UndirectedVertex v = (UndirectedVertex) vertex;

        if (!vertexList.contains(vertex) && vertexList.stream().noneMatch(v1 -> v1.getUid().equals(vertex.getUid())))
            vertexList.add((UndirectedVertex) vertex);
    }

    @Override
    public void addVertex(UUID uid) {
        UndirectedVertex v = (UndirectedVertex) getVertex(uid);
        if (v == null)
            vertexList.add(new UndirectedVertex(uid));
    }

    @Override
    public void addVertex() {
        vertexList.add(new UndirectedVertex());
    }

    @Override
    public void deleteVertex(UUID uid) {
        vertexList.remove((UndirectedVertex) getVertex(uid));
    }

    @Override
    public void addEdge(IEdge edge) {
        UndirectedEdge e = (UndirectedEdge) edge;

        if (!edgeList.contains(edge)) {
            edgeList.add((UndirectedEdge) edge);

            ((UndirectedVertex) edge.getSource()).getEdgeMap().put((UndirectedEdge) edge, EdgeType.OUT);
            ((UndirectedVertex) edge.getTarget()).getEdgeMap().put((UndirectedEdge) edge, EdgeType.IN);
        }
    }

    @Override
    public void addEdge(IVertex v1, IVertex v2) {
        UndirectedEdge e = (UndirectedEdge) getEdge(v1, v2);

        if (e == null) {
            UndirectedEdge edge = new UndirectedEdge(new HashMap<>() {{
                put(VertexType.SOURCE, (UndirectedVertex) v1);
                put(VertexType.TARGET, (UndirectedVertex) v2);
            }});

            edgeList.add(edge);

            edge.getSource().getEdgeMap().put(edge, EdgeType.OUT);
            edge.getTarget().getEdgeMap().put(edge, EdgeType.IN);
        }
    }

    @Override
    public void addEdge(UUID uid1, UUID uid2) {
        addEdge(getVertex(uid1), getVertex(uid2));
    }

    @Override
    public void deleteEdge(UUID uid1, UUID uid2) {
        edgeList.remove((UndirectedEdge) getEdge(uid1, uid2));
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
        return edgeList.stream().filter(e -> (e.getSource().equals(v1)
                && e.getTarget().equals(v2)) || (e.getSource().equals(v2)
                && e.getTarget().equals(v1))).findFirst().orElse(null);
    }

    @Override
    public int getGraphSize() {
        return vertexList.size();
    }

    @Override
    public int getVertexIndex(IVertex vertex) {
        return vertexList.indexOf((UndirectedVertex) vertex);
    }
}
