package ru.vsu.savina.graphvisualization.layout;


import ru.vsu.savina.graphalgorithms.model.IEdge;
import ru.vsu.savina.graphalgorithms.model.UndirectedVertex;
import ru.vsu.savina.graphvisualization.graphics.Rectangle;

import java.util.*;

public class HierarchicalLayout {
    public static double[][] hierarchicalLayout(List<List<Integer>> adjList, Rectangle rect) {
        if (GraphLayoutHelper.checkCyclic(adjList)) {
            deleteCycles(adjList);
        }

        List<Integer> layers = new ArrayList<>();

        for (int v = 0; v < adjList.size(); v++) {
            layers.add(-1);
        }

        for (int v : findAllSources(adjList)) {
            assignLayers(longestPath(adjList, v), layers);
        }
        addVertices(layers, adjList);

        return placeVertices(orderVertices(layers, adjList), layers, rect);
    }

    /**
     * Назначение вершинам графа уровней при укладке графа методом поуровневого изображения
     *
     * @param paths  массив со значениями наиболее длинного пути до вершин
     * @param layers назначенные уровни
     */
    private static void assignLayers(int[] paths, List<Integer> layers) {
        for (int i = 0; i < paths.length; i++) {
            layers.set(i, paths[i] >= 0 && layers.get(i) == -1 ? paths[i] : layers.get(i));
        }
    }

    /**
     * Добавление вершин по уровням в методе поуровненого изображения
     *
     * @param layers  список уровней вершин
     * @param adjList список смежности графа (компоненты связности)
     */
    private static void addVertices(List<Integer> layers, List<List<Integer>> adjList) {
        for (int v = 0; v < adjList.size(); v++) {
            Queue<Integer> vertices = new ArrayDeque<>(adjList.get(v));
            while (!vertices.isEmpty()) {
                int u = vertices.poll();

                if (layers.get(u) - layers.get(v) > 1) {
                    adjList.add(new ArrayList<>());

                    adjList.get(adjList.size() - 1).add(u);
                    adjList.get(v).remove((Integer) u);
                    adjList.get(v).add(adjList.size() - 1);

                    layers.add(layers.get(v) + 1);
                }
            }
        }
    }

    /**
     * Упорядочение вершин в методе поуровневого изображения
     *
     * @param layers  список уровней вершин
     * @param adjList список смежности графа (компоненты связности)
     * @return массив со значениями порядка вершин
     */
    private static int[] orderVertices(List<Integer> layers, List<List<Integer>> adjList) {
        int[] order = new int[layers.size()];
        int layersCount = layers.stream().mapToInt(v -> v).max().orElse(0) + 1;

        for (int l = 0; l < layersCount; l++) {
            List<Integer> indices = getLayerIndices(l, layers);
            if (l == 0) {
                int currC = 0;

                for (int v : indices) {
                    order[v] = currC;
                    currC += 1;
                }
            } else {
                for (int v : indices) {
                    int cSum = 0;
                    int degree = 0;

                    for (int u = 0; u < adjList.size(); u++) {
                        if (layers.get(u) == l - 1 && adjList.get(u).contains(v)) {
                            cSum += order[u];
                            degree += 1;
                        }
                    }

                    order[v] = cSum / degree;
                }
            }
            for (int v = 0; v < indices.size(); v++) {
                for (int u = v + 1; u < indices.size(); u++) {
                    if (order[indices.get(v)] == order[indices.get(u)]) {
                        order[indices.get(u)] += 1;
                    }
                }
            }
        }
        return order;
    }

    /**
     * Получение списка индексов вершин, относящихся к уровню
     *
     * @param layer  индекс уровня
     * @param layers список уровней вершин
     * @return список индексов вершин
     */
    private static List<Integer> getLayerIndices(int layer, List<Integer> layers) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i) == layer) {
                indices.add(i);
            }
        }
        return indices;
    }

    /**
     * Вычисление координат вершин в методе поуровневого изображения
     *
     * @param order  массив со значениями порядка вершин
     * @param layers список уровней вершин
     * @param rect   область отрисовки
     * @return матрица координат вершин
     */
    private static double[][] placeVertices(int[] order, List<Integer> layers, Rectangle rect) {
        double[][] coordinates = new double[order.length][2];

        int layersCount = layers.stream().mapToInt(v -> v).max().orElse(0) + 1;
        int layerWidth = Arrays.stream(order).max().orElse(1);
        if (layerWidth == 0) layerWidth = 1;

        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i][0] = (rect.getX1() + rect.getCenterX()) / 2 + rect.getWidth() / layerWidth / 6 * order[i];
            coordinates[i][1] = (rect.getY1() + rect.getCenterY()) / 2 + rect.getHeight() / layersCount / 1.5 * layers.get(i);
        }
        return coordinates;
    }


    /**
     * Удаление циклов из ориентированного графа. Подготовка графа перед укладкой методом поуровненого изображения
     *
     * @param adjList - список смежности графа (компоненты связности)
     */
    private static void deleteCycles(List<List<Integer>> adjList) {
        List<List<Integer>> reverse = new ArrayList<>();

        for (int i = 0; i < adjList.size(); i++) {
            reverse.add(new ArrayList<>());
        }


        boolean[] marked = new boolean[adjList.size()];
        boolean[] onStack = new boolean[adjList.size()];

        for (int v = 0; v < adjList.size(); v++) {
            reverseDFS(v, reverse, marked, onStack, adjList);
        }

        for (int i = 0; i < adjList.size(); i++) {
            adjList.get(i).addAll(reverse.get(i));
        }

    }

    /**
     * Поиск в глубину при удалении циклов из ориентированного графа
     *
     * @param adjList список смежности графа
     * @param v       индекс вершины, от которой производится поиск
     * @param marked  массив с посещенными ("отмеченными") вершинами
     * @param onStack массив вершин на очереди
     */
    private static void reverseDFS(int v, List<List<Integer>> reverse, boolean[] marked, boolean[] onStack, List<List<Integer>> adjList) {
        marked[v] = true;
        onStack[v] = true;

        for (int w = 0; w < adjList.size(); w++) {
            if (adjList.get(w).contains(v)) {
                if (onStack[w]) {
                    reverse.get(v).add(w);
                    adjList.get(w).remove((Integer) v);
                } else {
                    if (!marked[w]) {
                        reverseDFS(w, reverse, marked, onStack, adjList);
                    }
                }
            }
        }

        onStack[v] = false;
    }

    /**
     * Поиск всех источников для вершины в методе поуровневого изображения
     *
     * @param adjList список смежности графа (компоненты связности)
     * @return список индексов вершин - источников
     */
    private static List<Integer> findAllSources(List<List<Integer>> adjList) {
        List<Integer> sources = new ArrayList<>();

        for (int v = 0; v < adjList.size(); v++) {
            boolean source = false;

            for (List<Integer> l : adjList) {
                if (l.contains(v)) {
                    source = true;
                    break;
                }
            }

            if (!source)
                sources.add(v);
        }

        return sources;
    }

    /**
     * Поиск наиболее длинного пути из вершины до остальных
     *
     * @param adjList список смежности графа (компоненты связности)
     * @param v       вершина
     * @return массив с длинами путей
     */
    private static int[] longestPath(List<List<Integer>> adjList, int v) {
        Stack<Integer> stack = new Stack<>();
        int[] distances = new int[adjList.size()];
        boolean[] visited = new boolean[adjList.size()];

        for (int i = 0; i < adjList.size(); i++)
            if (!visited[i])
                topologicalSort(i, visited, stack, adjList);

        for (int i = 0; i < adjList.size(); i++)
            distances[i] = Integer.MIN_VALUE;

        distances[v] = 0;

        while (!stack.isEmpty()) {
            int u = stack.peek();
            stack.pop();

            if (distances[u] != Integer.MIN_VALUE) {
                for (int i : adjList.get(u)) {
                    if (distances[i] < distances[u] + 1)
                        distances[i] = distances[u] + 1;
                }
            }
        }

        return distances;
    }

    /**
     * Топологическая сортировка вершин графа
     *
     * @param v       просматриваемая вершина
     * @param visited массив посещенных вершин
     * @param stack   стек посещения вершин
     * @param adjList список смежности графа (компоненты связности)
     */
    private static void topologicalSort(int v, boolean[] visited, Stack<Integer> stack, List<List<Integer>> adjList) {
        visited[v] = true;

        for (int i : adjList.get(v)) {
            if (!visited[i]) {
                topologicalSort(i, visited, stack, adjList);
            }
        }

        stack.push(v);
    }

    public static List<List<Integer>> createDirectedAdjustmentList(List<UndirectedVertex> vertexList, List<? extends IEdge> edgeList) {
        List<List<Integer>> subgraphAdjList = new ArrayList<>();

        for (int i = 0; i < vertexList.size(); i++) {
            subgraphAdjList.add(new ArrayList<>());
        }

        for (IEdge e : edgeList) {
            if (vertexList.contains((UndirectedVertex) e.getSource())) {
                int index1 = vertexList.indexOf((UndirectedVertex) e.getSource());
                int index2 = vertexList.indexOf((UndirectedVertex) e.getTarget());

                subgraphAdjList.get(index1).add(index2);
            }
        }

        return subgraphAdjList;
    }
}
