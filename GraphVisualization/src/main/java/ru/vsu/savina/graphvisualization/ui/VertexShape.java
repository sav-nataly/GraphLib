package ru.vsu.savina.graphvisualization.ui;

import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class VertexShape extends Region {
    private final Map<String, Object> metadata;

    public VertexShape(Map<String, Object> metadata, Circle circle) {
        this.metadata = metadata;


        if (metadata.containsKey("name")) {
            Text text = new Text();
            text.setText((String) metadata.get("name"));
            text.relocate(5 , -35);
            this.getChildren().add(text);
        }
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
