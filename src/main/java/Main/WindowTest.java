package Main;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.WritableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.Controller.Binder;
import org.Controller.UIInterface;

public class WindowTest extends Application {

    public static WindowTest instance;
    public Stage stage;
    public Scene rootScene;
    private MainViewController mainViewController;

    private UIInterface uiInterface;

    public static void launch() {
        launch(null);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Platform.setImplicitExit(false);
        instance = this;
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Viewfxml/Main.fxml"));
        Pane root = fxmlLoader.load();
        root.getStylesheets().add(getClass().getClassLoader().getResource("styles/styles.css").toExternalForm());
        rootScene = new Scene(root);
        stage.setScene(rootScene);
        stage.initStyle(StageStyle.UNDECORATED);
        uiInterface = Binder.Companion.getUiInterface();
        mainViewController = fxmlLoader.getController();
        mainViewController.closeFunction = () -> uiInterface.toggleBar();
        mainViewController.resizeInterface = value -> setupRootDimensions(root, value);
        mainViewController.setupDragAndDrop(rootScene);
        setupRootDimensions(root, null);
        setupTopLeftCorner(true);
    }


    public void setupRootDimensions(Pane pane, Double width) {
        javafx.geometry.Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        if (width == null) {
            pane.setPrefWidth(bounds.getMaxX() * 0.5);
            pane.setPrefHeight(bounds.getMaxY() - bounds.getMinY());
        } else {
            stage.setWidth(bounds.getMaxX() * ((width > 0.2) ? width : 0.2));
            pane.setPrefWidth(bounds.getMaxX() * ((width > 0.2) ? width : 0.2));

        }
    }

    public void setupTopLeftCorner(boolean hidden) {
        javafx.geometry.Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX(hidden ? -stage.getWidth() : 0);
        stage.setY(bounds.getMinY());
    }

    public void show() {
        stage.show();
        stage.toFront();
        stage.requestFocus();
        stage.setAlwaysOnTop(true);
        setupTopLeftCorner(true);
        Timeline timeline = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(200), new KeyValue(xPosition(), 0d, Interpolator.LINEAR));
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    public WritableValue<Double> xPosition() {
        return new WritableValue<Double>() {
            @Override
            public Double getValue() {
                return stage.getX();
            }
            @Override
            public void setValue(Double value) {
                stage.setX(value);
            }
        };
    }

    public void hide() {
        setupTopLeftCorner(false);
        Timeline timeline = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(200), new KeyValue(xPosition(), -stage.getWidth(), Interpolator.LINEAR));
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> {
            stage.hide();
        });
        timeline.play();
    }
}
