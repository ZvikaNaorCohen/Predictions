package action.impl;

import action.api.AbstractAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import definition.entity.SecondaryEntityDefinition;
import definition.property.api.PropertyType;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import execution.instance.property.PropertyInstance;
import function.api.Function;
import function.impl.*;

import static definition.value.generator.transformer.Transformer.StringToFloat;
import static definition.value.generator.transformer.Transformer.StringToInteger;

public class DecreaseAction extends AbstractAction {
    private final String property;
    private final String byExpression;

    public DecreaseAction(EntityDefinition entityDefinition, String property, String byExpression, SecondaryEntityDefinition secondaryEntity) {
        super(ActionType.DECREASE, entityDefinition, secondaryEntity);
        this.property = property;
        this.byExpression = byExpression;
    }

    public void invoke(Context context) {
        EntityInstance primaryInstance = context.getPrimaryEntityInstance();
        EntityInstance secondaryInstance = context.getSecondaryEntityInstance();

        PropertyInstance propertyInstance = null;
        if (entityDefinition.getName().equals(primaryInstance.getEntityDefinitionName())) {
            propertyInstance = primaryInstance.getPropertyByName(property);
        } else {
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

    private void updatePropertyInstanceValueByPercent(Context context, PropertyInstance propertyInstance){
        Function func = new PercentFunction(byExpression);
        Float oldValue = PropertyType.FLOAT.convert(propertyInstance.getValue());
        Float newValue = (float) (oldValue - func.getPercentFromFunction(context));
        if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)){
            propertyInstance.updateValue(newValue);
        }
    }

    private void updatePropertyInstanceValueByTicks(Context context, PropertyInstance propertyInstance){
        Function func = new TicksFunction(byExpression);
        Float oldValue = PropertyType.DECIMAL.convert(propertyInstance.getValue());
        Float newValue = oldValue - func.getTicksNotUpdated(context);
        if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)){
            propertyInstance.updateValue(newValue);
        }
    }

    // old, not good
//    private void updatePropertyInstanceValueByEvaluate(Context context, PropertyInstance propertyInstance){
//        Function func = new EvaluateFunction(byExpression);
//        Float oldValue = PropertyType.DECIMAL.convert(propertyInstance.getValue());
//        Float newValue = oldValue - (Float)func.getValueFromEvaluate(context);
//        if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)){
//            propertyInstance.updateValue(newValue);
//        }
//    }

    // new, good.
    private void updatePropertyInstanceValueByEvaluate(Context context, PropertyInstance propertyInstance){
        Float oldValue = PropertyType.FLOAT.convert(propertyInstance.getValue());
        Float newValue = oldValue - (Float)getValueFromEvaluate(context, byExpression);
        if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)){
            propertyInstance.updateValue(newValue);
        }
    }

    private void updatePropertyInstanceValueByProperty(Context context, PropertyInstance propertyInstance) {
        Object oldValue = PropertyType.DECIMAL.convert(propertyInstance.getValue());
        if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")) {
            Integer newValue = (Integer) oldValue - (Integer) context.getPrimaryEntityInstance().getPropertyByName(byExpression).getValue();
            if (propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)) {
                propertyInstance.updateValue(newValue);
            }
        }

        else{
            Float newValue = (Float) oldValue - (Float)context.getPrimaryEntityInstance().getPropertyByName(byExpression).getValue();
            if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)) {
                propertyInstance.updateValue(newValue);
            }
        }

    }
    private void updatePropertyInstanceValueByFreeValue(PropertyInstance propertyInstance) {
        Object oldValue = PropertyType.DECIMAL.convert(propertyInstance.getValue());
        if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")){
            Integer newValue = StringToInteger(byExpression);
            if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue - (Integer)oldValue)) {
                propertyInstance.updateValue(newValue - (Integer) oldValue);
            }
        }
        else {
            Float newValue = StringToFloat(byExpression);
            if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue - (Float)oldValue)){
                propertyInstance.updateValue(newValue - (Float)oldValue);
            }
        }
    }

    private void updatePropertyInstanceValueByRandom(PropertyInstance propertyInstance) {
        Function envFunction = new RandomFunction(byExpression);
        Object oldValue = PropertyType.DECIMAL.convert(propertyInstance.getValue());
        if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")){
            {
                if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(envFunction.getRandomValue() - (Integer) oldValue))
                {
                    propertyInstance.updateValue(envFunction.getRandomValue() - (Integer) oldValue);
                }
            }
        }
        else {
            if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(envFunction.getRandomValue() - (Float) oldValue))
            {
                propertyInstance.updateValue(envFunction.getRandomValue() - (Float) oldValue);
            }
        }
    }

    private void updatePropertyInstanceValueByEnvironment(Context context, PropertyInstance propertyInstance){
        Function envFunction = new EnvironmentFunction(byExpression);
        if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")){
            Object oldValue = PropertyType.DECIMAL.convert(propertyInstance.getValue());
            Integer newValue = (Integer) oldValue - (Integer)PropertyType.DECIMAL.convert(envFunction.getPropertyInstanceValueFromEnvironment(context));
            if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)){
                propertyInstance.updateValue(newValue);
            }
        }
        else {
            Object oldValue = PropertyType.FLOAT.convert(propertyInstance.getValue());
            Float newValue = (Float)oldValue - (Float)PropertyType.FLOAT.convert(envFunction.getPropertyInstanceValueFromEnvironment(context));
            if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue))
            {
                propertyInstance.updateValue(newValue);
            }
        }
    }
}
