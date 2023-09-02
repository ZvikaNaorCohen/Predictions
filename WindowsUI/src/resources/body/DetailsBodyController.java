package resources.body;

import action.api.Action;
import definition.entity.EntityDefinition;
import definition.environment.api.EnvVariablesManager;
import definition.property.api.PropertyDefinition;
import engine.AllData;
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


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    private Rule getRuleFromObservableList(ObservableList<Rule> rules, String name){
        for(Rule r : rules){
            if(r.getName().equals(name)){
                return r;
            }
        }
        return null;
    }

    private void updateRulesSub(TreeItem<String> rulesSub, Set<Rule> allRules){
        for (Rule rule : allRules) {
            TreeItem<String> ruleItem = new TreeItem<>(rule.getName());

            for (Action action : rule.getActionsToPerform()) {
                String actionItemToAdd = "Type:" + action.getActionType().toString() + ". Entity: " + action.getContextEntity().getName() + ". \n";
                TreeItem<String> actionItem = new TreeItem<>(actionItemToAdd);
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
            }
        }
    }

    public void displayAllData(Context context, AllData allData) {
        TreeItem<String> root = new TreeItem<>("All Data");
        TreeItem<String> rulesSub = new TreeItem<>("Rules");
        TreeItem<String> envSub = new TreeItem<>("EnvVariables");
        TreeItem<String> entitiesSub = new TreeItem<>("Entities");

        root.getChildren().add(rulesSub);
        root.getChildren().add(envSub);
        root.getChildren().add(entitiesSub);

        root.setExpanded(true);

        Set<Rule> allRules = context.getAllRules();
        updateRulesSub(rulesSub, allRules);
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