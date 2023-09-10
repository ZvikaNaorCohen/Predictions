package resources.body;

import DTO.ContextDTO;
import definition.entity.EntityDefinition;
import execution.context.Context;
import executionManager.EntityData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import resources.app.AppController;

import java.util.List;
import java.util.Map;

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

    @FXML private TableView<EntityData> entitiesTable;
    @FXML private TableColumn<EntityData, String> nameCol;
    @FXML private TableColumn<EntityData, Integer> countCol;

    @FXML private ListView executionDetailsListView;

    public ResultsBodyController() {
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void addNewExecution(Context context){
        executionListView.getItems().add(context.getID());
        addExecutionEntities(context);
    }

    private void addExecutionEntities(Context context){
        ContextDTO contextDTO = new ContextDTO(context);

        Map<String, Integer> entityNameToAliveCount = contextDTO.getAliveCountMap();
        ObservableList<EntityData> entityDataList = FXCollections.observableArrayList();

        // Populate the entityDataList with EntityData objects
        entityDataList.clear();
        for (Map.Entry<String, Integer> entry : entityNameToAliveCount.entrySet()) {
            String entityName = entry.getKey();
            Integer count = entry.getValue();

            EntityData entityData = new EntityData(entityName, count);
            entityDataList.add(entityData);
        }

        // Set the items for the entitiesTable
        entitiesTable.setItems(entityDataList);

        // Define how the columns should map to the data properties
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
    }
}
