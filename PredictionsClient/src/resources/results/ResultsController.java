package resources.results;

import executionManager.EntityData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import resources.app.ClientAppController;

import java.util.concurrent.ScheduledExecutorService;

public class ResultsController {

    ClientAppController mainController;

    @FXML private AnchorPane resultsAnchorPane;
    private final String runningLabel = " - RUNNING";
    private final String finishedLabel = " - FINISHED";

    @FXML
    private ListView<String> executionListView;

    @FXML
    private ComboBox<String> entityComboBox;

    @FXML
    private ComboBox<String> propertyComboBox;

    @FXML private ListView executionResultListView;

    @FXML private Text progressText;
    @FXML private Text secondsText;
    @FXML private Text ticksText;

    @FXML private ProgressBar progressBar;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    @FXML private Button stopButton;
    @FXML private Button reRunButton;

    @FXML private Button consistencyButton;
    @FXML private Button averageButton;

    @FXML private TableView<EntityData> entitiesTable;
    @FXML private TableColumn<EntityData, String> nameCol;
    @FXML private TableColumn<EntityData, Integer> countCol;

    @FXML private AnchorPane graphAnchorPane;
    @FXML private ScrollPane graphScrollPane;
    private ScheduledExecutorService executorService;

    @FXML private ListView executionDetailsListView;

    @FXML
    private Button histogramButton;

    private int selectedContextID = -1; // Initialize to an invalid value






    public void setClientAppController(ClientAppController mainController){
        this.mainController = mainController;
    }
    public void onExecutionItemSelected(MouseEvent mouseEvent) {
    }

    public void reRunButtonOnClick(ActionEvent actionEvent) {
    }

    public void histogramButtonOnPressed(ActionEvent actionEvent) {
    }

    public void onSelectedEntity(ActionEvent actionEvent) {
    }

    public void averageButtonOnClick(ActionEvent actionEvent) {
    }

    public void updateScreens(){

    }
}
