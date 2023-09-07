package resources.app;

import DTO.ContextDTO;
import engine.AllData;
import execution.context.ContextImpl;
import generated.PRDWorld;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import resources.body.DetailsBodyController;
import resources.body.NewExecutionBodyController;
import resources.body.ResultsBodyController;
import resources.header.HeaderController;
import execution.context.Context;

import java.util.HashMap;
import java.util.Map;

public class AppController {
    private Context myRunningWorld;
    private PRDWorld oldWorld;
    private Map<String, Context> pastRuns;

    private AllData allData;
    @FXML
    private ScrollPane headerComponent;
    @FXML private HeaderController headerComponentController;
    @FXML private AnchorPane detailsBodyComponent;
    @FXML private DetailsBodyController detailsBodyComponentController;
    @FXML private TabPane allTabs;
    @FXML private Tab newExecutionTab;
    @FXML private Tab detailsTab;
    @FXML private Tab resultsTab;

    @FXML private AnchorPane resultsBodyComponent;
    @FXML private ResultsBodyController resultsBodyComponentController;

    @FXML private AnchorPane newExecutionBody;

    @FXML private NewExecutionBodyController newExecutionBodyController;

    @FXML
    public void initialize() {
        if(headerComponentController != null && detailsBodyComponentController != null){
            headerComponentController.setMainController(this);
            detailsBodyComponentController.setMainController(this);
            resultsBodyComponentController.setMainController(this);
            newExecutionBodyController.setMainController(this);

        }
    }

    public void switchToResultsTab() {
        allTabs.getSelectionModel().select(resultsTab);
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

    public void setDataFromFile(PRDWorld world){
        pastRuns = new HashMap<>();
        oldWorld = world;
        allData = new AllData(world);
        myRunningWorld = new ContextImpl(allData);
    }
}
