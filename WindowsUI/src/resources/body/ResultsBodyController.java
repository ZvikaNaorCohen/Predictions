package resources.body;

import DTO.ContextDTO;
import definition.property.api.PropertyType;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import execution.instance.property.PropertyInstance;
import executionManager.EntityData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import resources.app.AppController;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ResultsBodyController {
    private final String runningLabel = " - RUNNING";
    private final String finishedLabel = " - FINISHED";
    @FXML
    private AnchorPane subAnchorPane;
    @FXML private AppController mainController;

    @FXML
    private ListView<String> executionListView;

    @FXML
    private ComboBox<String> entityComboBox;

    @FXML
    private ComboBox<String> propertyComboBox;

    @FXML private ListView executionResultListView;

    @FXML private Text progressText;
    // @FXML private Text simulationFinishedText;
    @FXML private Text secondsText;
    @FXML private Text ticksText;

    @FXML private ProgressBar progressBar;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    @FXML private Button stopButton;

    @FXML private Button consistencyButton;
    @FXML private Button averageButton;

    @FXML private TableView<EntityData> entitiesTable;
    @FXML private TableColumn<EntityData, String> nameCol;
    @FXML private TableColumn<EntityData, Integer> countCol;

    @FXML private AnchorPane graphAnchorPane;
    @FXML private ScrollPane graphScrollPane;
    private ScheduledExecutorService executorService;

    @FXML private ListView executionDetailsListView;

    @FXML
    private Button histogramButton;

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
                float progressPercent = contextDTO.getProgressPercent();
                String ticksPassed = String.valueOf(contextDTO.getCurrentTicks());
                String secondsPassed = String.valueOf(contextDTO.getSecondsPassed());
                String progress = String.valueOf(contextDTO.getProgressPercent());
                Platform.runLater(() -> {
                    ticksText.setText(ticksPassed);
                    secondsText.setText(secondsPassed);
                    progressText.setText(progress + "%");
                    progressBar.setProgress(progressPercent/100);

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

    private void updateGraphTab() {
        Context context = findContextById(selectedContextID);
        Map<Integer, Integer> aliveEntitiesMap = context.getDataForGraph();
        graphAnchorPane.getChildren().clear();

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);
        xAxis.setLabel("Tick");
        yAxis.setLabel("Alive Entities");
        XYChart.Series series = new XYChart.Series();
        bc.setPrefWidth(800); // Set an appropriate width
        bc.setPrefHeight(270); // Set an appropriate height

        for (Map.Entry<Integer, Integer> entry : aliveEntitiesMap.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey().toString(), entry.getValue());
            series.getData().add(data);
        }

        bc.getData().addAll(series);
        graphAnchorPane.getChildren().add(bc);
        graphScrollPane.setContent(graphAnchorPane);
    }

    private void handleEndOfSimulation(Context context){
        if(!context.isRunning().get()){
            updateExecutionLabelToFinished();
            pauseButton.disableProperty().unbind();
            resumeButton.disableProperty().unbind();
            stopButton.disableProperty().unbind();
            Set<String> entityNames = context.getAliveEntityNames();
            for(String name : entityNames){
                if(!entityComboBox.getItems().contains(name)){
                    entityComboBox.getItems().add(name);
                }
            }
            progressBar.setProgress(1);
            progressText.setText("100%");
            pauseButton.setDisable(true);
            resumeButton.setDisable(true);
            stopButton.setDisable(true);
        }
        else{
            entityComboBox.getItems().clear();
            graphAnchorPane.getChildren().clear();
        }
    }

    @FXML
    public void onSelectedEntity(){
        String selectedItem = entityComboBox.getSelectionModel().getSelectedItem();
        propertyComboBox.getItems().clear(); // Clear the current properties

        if (selectedItem != null) {
            Context context = findContextById(selectedContextID);
            Set<String> properties = context.getSpecificEntityProperties(selectedItem);

            // Add the properties of the selected entity to the propertyComboBox
            propertyComboBox.getItems().addAll(properties);
        }
    }

    private void setDisabledProperties(Context selectedContext){
        pauseButton.disableProperty().bind(selectedContext.isPaused());
        resumeButton.disableProperty().bind(selectedContext.isPaused().not());
        stopButton.disableProperty().bind(selectedContext.isRunning().not());
        entityComboBox.disableProperty().bind(selectedContext.isRunning());
        propertyComboBox.disableProperty().bind(selectedContext.isRunning());
        histogramButton.disableProperty().bind(selectedContext.isRunning());
        averageButton.disableProperty().bind(selectedContext.isRunning());
        consistencyButton.disableProperty().bind(selectedContext.isRunning());
    }

    private void setOnMouseClicked(Context selectedContext){
        pauseButton.setOnMouseClicked(event -> {selectedContext.pauseRun();});
        resumeButton.setOnMouseClicked(event -> {selectedContext.resumeRun();});
        stopButton.setOnMouseClicked(event -> {selectedContext.stopRun();
            pauseButton.disableProperty().unbind();
            resumeButton.disableProperty().unbind();
            pauseButton.setDisable(true);
            resumeButton.setDisable(true);});
    }

    private boolean checkEntityAndPropertySelected(){
        if(entityComboBox.getSelectionModel().getSelectedItem() == null || propertyComboBox.getSelectionModel().getSelectedItem() == null){
            Alert newAlert = new Alert(Alert.AlertType.ERROR);
            newAlert.setContentText("Please select entity and property!");
            newAlert.showAndWait();
            return false;
        }
        return true;
    }

    @FXML
    private void averageButtonOnClick(){
        if(!checkEntityAndPropertySelected()){
            return;
        }
        Context context = findContextById(selectedContextID);
        String entityName = entityComboBox.getSelectionModel().getSelectedItem();
        String propertyName = propertyComboBox.getSelectionModel().getSelectedItem();
        float average = 0;
        int counter = 0;
        for (EntityInstance instance : context.getEntityInstanceManager().getInstances()) {
            PropertyInstance propInstance = instance.getPropertyByName(propertyName);
            if(entityName.equals(instance.getEntityDefinitionName())){
                if(propInstance.getPropertyDefinition().getType().name().equalsIgnoreCase("float")){
                    average += (float)propInstance.getValue();
                    counter++;
                }
                else{
                    Alert newAlert = new Alert(Alert.AlertType.ERROR);
                    newAlert.setContentText("Property is not a float! Can't check average!");
                    newAlert.showAndWait();
                    return;
                }
            }
        }

        Alert newAlert = new Alert(Alert.AlertType.INFORMATION);
        newAlert.setContentText("Average for property: " + propertyName + " is: " + average/counter + ". \n");
        newAlert.showAndWait();
    }

    @FXML
    private void histogramButtonOnPressed(){
        if(!checkEntityAndPropertySelected()){
            return;
        }
        Map<Object, Integer> propertyHistogram = new HashMap<>();
        Context context = findContextById(selectedContextID);
        String entityName = entityComboBox.getSelectionModel().getSelectedItem();
        String propInstanceName = propertyComboBox.getSelectionModel().getSelectedItem();
        for (EntityInstance instance : context.getEntityInstanceManager().getInstances()) {
            if (instance.getEntityDefinitionName().equals(entityName)) {
                PropertyInstance propInstance = instance.getPropertyByName(propInstanceName);
                    Object propValue = propInstance.getValue();
                    propertyHistogram.put(propValue, propertyHistogram.getOrDefault(propValue, 0) + 1);
            }
        }

        Alert alertForStatistics = new Alert(Alert.AlertType.INFORMATION);
        alertForStatistics.setTitle("Data Display");
        alertForStatistics.setHeaderText("Most updated information:");

        TextArea textArea = new TextArea();
        textArea.setEditable(false);

        for (Map.Entry<Object, Integer> entry : propertyHistogram.entrySet()) {
            Object key = entry.getKey();
            Integer value = entry.getValue();
            textArea.appendText("Property value: " + key + ", count: " + value + ".\n");
        }

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(scrollPane, 0, 0);

        alertForStatistics.getDialogPane().setContent(gridPane);
        alertForStatistics.showAndWait();
    }

    @FXML
    private void onExecutionItemSelected() {
        try{
            String selectedIDString = executionListView.getSelectionModel().getSelectedItem();
            String[] parts1 = selectedIDString.split("[\\s-]+");
            Integer selectedID = Integer.parseInt(parts1[0]);
            if (selectedID != null) {
                selectedContextID = selectedID;

                Context selectedContext = findContextById(selectedContextID); // Implement this method
                addExecutionEntities(selectedContext);
                setDisabledProperties(selectedContext);
                handleEndOfSimulation(selectedContext);
                setOnMouseClicked(selectedContext);
                if(!selectedContext.isRunning().get()){
                    updateGraphTab();
                }
                onSelectedEntity();
            }
        }catch(NullPointerException e){
            return;
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
