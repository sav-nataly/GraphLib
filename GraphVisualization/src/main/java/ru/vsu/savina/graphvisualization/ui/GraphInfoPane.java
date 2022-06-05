package ru.vsu.savina.graphvisualization.ui;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class GraphInfoPane extends BorderPane {
    private final Text text = new Text();

    public GraphInfoPane() {
        text.setTextAlignment(TextAlignment.LEFT);
        text.setFont(new Font(20));
        this.setPadding(new Insets(20));
        this.setCenter(text);
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), BorderStroke.THIN)));
    }

    public void showMessage(String string) {
        text.setText(string);
    }

}
