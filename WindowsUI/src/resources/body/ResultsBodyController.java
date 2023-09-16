package resources.body;

import DTO.ContextDTO;
import execution.context.Context;
import executionManager.EntityData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import resources.app.AppController;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import java.util.Map;

public class ResultsBodyController {
    private final String runningLabel = " - RUNNING";
    private final String finishedLabel = " - FINISHED";
    @FXML
    private AnchorPane subAnchorPane;
    @FXML private AppController mainController;

    @FXML
    private ListView<String> executionListView;

    @FXML private ListView executionResultListView;

    @FXML private Text progressText;
    // @FXML private Text simulationFinishedText;
    @FXML private Text secondsText;
    @FXML private Text ticksText;

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
    private ScheduledExecutorService executorService;

    @FXML private ListView executionDetailsListView;

    private int selectedContextID = -1; // Initialize to an invalid value

    public ResultsBodyController() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::updateTexts, 0, 200, TimeUnit.MILLISECONDS);
    }

    private void updateTexts() {
        if (selectedContextID != -1) {
            Context selectedContext = findContextById(selectedContextID);
            if (selectedContext != null) {
                ContextDTO contextDTO = new ContextDTO(selectedContext);
                String ticksPassed = String.valueOf(contextDTO.getCurrentTicks());
                String secondsPassed = String.valueOf(contextDTO.getSecondsPassed());
                Platform.runLater(() -> {
                    ticksText.setText(ticksPassed);
                    secondsText.setText(secondsPassed);
                    addExecutionEntities(selectedContext);
                    handleEndOfSimulation(selectedContext);
                });
            }
        }
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void addNewExecution(Context context){
        String dataToAdd = context.getID() + runningLabel;
        executionListView.getItems().add(dataToAdd);
    }

    private void updateExecutionLabelToFinished(){
        ObservableList<String> items = executionListView.getItems();

        for (int i = 0; i < items.size(); i++) {
            String item = items.get(i);
            if (item.equals(selectedContextID + runningLabel)) {
                String updatedItem = selectedContextID + finishedLabel;
                items.set(i, updatedItem);
                executionListView.refresh();
                break;
            }
        }
    }

    private void handleEndOfSimulation(Context context){
        if(!context.isRunning().get()){
            updateExecutionLabelToFinished();
            pauseButton.disableProperty().unbind();
            resumeButton.disableProperty().unbind();
            stopButton.disableProperty().unbind();
            // simulationFinishedText.setText("SIMULATION FINISHED");
            pauseButton.setDisable(true);
            resumeButton.setDisable(true);
            stopButton.setDisable(true);
        }
        else{
            // simulationFinishedText.setText("");
        }
    }

    @FXML
    private void onExecutionItemSelected() {
        String selectedIDString = executionListView.getSelectionModel().getSelectedItem();
        String[] parts1 = selectedIDString.split("[\\s-]+");
        Integer selectedID = Integer.parseInt(parts1[0]);
        if (selectedID != null) {
            selectedContextID = selectedID;

            Context selectedContext = findContextById(selectedContextID); // Implement this method
            addExecutionEntities(selectedContext);

            pauseButton.disableProperty().bind(selectedContext.isPaused());
            resumeButton.disableProperty().bind(selectedContext.isPaused().not());
            stopButton.disableProperty().bind(selectedContext.isRunning().not());
            handleEndOfSimulation(selectedContext);
            pauseButton.setOnMouseClicked(event -> {selectedContext.pauseRun();});
            resumeButton.setOnMouseClicked(event -> {selectedContext.resumeRun();});
            stopButton.setOnMouseClicked(event -> {selectedContext.stopRun();
                pauseButton.disableProperty().unbind();
                resumeButton.disableProperty().unbind();
                // simulationFinishedText.setText("SIMULATION FINISHED");
                pauseButton.setDisable(true);
                resumeButton.setDisable(true);});
        }
    }

    private Context findContextById(int contextID) {
        return mainController.getExecutionManager().getContextByID(contextID);
    }

    private void addExecutionEntities(Context context){
        if (context != null) {
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

            refreshTables();
        }
    }

    private void refreshTables(){
        entitiesTable.refresh();
    }
}
