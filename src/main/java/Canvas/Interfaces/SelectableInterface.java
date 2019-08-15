package Canvas.Interfaces;

import Canvas.aPrimitives.DrawbleObj;
import javafx.scene.layout.Pane;

public interface SelectableInterface {
    void selected(DrawbleObj node);
    void resizeMode(boolean b);
    Pane getDrawingPane();
}