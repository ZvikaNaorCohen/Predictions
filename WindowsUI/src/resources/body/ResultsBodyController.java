package resources.body;

import definition.entity.EntityDefinition;
import execution.context.Context;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import resources.app.AppController;

public class ResultsBodyController {
    @FXML
    private AnchorPane subAnchorPane;
    @FXML private AppController mainController;

    @FXML
    private ListView<Integer> executionListView;

    @FXML private ListView executionResultListView;

    @FXML private Label progressLabel;
    @FXML private Label tickLabel;
    @FXML private Label secondsLabel;
    @FXML private ProgressBar progressBar;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    @FXML private Button stopButton;

    @FXML private TableView<EntityDefinition> entitiesTable;
    @FXML private TableColumn<EntityDefinition, String> nameCol;
    @FXML private TableColumn<EntityDefinition, String> countCol;

    @FXML private ListView executionDetailsListView;

    public ResultsBodyController() {
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void addNewExecution(Context context){
        executionListView.getItems().add(context.getID());
    }
}
