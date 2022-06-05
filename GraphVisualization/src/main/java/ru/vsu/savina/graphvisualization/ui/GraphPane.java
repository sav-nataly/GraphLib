package ru.vsu.savina.graphvisualization.ui;

import javafx.scene.layout.Pane;
import ru.vsu.savina.graphvisualization.wrapper.IGraphWrapper;

public class GraphPane extends Pane {
    private final IGraphWrapper wrapper;

    public GraphPane(IGraphWrapper wrapper) {
        this.wrapper = wrapper;

        buildPane();
    }

    private void buildPane() {
        GraphDrawUtil.draw(wrapper, this);
    }
}
