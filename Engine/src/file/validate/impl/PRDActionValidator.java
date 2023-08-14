package file.validate.impl;

import action.api.Action;
import engine.AllData;
import generated.*;

import java.util.HashSet;
import java.util.Set;

public class PRDActionValidator {
    boolean allActionsToExistingEntities = true;
    boolean allPropertiesAreInEntities = true;
    boolean mathActionsReceiveOnlyNumber = true;

    boolean allTypesAreCorrect = true;

    public boolean AreActionsValid(PRDActions actions, PRDWorld oldWorld){
        allActionsToExistingEntities = true;
        allPropertiesAreInEntities = true;
        allTypesAreCorrect = true;

        allTypesAreCorrect = checkAllTypesAreCorrect(actions);
        if(allTypesAreCorrect){
            allActionsToExistingEntities = allActionsExistingEntities(actions, oldWorld);
            if(allActionsToExistingEntities){
                allPropertiesAreInEntities = allActionsToExistingProperties(actions, oldWorld);
            }

            mathActionsReceiveOnlyNumber = allMathActionsReceiveNumber(actions, oldWorld);
        }
        else {
            return false;
        }

        return allActionsToExistingEntities && allPropertiesAreInEntities && mathActionsReceiveOnlyNumber;
    }

    private boolean checkAllTypesAreCorrect(PRDActions actions){
        boolean answer = true;
        for(PRDAction action : actions.getPRDAction()){
            if(!action.getType().equals("increase") || !action.getType().equals("decrease") || !action.getType().equals("calculation")
            || !action.getType().equals("condition") || !action.getType().equals("set") || !action.getType().equals("kill")){
                answer = false;
            }
        }
        return answer;
    }

    private boolean allMathActionsReceiveNumber(PRDActions actions, PRDWorld oldWorld){
        return true;
    }

    private boolean allActionsExistingEntities(PRDActions actions, PRDWorld oldWorld){
        PRDEntities allEntities = oldWorld.getPRDEntities();
        Set<String> entityNames = new HashSet<>();

        for (PRDEntity entity : allEntities.getPRDEntity()) {
            entityNames.add(entity.getName());
        }

        for (PRDAction action : actions.getPRDAction()) {
            String actionEntityName = action.getEntity();
            if (actionEntityName != null && !entityNames.contains(actionEntityName)) {
                return false;
            }
        }

        return true;
    }

    private boolean validSingleCondition(PRDCondition condition, PRDWorld oldWorld){
        PRDEntities allEntities = oldWorld.getPRDEntities();
        Set<String> entityNames = new HashSet<>();
        boolean answer = true;
        for (PRDEntity entity : allEntities.getPRDEntity()) {
            entityNames.add(entity.getName());
        }
        String conditionEntityName = condition.getEntity();

        // entity name exists
        answer = answer && conditionEntityName != null && !entityNames.contains(conditionEntityName);

        // property exists in entity
        if(doesPropertyExist(findEntityInWorldByName(conditionEntityName, oldWorld), condition.getProperty())){
            answer = answer && true;
        }
        else
        {
            answer = answer && false;
        }


        // operator is valid


        // value is valid


        return answer;
    }

    private boolean PRDConditionValidSingularity(PRDCondition condition){
        return condition.getSingularity().equals("multiple") || condition.getSingularity().equals("single");
    }

    private boolean PRDConditionValidLogical(PRDCondition condition){
        return condition != null && (condition.getLogical().equals("or") || condition.getLogical().equals("and"));
    }

    private boolean allActionsToExistingProperties(PRDActions actions, PRDWorld oldWorld){
        PRDEntities allEntities = oldWorld.getPRDEntities();

        for (PRDAction action : actions.getPRDAction()) {
            String actionPropertyName = action.getProperty();
            if(action.getType().equals("calculation")){
                actionPropertyName = "result-prop";
            }
            String actionEntityName = action.getEntity();

            if(actionEntityName == null){ // For example: Action calculation has "result prop" and not Property
                continue;
            }


            // Find the corresponding entity
            PRDEntity correspondingEntity = null;
            for (PRDEntity entity : allEntities.getPRDEntity()) {
                if (entity.getName().equals(actionEntityName)) {
                    correspondingEntity = entity;
                    break;
                }
            }

            // If the entity doesn't exist, or the property doesn't exist in the entity, return false
            if (correspondingEntity == null || !doesPropertyExist(correspondingEntity, actionPropertyName)) {
                return false;
            }
        }
        // All actions' properties are valid
        return true;
    }

    private PRDEntity findEntityInWorldByName(String name, PRDWorld oldWorld){
        PRDEntity correspondingEntity = null;
        for (PRDEntity entity : oldWorld.getPRDEntities().getPRDEntity()) {
            if (entity.getName().equals(name)) {
                return entity;
            }
        }
        return null;
    }

    private boolean doesPropertyExist(PRDEntity entity, String propertyName) {
        for (PRDProperty property : entity.getPRDProperties().getPRDProperty()) {
            if (property.getPRDName().equals(propertyName)) {
                return true;
            }
        }
        return false;
    }
}
