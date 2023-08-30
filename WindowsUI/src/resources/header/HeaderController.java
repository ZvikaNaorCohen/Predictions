package resources.header;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import resources.MainMenuController;
import resources.app.AppController;

public class HeaderController {
    private AppController mainController;
    @FXML
    private Button changeColorButton;


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}
