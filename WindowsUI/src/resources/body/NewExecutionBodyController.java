package resources.body;

import definition.entity.EntityDefinition;
import engine.AllData;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.DefaultStringConverter;
import resources.app.AppController;

public class NewExecutionBodyController {

    @FXML
    AppController mainController;
    @FXML
    private Button clearButton;

    @FXML
    private Button startButton;

    // @FXML
    // private ScrollPane simEntitiesPopulationSP;

    @FXML
    private AnchorPane subAnchorPane;

    @FXML
    private TableColumn<String, String> entityCol; // Update the TableColumn type

    @FXML
    private TableColumn<String, String> desiredPopulationCol; // Change the TableColumn type

    @FXML
    private TableView<String> simEntitiesTable;
    @FXML
    private ScrollPane simEnvironmentInputsSP;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void displayAllData(AllData allData){
        ObservableList<String> data = FXCollections.observableArrayList(allData.getMapAllEntities().keySet());
        simEntitiesTable.setItems(data);
        entityCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        desiredPopulationCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        desiredPopulationCol.setOnEditCommit(event -> {
            String newValueInString = event.getNewValue();
            String entityName = event.getRowValue();
            try {
                int newIntValue = Integer.parseInt(newValueInString);
                EntityDefinition entity = allData.getMapAllEntities().get(entityName);
                entity.setDesiredPopulation(newIntValue);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                int maxEntitiesAllowed = allData.getMaxEntitiesAllowed();
                int numberOfAliveEntities = allData.getCountOfAliveEntities();

                if(newIntValue + numberOfAliveEntities <= maxEntitiesAllowed){
                    alert.setContentText("Entity: " + entity.getName() + " received population: " + newIntValue + ". ");
                }
                else if(maxEntitiesAllowed < numberOfAliveEntities+newIntValue){
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setContentText("Can only have: " + maxEntitiesAllowed + " entities on screen. You tried to put: " + newIntValue + " + " + numberOfAliveEntities + " entities. ");
                    event.getTableView().getItems().set(event.getTablePosition().getRow()-1, entityName);
                }

                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Population Input");
                alert.setContentText("The string input: " + newValueInString + " is not an integer. This is population field - give me a number !!!");
                alert.showAndWait();

                event.getTableView().getItems().set(event.getTablePosition().getRow(), entityName);
            }
        });

    }

}
