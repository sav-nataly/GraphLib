package ru.vsu.savina.graphvisualization.ui;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import ru.vsu.savina.graphvisualization.graphics.Point;
import ru.vsu.savina.graphvisualization.wrapper.IEdgeWrapper;
import ru.vsu.savina.graphvisualization.wrapper.IGraphWrapper;
import ru.vsu.savina.graphvisualization.wrapper.IVertexWrapper;
import ru.vsu.savina.graphvisualization.wrapper.implementation.DirectedEdgeWrapper;

import java.util.Map;

public class GraphDrawUtil {
    public static void draw(IGraphWrapper wrapper, Pane pane) {
        drawEdges(wrapper, pane);
        drawVertices(wrapper, pane);
    }

    private static void drawVertices(IGraphWrapper wrapper, Pane pane) {
        for (IVertexWrapper v : wrapper.getVertices()) {
            Circle circle = new Circle(15);

            if (v.getVertex() == null)
                continue;

            colorShape(v.getVertex().getMetadata(), circle);


            VertexShape v1 = new VertexShape(v.getVertex().getMetadata(), circle);
            v1.relocate(v.getX(), v.getY());

            pane.getChildren().add(v1);
        }
    }

    private static void colorShape(Map<String, Object> metadata, Shape shape) {
        if (metadata.containsKey("color")) {
            Color color;
            if (metadata.get("color") instanceof String)
                color = Color.web((String) metadata.get("color"));
            else
                color = (Color) metadata.get("color");

            if (shape instanceof Polyline || shape instanceof Line) {
                shape.setStroke(color);
            }
            else
                shape.setFill(color);
        }

        shape.setStrokeWidth(4);
    }

    private static void drawEdges(IGraphWrapper wrapper, Pane pane) {
        for (IEdgeWrapper e : wrapper.getEdges()) {
            drawEdge(e, pane);
        }
    }

    private static void drawLoopEdge(IEdgeWrapper edge, Pane pane) {
        Circle loop = new Circle(30);

        colorShape(edge.getEdge().getMetadata(), loop);

        EdgeShape edgeShape = new EdgeShape(edge.getEdge().getMetadata(), loop);
        edgeShape.relocate(edge.getSource().getX(), edge.getSource().getY());

        pane.getChildren().add(edgeShape);
    }

    private static void drawEdge(IEdgeWrapper edge, Pane pane) {
        if (edge.getTarget().equals(edge.getSource())) {
            drawLoopEdge(edge, pane);
        } else if (edge.getPoints().size() > 0) {
            drawPathEdge(edge, pane);
        } else {
            double x1 = 0;
            double y1 = 0;
            double x2 = edge.getTarget().getX() - edge.getSource().getX();
            double y2 = edge.getTarget().getY() - edge.getSource().getY();

            Line e = new Line(x1, y1, x2, y2);

            colorShape(edge.getEdge().getMetadata(), e);

            EdgeShape shape = new EdgeShape(edge.getEdge().getMetadata(), e);
            pane.getChildren().add(shape);

            if (edge.getClass().isAssignableFrom(DirectedEdgeWrapper.class))
                drawArrow((DirectedEdgeWrapper) edge, shape);

            shape.relocate(edge.getSource().getX(), edge.getSource().getY());
        }
    }

    private static void drawArrow(DirectedEdgeWrapper edge, EdgeShape shape) {
        Point p1, p2;

        p1 = new Point(edge.getTarget().getX(),  edge.getTarget().getY());

        if (edge.getPoints().size() > 0) {
            p2 = edge.getPoints().get(edge.getPoints().size() - 1);
        } else {
            p2 = new Point(edge.getSource().getX(), edge.getSource().getY());
        }

        double alpha = Math.atan(Math.abs(p1.getY() - p2.getY()) / Math.abs((p1.getX() - p2.getX())));

        int yC = p1.getY() > p2.getY() ? 1 : -1;

        int xC = p1.getX() > p2.getX() ? 1 : -1;

        double x = p1.getX() - 15 * Math.cos(alpha) * xC - edge.getSource().getX();
        double y = p1.getY() - 15 * Math.sin(alpha) * yC - edge.getSource().getY();

        Circle arrow = new Circle(x, y, 6);

        colorShape(edge.getEdge().getMetadata(), arrow);

        shape.addArrow(arrow);
    }

    private static void drawPathEdge(IEdgeWrapper edge, Pane pane) {
        Double[] coordinates = new Double[(edge.getPoints().size() + 2) * 2];

        coordinates[0] = 0.0;
        coordinates[1] = 0.0;

        for (int i = 0; i < edge.getPoints().size(); i++) {
            coordinates[(i + 1) * 2] = edge.getPoints().get(i).getX() - edge.getSource().getX();
            coordinates[(i + 1) * 2 + 1] = edge.getPoints().get(i).getY() - edge.getSource().getY();
        }

        coordinates[coordinates.length - 2] = edge.getTarget().getX() - edge.getSource().getX();
        coordinates[coordinates.length - 1] = edge.getTarget().getY() - edge.getSource().getY();

        Polyline e = new Polyline();
        e.getPoints().addAll(coordinates);
        e.setFill(Color.TRANSPARENT);

        colorShape(edge.getEdge().getMetadata(), e);

        EdgeShape shape = new EdgeShape(edge.getEdge().getMetadata(), e);
        pane.getChildren().add(shape);

        if (edge.getClass().isAssignableFrom(DirectedEdgeWrapper.class))
            drawArrow((DirectedEdgeWrapper) edge, shape);

        shape.relocate(edge.getSource().getX(), edge.getSource().getY());
    }
}
