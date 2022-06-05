package ru.vsu.savina.graphvisualization.application;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.vsu.savina.graphvisualization.ui.MainPane;
import ru.vsu.savina.graphvisualization.ui.MainWindow;
import ru.vsu.savina.graphvisualization.wrapper.IGraphWrapper;

public class GraphApplication {
    private IGraphWrapper wrapper = null;

    public <T extends IGraphWrapper> GraphApplication(T wrapper) {
        this.wrapper = wrapper;
    }

    public void start() throws Exception {
        MainPane mainPane = new MainPane(wrapper);

        MainWindow w = new MainWindow();
        w.init();

        Platform.startup(() -> {
            Scene scene = new Scene(mainPane, 1200, 800);
            Stage stage = new Stage();

            stage.setTitle("GraphLib");
            stage.setScene(scene);

            try {
                w.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void setWrapper(IGraphWrapper wrapper) {
        this.wrapper = wrapper;
    }

}