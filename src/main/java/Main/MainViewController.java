package Main;

import Canvas.Canvas.CanvasInterfaceImplementation;
import Canvas.Canvas.DrawableCanvas;
import Canvas.Interfaces.ResizeInterface;
import Canvas.Project.ProjectInfoFile;
import Canvas.Project.ProjectInfoLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML public ListView<ProjectInfoFile> projectListView;
    @FXML private Label emptyLabel;
    @FXML private Pane dragPane;
    @FXML private GridPane projectOverviewGridPane;
    @FXML private BorderPane resizeBorderPane;
    @FXML private StackPane stackPane;
    @FXML private GridPane whiteboardGridpane;

    DrawableCanvas canvas;

    private ObservableList<ProjectInfoFile> linkObservableList = FXCollections.observableArrayList ();

    public Runnable closeFunction;
    public ResizeInterface resizeInterface;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        linkObservableList.clear();
        linkObservableList.addAll(new ProjectInfoLoader().getProjectList());
        projectListView.setCellFactory(e -> new ProjectListCell());
        projectListView.setItems(linkObservableList);
        dragPane.setOnMouseDragged(event -> {
            resizeInterface.newWidth(event.getScreenX() / Toolkit.getDefaultToolkit().getScreenSize().getWidth());
        });
        canvas = new DrawableCanvas(stackPane);
        stackPane.getChildren().addAll(canvas.getOutermostContainer());
        resizeBorderPane.setPickOnBounds(false);
        showProjectsAction();

        projectListView.setOnMouseClicked(event -> {
            canvas.loadProject(projectListView.getSelectionModel().getSelectedItem());
            showWhiteboardAction();
            canvas.zoomOut();
        });
    }

    public void setupDragAndDrop(Scene scene) {
        canvas.setupDragAndDrop(scene);
    }

    public void addItem(ProjectInfoFile... item) {
        for (ProjectInfoFile itemLink : item) {
            ProjectInfoLoader projectInfoLoader = new ProjectInfoLoader();
            projectInfoLoader.add(itemLink);
            projectInfoLoader.putToPrefs();
            linkObservableList.clear();
            linkObservableList.addAll(new ProjectInfoLoader().getProjectList());
            projectListView.setItems(linkObservableList);
        }
        showProjectsAction();
    }

    @FXML private void showProjectsAction() {
        emptyLabel.setText("Create a new Project. [+]");
        canvas.setVisible(false);
        emptyLabel.setVisible(linkObservableList.isEmpty());
        projectOverviewGridPane.setVisible(true);
        projectOverviewGridPane.toFront();
        resizeBorderPane.toFront();
    }

    @FXML private void showWhiteboardAction() {
        emptyLabel.setText("Please select a Project.");
        boolean projectLoaded = CanvasInterfaceImplementation.Companion.getLoadedProject() == null;
        emptyLabel.setVisible(projectLoaded);
        canvas.setVisible(!projectLoaded);
        canvas.getOutermostContainer().toFront();
        resizeBorderPane.toFront();
    }

    @FXML private void createProjectAction() {
        new ProjectInfoLoader().createNewProject();
        linkObservableList.clear();
        linkObservableList.addAll(new ProjectInfoLoader().getProjectList());
    }

    @FXML private void openProjectAction() {
        new ProjectInfoLoader().openProject();
        linkObservableList.clear();
        linkObservableList.addAll(new ProjectInfoLoader().getProjectList());
    }

    @FXML
    private void closeAction() {
        closeFunction.run();
    }
}