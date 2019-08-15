package Main;

import Canvas.Project.ProjectInfoFile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ProjectListCell extends ListCell<ProjectInfoFile> {

    @FXML private Label nameLabel;
    @FXML private Label urlLabel;
    @FXML private GridPane gridPane;

    ProjectInfoFile project;

    private FXMLLoader fxmlLoader;

    @Override
    protected void updateItem(ProjectInfoFile item, boolean empty) {
        super.updateItem(item, empty);

        if(!(empty || item == null)) {
            if (fxmlLoader == null) {
                this.project = item;
                fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Viewfxml/ProjectListCell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                nameLabel.setText(project.name);
                urlLabel.setText(project.url);
                setGraphic(gridPane);
            }
        }
    }
}

