package ru.vsu.savina.graphvisualization.graphics;

public class Rectangle {
    private double x1;
    private double y1;
    private double x2;
    private double y2;

    public Rectangle(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Rectangle(double w, double h) {
        this.x1 = 0;
        this.y1 = 0;
        this.x2 = w;
        this.y2 = h;
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public double getArea() {
        return Math.abs(x2 - x1) * Math.abs(y2 - y1);
    }

    public double getWidth() {
        return Math.abs(x2 - x1);
    }

    public double getHeight() {
        return Math.abs(y2 - y1);
    }

    public double getCenterX() {
        return (x2 + x1) / 2;
    }

    public double getCenterY() {
        return (y2 + y1) / 2;
    }
}
