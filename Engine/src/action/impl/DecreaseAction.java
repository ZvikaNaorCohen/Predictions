package action.impl;

import action.api.AbstractAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import definition.property.api.PropertyType;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import execution.instance.property.PropertyInstance;
import function.api.Function;
import function.impl.EnvironmentFunction;
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
        for(EntityInstance instance : context.getEntityInstanceManager().getInstances()){
            if(instance.getEntityDefinitionName().equals(entityDefinition.getName()))
            {
                context.setPrimaryEntityInstance(instance);
                PropertyInstance propertyInstance = instance.getPropertyByName(property); // Property AGE
                if(byExpression.startsWith("environment"))
                {
                    updatePropertyInstanceValueByEnvironment(context, propertyInstance);
                }
                else if(byExpression.startsWith("random")){
                    updatePropertyInstanceValueByRandom(propertyInstance);
                }
                else if(instance.hasPropertyByName(byExpression)){
                    updatePropertyInstanceValueByProperty(context, propertyInstance);
                }
                else { // ערך חופשי
                    updatePropertyInstanceValueByFreeValue(propertyInstance);
                }
            }
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
        Object oldValue = PropertyType.DECIMAL.convert(propertyInstance.getValue());
        if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")){
            Integer newValue = (Integer) oldValue - (Integer)PropertyType.DECIMAL.convert(envFunction.getPropertyInstanceValueFromEnvironment(context));
            if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)){
                propertyInstance.updateValue(newValue);
            }
        }
        else {
            Float newValue = (Float)oldValue - (Float)PropertyType.DECIMAL.convert(envFunction.getPropertyInstanceValueFromEnvironment(context));
            if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue))
            {
                propertyInstance.updateValue(newValue);
            }

        }
    }
}
