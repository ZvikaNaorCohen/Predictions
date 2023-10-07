package resources.app;

import com.sun.istack.internal.NotNull;
import engine.generated.PRDWorld;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import resources.ClientAppController;
import resources.app.allocations.AllocationsController;
import resources.app.management.ManagementHeaderController;
import resources.app.management.ManagementPageController;
import resources.app.management.ManagementSimulationBreakdownController;
import resources.utils.Constants;
import resources.utils.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class AdminClient extends Application {

    @FXML private AnchorPane managementPageComponent;
    @FXML private AnchorPane allocationsComponent;
    @FXML private ManagementPageController managementPageComponentController;
    @FXML private AllocationsController allocationsComponentController;

    @FXML
    public void initialize() {
        managementPageComponentController.setMainControllers(this);
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        launch(args);
    }

    private void printError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String finalUrl = "";
        try {
            try {
                finalUrl = HttpUrl
                        .parse(Constants.ADMIN_LOGIN_PAGE)
                        .newBuilder()
                        .build()
                        .toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            HttpClientUtil.runAsync(finalUrl, new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() ->
                            printError(e.getMessage()));
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                    if (response.code() != 200) {
//                        String responseBody = response.body().string();
//                        Platform.runLater(() ->
//                                printError(responseBody)
//                        );
//                    } else {
                        Platform.runLater(() -> {
                            freshStart(primaryStage);
                        });
//                    }
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void freshStart(Stage primaryStage){
        try{
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/resources/app/adminClient.fxml");
        fxmlLoader.setLocation(url);
        AnchorPane root = fxmlLoader.load(url.openStream());

        Scene scene = new Scene(root, 1260, 720);
        // Get the controller instance
        primaryStage.setTitle("Predictions V3.0");
        primaryStage.setScene(scene);
        primaryStage.show();}
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void updateSimulationsBreakdown(PRDWorld oldWorld, AllData allData){
        managementPageComponentController.displayAllData(oldWorld, allData);
    }
}
