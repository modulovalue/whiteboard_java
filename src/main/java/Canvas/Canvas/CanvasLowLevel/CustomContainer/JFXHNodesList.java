package Canvas.Canvas.CanvasLowLevel.CustomContainer;

import javafx.animation.Animation.Status;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * list of nodes that are toggled On/Off by clicking on the 1st node
 *
 * @author Shadi Shaheen
 * @version 1.0
 * @since 2016-03-09
 */
public class JFXHNodesList extends HBox {

    private final HashMap<Node, Callback<Boolean, Collection<KeyValue>>> animationsMap = new HashMap<>();
    public boolean expanded = false;
    private final Timeline animateTimeline = new Timeline();

    /**
     * Creates empty nodes list.
     */
    public JFXHNodesList(double spacing) {
        this.setPickOnBounds(false);
        this.getStyleClass().add("jfx-nodes-list");
        this.setSpacing(spacing);
    }

    /**
     * Adds node to list.
     * Note: this method must be called instead of getChildren().add().
     *
     * @param node {@link Region} to add
     */
    public void addAnimatedNode(Region node) {
        addAnimatedNode(node, null);
    }
    /**
     * Adds node to list.
     * Note: this method must be called instead of getChildren().add().
     *
     * @param node {@link Region} to add
     */
    public void add(Region node) {
        addAnimatedNode(node, null);
    }

    /**
     * add node to list with a specified callback that is triggered after the node animation is finished.
     * Note: this method must be called instead of getChildren().add().
     *
     * @param node {@link Region} to add
     */
    public void addAnimatedNode(Region node, Callback<Boolean, Collection<KeyValue>> animationCallBack) {
        // create container for the node if it's a sub nodes list
        if (node instanceof JFXHNodesList) {
            StackPane container = new StackPane(node);
            container.setPickOnBounds(false);
            addAnimatedNode(container, animationCallBack);
            return;
        }

        // init node property
        node.setVisible(false);
        node.minWidthProperty().bind(node.prefWidthProperty());
        node.minHeightProperty().bind(node.prefHeightProperty());
        if (this.getChildren().size() > 0) {
            initNode(node);
        } else {
            if (node instanceof Button) {
                ((Button) node).setOnAction((action) -> this.animateList());
            } else {
                node.setOnMouseClicked((click) -> this.animateList());
            }
            node.getStyleClass().add("trigger-node");
        }

        // init the list height and width
        if (this.getChildren().size() == 0) {
            node.setVisible(true);
            this.minHeightProperty().bind(node.prefHeightProperty());
            this.maxHeightProperty().bind(node.prefHeightProperty());
            this.minWidthProperty().bind(node.prefWidthProperty());
            this.maxWidthProperty().bind(node.prefWidthProperty());
        }

        // add the node and its listeners
        this.getChildren().add(node);
        this.rotateProperty()
                .addListener((o, oldVal, newVal) -> node.setRotate(newVal.doubleValue() % 180 == 0 ? newVal.doubleValue() : -newVal
                        .doubleValue()));
        if (animationCallBack == null && this.getChildren().size() != 1) {
            animationCallBack = (expanded) -> initDefaultAnimation(node, expanded);
        } else if (animationCallBack == null && this.getChildren().size() == 1) {
            animationCallBack = (expanded) -> new ArrayList<>();
        }
        animationsMap.put(node, animationCallBack);
    }

    /**
     * Animates the list to show/hide the nodes.
     */
    public void animateList() {
        expanded = !expanded;

        if (animateTimeline.getStatus() == Status.RUNNING) {
            animateTimeline.stop();
        }

        animateTimeline.getKeyFrames().clear();
        double duration = 120 / (double) this.getChildren().size();

        // show child nodes
        if (expanded) {
            this.getChildren().forEach(child -> child.setVisible(true));
        }

        // add child nodes animation
        for (int i = 1; i < this.getChildren().size(); i++) {
            Node child = this.getChildren().get(i);
            Collection<KeyValue> keyValues = animationsMap.get(child).call(expanded);
            animateTimeline.getKeyFrames()
                    .add(new KeyFrame(Duration.millis(i * duration),
                            keyValues.toArray(new KeyValue[keyValues.size()])));
        }
        // add 1st element animation
        Collection<KeyValue> keyValues = animationsMap.get(this.getChildren().get(0)).call(expanded);
        animateTimeline.getKeyFrames()
                .add(new KeyFrame(Duration.millis(160), keyValues.toArray(new KeyValue[keyValues.size()])));

        // hide child nodes to allow mouse events on the nodes behind them
        if (!expanded) {
            animateTimeline.setOnFinished((finish) -> {
                for (int i = 1; i < this.getChildren().size(); i++) {
                    this.getChildren().get(i).setVisible(false);
                }
            });
        } else {
            animateTimeline.setOnFinished(null);
        }

        animateTimeline.play();
    }

    protected void initNode(Node node) {
        node.setScaleX(0);
        node.setScaleY(0);
        node.getStyleClass().add("sub-node");
    }

    // init default animation keyvalues
    private ArrayList<KeyValue> initDefaultAnimation(Region region, boolean expanded) {
        ArrayList<KeyValue> defaultAnimationValues = new ArrayList<>();
        defaultAnimationValues.add(new KeyValue(region.scaleXProperty(), expanded ? 1 : 0, Interpolator.EASE_BOTH));
        defaultAnimationValues.add(new KeyValue(region.scaleYProperty(), expanded ? 1 : 0, Interpolator.EASE_BOTH));
        return defaultAnimationValues;
    }
}

