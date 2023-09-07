package file.validate.impl;

import generated.*;
import static java.lang.Character.isDigit;

public class PRDActionValidator {
    protected String errorMessage = "";

    public String getErrorMessage(){return errorMessage;}

    public boolean allActionsAreValid(PRDActions actions, PRDWorld oldWorld){
        errorMessage = "";
        for (PRDAction action : actions.getPRDAction()) {
            if(action.getType() != null){
                PRDEntity firstEntity = findEntityInWorldByName(action.getEntity(), oldWorld);
                if(firstEntity == null && !action.getType().equals("replace") && !action.getType().equals("proximity")){
                    errorMessage = "Action: " + action.getType() + "has entity that is not found. Entity tried: " + action.getEntity() + ". \n";
                    return false;
                }

                switch(action.getType()){
                    case "increase":{
                        // check entity exists
                        // check property exists
                        // check "by" is correct
                        if(!checkIncreaseDecreaseActions(action.getBy(), firstEntity, action, oldWorld)){
                            return false;
                        }
                        break;
                    }
                    case "decrease":{ // Same as increase
                        if(!checkIncreaseDecreaseActions(action.getBy(), firstEntity, action, oldWorld)){
                            return false;
                        }
                        break;
                    }
                    case "calculation":{
                        // check entity exists
                        // check "result-prop" is correct
                        if(!checkCalculationAction(firstEntity, action)){
                            return false;
                        }
                        break;
                    }
                    case "condition":{
                        // check entity exists. Checked already before loop.
                        break;
                    }
                    case "set":{
                        // check entity exists
                        // check property exists
                        // check "value" is correct
                        if(!checkSetAction(firstEntity, action, oldWorld)){
                            return false;
                        }
                        break;
                    }
                    case "kill":{
                        if(!checkKillAction(firstEntity, oldWorld)){
                            return false;
                        }
                        break;
                    }
                    case "replace":{
                        if(!checkReplaceAction(oldWorld, action)){
                            return false;
                        }
                        break;
                    }
                    case "proximity":{
                        if(!checkProximityAction(oldWorld, action)){
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
                        if(!checkSingleConditionAction(action, oldWorld, action.getPRDCondition())){
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
                    if(!checkValidPRDMultiplyDivide(action, oldWorld, action.getPRDMultiply().getArg1(), action.getPRDMultiply().getArg2(), "multiply")){
                        return false;
                    }
                }
                else if (action.getPRDDivide() != null) {
                    if(!checkValidPRDMultiplyDivide(action, oldWorld, action.getPRDDivide().getArg1(), action.getPRDMultiply().getArg2(), "divide")){
                        return false;
                    }
                }
                else{
                    errorMessage = "Can't find neither action type: " + action.getType() + "or divide/multiply/condition. \n";
                    return false;
                }
            }
        }

        return true;
    }

    private boolean checkReplaceAction(PRDWorld oldWorld, PRDAction action){
        String firstEntityName = action.getKill();
        String secondEntityName = action.getCreate();
        PRDEntity firstEntity = findEntityInWorldByName(firstEntityName, oldWorld);
        PRDEntity secondEntity = findEntityInWorldByName(secondEntityName, oldWorld);
        if(firstEntity == null){
            errorMessage = "Entity: " + firstEntityName + "was not found in Replace action. \n";
            return false;
        }
        if(secondEntity == null){
            errorMessage = "Entity: " + secondEntityName + "was not found in Replace action. \n";
            return false;
        }

        if(!action.getMode().equals("scratch") && !action.getMode().equals("derived")){
            errorMessage = "In replace action, mode: " + action.getMode() + " is nor scratch or derived. \n";
            return false;
        }
        return true;
    }

    private boolean checkProximityAction(PRDWorld oldWorld, PRDAction action){
        String firstEntityName = action.getPRDBetween().getSourceEntity();
        String secondEntityName = action.getPRDBetween().getTargetEntity();
        PRDEntity firstEntity = findEntityInWorldByName(firstEntityName, oldWorld);
        PRDEntity secondEntity = findEntityInWorldByName(secondEntityName, oldWorld);
        if(firstEntity == null){
            errorMessage = "Source Entity: " + firstEntityName + "was not found in Proximity action. \n";
            return false;
        }
        if(secondEntity == null){
            errorMessage = "Target entity: " + secondEntityName + "was not found in Proximity action. \n";
            return false;
        }
        return true;
    }

    private boolean validExpressionForMultiplyOrDivide(PRDAction action, PRDWorld oldWorld, String arg){
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
        else if (arg.startsWith("evaluate")){
            return isEvaluationValid(oldWorld, arg);
        }
        else if (arg.startsWith("percent")){
            return isPercentValid(action, oldWorld, arg);
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

    private boolean checkValidPRDMultiplyDivide(PRDAction action, PRDWorld oldWorld, String arg1, String arg2, String multiOrDivide){
        if(!validExpressionForMultiplyOrDivide(action, oldWorld, arg1)){
            errorMessage = "Expression arg1 in " + multiOrDivide + " is invalid. Received: " + arg1 + "\n";
            return false;
        }
        else if(!validExpressionForMultiplyOrDivide(action, oldWorld, arg2)){
            errorMessage = "Expression arg2 in " + multiOrDivide + " is invalid. Received: " + arg2 + "\n";
            return false;
        }
        return true;
    }



    private boolean checkSingleConditionAction(PRDAction action, PRDWorld oldWorld, PRDCondition condition){
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
        else if(!singleConditionValidExpression(action, oldWorld, condition.getValue(), entityName, condition.getProperty())){
            errorMessage = "Invalid expression in single condition. Received: " + condition.getValue() + "\n";
            return false;
        }
        return true;
    }

    private boolean singleConditionValidExpression(PRDAction action, PRDWorld oldWorld, String arg, String entity, String property){
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
        else if(arg.startsWith("percent")){
            return isPercentValid(action, oldWorld, arg);
        }
        else if(stringPotentiallyFloat(arg))
            {
                PRDProperty propForCondition = getPropertyByEntityNameAndPropName(property, entity, oldWorld);
                if(propForCondition == null){
                    errorMessage = "In single condition, entity: " + entity + "has no property: " + property + ". \n";
                    return false;
                }
                if(!allCharactersAreDigits(arg) && propForCondition.getType() != null && propForCondition.getType().equals("decimal")){
                    errorMessage = "Trying to cast float value to decimal property. In single condition, entity: " + entity + ". \n";
                    return false;
                }

            }else
            {
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
        else if(!doesPropertyExistInEntity(myEntity, action.getProperty())){
            errorMessage = "Property " + action.getProperty() + "doesnt exist in entity " + myEntity.getName() +
                    ". Action was: " + action.getType() + "\n";
            return false;
        }
        else if (!validValueInSetAction(myWorld, action)){
            errorMessage = "Value: " + action.getValue() + "is not valid in entity:  " + myEntity.getName() +
                    ". Action was: " + action.getType() + "\n";
            return false;
        }
        return true;
    }

    private boolean checkCalculationAction(PRDEntity myEntity, PRDAction action){
        if(myEntity == null){
            errorMessage = "Entity " + action.getEntity() + "doesnt exist. Action was: " + action.getType() + "\n";
            return false;
        }
        else if(!doesPropertyExistInEntity(myEntity, action.getResultProp())){
            errorMessage = "Property " + action.getProperty() + "doesnt exist in entity " + myEntity.getName() +
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
        else if(!doesPropertyExistInEntity(myEntity, action.getProperty())){
            errorMessage = "Property " + action.getProperty() + "doesnt exist in entity " + myEntity.getName() +
                    ". Action was: " + action.getType() + "\n";
            return false;
        }
        else if (!validExpressionForMultiplyOrDivide(action, oldWorld, expression)){
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
        for(PRDEnvProperty property : oldWorld.getPRDEnvironment().getPRDEnvProperty()){
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

    private boolean setExpressionTestTwoFromThree(PRDAction action, PRDWorld oldWorld){
        String actionPropertyName = action.getProperty();
        String actionEntityName = action.getEntity();
        PRDProperty propFromAction = getPropertyByEntityNameAndPropName(actionPropertyName, actionEntityName, oldWorld);
        if(!propFromAction.getType().equals("boolean")){
            errorMessage = "Property " + actionEntityName + " in entity " + actionEntityName + " is not boolean. \n";
            return false;
        }

        return true;
    }

    private boolean setExpressionTestThreeOfThree(PRDAction action, PRDWorld oldWorld){
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

        return true;
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
        else if(action.getValue().startsWith("evaluate")){
            return handleEvaluateExpression(action, oldWorld);
        }
        else if(action.getValue().equals("true") || action.getValue().equals("false")){
            return setExpressionTestTwoFromThree(action, oldWorld);
        }
        else {
            return setExpressionTestThreeOfThree(action, oldWorld);
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
        else if (action.getValue().startsWith("evaluate")){
            return handleEvaluateExpression(action, myWorld);
        }
        else if (action.getValue().startsWith("percent")){
            return isPercentValid(action, myWorld, action.getValue());
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

    private boolean expressionSecondCheck(PRDAction action, PRDWorld oldWorld, String propName){
        String entityName = "";
        if(action.getType().equals("increase") || action.getType().equals("decrease") || action.getType().equals("set")
        || action.getType().equals("calculation") || action.getType().equals("condition")){
            entityName = action.getEntity();
        }
        if(action.getType().equals("kill")) {
            entityName = action.getKill();
        }
        if(action.getType().equals("proximity")) {
            entityName = action.getPRDBetween().getSourceEntity();
        }

        if(!entityName.equals("")){
            PRDEntity entity = findEntityInWorldByName(entityName, oldWorld);
            return doesPropertyExistInEntity(entity, propName);
        }
        else{
            errorMessage = "Couldn't check expressionSecondCheck of action: " + action + ". \n";
            return false;
        }
    }

    private String getEntityNameFromAction(PRDAction action){
        if(action.getType().equals("increase") || action.getType().equals("decrease") || action.getType().equals("set")
                || action.getType().equals("calculation") || action.getType().equals("condition")){
            return action.getEntity();
        }
        if(action.getType().equals("kill")) {
            return action.getKill();
        }
        if(action.getType().equals("proximity")) {
            return action.getPRDBetween().getSourceEntity();
        }

        return "";
    }


    private boolean handleEvaluateExpression(PRDAction action, PRDWorld oldWorld){
        if(action.getType().equals("increase") || action.getType().equals("decrease")){
            return isEvaluationValid(oldWorld, action.getBy());
        }
        else if(action.getType().equals("calculation")){
            if(action.getPRDMultiply() != null){
                return isEvaluationValid(oldWorld, action.getPRDMultiply().getArg1()) &&
                        isEvaluationValid(oldWorld, action.getPRDMultiply().getArg2());
            }
            else if(action.getPRDDivide() != null){
                return isEvaluationValid(oldWorld, action.getPRDDivide().getArg1()) &&
                        isEvaluationValid(oldWorld, action.getPRDDivide().getArg2());
            }
            else {
                errorMessage = "Calculation has no divide or multiply. \n";
                return false;
            }
        } else if (action.getType().equals("set")){
            return isEvaluationValid(oldWorld, action.getValue());
        }
        else if(action.getType().equals("condition") && action.getPRDCondition().getSingularity().equals("single"))
        {
            return isEvaluationValid(oldWorld, action.getPRDCondition().getValue());
        } else if (action.getType().equals("proximity")){
            return isEvaluationValid(oldWorld, action.getPRDEnvDepth().getOf());
        }
        else{
            return true;
        }
    }

    private boolean validInsidePercentExpression(PRDAction action, PRDWorld oldWorld, String percent){
        if(percent.startsWith("environment")){
            String valueInsideEnvironment = extractValueFromExpression(percent);
            PRDEnvProperty prop = getEnvPropFromWorldByName(valueInsideEnvironment, oldWorld);
            if(prop == null){
                errorMessage = "Couldn't find property: " + valueInsideEnvironment + " as environment property. \n";
                return false;
            }
            if(!prop.getType().equals("decimal") && !prop.getType().equals("float")){
                errorMessage = "In environment expression, environment property " + valueInsideEnvironment + " is not numeric. \n";
                return false;
            }
        }
        if(percent.startsWith("random")){
            String valueInsideRandom = extractValueFromExpression(percent);
            if(!stringPotentiallyFloat(valueInsideRandom)){
                errorMessage = "Value inside random in increase/decrease/multiply/divide function is not a number. \n";
                return false;
            }
        }
        if(percent.startsWith("evaluate")){
            String insideEvaluate = extractValueFromExpression(percent);
            String[] parts = insideEvaluate.split("\\.");

            if (parts.length == 2) {
                String entName = parts[0];
                String propName = parts[1];
                PRDEntity tempEntity = findEntityInWorldByName(entName, oldWorld);
                boolean propExist = doesPropertyExistInEntity(tempEntity, propName);
                if(propExist){
                    PRDProperty def = getPropertyByEntityNameAndPropName(propName, entName, oldWorld);
                    if(def.getType().equals("decimal") || def.getType().equals("float")){
                        return isEvaluationValid(oldWorld, insideEvaluate);
                    }
                }else{
                    errorMessage = "Can't find property: " + propName + "in entity: " + entName + " in percent, evaluation. \n";
                    return false;
                }
            }
            else {
                errorMessage = "Can't work with evaluation expression: " + insideEvaluate + ". \n";
                return false;
            }

            return true;
        }
        else if(expressionSecondCheck(action, oldWorld, percent)){
            return true;
        }
        else{
            if(action.getProperty() != null){
                PRDProperty prop =getPropertyByEntityNameAndPropName(action.getProperty(), getEntityNameFromAction(action), oldWorld);
                if(prop != null){
                    if(!prop.getType().equals("string")){
                        errorMessage = "Property: " + prop + " has type different than string. \n";
                        return false;
                    }
                    else{
                        return true;
                    }
                }
                else{
                    errorMessage = "Can't find property in action: " + action + ". \n";
                    return false;
                }
            }
        }

        errorMessage = ("Can't figure expression: " + percent);
        return false;
    }

    private boolean isPercentValid(PRDAction action, PRDWorld oldWorld, String wholeString){
        String[] parts = wholeString.split(",");

        // Check if there are two parts
        if (parts.length == 2) {
            String part1 = parts[0].trim(); // Trim to remove leading/trailing spaces
            String part2 = parts[1].trim(); // Trim to remove leading/trailing spaces

            if(validInsidePercentExpression(action, oldWorld, part1) && validInsidePercentExpression(action, oldWorld, part2)){
                // Check that both are decimal
                return true;
            }

            errorMessage = "Couldn't figure expressions in percent. Expression1: " + part1 + ". Expression2: " + part2 + ". \n";

        } else {
            errorMessage = "In Percent expression, couldn't resolve the expression: " + wholeString + ". \n";
        }
        return false;
    }

    private boolean isEvaluationValid(PRDWorld oldWorld, String insideEvaluate){
        insideEvaluate = extractValueFromExpression(insideEvaluate);
        String[] parts = insideEvaluate.split("\\.");

        if (parts.length == 2) {
            String entName = parts[0];
            String propName = parts[1];
            PRDEntity tempEntity = findEntityInWorldByName(entName, oldWorld);
            boolean propExist = doesPropertyExistInEntity(tempEntity, propName);
            if(!propExist){
                errorMessage = "Received: " + insideEvaluate + ", but can't find property: " + propName +
                        "in entity: " + entName + ". \n";
                return false;
            }
            if(tempEntity == null){
                errorMessage = "Received: " + insideEvaluate + ", but can't find entity: " + entName + ". \n";
                return false;
            }
        }
        else {
            errorMessage = "Can't work with expression: " + insideEvaluate + ". \n";
            return false;
        }

        return true;
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