package xExperimental;

import Canvas.Canvas.DrawableCanvas;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CanvasTester extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        DrawableCanvas canvas = new DrawableCanvas(null);
        Scene scene = new Scene(canvas.getOutermostContainer());
        canvas.setupDragAndDrop(scene);
        stage.setScene(scene);
        stage.getScene().getStylesheets().add(getClass().getClassLoader().getResource("styles/fx-listview.css").toExternalForm());
        stage.show();
    }
}

