package ru.vsu.savina.graphvisualization.ui;

import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;

import java.util.Map;

public class EdgeShape extends Region {
    private final Map<String, Object> metadata;

    public EdgeShape(Map<String, Object> metadata, Line line) {
        this.metadata = metadata;
        this.getChildren().add(line);

        line.setDisable(true);
    }

    public EdgeShape(Map<String, Object> metadata, Polyline path) {
        this.metadata = metadata;
        this.getChildren().add(path);


        path.setDisable(true);
    }

    public EdgeShape(Map<String, Object> metadata, Circle circle) {
        this.metadata = metadata;
        this.getChildren().add(circle);
        circle.setDisable(true);
    }

    public void addArrow(Circle circle) {
        this.getChildren().add(circle);
        circle.setDisable(true);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Edge\n");

        for (Map.Entry<String, Object> entry : metadata.entrySet()) {
            str.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        return str.toString();
    }
}
