package resources.body;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import resources.app.AppController;

import javax.swing.text.html.ListView;

public class DetailsBodyController {

    @FXML
    private AppController mainController;

    @FXML
    private ScrollPane masterScrollPane;

    @FXML
    private ScrollPane detailsScrollPane;

    @FXML
    private HBox masterHBox;
    // @FXML
    // private ListView masterListView;

//    @FXML
//    private AnchorPane anchorPaneDetails;

    @FXML
    private TextArea detailsTextArea;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}
