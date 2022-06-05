package ru.vsu.savina.graphvisualization.util;


import ru.vsu.savina.graphvisualization.graphics.Point;

public final class VectorUtil {
    public static Point subtract(Point p1, Point p2) {
        return new Point(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }

    public static Point add(Point p1, Point p2) {
        return new Point(p1.getX() + p2.getX(), p1.getY() + p2.getY());
    }

    public static Point divideByNum(Point p, double k) {
        return new Point(p.getX() / k, p.getY() / k);
    }

    public static Point multiplyByNum(Point p, double k) {
        return new Point(p.getX() * k, p.getY() * k);
    }

    public static double norm(Point p) {
        return Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY());
    }
}
