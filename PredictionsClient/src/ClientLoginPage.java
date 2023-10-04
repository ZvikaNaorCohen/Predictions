import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import resources.ClientAppController;
import resources.app.LoginPageController;

import java.net.URL;

public class ClientLoginPage extends Application {
    ClientAppController mainAppController = new ClientAppController();
    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // FXMLLoader
        FXMLLoader fxmlLoader = new FXMLLoader();
        // URL
        URL url = getClass().getResource("/resources/app/clientLoginPage.fxml");
        fxmlLoader.setLocation(url);
        AnchorPane root = fxmlLoader.load(url.openStream());
        LoginPageController loginPageController = fxmlLoader.getController();
        mainAppController.setLoginPageController(loginPageController);


        Scene scene = new Scene(root, 1260, 720);
        primaryStage.setTitle("Predictions V3.0");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
};
