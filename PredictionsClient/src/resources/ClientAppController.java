package resources;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import resources.app.execution.ExecutionController;
import resources.app.login.LoginPageController;
import resources.app.requests.RequestsController;
import resources.app.results.ResultsController;
import resources.app.simulation.SimulationController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientAppController implements Initializable {

    private LoginPageController loginPageController;
    private SimulationController simulationComponentController;
    private ResultsController resultsComponentController;
    private ExecutionController executionComponentController;
    private RequestsController requestsComponentController;

    @FXML private AnchorPane mainApp;
    @FXML private AnchorPane requestsComponent;
    @FXML private AnchorPane executionComponent;
    @FXML private AnchorPane simulationComponent;
    @FXML private AnchorPane resultsComponent;

    @FXML private ScrollPane appScrollPaneComponent;
    @FXML private Text welcomeText;

    private String loggedInUser = "";
    public void setLoginPageController(LoginPageController loginPageController){
        this.loginPageController = loginPageController;
        loginPageController.setClientAppController(this);
    }

    public void freshLogin(String userName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/clientApp.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setTitle("Client App");
            newStage.setScene(new Scene(root));

            // Get the controller from the loaded FXML
            ClientAppController clientAppController = loader.getController();

            // Set the loggedInUser before calling initialize
            clientAppController.setLoggedInUser(userName);

            // Call initialize manually (since FXMLLoader won't call it again)
            clientAppController.initialize(null, null);

            // setMainControllers();

            newStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    public void updateAllScreens(){
        simulationComponentController.updateScreens();
        resultsComponentController.updateScreens();
        executionComponentController.updateScreens();
        requestsComponentController.updateScreens();
    }

    public void setMainControllers(){
        loginPageController.setClientAppController(this);
        simulationComponentController.setClientAppController(this);
        resultsComponentController.setClientAppController(this);
        executionComponentController.setClientAppController(this);
        requestsComponentController.setClientAppController(this);
    }

    public void setLoggedInUser(String userName) {
        loggedInUser = userName;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeText.setText("Welcome, " + loggedInUser);
    }
}
