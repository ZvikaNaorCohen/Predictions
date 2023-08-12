package action.impl;

import action.api.AbstractAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import definition.property.api.PropertyType;
import execution.context.Context;
import execution.instance.property.PropertyInstance;
import function.api.Function;
import function.impl.RandomFunction;

import static definition.value.generator.transformer.Transformer.StringToFloat;
import static definition.value.generator.transformer.Transformer.StringToInteger;

public class DecreaseAction extends AbstractAction {
    private final String property;
    private final String byExpression;

    public DecreaseAction(EntityDefinition entityDefinition, String property, String byExpression) {
        super(ActionType.DECREASE, entityDefinition);
        this.property = property;
        this.byExpression = byExpression;
    }

    public void invoke(Context context) {
        // <PRD-action entity="Smoker" type="increase" property="age" by="1"/>
        // <PRD-action type="decrease" entity="ent-1" property="p2" by="environment(e3)"/>

        // Handle expression:
        PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(property); // Property AGE

        if(byExpression.startsWith("environment"))
        {
            updatePropertyInstanceValueByEnvironment(context, propertyInstance);
        }
        else if(byExpression.startsWith("random")){
            updatePropertyInstanceValueByRandom(propertyInstance);
        }
        else if(context.getPrimaryEntityInstance().hasPropertyByName(byExpression)){
            updatePropertyInstanceValueByProperty(context, propertyInstance);
        }
        else { // ערך חופשי
            updatePropertyInstanceValueByFreeValue(propertyInstance);
        }
    }
    private void updatePropertyInstanceValueByProperty(Context context, PropertyInstance propertyInstance) {
        propertyInstance.updateValue(context.getPrimaryEntityInstance().getPropertyByName(byExpression).getValue());
    }
    private void updatePropertyInstanceValueByFreeValue(PropertyInstance propertyInstance) {
        if(propertyInstance.getValue().getClass().getName().equals("Integer")){
            Integer newValue = StringToInteger(byExpression);
            propertyInstance.updateValue(newValue);
        }
        else {
            Float newValue = StringToFloat(byExpression);
            propertyInstance.updateValue(newValue);
        }
    }
    private void updatePropertyInstanceValueByRandom(PropertyInstance propertyInstance) {
        Function envFunction = new RandomFunction(byExpression);
        propertyInstance.updateValue(envFunction.getRandomValue());
    }
    private void updatePropertyInstanceValueByEnvironment(Context context, PropertyInstance propertyInstance){
        String envVarName = "";
        envVarName = getVarNameFromExpression();

        Object valueFromEnvVar = context.getEnvironmentVariable(envVarName).getValue();
        if(propertyInstance.getValue().getClass().getName().equals("Integer")){
            Integer oldValue = PropertyType.DECIMAL.convert(propertyInstance.getValue());
            Integer newValue = oldValue - (Integer)PropertyType.DECIMAL.convert(valueFromEnvVar);
            propertyInstance.updateValue(newValue);
        }
        else {
            Float oldValue = PropertyType.DECIMAL.convert(propertyInstance.getValue());
            Float newValue = oldValue - (Float)PropertyType.DECIMAL.convert(valueFromEnvVar);
            propertyInstance.updateValue(newValue);
        }
    }

    private String getVarNameFromExpression(){
        String input = byExpression, answer = "";
        int startIndex = input.indexOf("(");
        int endIndex = input.indexOf(")");
        answer = input.substring(startIndex + 1, endIndex);

        return answer;
    }
}
