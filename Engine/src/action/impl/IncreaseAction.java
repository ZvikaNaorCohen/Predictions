package action.impl;

import action.api.AbstractAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import definition.property.api.PropertyDefinition;
import definition.property.api.PropertyType;
import execution.context.Context;
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
        Function envFunction = new EnvironmentFunction(byExpression);
        if(propertyInstance.getValue().getClass().getName().equals("Integer")){
            Integer oldValue = PropertyType.DECIMAL.convert(propertyInstance.getValue());
            Integer newValue = oldValue + (Integer)PropertyType.DECIMAL.convert(envFunction.getPropertyInstanceValueFromEnvironment(context));
            propertyInstance.updateValue(newValue);
        }
        else {
            Float oldValue = PropertyType.DECIMAL.convert(propertyInstance.getValue());
            Float newValue = oldValue + (Float)PropertyType.DECIMAL.convert(envFunction.getPropertyInstanceValueFromEnvironment(context));
            propertyInstance.updateValue(newValue);
        }
    }

    private boolean verifyNumericPropertyTYpe(PropertyInstance propertyValue) {
        return
                PropertyType.DECIMAL.equals(propertyValue.getPropertyDefinition().getType()) || PropertyType.FLOAT.equals(propertyValue.getPropertyDefinition().getType());
    }
}
