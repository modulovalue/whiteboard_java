package Canvas.Interfaces;

import Canvas.aPrimitives.DrawbleObj;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

public interface CanvasInterface {
    void addNewObject(DrawbleObj object);
    void erase(Point2D point, double radius, Class aClass);
    Pane getDrawingPane();
}
