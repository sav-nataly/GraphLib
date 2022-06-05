package ru.vsu.savina.graphvisualization.layout;

import ru.vsu.savina.graphalgorithms.model.UndirectedGraph;
import ru.vsu.savina.graphalgorithms.model.UndirectedVertex;
import ru.vsu.savina.graphvisualization.graphics.Point;
import ru.vsu.savina.graphvisualization.graphics.Rectangle;
import ru.vsu.savina.graphvisualization.util.VectorUtil;
import ru.vsu.savina.graphvisualization.wrapper.IGraphWrapper;
import ru.vsu.savina.graphvisualization.wrapper.UndirectedGraphWrapper;

import java.util.List;

public class ForceBasedLayout implements ILayout<UndirectedGraph> {
    @Override
    public IGraphWrapper layout(UndirectedGraph graph) {
        UndirectedGraphWrapper wrapper = new UndirectedGraphWrapper(graph);
        List<List<UndirectedVertex>> components = GraphLayoutHelper.getConnectedComponents(graph);

        List<Rectangle> rectangles = GraphLayoutHelper.getRectangles(components.size());
        for (int i = 0; i < components.size(); i++) {
            List<List<Integer>> subgraphAdjList = GraphLayoutHelper.getComponentAdjList(graph, components.get(i));
            double[][] coordinates = forceBasedLayout(subgraphAdjList, rectangles.get(i));

            GraphLayoutHelper.createGraphWrapper(wrapper, components.get(i), coordinates, subgraphAdjList);
        }
        return wrapper;
    }

    private double[][] forceBasedLayout(List<List<Integer>> adjList, Rectangle rect) {
        int iterations = 1;

        double k = Math.sqrt(rect.getArea() / adjList.size());

        Point[] position = new Point[adjList.size()];
        Point[] disposition = new Point[adjList.size()];

        Point delta;

        double t = 10 * Math.sqrt(adjList.size());

        double radius = Math.min(rect.getWidth(), rect.getHeight()) / adjList.size();

        for (int i = 0; i < adjList.size(); i++) {
            double x = rect.getCenterX() + radius * Math.cos(i * 2 * Math.PI / adjList.size());
            double y = rect.getCenterY() + radius * Math.sin(i * 2 * Math.PI / adjList.size());

            position[i] = new Point(x, y);
            disposition[i] = new Point(0, 0);
        }

        for (int i = 0; i < iterations; i++) {
            for (int v = 0; v < adjList.size(); v++) {
                disposition[v] = new Point(0, 0);
                for (int u = 0; u < adjList.size(); u++) {
                    if (u != v) {
                        delta = VectorUtil.subtract(position[v], position[u]);
                        disposition[v] = VectorUtil.add(disposition[v],
                                VectorUtil.multiplyByNum(
                                        VectorUtil.divideByNum(delta, VectorUtil.norm(delta)),
                                        fa(k, VectorUtil.norm(delta))));
                    }
                }
            }

            for (int v = 0; v < adjList.size(); v++) {
                for (int u : adjList.get(v)) {
                    if (u > v) {
                        continue;
                    }
                    delta = VectorUtil.subtract(position[v], position[u]);

                    disposition[v] = VectorUtil.subtract(disposition[v],
                            VectorUtil.multiplyByNum(
                                    VectorUtil.divideByNum(delta, VectorUtil.norm(delta)),
                                    fa(k, VectorUtil.norm(delta))));

                    disposition[u] = VectorUtil.add(disposition[u],
                            VectorUtil.multiplyByNum(
                                    VectorUtil.divideByNum(delta, VectorUtil.norm(delta)),
                                    fa(k, VectorUtil.norm(delta))));
                }
            }

            for (int v = 0; v < adjList.size(); v++) {
                position[v] = VectorUtil.add(position[v],
                        VectorUtil.multiplyByNum(
                                VectorUtil.divideByNum(disposition[v], VectorUtil.norm(disposition[v])),
                                Math.min(VectorUtil.norm(disposition[v]), t)));
            }

            if (t > 1.5) {
                t *= 0.85;
            } else {
                t = 1.5;
            }

        }
        scaleVertices(position, rect);

        return positionToCoordinates(position);
    }

    /**
     * Вычисление значения силы притяжения между двумя вершинами
     *
     * @param x расстояние между вершинами
     * @param k идеальное расстояние между вершинами
     * @return значение силы притяжения
     */
    private double fa(double x, double k) {
        return x * x / k;
    }

    /**
     * Масштабирование координат вершин под область отрисовки
     *
     * @param position массив векторов позиций вершин
     * @param rect     область отрисовки
     */
    private void scaleVertices(Point[] position, Rectangle rect) {
        double maxX = 0;
        double maxY = 0;

        for (int i = 0; i < position.length; i++) {
            for (int j = 0; j < position.length; j++) {
                if (i < j) {
                    double x = Math.sqrt(position[i].getX() * position[i].getX() + position[j].getX() * position[j].getX());
                    double y = Math.sqrt(position[i].getY() * position[i].getY() + position[j].getY() * position[j].getY());

                    maxX = Math.max(x, maxX);
                    maxY = Math.max(y, maxY);
                }
            }
        }

        double kX = rect.getWidth() / maxX;
        double kY = rect.getHeight() / maxY;

        for (Point vector : position) {
            vector.setX(vector.getX() * kX);
            vector.setY(vector.getY() * kY);
        }
    }

    private double[][] positionToCoordinates(Point[] position) {
        double[][] coordinates = new double[position.length][2];

        for (int i = 0; i < position.length; i++) {
            coordinates[i][0] = Math.abs(position[i].getX());
            coordinates[i][1] = Math.abs(position[i].getY());
        }

        return coordinates;
    }


}
