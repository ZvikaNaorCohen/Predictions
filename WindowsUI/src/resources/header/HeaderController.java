package resources.header;

import DTO.ContextDTO;
import engine.AllData;
import execution.context.ContextImpl;
import file.validate.impl.PRDWorldValid;
import generated.PRDWorld;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import resources.app.AppController;

import javax.xml.bind.JAXBException;
import java.io.File;

import static file.read.XMLRead.getWorldFromScheme;

public class HeaderController {

    @FXML
    private AppController mainController;
    @FXML
    private Button loadFileButton;

    @FXML
    private Text filePathText;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void fileButtonClicked() throws JAXBException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");

        // Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog((loadFileButton.getScene().getWindow()));

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();

            if (filePath.toLowerCase().endsWith(".xml")) {
                // Translate the file to World

                // Check that the xml is fine
                PRDWorld inputWorld = getWorldFromScheme(filePath.toLowerCase());
                PRDWorldValid worldValidator = new PRDWorldValid();
//                if(!worldValidator.isWorldValid(inputWorld)){
//                    showInvalidFileAlert(worldValidator.getErrorMessage());
//                    return;
//                }

                mainController.setDataFromFile(inputWorld);

                // If yes,
                filePathText.setText(filePath);
                showFileLoaded();
                mainController.updateScreenOne();

            } else {
                showInvalidFileAlert("File is not an XML file! Please try again.");
            }
        }
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
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }
}
