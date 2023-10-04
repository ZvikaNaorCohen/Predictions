package resources.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import resources.ClientAppController;
import resources.app.management.ManagementPageController;

import java.net.URL;

public class AdminClient extends Application {
    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
            // Load the FXML file
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/resources/app/management/management.fxml");
            fxmlLoader.setLocation(url);
            AnchorPane root = fxmlLoader.load(url.openStream());

            Scene scene = new Scene(root, 1260, 720);
            // Get the controller instance
            primaryStage.setTitle("Predictions V3.0");
            ClientAppController.adminNowConnected();
            primaryStage.setScene(scene);
            primaryStage.show();
    }
}
