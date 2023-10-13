package resources.app;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import resources.execution.ExecutionController;
import resources.login.LoginPageController;
import resources.requests.RequestsController;
import resources.results.ResultsController;
import resources.simulation.SimulationController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClientAppController {

    @FXML private AnchorPane mainApp;


    @FXML private AnchorPane requestsComponent;
    @FXML private RequestsController requestsComponentController;
    @FXML private AnchorPane executionComponent;
    @FXML private ExecutionController executionComponentController;
    @FXML private AnchorPane simulationComponent;
    @FXML private SimulationController simulationComponentController;
    @FXML private AnchorPane resultsComponent;
    @FXML private ResultsController resultsComponentController;


    @FXML private ScrollPane appScrollPaneComponent;
    @FXML private Text welcomeText;

    private LoginPageController loginPageController;

    @FXML private Tab simulationTab;
    @FXML private Tab requestsTab;
    @FXML private Tab executionTab;
    @FXML private Tab resultsTab;

    private String loggedInUser = "";
    public void setLoginPageController(LoginPageController loginPageController){
        this.loginPageController = loginPageController;
        loginPageController.setClientAppController(this);
    }

    public void updateAllScreens(){
        simulationComponentController.run();
        resultsComponentController.updateScreens();
        executionComponentController.updateScreens();
        requestsComponentController.updateScreens();
    }

    public void initializeController() {
        simulationComponentController.setClientAppController(this);
        resultsComponentController.setClientAppController(this);
        executionComponentController.setClientAppController(this);
        requestsComponentController.setClientAppController(this);
    }

    public void setLoggedInUser(String userName) {
        loggedInUser = userName;
        welcomeText.setText("Welcome, " + loggedInUser + "! ");
    }
}
