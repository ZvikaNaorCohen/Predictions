package resources.app.management;

import engine.AllData;
import file.validate.impl.PRDWorldValid;
import generated.PRDWorld;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import resources.app.AdminClient;

import javax.xml.bind.JAXBException;
import java.io.File;

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
    public void loadFileButtonClicked() throws JAXBException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        File selectedFile = fileChooser.showOpenDialog((loadFileButton.getScene().getWindow()));

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();

            if (filePath.toLowerCase().endsWith(".xml")) {
                // Translate the file to World

                // Check that the xml is fine
                PRDWorld inputWorld = getWorldFromScheme(filePath.toLowerCase());
                PRDWorldValid worldValidator = new PRDWorldValid();
                if(!worldValidator.isWorldValid(inputWorld)){
                    showInvalidFileAlert(worldValidator.getErrorMessage());
                    return;
                }

                setDataFromFile(inputWorld);

                // If yes,
                filePathText.setText(filePath);
                showFileLoaded();
                mainController.updateSimulationsBreakdown(inputWorld, allData);

            } else {
                showInvalidFileAlert("File is not an XML file! Please try again.");
            }
        }
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
