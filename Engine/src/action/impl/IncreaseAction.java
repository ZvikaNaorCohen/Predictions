package action.impl;

import action.api.AbstractAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import definition.property.api.PropertyDefinition;
import definition.property.api.PropertyType;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import execution.instance.property.PropertyInstance;
import function.api.Function;
import function.impl.EnvironmentFunction;
import function.impl.RandomFunction;

import static definition.value.generator.transformer.Transformer.StringToFloat;
import static definition.value.generator.transformer.Transformer.StringToInteger;

public class IncreaseAction extends AbstractAction {

    private final String property;
    private final String byExpression;

    public IncreaseAction(EntityDefinition entityDefinition, String property, String byExpression) {
        super(ActionType.INCREASE, entityDefinition);
        this.property = property;
        this.byExpression = byExpression;
    }
    @Override
    public void invoke(Context context) {
        // <PRD-action entity="Smoker" type="increase" property="age" by="1"/>
        // <PRD-action type="decrease" entity="ent-1" property="p2" by="environment(e3)"/>

        // Handle expression:
        for (EntityInstance instance : context.getEntityInstanceManager().getInstances()) {
            if (instance.getEntityDefinitionName().equals(entityDefinition.getName())) {
                PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(property); // Property AGE
                if (byExpression.startsWith("environment")) {
                    updatePropertyInstanceValueByEnvironment(context, propertyInstance);
                } else if (byExpression.startsWith("random")) {
                    updatePropertyInstanceValueByRandom(propertyInstance);
                } else if(instance.hasPropertyByName(byExpression)) {
                    updatePropertyInstanceValueByProperty(context, propertyInstance);
                } else { // ערך חופשי
                    updatePropertyInstanceValueByFreeValue(propertyInstance);
                }
            }
        }
    }
    private void updatePropertyInstanceValueByProperty(Context context, PropertyInstance propertyInstance) {
        Object oldValue = PropertyType.DECIMAL.convert(propertyInstance.getValue());
        if(propertyInstance.getValue().getClass().getName().equals("Integer")){
            propertyInstance.updateValue((Integer) oldValue + (Integer)context.getPrimaryEntityInstance().getPropertyByName(byExpression).getValue());
        }
        else{
            propertyInstance.updateValue((Float) oldValue + (Float)context.getPrimaryEntityInstance().getPropertyByName(byExpression).getValue());
        }

    }
    private void updatePropertyInstanceValueByFreeValue(PropertyInstance propertyInstance) {
        Object oldValue = PropertyType.DECIMAL.convert(propertyInstance.getValue());
        if(propertyInstance.getValue().getClass().getName().equals("Integer")){
            Integer newValue = StringToInteger(byExpression);
            propertyInstance.updateValue(newValue + (Integer)oldValue);
        }
        else {
            Float newValue = StringToFloat(byExpression);
            propertyInstance.updateValue(newValue + (Float)oldValue);
        }
    }
    private void updatePropertyInstanceValueByRandom(PropertyInstance propertyInstance) {
        Function envFunction = new RandomFunction(byExpression);
        Object oldValue = PropertyType.DECIMAL.convert(propertyInstance.getValue());
        if(propertyInstance.getValue().getClass().getName().equals("Integer")){
            propertyInstance.updateValue(envFunction.getRandomValue() + (Integer)oldValue);
        }
        else {
            propertyInstance.updateValue(envFunction.getRandomValue() + (Float)oldValue);
        }

    }

    private void updatePropertyInstanceValueByEnvironment(Context context, PropertyInstance propertyInstance){
        Function envFunction = new EnvironmentFunction(byExpression);
        Object oldValue = PropertyType.DECIMAL.convert(propertyInstance.getValue());
        if(propertyInstance.getValue().getClass().getName().equals("Integer")){
            Integer newValue = (Integer) oldValue + (Integer)PropertyType.DECIMAL.convert(envFunction.getPropertyInstanceValueFromEnvironment(context));
            propertyInstance.updateValue(newValue);
        }
        else {
            Float newValue = (Float)oldValue + (Float)PropertyType.DECIMAL.convert(envFunction.getPropertyInstanceValueFromEnvironment(context));
            propertyInstance.updateValue(newValue);
        }
    }

    private boolean verifyNumericPropertyTYpe(PropertyInstance propertyValue) {
        return
                PropertyType.DECIMAL.equals(propertyValue.getPropertyDefinition().getType()) || PropertyType.FLOAT.equals(propertyValue.getPropertyDefinition().getType());
    }
}
