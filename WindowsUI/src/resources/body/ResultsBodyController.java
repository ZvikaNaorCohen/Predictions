package resources.body;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import resources.app.AppController;

public class ResultsBodyController {
    @FXML
    AnchorPane subAnchorPane;
    @FXML AppController mainController;

    @FXML
    ListView executionListView;

    @FXML ListView executionResultListView;

    @FXML ListView executionDetailsListView;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}
