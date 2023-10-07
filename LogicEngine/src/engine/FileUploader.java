//package engine;
//
//import file.validate.impl.PRDWorldValid;
//import generated.PRDWorld;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ButtonType;
//
//import javax.xml.bind.JAXBException;
//import java.io.File;
//
//import static file.read.XMLRead.getWorldFromScheme;
//
//public class FileUploader {
//
//    public boolean validFile(File selectedFile) throws JAXBException {
//        if (selectedFile != null) {
//            String filePath = selectedFile.getAbsolutePath();
//
//            if (filePath.toLowerCase().endsWith(".xml")) {
//                PRDWorld inputWorld = getWorldFromScheme(filePath.toLowerCase());
//                PRDWorldValid worldValidator = new PRDWorldValid();
//                if(!worldValidator.isWorldValid(inputWorld)){
//                    showInvalidFileAlert(worldValidator.getErrorMessage());
//                    return false;
//                }
//
//                return true;
//
//            } else {
//                showInvalidFileAlert("File is not an XML file! Please try again.");
//                return false;
//            }
//        }
//
//        return false;
//    }
//
//    private void showInvalidFileAlert(String text) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Invalid File");
//        alert.setHeaderText("INVALID FILE!");
//        alert.setContentText(text);
//        alert.getButtonTypes().setAll(ButtonType.OK);
//        alert.showAndWait();
//    }
//}
