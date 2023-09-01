package resources.header;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import resources.app.AppController;

import java.io.File;

public class HeaderController {
    private AppController mainController;
    @FXML
    private Button loadFileButton;

    @FXML
    private Text filePathText;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void fileButtonClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");

        // Set the initial directory (optional)
        // fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(((Stage) loadFileButton.getScene().getWindow()));

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();

            if (filePath.toLowerCase().endsWith(".xml")) {
                filePathText.setText(filePath);
            } else {
                showInvalidFileAlert();
            }
        }
    }

    private void showInvalidFileAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid File");
        alert.setHeaderText(null);
        alert.setContentText("File is not an XML file! Please try again.");
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }
}
