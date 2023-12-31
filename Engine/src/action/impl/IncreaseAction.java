package action.impl;

import action.api.AbstractAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import definition.entity.SecondaryEntityDefinition;
import definition.property.api.PropertyDefinition;
import definition.property.api.PropertyType;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import execution.instance.property.PropertyInstance;
import function.api.Function;
import function.impl.*;
import generated.PRDAction;

import static definition.value.generator.transformer.Transformer.StringToFloat;
import static definition.value.generator.transformer.Transformer.StringToInteger;

public class IncreaseAction extends AbstractAction {

    private final String property;
    private final String byExpression;

    public IncreaseAction(EntityDefinition entityDefinition, String property, String byExpression, SecondaryEntityDefinition secondaryEntity) {
        super(ActionType.INCREASE, entityDefinition, secondaryEntity);
        this.property = property;
        this.byExpression = byExpression;
    }
    @Override
    public void invoke(Context context) {
        EntityInstance primaryInstance = context.getPrimaryEntityInstance();
        EntityInstance secondaryInstance = context.getSecondaryEntityInstance();

        PropertyInstance propertyInstance = null;
        if(entityDefinition.getName().equals(primaryInstance.getEntityDefinitionName())){
            propertyInstance = primaryInstance.getPropertyByName(property);
        }
        else{
            propertyInstance = secondaryInstance.getPropertyByName(property);
        }

        if (byExpression.startsWith("environment")) {
            updatePropertyInstanceValueByEnvironment(context, propertyInstance);
        } else if (byExpression.startsWith("random")) {
            updatePropertyInstanceValueByRandom(propertyInstance);
        } else if (byExpression.startsWith("evaluate")) {
            updatePropertyInstanceValueByEvaluate(context, propertyInstance);
        } else if (byExpression.startsWith("percent")) {
            updatePropertyInstanceValueByPercent(context, propertyInstance);
        } else if (byExpression.startsWith("ticks")) {
            updatePropertyInstanceValueByTicks(context, propertyInstance);
        } else if (primaryInstance.hasPropertyByName(byExpression)) {
            updatePropertyInstanceValueByProperty(context, propertyInstance);
        } else if (secondaryInstance != null && secondaryInstance.hasPropertyByName(byExpression)){
            propertyInstance = secondaryInstance.getPropertyByName(property);
            updatePropertyInstanceValueByProperty(context, propertyInstance);
        }
        else { // ערך חופשי
            updatePropertyInstanceValueByFreeValue(propertyInstance);
        }
    }

    private void updatePropertyInstanceValueByTicks(Context context, PropertyInstance propertyInstance){
        Function func = new TicksFunction(byExpression);
        Float oldValue = PropertyType.FLOAT.convert(propertyInstance.getValue());
        Float newValue = oldValue + func.getTicksNotUpdated(context);
        if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)){
            propertyInstance.updateValue(newValue);
        }
    }

    private void updatePropertyInstanceValueByPercent(Context context, PropertyInstance propertyInstance){
        Function func = new PercentFunction(byExpression);
        Float oldValue = PropertyType.FLOAT.convert(propertyInstance.getValue());
        Float newValue = (float) (oldValue + func.getPercentFromFunction(context));
        if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)){
            propertyInstance.updateValue(newValue);
        }
    }

    private void updatePropertyInstanceValueByEvaluate(Context context, PropertyInstance propertyInstance){
        Float oldValue = PropertyType.FLOAT.convert(propertyInstance.getValue());
        Float newValue = oldValue + (Float)getValueFromEvaluate(context, byExpression);
        if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)){
            propertyInstance.updateValue(newValue);
        }
    }
    private void updatePropertyInstanceValueByProperty(Context context, PropertyInstance propertyInstance) {
        Object oldValue = PropertyType.FLOAT.convert(propertyInstance.getValue());
        if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")){
            Integer newValue = (Integer) oldValue + (Integer)context.getPrimaryEntityInstance().getPropertyByName(byExpression).getValue();
            if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)) {
                propertyInstance.updateValue(newValue);
            }
        }
        else{
            Float newValue = (Float) oldValue + (Float)context.getPrimaryEntityInstance().getPropertyByName(byExpression).getValue();
            if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)){
                propertyInstance.updateValue(newValue);
            }
        }

    }
    private void updatePropertyInstanceValueByFreeValue(PropertyInstance propertyInstance) {
        Object oldValue = PropertyType.FLOAT.convert(propertyInstance.getValue());
        if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")){
            Integer newValue = StringToInteger(byExpression);
            newValue += (Integer)oldValue;
            if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)){
                propertyInstance.updateValue(newValue);
            }

        }
        else {
            Float newValue = StringToFloat(byExpression);
            newValue += (Float)oldValue;
            if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)){
                propertyInstance.updateValue(newValue + (Float)oldValue);
            }
        }
    }
    private void updatePropertyInstanceValueByRandom(PropertyInstance propertyInstance) {
        Function envFunction = new RandomFunction(byExpression);
        Object oldValue = PropertyType.FLOAT.convert(propertyInstance.getValue());
        if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")){
            Integer newValue = envFunction.getRandomValue() + (Integer)oldValue;
            if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)) {
                propertyInstance.updateValue(newValue);
            }
        }
        else {
            Float newValue = envFunction.getRandomValue() + (Float)oldValue;
            if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)) {
                propertyInstance.updateValue(newValue);
            }
        }
    }

    private void updatePropertyInstanceValueByEnvironment(Context context, PropertyInstance propertyInstance) {
        Function envFunction = new EnvironmentFunction(byExpression);
        Object oldValue = PropertyType.FLOAT.convert(propertyInstance.getValue());
//        if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")){
//            Integer newValue = (Integer) oldValue + (Integer)PropertyType.DECIMAL.convert(envFunction.getPropertyInstanceValueFromEnvironment(context));
//            if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)){
//                propertyInstance.updateValue(newValue);
//            }
//        }
//        else
//        {
        Float newValue = (Float) oldValue + (Float) PropertyType.FLOAT.convert(envFunction.getPropertyInstanceValueFromEnvironment(context));
        if (propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)) {
            propertyInstance.updateValue(newValue);
        }
    }

    private boolean verifyNumericPropertyType(PropertyInstance propertyValue) {
        return
                PropertyType.DECIMAL.equals(propertyValue.getPropertyDefinition().getType()) || PropertyType.FLOAT.equals(propertyValue.getPropertyDefinition().getType());
    }
}
