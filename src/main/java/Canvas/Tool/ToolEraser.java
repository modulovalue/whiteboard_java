package Canvas.Tool;

import javafx.scene.input.MouseEvent;

public class ToolEraser extends ClickAndDragTool {

    private double radius;
    private Class aClass;

    public ToolEraser(double radius, Class aClass) {
        this.radius = radius;
        this.aClass = aClass;
    }

    public void mousePressedHandler(MouseEvent event) {
    }

    public void mouseDraggedHandler(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            canvasInterface.erase(getEventPoint(event), radius, aClass);
        }
    }

    public void mouseReleasedHandler(MouseEvent event) {
    }
}
