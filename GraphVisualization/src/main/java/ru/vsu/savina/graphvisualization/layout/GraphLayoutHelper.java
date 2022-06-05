package ru.vsu.savina.graphvisualization.layout;

import ru.vsu.savina.graphalgorithms.algorithms.implementation.DepthFirstTraversalDirectedAlgorithm;
import ru.vsu.savina.graphalgorithms.algorithms.implementation.DepthFirstTraversalUndirectedAlgorithm;
import ru.vsu.savina.graphalgorithms.model.*;
import ru.vsu.savina.graphvisualization.graphics.Point;
import ru.vsu.savina.graphvisualization.graphics.Rectangle;
import ru.vsu.savina.graphvisualization.wrapper.*;

import java.util.*;

public class GraphLayoutHelper {

    public static final double WIDTH = 1200;
    public static final double HEIGHT = 800;

    public static List<Rectangle> getRectangles(int componentsCount) {
        List<Rectangle> rectangles = new ArrayList<>();

        int rows = (int) Math.round(Math.sqrt(componentsCount));
        int columns = (int) Math.ceil((double) componentsCount / (double) rows);

        Rectangle drawRect = new Rectangle(GraphLayoutHelper.WIDTH, GraphLayoutHelper.HEIGHT);

        double width = drawRect.getWidth() / columns;
        double height = drawRect.getHeight() / rows;

        for (int i = 0; i < componentsCount; i++) {
            int rowIndex = (int) Math.floor((double) i / (double) columns);
            int columnIndex = i - columns * rowIndex;
            rectangles.add(new Rectangle(
                    drawRect.getX1() + columnIndex * width + 25,
                    drawRect.getY1() + rowIndex * height + 25,
                    drawRect.getX1() + (columnIndex + 1) * width - 25,
                    drawRect.getY1() + (rowIndex + 1) * height - 25));
        }

        return rectangles;
    }


    public static List<List<DirectedVertex>> getConnectedComponents(DirectedGraph graph) {
        DepthFirstTraversalDirectedAlgorithm algorithm = new DepthFirstTraversalDirectedAlgorithm();
        List<List<DirectedVertex>> components = new ArrayList<>();

        HashSet<DirectedVertex> visited = new HashSet<>();

        for (IVertex vertex : graph.getVertexList()) {
            DirectedVertex v = (DirectedVertex) vertex;

            if (!visited.contains(v)) {
                List<DirectedVertex> component = algorithm.execute(graph, v);
                components.add(component);
                component.removeIf(visited::contains);
                visited.addAll(component);
            }
        }

        List<List<DirectedVertex>> connected = new ArrayList<>();

        for (List<DirectedVertex> listV : components) {
            if (listV.size() == 1 && listV.get(0).getEdgeMap().size() > 0) {
                DirectedEdge edge = (DirectedEdge) graph.getEdgeList().stream().filter(e -> e.getVertexMap().containsValue(listV.get(0))).findFirst().orElse(null);

                if (edge != null) {
                    DirectedVertex v = (DirectedVertex) edge.getOpposite(listV.get(0));
                    components.stream().filter(l -> l.contains(v)).findFirst().get().add(listV.get(0));
                    connected.add(listV);
                }
            }
        }

        components.removeAll(connected);

        return components;
    }

    public static List<List<UndirectedVertex>> getConnectedComponents(UndirectedGraph graph) {
        DepthFirstTraversalUndirectedAlgorithm algorithm = new DepthFirstTraversalUndirectedAlgorithm();
        List<List<UndirectedVertex>> components = new ArrayList<>();

        HashSet<UndirectedVertex> visited = new HashSet<>();

        for (IVertex vertex : graph.getVertexList()) {
            UndirectedVertex v = (UndirectedVertex) vertex;

            if (!visited.contains(v)) {
                List<UndirectedVertex> component = algorithm.execute(graph, v);
                components.add(component);
                visited.addAll(component);
            }
        }

        return components;
    }


    public static List<List<Integer>> getComponentAdjList(IGraph graph, List<? extends IVertex> vertexList) {
        List<List<Integer>> subgraphAdjList = new ArrayList<>();

        for (int i = 0; i < vertexList.size(); i++) {
            subgraphAdjList.add(new ArrayList<>());
        }

        for (IEdge e : graph.getEdgeList()) {
            if (vertexList.contains(e.getSource())) {
                subgraphAdjList.get(vertexList.indexOf(e.getSource())).add(vertexList.indexOf(e.getTarget()));

                if (e.getClass().isAssignableFrom(UndirectedEdge.class)) {
                    subgraphAdjList.get(vertexList.indexOf(e.getTarget())).add(vertexList.indexOf(e.getSource()));
                }
            }
        }

        return subgraphAdjList;
    }

    public static void createGraphWrapper(DirectedGraphWrapper wrapper, List<DirectedVertex> component, double[][] coordinates, List<List<Integer>> subGraph) {
        for (int i = 0; i < wrapper.getGraph().getGraphSize(); i++) {
            DirectedVertexWrapper vertex;
            vertex = new DirectedVertexWrapper((DirectedVertex) wrapper.getGraph().getVertexList().get(i));

            vertex.setX(coordinates[i][0]);
            vertex.setY(coordinates[i][1]);

            wrapper.getVertices().add(vertex);
        }

        for (int i = 0; i < wrapper.getGraph().getEdgeList().size(); i++) {
            DirectedEdgeWrapper edgeWrapper = new DirectedEdgeWrapper((DirectedEdge) wrapper.getGraph().getEdgeList().get(i));
            DirectedVertexWrapper v1 = wrapper.getVertex(((DirectedEdge) wrapper.getGraph().getEdgeList().get(i)).getSource());
            DirectedVertexWrapper v2 = wrapper.getVertex(((DirectedEdge) wrapper.getGraph().getEdgeList().get(i)).getTarget());

            edgeWrapper.getVertexMap().put(VertexType.SOURCE, v1);
            edgeWrapper.getVertexMap().put(VertexType.TARGET, v2);
            wrapper.getEdges().add(edgeWrapper);
        }

        if (coordinates.length > wrapper.getGraph().getGraphSize()) {
            createEdgePath(wrapper, component, subGraph, coordinates);
        }
    }


    public static void createGraphWrapper(UndirectedGraphWrapper wrapper, List<UndirectedVertex> component, double[][] coordinates, List<List<Integer>> subGraph) {
        for (int i = 0; i < wrapper.getGraph().getGraphSize(); i++) {
            UndirectedVertexWrapper vertex;

            vertex = new UndirectedVertexWrapper((UndirectedVertex) wrapper.getGraph().getVertexList().get(i));

            vertex.setX(coordinates[i][0]);
            vertex.setY(coordinates[i][1]);

            wrapper.getVertices().add(vertex);
        }

        for (int i = 0; i < wrapper.getGraph().getEdgeList().size(); i++) {
            UndirectedEdgeWrapper edgeWrapper = new UndirectedEdgeWrapper((UndirectedEdge) wrapper.getGraph().getEdgeList().get(i));
            wrapper.getEdges().add(edgeWrapper);

            UndirectedVertexWrapper v1 = wrapper.getVertex(((UndirectedEdge) wrapper.getGraph().getEdgeList().get(i)).getSource());
            UndirectedVertexWrapper v2 = wrapper.getVertex(((UndirectedEdge) wrapper.getGraph().getEdgeList().get(i)).getTarget());

            edgeWrapper.getVertexMap().put(VertexType.SOURCE, v1);
            edgeWrapper.getVertexMap().put(VertexType.TARGET, v2);
        }

        if (coordinates.length > wrapper.getGraph().getGraphSize()) {
            createEdgePath(wrapper, subGraph, coordinates);
        }
    }

    private static void createEdgePath(DirectedGraphWrapper wrapper, List<DirectedVertex> component, List<List<Integer>> subGraph, double[][] coordinates) {
        for (int v = 0; v < component.size(); v++) {
            for (Object o : subGraph.get(v).stream().filter(i -> i >= wrapper.getGraph().getGraphSize()).toArray()) {
                List<Point> points = new ArrayList<>();
                List<Integer> path = createPath((int) o, wrapper.getGraph().getGraphSize(), subGraph);
                int target = path.get(path.size() - 1);

                for (int u : path) {
                    if (u != target) {
                        points.add(new Point(coordinates[u][0], coordinates[u][1]));
                    }
                }

                DirectedVertex v1 = component.get(v);
                DirectedVertex v2 = component.get(target);
                DirectedEdge edge = (DirectedEdge) wrapper.getGraph().getEdge(v1, v2);

                DirectedEdgeWrapper w = (DirectedEdgeWrapper) wrapper.getEdge(edge);

                Collections.reverse(points);
                w.setPoints(points);
            }

        }
    }

    private static void createEdgePath(UndirectedGraphWrapper wrapper, List<List<Integer>> subGraph, double[][] coordinates) {
        for (int v = 0; v < wrapper.getGraph().getGraphSize(); v++) {
            for (Object o : subGraph.get(v).stream().filter(i -> i >= wrapper.getGraph().getGraphSize()).toArray()) {
                List<Point> points = new ArrayList<>();
                List<Integer> path = createPath((int) o, wrapper.getGraph().getGraphSize(), subGraph);
                int target = path.get(path.size() - 1);

                for (int u : path) {
                    if (u != target) {
                        points.add(new Point(coordinates[u][0], coordinates[u][1]));
                    }
                }

                UndirectedEdge e = (UndirectedEdge) wrapper.getGraph().getEdge(wrapper.getGraph().getVertexList().get(v), wrapper.getGraph().getVertexList().get(target));
                Collections.reverse(points);
                wrapper.getEdge(e).setPoints(points);
            }
        }
    }

    private static List<Integer> createPath(int second, int graphSize, List<List<Integer>> subGraph) {
        List<Integer> path = new ArrayList<>();

        int v = second;
        while (v >= graphSize) {
            path.add(v);
            v = subGraph.get(v).get(0);
        }

        path.add(v);
        return path;
    }

    /**
     * Поиск в глубину для определения компонент связности
     *
     * @param vertex         посещаемая вершина
     * @param curr_component текущая компонента связности
     * @param adjList        список смежности графа
     * @param visited        массив посещенных вершин
     * @param components     массив компонент связности
     */
    public static void connectionDFS(int vertex, int curr_component, List<List<Integer>> adjList, boolean[] visited, int[] components) {
        visited[vertex] = true;
        components[vertex] = curr_component;

        for (int i : adjList.get(vertex)) {
            if (!visited[i]) {
                connectionDFS(i, curr_component, adjList, visited, components);
            }
        }

    }

    /**
     * Проверка неориентированного графа на цикличность
     *
     * @param adjList список смежности графа (компоненты связности)
     */
    public static boolean checkCyclic(List<List<Integer>> adjList) {
        boolean[] visited = new boolean[adjList.size()];
        int[] color = new int[adjList.size()];

        for (int i = 0; i < adjList.size(); i++) {
            if (cyclicDFS(i, -1, adjList, color))
                    return true;
        }
        return false;
    }

    /**
     * Поиск в глубину для проверки графа на цикличность
     *
     * @param vertex  просматриваемая вершина
     * @param parent  родительская вершина
     * @param adjList список смежности графа (компоненты связности)
     */
    public static boolean cyclicDFS(int vertex, int parent, List<List<Integer>> adjList, int[] color) {
        color[vertex] = 1;

        for (int i : adjList.get(vertex)) {
            if (color[i] == 0) {
                if( cyclicDFS(i, vertex, adjList, color)) return true;
            } else if (color[i] == 1 && i != parent) {
                    return true;
            }
        }

        color[vertex] = 2;
        return false;
    }


    /**
     * Поиск центра дерева
     *
     * @param adjList список смежности графа (компоненты связности)
     * @return список индексов вершин - центров дерева
     */
    public static List<Integer> getCenterIndex(List<List<Integer>> adjList) {
        int maxLevel = 0;
        int[] level = new int[adjList.size()];
        int[] degree = new int[adjList.size()];

        List<Integer> center = new ArrayList<>();

        Queue<Integer> queue = new ArrayDeque<>();

        for (int i = 0; i < adjList.size(); i++) {
            degree[i] = adjList.get(i).size();
        }


        for (int i = 0; i < adjList.size(); i++) {
            if (degree[i] == 1) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            int v = queue.poll();

            for (int i = 0; i < adjList.size(); i++) {
                if (adjList.get(v).contains(i)) {
                    degree[i]--;

                    if (degree[i] == 1) {
                        queue.add(i);
                        level[i] = level[v] + 1;
                        maxLevel = Math.max(maxLevel, level[i]);
                    }
                }
            }

        }

        for (int i = 0; i < adjList.size(); i++) {
            if (level[i] == maxLevel) {
                center.add(i);
            }
        }
        return center;
    }
}
