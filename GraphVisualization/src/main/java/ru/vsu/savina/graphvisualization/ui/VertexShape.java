package ru.vsu.savina.graphvisualization.ui;

import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;

import java.util.Map;

public class VertexShape extends Region {
    private final Map<String, Object> metadata;

    public VertexShape(Map<String, Object> metadata, Circle circle) {
        this.metadata = metadata;
        this.getChildren().add(circle);
        circle.setDisable(true);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Vertex\n");

        for (Map.Entry<String, Object> entry : metadata.entrySet()) {
            str.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        return str.toString();
    }
}
