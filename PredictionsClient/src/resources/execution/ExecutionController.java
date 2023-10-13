package resources.execution;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import resources.app.ClientAppController;

public class ExecutionController {
    ClientAppController mainController;
    @FXML private Button clearButton;
    @FXML private AnchorPane executionAnchorPane;

    public void setClientAppController(ClientAppController mainController){
        this.mainController = mainController;
    }

    public void clearButtonOnClick(){
    }

    public void updateScreens(){

    }
}
