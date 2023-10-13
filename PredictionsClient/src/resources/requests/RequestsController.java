package resources.requests;

import generated.PRDWorld;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import resources.app.ClientAppController;

import java.util.List;

public class RequestsController {
    ClientAppController mainController;

    // Requests
    @FXML private AnchorPane requestsAnchorPane;
    @FXML private TableColumn<PRDWorld, String> simulationNameCol;
    @FXML private TableColumn<PRDWorld, Integer> numberOfRunsCol;
    @FXML private TableColumn endSettingsCol;
    @FXML private TableView<PRDWorld> requestsTableView;

    // Allowed
    @FXML private TableColumn<PRDWorld, Integer> IDCol;
    @FXML private TableColumn<PRDWorld, String> nameCol;
    @FXML private TableColumn<PRDWorld, Integer> simulationsAllowedCol;
    @FXML private TableColumn statusCol;
    @FXML private TableColumn<PRDWorld, Integer> runningCol;
    @FXML private TableColumn<PRDWorld, Integer> finishedCol;
    @FXML private TableView<PRDWorld> allowedTableView;

    // Buttons
    @FXML private Button submitButton;
    @FXML private Button executeButton;



    public void setClientAppController(ClientAppController mainController){
        this.mainController = mainController;
    }

    public void updateScreens(){
//        List<PRDWorld> allWorlds = mainController.getAllPRDWorlds();
//        requestsTableView.getItems().addAll(allWorlds);
//        simulationNameCol.setCellValueFactory(new PropertyValueFactory<>("Simulation Name"));
    }
}
