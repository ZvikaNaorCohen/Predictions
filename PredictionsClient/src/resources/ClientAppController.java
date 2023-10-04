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
import resources.app.LoginPageController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientAppController implements Initializable {

    private LoginPageController loginPageController;

    @FXML private AnchorPane mainApp;
    @FXML private ScrollPane appScrollPane;
    @FXML private Text welcomeText;

    private static boolean adminConnected = false;

    public static void adminNowConnected(){
        adminConnected = true;
    }

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

            newStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    public void setLoggedInUser(String userName) {
        loggedInUser = userName;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeText.setText("Welcome, " + loggedInUser);
    }
}
