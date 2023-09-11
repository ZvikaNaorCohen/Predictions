package resources.body;

import definition.entity.EntityDefinition;
import definition.property.api.PropertyDefinition;
import definition.property.api.PropertyType;
import engine.AllData;
import execution.context.Context;
import execution.context.ContextImpl;
import execution.instance.environment.api.ActiveEnvironment;
import execution.instance.environment.impl.ActiveEnvironmentImpl;
import execution.instance.property.PropertyInstance;
import execution.instance.property.PropertyInstanceImpl;
import generated.PRDEntity;
import generated.PRDEnvProperty;
import generated.PRDWorld;
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

import java.util.HashMap;
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

    private Map<PRDEnvProperty, Object> userPRDEnvPropsInputs = new HashMap<>();

    @FXML
    private AnchorPane subAnchorPane;

    @FXML
    private TableColumn<EntityDefinition, String> entityCol;

    @FXML
    private TableColumn<EntityDefinition, String> desiredPopulationCol;

    @FXML
    private TableView<EntityDefinition> simEntitiesTable;

    @FXML
    private TableView<PRDEnvProperty> envPropertiesTable;

    @FXML
    private TableColumn<PRDEnvProperty, String> envPropNameCol;

    @FXML
    private TableColumn<PRDEnvProperty, String> envPropTypeCol;

    @FXML
    private TableColumn<PRDEnvProperty, String> envPropRangeCol;

    @FXML
    private TableColumn<PRDEnvProperty, String> envPropDesiredValueCol;

    @FXML
    private ScrollPane simEnvironmentInputsSP;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    private void setOnEditCommitForEntityDefStringTableCol() {
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

    private void setOnEditCommitForEnvPropertyCol(){
        envPropDesiredValueCol.setOnEditCommit(event -> {
            String newValueInString = event.getNewValue();
            PRDEnvProperty envProperty = event.getRowValue();

            try {
                if (envProperty.getType().equals("string")) {
                    userPRDEnvPropsInputs.put(envProperty, newValueInString);
                } else if (envProperty.getType().equals("float")) {
                    float newFloatValue = Float.parseFloat(newValueInString);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                    if (envProperty.getType().equals("float")) {
                        if (envProperty.getPRDRange() != null) {
                            if (newFloatValue < envProperty.getPRDRange().getFrom() || newFloatValue > envProperty.getPRDRange().getTo()) {
                                throw new Exception("New float value: " + newFloatValue + " is bigger or smaller than range. \n");
                            }

                        } else {
                            throw new Exception("New float value: " + newFloatValue + " is bigger or smaller than range. \n");
                        }
                    }
                    alert.showAndWait();
                }
                else if (envProperty.getType().equals("boolean")){
                    if(newValueInString.equals("true")) {
                        userPRDEnvPropsInputs.put(envProperty, true);
                    }
                    else if(newValueInString.equals("false")){
                        userPRDEnvPropsInputs.put(envProperty, false);
                    }
                    else{
                        throw new Exception("Boolean env property and received something different than true/false. \n");
                    }
                }
                else{
                    throw new Exception("Couldn't commit new value: " + newValueInString + ". \n");
                }
            }
            catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Population Input");
                alert.setContentText("Invalid input. Reason: " + e);
                alert.showAndWait();

                envPropertiesTable.refresh(); // Refresh the table to reflect the change
            }
        });
    }

    private void setEntitiesObservableList(AllData allData, ObservableList<EntityDefinition> entities){
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
    }

    private void setEnvPropertiesList(PRDWorld oldWorld, ObservableList<PRDEnvProperty> envProperties, AllData allData){
        envPropertiesTable.setItems(envProperties);
        envPropNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        envPropDesiredValueCol.setCellValueFactory(cellData -> {
            PRDEnvProperty prop = cellData.getValue();
            PropertyDefinition propDef = allData.getEnvVariablesManager().getPropertyDefinitionByName(prop.getPRDName());
            return new SimpleStringProperty(propDef.generateValue().toString());
        });

        envPropNameCol.setCellValueFactory(cellData -> {
            PRDEnvProperty prop = cellData.getValue();
            return new SimpleStringProperty(prop.getPRDName());
        });

        envPropTypeCol.setCellValueFactory(cellData -> {
            PRDEnvProperty prop = cellData.getValue();
            return new SimpleStringProperty(prop.getType());
        });

        envPropRangeCol.setCellValueFactory(cellData -> {
            PRDEnvProperty prop = cellData.getValue();
            String newValue = "";
                    if(cellData.getValue().getPRDRange() != null){
                        newValue += "From: " + prop.getPRDRange().getFrom() + " to " + prop.getPRDRange().getTo() + ". \n";
                    }
            return new SimpleStringProperty(newValue);
        });
    }

    private void handleStartButtonClick(AllData allData){
        Context context = new ContextImpl(allData);
        context.setActiveEnvironment(createActiveEnvironment(allData, userPRDEnvPropsInputs));
        context.setContextID(mainController.getIDForContext());
        mainController.addNewExecution(context);
        mainController.runExecution(context);
        mainController.switchToResultsTab();
    }

    private void handleClearButtonClick(PRDWorld oldWorld, AllData allData){
        for(EntityDefinition def : allData.getMapAllEntities().values()){
            def.setDesiredPopulation(0);
        }

        for (PRDEnvProperty item : oldWorld.getPRDEnvironment().getPRDEnvProperty()) {
            envPropDesiredValueCol.setCellValueFactory(new PropertyValueFactory<>("")); // Clear the value
        }

        for (PRDEntity entity : oldWorld.getPRDEntities().getPRDEntity()) {
            desiredPopulationCol.setCellValueFactory(new PropertyValueFactory<>("")); // Clear the value
        }
        currentTotalEntities = 0;

        envPropertiesTable.refresh();
        simEntitiesTable.refresh();
    }

    public void displayAllData(PRDWorld oldWorld, AllData allData) {
        startButton.setOnMouseClicked(event -> handleStartButtonClick(allData));
        clearButton.setOnMouseClicked(event -> handleClearButtonClick(oldWorld, allData));
        ObservableList<EntityDefinition> entities = FXCollections.observableArrayList(allData.getMapAllEntities().values());
        ObservableList<PRDEnvProperty> envProperties = FXCollections.observableArrayList(allData.getMapOfPropEnvNameAndDef().values());


        setEntitiesObservableList(allData, entities);
        setEnvPropertiesList(oldWorld, envProperties, allData);


        desiredPopulationCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        envPropDesiredValueCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));

        setOnEditCommitForEntityDefStringTableCol();
        setOnEditCommitForEnvPropertyCol();

    }

    private PropertyInstance propertyInstanceFixedValue(String oldValue, PropertyDefinition definition){
        switch(definition.getType()){
            case FLOAT:{
                return new PropertyInstanceImpl(definition, Float.parseFloat(oldValue));
            }

            case BOOLEAN:
            {
                if(oldValue.equals("true")){
                    return new PropertyInstanceImpl(definition, true);
                }
                else{
                    return new PropertyInstanceImpl(definition, false);
                }
            }

            case STRING:{
                return new PropertyInstanceImpl(definition, oldValue);
            }
        }

        return null;
    }

    private ActiveEnvironment createActiveEnvironment(AllData allData, Map<PRDEnvProperty, Object> userInputsForEnvProps) {
        ActiveEnvironment newActiveEnv = new ActiveEnvironmentImpl();
        for (PRDEnvProperty envProperty : envPropertiesTable.getItems()) {
            String desiredValue = (String)userInputsForEnvProps.get(envProperty);
            String propertyName = envProperty.getPRDName();
            PropertyDefinition newDefinition = allData.getEnvVariablesManager().getPropertyDefinitionByName(propertyName);
            PropertyInstance prop;
            if (desiredValue == null) {
                prop = new PropertyInstanceImpl(newDefinition, newDefinition.generateValue());
            } else {
                prop = propertyInstanceFixedValue(desiredValue, newDefinition);
            }
            newActiveEnv.addPropertyInstance(prop);

        }
        return newActiveEnv;
    }
}
