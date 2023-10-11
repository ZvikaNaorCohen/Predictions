package resources.app;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import resources.ClientAppController;
import resources.utils.Constants;
import resources.utils.HttpClientUtil;

import java.io.IOException;

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
            // Rest of your code
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
                        mainController.freshLogin(input);
                        mainController.setMainControllers();
                    });
                }
            }
        });
    }

    @FXML
    public void onExitPressed(){
        exit();
    }
}
