package resources.app.management;

import engine.AllData;
import generated.PRDWorld;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import resources.app.AdminClient;

import java.net.URL;

public class ManagementPageController {
    @FXML private ManagementHeaderController headerComponentController;
    @FXML private ScrollPane headerComponent;
    @FXML private ManagementSimulationBreakdownController simulationBreakdownComponentController;

    @FXML private AnchorPane simulationBreakdownComponent;


    @FXML private AdminClient mainController;

    public void setMainControllers(AdminClient mainController){
        headerComponentController.setMainController(mainController);
        headerComponentController.setManagementPageController(this);
        simulationBreakdownComponentController.setMainController(mainController);
        simulationBreakdownComponentController.setManagementPageController(this);
    }

    public void updateThreadsCount(int newCount){
        simulationBreakdownComponentController.updateThreadsCount(newCount);
    }

    public void displayAllData(PRDWorld oldWorld, AllData allData){
        simulationBreakdownComponentController.displayAllData(oldWorld, allData);
    }

}
