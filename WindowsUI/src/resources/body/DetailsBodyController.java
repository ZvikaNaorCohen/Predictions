package resources.body;

import action.api.Action;
import definition.entity.EntityDefinition;
import definition.environment.api.EnvVariablesManager;
import definition.property.api.PropertyDefinition;
import engine.AllData;
import generated.PRDAction;
import generated.PRDRule;
import generated.PRDRules;
import generated.PRDWorld;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import resources.app.AppController;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import execution.instance.property.PropertyInstance;
import rule.Rule;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DetailsBodyController {

    @FXML public ScrollPane detailsScrollPane;
    @FXML
    private ScrollPane masterScrollPane;

    @FXML private AnchorPane anchorPaneDetails;
    @FXML
    private AppController mainController;

    private Map<Rule, ObservableList<Action>> ruleToActionsMap = new HashMap<>();

    @FXML
    private TreeView<String> masterTreeView;

    @FXML
    private TextArea detailsTextArea;

    @FXML
    private HBox masterHBox;

    private Map<String, Rule> ruleMap = new HashMap<>();

    private String increaseActionItemToAdd(PRDAction action){
        String answer = "";
        answer += "Action type: " + action.getType() + ". Main entity: " + action.getEntity() + ". \n ";
        if(action.getPRDSecondaryEntity() != null){
            answer += "Second entity: " + action.getPRDSecondaryEntity() + ". \n";
        }
        else{
            answer += "No second entity in this action. \n";
        }

        answer += "Entity property to edit: " + action.getProperty() + ". By: " + action.getBy() + ". \n";
        return answer;
    }

    private String calculationActionItemToAdd(PRDAction action){
        String answer = "";
        answer += "Action type: " + action.getType() + ". Main entity: " + action.getEntity() + ". \n ";
        answer += "Result-prop: " + action.getResultProp() +". \n";


        if(action.getPRDSecondaryEntity() != null){
            answer += "Second entity: " + action.getPRDSecondaryEntity() + ". \n";
        }
        else{
            answer += "No more entities in this action. \n";
        }

        if(action.getPRDMultiply() != null){
            answer += "Concrete action: multiply \n";
            answer += "Arg1: " + action.getPRDMultiply().getArg1() + ". Arg2: " + action.getPRDMultiply().getArg2() + ". \n";
        }
        else if(action.getPRDDivide() != null){
            answer += "Concrete action: divide \n";
            answer += "Arg1: " + action.getPRDDivide().getArg1() + ". Arg2: " + action.getPRDDivide().getArg2() + ". \n";
        }

        return answer;
    }

    private String conditionActionItemToAdd(PRDAction action){
        String answer = "";
        answer += "Action type: condition. Main entity: " + action.getEntity() + ". \n";
        answer += "In 'Then' there are: " + action.getPRDThen().getPRDAction().size() + " actions. \n";
        answer += "In 'Else' there are: " + action.getPRDElse().getPRDAction().size() + " actions. \n";
        if(action.getPRDSecondaryEntity() != null){
            answer += "Second entity: " + action.getPRDSecondaryEntity() + ". \n";
        }
        else{
            answer += "No second entity in this action. \n";
        }
        if(action.getPRDCondition().getSingularity().equals("single")){
            answer += "Single condition. Property: " + action.getPRDCondition().getProperty() + ". Operator: " + action.getPRDCondition().getOperator() +
                    ". Value: " + action.getPRDCondition().getValue() + ". \n";
        }
        else if(action.getPRDCondition().getSingularity().equals("multiple")){
            answer += "Multiple Condition. The logic: " + action.getPRDCondition().getLogical() + ". Number of sub-conditions: " + action.getPRDCondition().getPRDCondition().size() + ". \n";
        }
        return answer;
    }

    private String setActionItemToAdd(PRDAction action){
        String answer = "";
        answer += "Action type: " + action.getType() + ". Entity: " + action.getEntity() + ". Property: " + action.getProperty() + ". \n";
        answer += "New value: " + action.getValue() + ". \n";
        if(action.getPRDSecondaryEntity() != null){
            answer += "Second entity: " + action.getPRDSecondaryEntity() + ". \n";
        }
        else{
            answer += "No second entity in this action. \n";
        }
        return answer;
    }

    private String killActionItemToAdd(PRDAction action){
        String answer = "";
        answer += "Action type: " + action.getType() + ". Entity to kill: " + action.getEntity() + ". \n";
        if(action.getPRDSecondaryEntity() != null){
            answer += "Second entity: " + action.getPRDSecondaryEntity() + ". \n";
        }
        else{
            answer += "No second entity in this action. \n";
        }
        return answer;
    }

    private String replaceActionItemToAdd(PRDAction action){
        String answer = "";
        answer += "Action type: " + action.getType() + ". Entity to kill: " + action.getKill() + ". Entity to create: " + action.getCreate() + ". \n";
        answer += "Mode: " + action.getMode() + ". \n";
        if(action.getPRDSecondaryEntity() != null){
            answer += "Second entity: " + action.getPRDSecondaryEntity() + ". \n";
        }
        else{
            answer += "No more entities in this action. \n";
        }
        return answer;
    }

    private String proximityActionItemToAdd(PRDAction action){
        String answer = "";
        answer += "Action type: " + action.getType() + ". Source entity: " + action.getPRDBetween().getSourceEntity() +
                ". Target entity: " + action.getPRDBetween().getTargetEntity() +". \n ";
        answer += "Depth: " + action.getPRDEnvDepth().getOf() + ". Number of actions: " + action.getPRDActions().getPRDAction().size() + ". \n";
        if(action.getPRDSecondaryEntity() != null){
            answer += "Second entity: " + action.getPRDSecondaryEntity() + ". \n";
        }
        else{
            answer += "No more entities in this action. \n";
        }
        return answer;
    }


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    private void updateRulesSub(TreeItem<String> rulesSub, PRDRules allRules){
        for (PRDRule rule : allRules.getPRDRule()) {
            TreeItem<String> ruleItem = new TreeItem<>(rule.getName());
            for(PRDAction action : rule.getPRDActions().getPRDAction()){
                String actionItemToAdd = "";
                switch(action.getType()){

                    case "increase": // type, entity, property, by
                    {
                        actionItemToAdd = increaseActionItemToAdd(action);
                        break;
                    }
                    case "decrease": // type, entity, property, by
                    {
                        actionItemToAdd = increaseActionItemToAdd(action);
                        break;
                    }
                    case "calculation": // type, entity, result-prop
                    {
                        actionItemToAdd = calculationActionItemToAdd(action);
                        break;
                    }
                    case "condition": // type, entity
                    {
                        actionItemToAdd = conditionActionItemToAdd(action);
                        break;
                    }case "set": // type, entity, property, value
                    {
                        actionItemToAdd = setActionItemToAdd(action);
                        break;
                    }case "kill": // type, entity
                    {
                        actionItemToAdd = killActionItemToAdd(action);
                        break;
                    }
                    case "replace": // type, kill, create, mode
                    {
                        actionItemToAdd = replaceActionItemToAdd(action);
                        break;
                    }
                    case "proximity": // between: source-entity, target-entity. of: (expression). PRDActions.
                    {
                        actionItemToAdd = proximityActionItemToAdd(action);
                        break;
                    }
                }
                // String actionItemToAdd = "Type:" + action.getActionType().toString() + ". Entity: " + action.getContextEntity().getName() + ". \n";
                TreeItem<String> actionItem = new TreeItem<>(action.getType());
                ruleItem.getChildren().add(actionItem);
            }
            rulesSub.getChildren().add(ruleItem);
        }
    }

    private void updateEnvSub(TreeItem<String> envSub, Map<String, String> allEnvVariables){
        for(Map.Entry<String, String> entry : allEnvVariables.entrySet()){
            String name = entry.getKey();
            String type = entry.getValue();
            TreeItem<String> envName = new TreeItem<>(name);
            String dataToAdd = "Name: " + name + ". Type: " + type + ". \n";
            TreeItem<String> data = new TreeItem<>(dataToAdd);
            envName.getChildren().add(data);
            envSub.getChildren().add(envName);
        }
    }

    private void updateEntitiesSub(TreeItem<String> entitiesSub, Map<String, EntityDefinition> allEntityDefinitions){
        for(Map.Entry<String, EntityDefinition> entry : allEntityDefinitions.entrySet()){
            String name = entry.getKey();
            // EntityDefinition def = entry.getValue();

            TreeItem<String> entityName = new TreeItem<>(name);
            entitiesSub.getChildren().add(entityName);
        }
    }
    private void handleRuleClicked(String itemValue, Set<Rule> allRules){
        Rule selectedRule = getRuleByName(itemValue, allRules);
        if (selectedRule != null) {
            String ruleData = getRuleData(selectedRule);
            detailsTextArea.setText(ruleData);
        }
    }

    private void handleEnvClicked(String itemValue, Map<String, String> allEnvVariables){
        String selectedEnv = allEnvVariables.get(itemValue);
        if (selectedEnv != null) {
            detailsTextArea.setText("Name: " + itemValue + ". Type: " + selectedEnv);
        }
    }

    private void handleEntityClicked(String itemValue, Map<String, EntityDefinition> allEntityDefinitions){
        EntityDefinition selectedEntity = allEntityDefinitions.get(itemValue);
        if (selectedEntity != null) {
            String text = "Name: " + selectedEntity.getName() + ". Population: " + selectedEntity.getPopulation() + ". \n";
            text += "Properties: \n";
            int counter = 1;
            for(PropertyDefinition def : selectedEntity.getProps()){
                text += counter + ". Prop name: " + def.getName() + ". Prop type: " + def.getType() + ". \n";
                counter++;
            }
            detailsTextArea.setText(text);
        }
    }

    private void handleClick(TreeItem<String> selectedItem, AllData allData) {
        if (selectedItem != null) {
            String itemValue = selectedItem.getValue();
            if (selectedItem.getParent() == null || selectedItem.getParent().getValue().equals("All Data")) {
                // Display action details in the TextArea
                detailsTextArea.setText("Selected:" + itemValue);
            } else {
                if (selectedItem.getParent().getValue().equals("Rules")) {
                    handleRuleClicked(itemValue, allData.getAllRulesFromAllData());
                } else if (selectedItem.getParent().getValue().equals("EnvVariables")) {
                    handleEnvClicked(itemValue, allData.getEnvPropertyNamesAndTypes());
                } else if (selectedItem.getParent().getValue().equals("Entities")) {
                    handleEntityClicked(itemValue, allData.getMapAllEntities());
                }
                // wait for aviad response in email - how to show the data
//                else if (selectedItem.getParent().getParent().getValue().equals("Rules")){
//                    handleActionClicked(itemValue, selectedItem.getParent().getValue(), allData.getAllRulesFromAllData());
//                }
            }
        }
    }

    private void handleActionClicked(String actionName, String ruleName, Set<Rule> allRules){

    }

    public void displayAllData(Context context, AllData allData, PRDWorld oldWorld) {
        TreeItem<String> root = new TreeItem<>("All Data");
        TreeItem<String> rulesSub = new TreeItem<>("Rules");
        TreeItem<String> envSub = new TreeItem<>("EnvVariables");
        TreeItem<String> entitiesSub = new TreeItem<>("Entities");

        root.getChildren().add(rulesSub);
        root.getChildren().add(envSub);
        root.getChildren().add(entitiesSub);

        root.setExpanded(true);

        Set<Rule> allRules = context.getAllRules();
        updateRulesSub(rulesSub, oldWorld.getPRDRules());
        updateEnvSub(envSub, allData.getEnvPropertyNamesAndTypes());
        updateEntitiesSub(entitiesSub, allData.getMapAllEntities());



        masterTreeView.setRoot(root);

        masterTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TreeItem<String> selectedItem = masterTreeView.getSelectionModel().getSelectedItem();
                handleClick(selectedItem, allData);
            }
        });
    }

    private String getRuleData(Rule rule){
        String answer = "Rule name: " + rule.getName() + ". \n";
        answer += "Rule activation: \t Ticks: " + rule.getActivation().getTicks() + ". \t Probability: " + rule.getActivation().getProb() + ". \n";
        answer += "Rule has " + rule.getActionsToPerform().size() + " actions. \n";

        return answer;
    }

    private Rule getRuleByName(String ruleName, Set<Rule> allRules) {
        for (Rule rule : allRules) {
            if (rule.getName().equals(ruleName)) {
                return rule;
            }
        }
        return null;
    }
};