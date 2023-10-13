package resources.login;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import resources.app.ClientAppController;
import resources.utils.Constants;
import resources.utils.HttpClientUtil;

import java.io.IOException;
import java.net.URL;

import static javafx.application.Platform.exit;

public class LoginPageController {

    @FXML private TextField usernameInput;
    @FXML private Button loginButton;
    @FXML private Button aboutUsButton;
    @FXML private Button exitButton;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    @FXML
    public Label errorMessageLabel;
    @FXML private AnchorPane mainPanel;

    private ClientAppController mainController;

    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
    }

    public void setClientAppController(ClientAppController mainController){
        this.mainController = mainController;
    }


    @FXML
    public void onLoginPressed() {
        String input = usernameInput.getText();

        if (input.isEmpty()) {
            errorMessageProperty.set("You can't login with empty user name");
            return;
        }

        if (input.equals("admin")){
            errorMessageProperty.set("You can't login with \"admin\" user name");
            return;
        }
        String finalUrl = "";
        try {
            finalUrl = HttpUrl
                    .parse(Constants.LOGIN_PAGE)
                    .newBuilder()
                    .addQueryParameter("username", input)
                    .build()
                    .toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Error: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            URL url = getClass().getResource("/resources/app/clientApp.fxml");
                            FXMLLoader loader = new FXMLLoader(url);
                            Parent root = loader.load();
                            mainController = loader.getController();
                            mainController.setLoggedInUser(input);
                            mainController.setLoginPageController(LoginPageController.this);
                            Stage newStage = new Stage();
                            newStage.setTitle("Client App");
                            newStage.setScene(new Scene(root));
                            newStage.show();

                             mainController.initializeController();
                             mainController.updateAllScreens();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }



//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if (response.code() != 200) {
//                    String responseBody = response.body().string();
//                    Platform.runLater(() ->
//                            errorMessageProperty.set("Error: " + responseBody)
//                    );
//                } else {
//
//                    // Create new ClientAppController instance
//                    // Fxml file is located in: "/resources/app/clientApp.fxml"
//                    // And make sure that all the components are not null
//
//
//
//
////                    mainController.freshLogin(input, LoginPageController.this);
////                    mainController.setLoginPageController(LoginPageController.this);
////                    try {
////                        mainController.setMainControllers();
////                    } catch (IOException e) {
////                        throw new RuntimeException(e);
////                    }
////                    mainController.updateAllScreens();
//                }
//            }
        });
    }

    @FXML
    public void onExitPressed(){
        exit();
    }
}
