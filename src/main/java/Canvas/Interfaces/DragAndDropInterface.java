package Canvas.Interfaces;

import javafx.scene.input.DragEvent;

import java.io.File;

/**
 * Created by valauskasmodestas on 18.06.17.
 */
public interface DragAndDropInterface {
    void newImage(File file, DragEvent e);
    void newDirectory(File file, DragEvent e);
    void newLink(String link, DragEvent e);
}
