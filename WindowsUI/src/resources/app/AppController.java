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
    @FXML private Tab detailsBodyComponent;
    @FXML private DetailsBodyController detailsBodyComponentController;

    @FXML
    public void initialize() {
        if(headerComponentController != null){
            headerComponentController.setMainController(this);
        }
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
        // World is updated, need to pass all the data to all screens
    }

    public void setDataFromFile(PRDWorld world){
        pastRuns = new HashMap<>();
        oldWorld = world;
        allData = new AllData(world);
        myRunningWorld = new ContextImpl(allData);
    }
}
