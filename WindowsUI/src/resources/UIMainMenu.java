package resources;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import resources.app.AppController;
import resources.body.DetailsBodyController;
import resources.header.HeaderController;

import java.net.URL;

public class UIMainMenu extends Application {



    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Header
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        URL url = getClass().getResource("header/header.fxml");
//        ScrollPane headerComponent = fxmlLoader.load(url.openStream());
//        HeaderController headerController = fxmlLoader.getController();
//
//
//        // Body
//        fxmlLoader = new FXMLLoader();
//        url = getClass().getResource("/resources/body/detailsBody.fxml");
//        fxmlLoader.setLocation(url);
//        AnchorPane bodyComponent = fxmlLoader.load(url.openStream());
//        DetailsBodyController bodyController = fxmlLoader.getController();

        // Main
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/resources/app/app.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane root = fxmlLoader.load(url.openStream());
        AppController appController = fxmlLoader.getController();

        // root.setTop(headerComponent);
        // root.setCenter(bodyComponent);

        // appController.setBodyComponentController(bodyController);
        // appController.setHeaderComponentController(headerController);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
