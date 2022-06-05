package ru.vsu.savina.graphvisualization.layout;

import ru.vsu.savina.graphvisualization.graphics.Rectangle;

import java.util.List;

public class RadialLayout {
    private static double treeDelta;

    public static double[][] radialLayout(List<List<Integer>> adjList, Rectangle rect) {
        double[][] coordinates = new double[adjList.size()][2];
        /*Поиск центра дерева*/
        List<Integer> centerNodes = GraphLayoutHelper.getCenterIndex(adjList);

        treeDelta = Math.min(rect.getWidth(), rect.getHeight()) / adjList.size();

        if (centerNodes.size() == 1) {
            subTreeRadialLayout(centerNodes.get(0), centerNodes.get(0), 0, 0, Math.PI * 2, adjList, coordinates, rect);
        } else {
            subTreeRadialLayout(centerNodes.get(0), centerNodes.get(1), treeDelta / 2, (-1) * Math.PI / 2, Math.PI / 2, adjList, coordinates, rect);
            subTreeRadialLayout(centerNodes.get(1), centerNodes.get(0), treeDelta / 2, Math.PI / 2, Math.PI * 3 / 2, adjList, coordinates, rect);
        }

        return coordinates;
    }


    public static void subTreeRadialLayout(int v, int p, double r, double a1, double a2, List<List<Integer>> adjList, double[][] coordinates, Rectangle rect) {
        double phi = (a1 + a2) / 2;

        coordinates[v][0] = rect.getCenterX() + r * Math.cos(phi);
        coordinates[v][1] = rect.getCenterY() + r * Math.sin(phi);

        double tau = 2 * Math.acos(r / (r + treeDelta));
        double w = getSubtreeWidth(v, p, adjList);
        double s;
        double alpha;

        if (tau < a2 - a1) {
            s = tau / w;
            alpha = (a1 + a2 - tau) / 2;
        } else {
            s = (a2 - a1) / w;
            alpha = a1;
        }

        for (Integer u : adjList.get(v)) {
            if (u != p) {
                int wu = getSubtreeWidth(u, v, adjList);
                subTreeRadialLayout(u, v, r + treeDelta, alpha, alpha + s * wu, adjList, coordinates, rect);
                alpha += s * wu;
            }
        }

    }


    /**
     * Вычисление ширины поддерева
     *
     * @param v       просматриваемая вершина
     * @param p       родительская вершина
     * @param adjList список смежности графа (компоненты связности)
     * @return значение ширины поддерева
     */
    public static int getSubtreeWidth(int v, int p, List<List<Integer>> adjList) {
        int width = 1;

        for (Integer i : adjList.get(v)) {
            if (i != p) {
                if (adjList.get(i).size() == 1) {
                    width += 1;
                } else {
                    width += getSubtreeWidth(i, v, adjList);
                }
            }
        }

        return width;
    }
}
