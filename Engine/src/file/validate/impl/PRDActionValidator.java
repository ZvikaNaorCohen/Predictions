package file.validate.impl;

import action.api.Action;
import engine.AllData;
import generated.*;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Character.isDigit;

public class PRDActionValidator {
    protected String errorMessage = "";

    public String getErrorMessage(){return errorMessage;}

    private boolean allActionsAreValid(PRDActions actions, PRDWorld oldWorld){
        errorMessage = "";
        for (PRDAction action : actions.getPRDAction()) {
            if(action.getType() != null){
                PRDEntity myEntity = findEntityInWorldByName(action.getEntity(), oldWorld);
                switch(action.getType()){
                    case "increase":{
                        // check entity exists
                        // check property exists
                        // check "by" is correct
                        if(!checkIncreaseDecreaseActions(action.getBy(), myEntity, action, oldWorld)){
                            return false;
                        }
                        break;
                    }
                    case "decrease":{ // Same as increase
                        if(!checkIncreaseDecreaseActions(action.getBy(), myEntity, action, oldWorld)){
                            return false;
                        }
                        break;
                    }
                    case "calculation":{
                        // check entity exists
                        // check "result-prop" is correct
                        if(!checkCalculationAction(myEntity, action)){
                            return false;
                        }
                        break;
                    }
                    case "condition":{
                        // check entity exists
                        if(!checkConditionAction(myEntity)){
                            return false;
                        }
                        break;
                    }
                    case "set":{
                        // check entity exists
                        // check property exists
                        // check "value" is correct
                        if(!checkSetAction(myEntity, action, oldWorld)){
                            return false;
                        }
                        break;
                    }
                    case "kill":{
                        if(!checkKillAction(myEntity, oldWorld)){
                            return false;
                        }
                        break;
                    }
                }
            }
            else{
                if(action.getPRDCondition() != null){
                    // if singularity == single then :
                    // check entity
                    // check property
                    // check operator ("=", "!=", "bt", "lt")
                    // check value

                    // else, if singularity == multiple:
                    // check logical
                    if(action.getPRDCondition().getSingularity().equals("single")){
                        if(!checkSingleConditionAction(oldWorld, action.getPRDCondition())){
                            return false;
                        }
                    }
                    else if(action.getPRDCondition().getSingularity().equals("multiple")){
                        if(!checkMultipleConditionAction(action.getPRDCondition())){
                            return false;
                        }
                    }
                    else {
                        errorMessage = "PRDCondition is not null, but singularity is neither single or multiple. \n";
                        return false;
                    }
                }
                else if (action.getPRDMultiply() != null){
                    if(!checkValidPRDMultiplyDivide(oldWorld, action.getPRDMultiply().getArg1(), action.getPRDMultiply().getArg2(), "multiply")){
                        return false;
                    }
                }
                else if (action.getPRDDivide() != null) {
                    if(!checkValidPRDMultiplyDivide(oldWorld, action.getPRDDivide().getArg1(), action.getPRDMultiply().getArg2(), "divide")){
                        return false;
                    }
                }
                else{
                    errorMessage = "Can't find neither action type: " + action.getType() + "or divide/multiply/condition. \n";
                }
            }
        }

        return true;
    }
    private boolean validExpressionForMultiplyOrDivide(PRDWorld oldWorld, String arg){
        if(arg.startsWith("environment")){
            String valueInsideEnvironment = extractValueFromExpression(arg);
            PRDEnvProperty prop = getEnvPropFromWorldByName(valueInsideEnvironment, oldWorld);
            if(prop == null){
                errorMessage = "Couldn't find property: " + valueInsideEnvironment + " as environment property. \n";
                return false;
            }
            if(!prop.getType().equals("decimal") && !prop.getType().equals("float")){
                errorMessage = "In increase/decrease/multiply/divide function, environment property " + valueInsideEnvironment + " is not numeric. \n";
                return false;
            }
        }
        else if(arg.startsWith("random")){
            String valueInsideRandom = extractValueFromExpression(arg);
            if(!stringPotentiallyFloat(valueInsideRandom)){
                errorMessage = "Value inside random in increase/decrease/multiply/divide function is not a number. \n";
                return false;
            }
        }
        else if(allCharactersAreDigits(arg)){
            return true;
        }
        else if(stringPotentiallyFloat(arg)){
            return true;
        }
        else {
            errorMessage = "In multiply/divide function there is an unknown argument: " + arg + ". \n";
            return false;
        }

        return true;
    }

    private boolean checkValidPRDMultiplyDivide(PRDWorld oldWorld, String arg1, String arg2, String multiOrDivide){
        if(!validExpressionForMultiplyOrDivide(oldWorld, arg1)){
            errorMessage = "Expression arg1 in " + multiOrDivide + " is invalid. Received: " + arg1 + "\n";
            return false;
        }
        else if(!validExpressionForMultiplyOrDivide(oldWorld, arg2)){
            errorMessage = "Expression arg2 in " + multiOrDivide + " is invalid. Received: " + arg2 + "\n";
            return false;
        }
        return true;
    }



    private boolean checkSingleConditionAction(PRDWorld oldWorld, PRDCondition condition){
        String entityName = condition.getEntity();
        PRDEntity myEntity = findEntityInWorldByName(entityName, oldWorld);
        if(myEntity == null){
            errorMessage = "In single condition could not found entity name: " + entityName + "\n";
            return false;
        }
        else if (!doesPropertyExistInEntity(myEntity, condition.getProperty())){
            errorMessage = "In single condition could not found property: " + condition.getProperty() + "in entity: " +
                    myEntity.getName() + "\n";
            return false;
        }
        else if(!condition.getOperator().equals("=") && !condition.getOperator().equals("!=") &&
                !condition.getOperator().equals("bt") && !condition.getOperator().equals("lt"))
        {
            errorMessage = "Unknown operator in single condition. Received operator: " + condition.getOperator() + "\n";
            return false;
        }
        else if(!singleConditionValidExpression(oldWorld, condition.getValue())){
            errorMessage = "Invalid expression in single condition. Received: " + condition.getValue() + "\n";
            return false;
        }
        return true;
    }

    private boolean singleConditionValidExpression(PRDWorld oldWorld, String arg){
        if(arg.startsWith("environment")){
            String valueInsideEnvironment = extractValueFromExpression(arg);
            PRDEnvProperty prop = getEnvPropFromWorldByName(valueInsideEnvironment, oldWorld);
            if(prop == null){
                errorMessage = "Couldn't find property: " + valueInsideEnvironment + " as environment property. \n";
                return false;
            }
        }
        else if(arg.startsWith("random")){
            String valueInsideRandom = extractValueFromExpression(arg);
            if(!stringPotentiallyFloat(valueInsideRandom)){
                errorMessage = "Value inside random in single condition function is not a number. \n";
                return false;
            }
        }
        else {
            errorMessage = "In single condition function there is an unknown argument: " + arg + ". \n";
            return false;
        }

        return true;
    }

    private boolean checkMultipleConditionAction(PRDCondition condition){
        if(!condition.getLogical().equals("and") && condition.getLogical().equals("or")){
            errorMessage = "Condition is multiple but the logical is different than 'and' and 'or'. Received: " + condition.getLogical() + "\n";
            return false;
        }
        return true;
    }
    private PRDEntity findEntityInWorldByName(String name, PRDWorld oldWorld){
        for (PRDEntity entity : oldWorld.getPRDEntities().getPRDEntity()) {
            if (entity.getName().equals(name)) {
                return entity;
            }
        }
        return null;
    }

    private boolean checkKillAction(PRDEntity myEntity, PRDWorld oldWorld){
        if(findEntityInWorldByName(myEntity.getName(), oldWorld) == null){
            errorMessage = "Entity " + myEntity.getName() + "doesnt exist. Action was: kill \n" ;
            return false;
        }
        return true;
    }
    private boolean checkSetAction(PRDEntity myEntity, PRDAction action, PRDWorld myWorld){
        if(myEntity == null){
            errorMessage = "Entity " + action.getEntity() + "doesnt exist. Action was: " + action.getType() + "\n";
            return false;
        }
        else if(doesPropertyExistInEntity(myEntity, action.getProperty())){
            errorMessage = "Property " + action.getEntity() + "doesnt exist in entity " + myEntity.getName() +
                    ". Action was: " + action.getType() + "\n";
            return false;
        }
        else if (validValueInSetAction(myWorld, action)){
            errorMessage = "Value: " + action.getValue() + "is not valid in entity:  " + myEntity.getName() +
                    ". Action was: " + action.getType() + "\n";
            return false;
        }
        return true;
    }
    private boolean checkConditionAction(PRDEntity entity){
        return entity != null;
    }
    private boolean checkCalculationAction(PRDEntity myEntity, PRDAction action){
        if(myEntity == null){
            errorMessage = "Entity " + action.getEntity() + "doesnt exist. Action was: " + action.getType() + "\n";
            return false;
        }
        else if(doesPropertyExistInEntity(myEntity, action.getResultProp())){
            errorMessage = "Property " + action.getEntity() + "doesnt exist in entity " + myEntity.getName() +
                    ". Action was: " + action.getType() + "\n";
            return false;
        }
        return true;
    }

    private boolean checkIncreaseDecreaseActions(String expression, PRDEntity myEntity, PRDAction action, PRDWorld oldWorld){
        if(myEntity == null){
            errorMessage = "Entity " + action.getEntity() + "doesnt exist. Action was: " + action.getType() + "\n";
            return false;
        }
        else if(doesPropertyExistInEntity(myEntity, action.getProperty())){
            errorMessage = "Property " + action.getEntity() + "doesnt exist in entity " + myEntity.getName() +
                    ". Action was: " + action.getType() + "\n";
            return false;
        }
        else if (!validExpressionForMultiplyOrDivide(oldWorld, expression)){
            errorMessage = "The BY Expression in action " + action.getType() + "for entity " + myEntity.getName() + "is not valid" + "\n";
            return false;
        }
        return true;
    }

    private boolean doesPropertyExistInEntity(PRDEntity entity, String propertyName) {
        for (PRDProperty property : entity.getPRDProperties().getPRDProperty()) {
            if (property.getPRDName().equals(propertyName)) {
                return true;
            }
        }
        return false;
    }

    private PRDEnvProperty getEnvPropFromWorldByName(String name, PRDWorld oldWorld){
        for(PRDEnvProperty property : oldWorld.getPRDEvironment().getPRDEnvProperty()){
            if(property.getPRDName().equals(name)){
                return property;
            }
        }
        return null;
    }

    private PRDProperty getPropertyByEntityNameAndPropName(String propName, String entityName, PRDWorld oldWorld){
        PRDEntity entity = findEntityInWorldByName(entityName, oldWorld);
        for(PRDProperty prop : entity.getPRDProperties().getPRDProperty()){
            if(prop.getPRDName().equals(propName)){
                return prop;
            }
        }
        return null;
    }

    private boolean canTwoPropertiesSetValues(PRDEnvProperty envProperty, PRDProperty receivingProperty){
        return envProperty.getType().equals(receivingProperty.getType()) ||
                (receivingProperty.getType().equals("float") && envProperty.getType().equals("decimal"));
    }
    private boolean setPropertyValidValue(PRDWorld oldWorld, PRDAction action){
        if(action.getValue().startsWith("environment")){
            {
                String propertyInEnvironment = extractValueFromExpression(action.getValue());
                PRDEnvProperty prop = getEnvPropFromWorldByName(propertyInEnvironment, oldWorld);

                String actionPropertyName = action.getProperty();
                String actionEntityName = action.getEntity();
                PRDProperty propFromAction = getPropertyByEntityNameAndPropName(actionPropertyName, actionEntityName, oldWorld);

                if(!canTwoPropertiesSetValues(prop, propFromAction)){
                    errorMessage = "Can't set environment(value) from env property: " + prop.getPRDName() + " to property: " + propFromAction.getPRDName() +
                            " because the types are different (and not float to decimal). \n";
                    return false;
                }

            }
        } else if (action.getValue().startsWith("random")) {
            String actionPropertyName = action.getProperty();
            String actionEntityName = action.getEntity();
            PRDProperty propFromAction = getPropertyByEntityNameAndPropName(actionPropertyName, actionEntityName, oldWorld);
            if(!propFromAction.getType().equals("decimal") || !propFromAction.getType().equals("float")){
                errorMessage = "Can't set random(value) to property: " + propFromAction.getPRDName() +
                        "because the property type is not a number. \n";
                return false;
            }
        }
        else if(action.getValue().equals("true") || action.getValue().equals("false")){
            String actionPropertyName = action.getProperty();
            String actionEntityName = action.getEntity();
            PRDProperty propFromAction = getPropertyByEntityNameAndPropName(actionPropertyName, actionEntityName, oldWorld);
            if(!propFromAction.getType().equals("boolean")){
                errorMessage = "Property " + actionEntityName + " in entity " + actionEntityName + " is not boolean. \n";
                return false;
            }
        }
        else {
            String actionPropertyName = action.getProperty();
            String actionEntityName = action.getEntity();
            PRDProperty propFromAction = getPropertyByEntityNameAndPropName(actionPropertyName, actionEntityName, oldWorld);
            if(propFromAction.getType().equals("string")) {
                return true;
            }
            if(propFromAction.getType().equals("decimal")  && !allCharactersAreDigits(action.getValue())){
                errorMessage = "Trying to put string to an integer property. Entity: " + actionEntityName + ". Property: " + actionPropertyName +
                            ". Type: " + propFromAction.getType() + ". \n";
                return false;
            }
            if(propFromAction.getType().equals("float") && !stringPotentiallyFloat(action.getValue()))
            {
                errorMessage = "Trying to put string to a float property. Entity: " + actionEntityName + ". Property: " + actionPropertyName +
                        ". Type: " + propFromAction.getType() + ". \n";
                return false;
            }
        }
        return true;
    }

    private boolean stringPotentiallyFloat(String input){
        try {
            float floatValue = Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validValueInSetAction(PRDWorld myWorld, PRDAction action){
        if(action.getValue().startsWith("environment")){
            return handleEnvironmentExpressionValue(myWorld, action) && setPropertyValidValue(myWorld, action);
        } else if (action.getValue().startsWith("random")) {
            return handleRandomExpressionValue(action) && setPropertyValidValue(myWorld, action);
        }
        else if((action.getValue().equals("true") || action.getValue().equals("false")) && setPropertyValidValue(myWorld, action)){
            return true;
        }
        else {
            return setPropertyValidValue(myWorld, action);
        }

            // if yes:
                // if it's random then it should receive AN INTEGER. not a number? throw error.
        // if not, check if the expression is a property name in the main entity.
            // if yes,
        // if not, it's a free value.
            // The test would pass only if: if it's an integer then the property is DECIMAL.
                                        // if it's a float then the property is FLOAT.
                                        // If it's a string then the property is STRING.
                                        // It's true/false and the property value is BOOLEAN.
    }
    private String extractValueFromExpression(String value){
        int startIndex = value.indexOf("(");
        int endIndex = value.indexOf(")");
        String extractedText = "";
        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            extractedText = value.substring(startIndex + 1, endIndex);
        }

        return extractedText;
    }
    private boolean handleEnvironmentExpressionValue(PRDWorld myWorld, PRDAction action){
        boolean found = false;
        String valueInExpression = extractValueFromExpression(action.getValue());

        if(getEnvPropFromWorldByName(valueInExpression, myWorld) == null){
            errorMessage = "Failure at expression: " + action.getValue() + ". Environment property: " + valueInExpression + "could not be found. \n";
        }
        return found;
    }

    private boolean handleRandomExpressionValue(PRDAction action){
        String extractedValue = extractValueFromExpression(action.getValue());
        if(!allCharactersAreDigits(extractedValue)){
                errorMessage = "Received expression: " + action.getValue() + ". Value inside random has to be only digits. \n";
                return false;
            }
        return true;
    }

    private boolean allCharactersAreDigits(String input){
        for(int i=0; i<input.length(); i++){
            if(!isDigit(input.charAt(i))){
                return false;
            }
        }
        return true;
    }
}