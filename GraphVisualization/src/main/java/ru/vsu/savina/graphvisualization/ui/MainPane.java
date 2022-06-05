package ru.vsu.savina.graphvisualization.ui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import ru.vsu.savina.graphvisualization.wrapper.IGraphWrapper;

public class MainPane extends BorderPane {
    private final GraphPane graphPane;
    private final GraphInfoPane graphInfoPane = new GraphInfoPane();

    public MainPane(IGraphWrapper wrapper) {
        graphPane = new GraphPane(wrapper);

        buildPane();
    }

    private void buildPane() {
        HBox hBox = new HBox();
        hBox.getChildren().add(graphPane);
        graphPane.setPrefWidth(900);
        graphPane.setPrefHeight(800);

        hBox.getChildren().add(graphInfoPane);
        graphInfoPane.setPrefWidth(300);
        graphInfoPane.setPrefHeight(800);

        setCenter(hBox);

        setEventHandlers();
    }

    private void setEventHandlers() {
        graphPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                graphInfoPane.showMessage(mouseEvent.getTarget().toString());
            }
        });
    }
}
