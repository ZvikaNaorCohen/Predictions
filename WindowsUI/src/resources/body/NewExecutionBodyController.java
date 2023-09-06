package resources.body;

import definition.entity.EntityDefinition;
import engine.AllData;
import execution.instance.entity.EntityInstance;
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

import java.util.Map;

public class NewExecutionBodyController {
    private int totalEntitiesAllowed;
    private int currentTotalEntities;

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
    private TableColumn<EntityDefinition, String> entityCol;

    @FXML
    private TableColumn<EntityDefinition, String> desiredPopulationCol;

    @FXML
    private TableView<EntityDefinition> simEntitiesTable;
    @FXML
    private ScrollPane simEnvironmentInputsSP;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void displayAllData(AllData allData) {
        ObservableList<EntityDefinition> entities = FXCollections.observableArrayList(allData.getMapAllEntities().values());
        // ObservableList<String> populations = FXCollections.observableArrayList();
        totalEntitiesAllowed = allData.getMaxEntitiesAllowed();
        simEntitiesTable.setItems(entities);
        for(Map.Entry<String, EntityDefinition> string : allData.getMapAllEntities().entrySet()){
            currentTotalEntities += string.getValue().getPopulation();
        }

        entityCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        desiredPopulationCol.setCellValueFactory(cellData -> {
            EntityDefinition entity = cellData.getValue();
            return new SimpleStringProperty(Integer.toString(entity.getPopulation()));
        });



        desiredPopulationCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        desiredPopulationCol.setOnEditCommit(event -> {
                String newValueInString = event.getNewValue();
                EntityDefinition entity = event.getRowValue();

            try {
                int newIntValue = Integer.parseInt(newValueInString);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                if (newIntValue + currentTotalEntities <= totalEntitiesAllowed) {
                    alert.setContentText("Entity: " + entity.getName() + " received population: " + newIntValue + ". ");
                    entity.setDesiredPopulation(newIntValue);
                    currentTotalEntities += newIntValue;
                } else {
                    throw new Exception("Can only have: " + totalEntitiesAllowed + " entities on screen. You tried to put: " + newIntValue + " + " + currentTotalEntities + " entities. ");
                }

                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Population Input");
                alert.setContentText("Invalid input. Reason: " + e);
                alert.showAndWait();

                currentTotalEntities -= entity.getPopulation();
                entity.setDesiredPopulation(0);
                simEntitiesTable.refresh(); // Refresh the table to reflect the change
            }
        });
    }


}
