package action.impl;

import action.api.AbstractAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import definition.property.api.PropertyType;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import execution.instance.property.PropertyInstance;
import function.api.Function;
import function.impl.*;

import static definition.value.generator.transformer.Transformer.StringToFloat;
import static definition.value.generator.transformer.Transformer.StringToInteger;

public class SetAction extends AbstractAction {
    String property, newPropertyValue;

    public SetAction(EntityDefinition entityDefinition, String inputProperty, String inputNewValue) {
        super(ActionType.SET, entityDefinition);
        property = inputProperty;
        newPropertyValue = inputNewValue;
    }


    @Override
    public void invoke(Context context) {
        EntityInstance primaryInstance = context.getPrimaryEntityInstance();
        EntityInstance secondaryInstance = context.getSecondaryEntityInstance();

        PropertyInstance propertyInstance = null;
        if (entityDefinition.getName().equals(primaryInstance.getEntityDefinitionName())) {
            propertyInstance = primaryInstance.getPropertyByName(property);
        } else {
            propertyInstance = secondaryInstance.getPropertyByName(property);
        }
        if (newPropertyValue.startsWith("environment")) {
            Function envFunction = new EnvironmentFunction(newPropertyValue);
            String className = envFunction.getPropertyInstanceValueFromEnvironment(context).getClass().getSimpleName();
            if (className.equals("Float") &&
                    propertyInstance.getPropertyDefinition().newValueInCorrectBounds((Float) envFunction.getPropertyInstanceValueFromEnvironment(context))) {
                propertyInstance.updateValue(envFunction.getPropertyInstanceValueFromEnvironment(context));
            }

        } else if (newPropertyValue.startsWith("random")) {
            Function randomFunction = new RandomFunction(newPropertyValue);
            String className = propertyInstance.getValue().getClass().getSimpleName();
            if (className.equals("Integer") && propertyInstance.getPropertyDefinition().newValueInCorrectBounds((Integer) randomFunction.getRandomValue())) {
                propertyInstance.updateValue(randomFunction.getRandomValue());
            }
        } else if (newPropertyValue.startsWith("evaluate")) {
            Function evaluateFunction = new EvaluateFunction(newPropertyValue);
            Float value = (Float) evaluateFunction.getValueFromEvaluate(context);
            if (propertyInstance.getPropertyDefinition().newValueInCorrectBounds((value))) {
                propertyInstance.updateValue(value);
            }
        } else if (newPropertyValue.startsWith("percent")) {
            updatePropertyInstanceValueByPercent(context, propertyInstance);
        } else if (newPropertyValue.startsWith("ticks")) {
            Function ticksFunction = new TicksFunction(newPropertyValue);
            Integer value = ticksFunction.getTicksNotUpdated(context);
            if (propertyInstance.getPropertyDefinition().newValueInCorrectBounds((value))) {
                propertyInstance.updateValue(value);
            }
        } else if (primaryInstance.hasPropertyByName(newPropertyValue)) {
            propertyInstance.updateValue(primaryInstance.getPropertyByName(newPropertyValue));
        } else if (secondaryInstance.hasPropertyByName(newPropertyValue)) {
            propertyInstance.updateValue(secondaryInstance.getPropertyByName(newPropertyValue));
        } else { // ערך חופשי
            updatePropertyInstanceValueByFreeValue(propertyInstance);
        }
    }
    private void updatePropertyInstanceValueByPercent(Context context, PropertyInstance propertyInstance){
        Function func = new PercentFunction(newPropertyValue);
        Double newValue = func.getPercentFromFunction(context);
        if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue.floatValue())){
            propertyInstance.updateValue(newValue.floatValue());
        }
    }

    private void updatePropertyInstanceValueByFreeValue(PropertyInstance propertyInstance) {
        if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")){
            Integer newValue = StringToInteger(newPropertyValue);
            if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)){
                propertyInstance.updateValue(newValue);
            }
        }
        else if(propertyInstance.getValue().getClass().getSimpleName().equals("Float")) {
            Float newValue = StringToFloat(newPropertyValue);
            if (propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue))
            {
                propertyInstance.updateValue(newValue);
            }
        } else if(propertyInstance.getValue().getClass().getSimpleName().equals("Boolean")) {
            Boolean newValue = newPropertyValue.equals("true");
            propertyInstance.updateValue(newValue);
        }
        else {
            propertyInstance.updateValue(newPropertyValue);
        }
    }
}
