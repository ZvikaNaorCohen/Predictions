package resources.app.execution;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import resources.ClientAppController;

public class ExecutionController {
    ClientAppController mainController;
    @FXML Button clearButton;

    public void setClientAppController(ClientAppController mainController){
        this.mainController = mainController;
    }

    public void clearButtonOnClick(){
    }

    public void updateScreens(){

    }
}
