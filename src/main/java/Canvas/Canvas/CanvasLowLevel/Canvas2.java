package Canvas.Canvas.CanvasLowLevel;

import Canvas.Canvas.ZoomHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import kotlin.Pair;

import java.net.URL;
import java.util.ResourceBundle;

public class Canvas2 implements Initializable {

    @FXML public ScrollPane canvasScrollPane;
    @FXML private GridPane toolOuterGridPane;
    @FXML public Pane canvasPane;
    @FXML private Group canvasGroup;
    @FXML public GridPane outerGridPane;
    @FXML public GridPane toolGridPane;
    @FXML private StackPane stackPane;

    public Node interactionNode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toolGridPane.setPickOnBounds(false);
        toolOuterGridPane.setPickOnBounds(false);
        toolOuterGridPane.toFront();
        toolGridPane.toFront();
        outerGridPane.setPrefSize(0.0, 0.0);

        canvasPane.setMinHeight(45000);
        canvasPane.setMinWidth(45000);

        canvasPane.setStyle("-fx-background-color: white;");

        ZoomHelper.Companion.zoom(canvasPane, canvasScrollPane, interactionNode == null ? canvasScrollPane : interactionNode, () -> {
            Pair pair;
            if (canvasScrollPane.getViewportBounds().getHeight() > canvasScrollPane.getViewportBounds().getHeight()) {
                pair = new Pair<>(canvasScrollPane.getLayoutBounds().getHeight(), canvasScrollPane.getMinHeight());
            } else {
                pair = new Pair<>(canvasScrollPane.getLayoutBounds().getWidth(), canvasScrollPane.getMinWidth());
            }
            return pair;
        });
    }

    public void add(Node node) {

        canvasPane.getChildren().addAll(node);
    }

    public void zoomOut() {
        ZoomHelper.Companion.zoom(canvasPane, canvasScrollPane, 0.0001);
        canvasScrollPane.setHvalue(0);
        canvasScrollPane.setVvalue(0);
    }
}