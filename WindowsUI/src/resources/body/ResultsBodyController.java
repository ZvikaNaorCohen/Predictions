package resources.body;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import resources.app.AppController;

public class ResultsBodyController {
    @FXML
    private AnchorPane subAnchorPane;
    @FXML private AppController mainController;

    @FXML
    private ListView executionListView;

    @FXML private ListView executionResultListView;

    @FXML private ListView executionDetailsListView;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}
