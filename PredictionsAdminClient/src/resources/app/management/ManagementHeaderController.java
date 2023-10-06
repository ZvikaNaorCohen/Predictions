package resources.app.management;

import com.google.gson.JsonObject;
import com.sun.istack.internal.NotNull;
import engine.AllData;
import file.validate.impl.PRDWorldValid;
import generated.PRDWorld;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import okhttp3.*;
import resources.app.AdminClient;
import resources.utils.Constants;
import resources.utils.HttpClientUtil;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

import static file.read.XMLRead.getWorldFromScheme;

public class ManagementHeaderController {
    @FXML private Button loadFileButton;
    @FXML private Button threadCountButton;
    @FXML private Text filePathText;
    @FXML private AdminClient mainController;
    @FXML private ManagementPageController managementPageController;

    private AllData allData;

    public void setMainController(AdminClient controller){
        mainController = controller;
    }
    public void setManagementPageController(ManagementPageController controller){
        managementPageController = controller;
    }

    public void threadsCountButtonClicked(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Thread Count");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter the number of threads:");

        dialog.showAndWait().ifPresent(threadCount -> {
            try {
                int numberOfThreads = Integer.parseInt(threadCount);
                managementPageController.updateThreadsCount(numberOfThreads);
            } catch (NumberFormatException e) {
                // Handle non-numeric input
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid numeric value for the number of threads.");
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void loadFileButtonClicked() throws JAXBException, IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        File selectedFile = fileChooser.showOpenDialog(loadFileButton.getScene().getWindow());
        String filePath = selectedFile.getAbsolutePath();

        RequestBody body = new FormBody.Builder().add("filePath", filePath).build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constants.FILE_UPLOAD_PAGE)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                try {
                PRDWorld inputWorld = getWorldFromScheme(filePath.toLowerCase());
                setDataFromFile(inputWorld);
                filePathText.setText(filePath);
                showFileLoaded();
                mainController.updateSimulationsBreakdown(inputWorld, allData);
                } catch (JAXBException e) {
                    throw new RuntimeException(e);
                }
            } else {
                printError(response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setDataFromFile(PRDWorld world){
        allData = new AllData(world);
    }

    private void showFileLoaded() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("File loaded successfully");
        alert.setContentText("File loaded successfully");
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }
    private void showInvalidFileAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid File");
        alert.setHeaderText("INVALID FILE!");
        alert.setContentText(text);
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }
}
