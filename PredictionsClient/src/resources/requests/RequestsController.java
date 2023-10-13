package resources.requests;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import resources.app.ClientAppController;

public class RequestsController {

    ClientAppController mainController;
    @FXML private AnchorPane requestsAnchorPane;

    public void setClientAppController(ClientAppController mainController){
        this.mainController = mainController;
    }

    public void updateScreens(){}
}
