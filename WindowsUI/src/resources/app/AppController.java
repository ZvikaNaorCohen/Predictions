package resources.app;

import DTO.ContextDTO;
import engine.AllData;
import execution.context.ContextImpl;
import executionManager.ExecutionManager;
import generated.PRDWorld;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import resources.body.DetailsBodyController;
import resources.body.NewExecutionBodyController;
import resources.body.ResultsBodyController;
import resources.header.HeaderController;
import execution.context.Context;

import java.util.HashMap;
import java.util.Map;

public class AppController {
    private Context myRunningWorld;
    private ExecutionManager executionManager;

    private Map<Integer, Context> idToContext = new HashMap<>();
    private Map<Integer, AllData> copiedIdToAllData = new HashMap<>();
    private Map<Integer, Context> copiedIdToContext = new HashMap<>();
    private PRDWorld oldWorld;
    private Map<String, Context> pastRuns;

    private AllData allData;
    @FXML
    private ScrollPane headerComponent;

    @FXML
    private GridPane secondGridPane;

    @FXML private HeaderController headerComponentController;
    @FXML private AnchorPane detailsBodyComponent;
    @FXML private DetailsBodyController detailsBodyComponentController;
    @FXML private TabPane allTabs;
    @FXML private Tab newExecutionTab;
    @FXML private Tab detailsTab;
    @FXML private Tab resultsTab;
    @FXML private Button queueManagementButton;

    @FXML private AnchorPane resultsBodyComponent;
    @FXML private ResultsBodyController resultsBodyComponentController;

    @FXML private AnchorPane newExecutionBody;

    @FXML private NewExecutionBodyController newExecutionBodyController;

    @FXML
    public void queueManagementButtonOnClick(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Queue Management");
        alert.setHeaderText("Most updated information:");
        String text = "";
        int queueSize = executionManager.getQueueSize();
        int runningContexts = executionManager.getRunningContexts();
        text += "Size of queue: " + queueSize + ". \n";
        text += "Running threads: " +  runningContexts + ". \n";
        text += "Finished threads: " + (queueSize - runningContexts) + ". \n";

        alert.setContentText(text);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        secondGridPane.toBack();
        if(headerComponentController != null && detailsBodyComponentController != null){
            headerComponentController.setMainController(this);
            detailsBodyComponentController.setMainController(this);
            resultsBodyComponentController.setMainController(this);
            newExecutionBodyController.setMainController(this);
        }
    }

    public int getIDForContext(){
        return executionManager.getIDForContext();
    }

    public void switchToResultsTab() {
        allTabs.getSelectionModel().select(resultsTab);
    }

    public void switchToExecutionTab(){
        allTabs.getSelectionModel().select(newExecutionTab);
    }

    public ExecutionManager getExecutionManager() {
        return executionManager;
    }

    public void runExecution(Context context, Context copied) {
        if (executionManager.addNewContext(context, copied)) {
            executionManager.runSpecificContext(context.getID());
        }
    }

    public void addNewExecution(Context context, AllData copiedAllData, Context copiedContext){
        idToContext.put(context.getID(), context);
        copiedIdToContext.put(context.getID(), copiedContext);
        copiedIdToAllData.put(context.getID(), copiedAllData);
        resultsBodyComponentController.addNewExecution(context);
    }

    public void rerunButtonClicked(int contextID){
        newExecutionBodyController.rerunButtonClicked(oldWorld, copiedIdToAllData.get(contextID), copiedIdToContext.get(contextID));
    }

    public void setHeaderComponentController(HeaderController headerComponentController) {
        this.headerComponentController = headerComponentController;
        headerComponentController.setMainController(this);
    }

    public void setBodyComponentController(DetailsBodyController bodyComponentController) {
        this.detailsBodyComponentController = bodyComponentController;
        bodyComponentController.setMainController(this);
    }

    public void updateScreenOne(){
        detailsBodyComponentController.displayAllData(myRunningWorld, allData, oldWorld);
    }

    public void updateScreenTwo(){
        newExecutionBodyController.displayAllData(oldWorld, allData);
    }

    public void updateScreenThree(){
        resultsBodyComponentController.clearData();
    }

    public void setDataFromFile(PRDWorld world){
        idToContext = new HashMap<>();
        oldWorld = world;
        allData = new AllData(world);
        myRunningWorld = new ContextImpl(allData);
        executionManager = new ExecutionManager(myRunningWorld.getThreadsCount());
    }
}
