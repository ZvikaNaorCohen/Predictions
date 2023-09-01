package app.code;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
            Thread.currentThread().setName("main");
            launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/body/detailsBody.fxml"));
        Parent root = fxmlLoader.load();

        // Create a scene with the loaded FXML content
        Scene scene = new Scene(root);

        // Label filePathLabel = (Label) root.lookup("filePathLabel"); // 'root' is the parent of your FXML content
        // filePathLabel.setText("Path of the loaded file");

        // scene.getStylesheets().add(getClass().getResource("/resources/mainPageStyle.css").toExternalForm());

        // Set the scene on the stage
        primaryStage.setScene(scene);

        // Set the stage title (optional)
        primaryStage.setTitle("Your Application Title");

        // Show the stage
        primaryStage.show();
    }
}
