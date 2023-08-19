package action.impl;

import action.api.AbstractConditionAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import definition.property.api.PropertyType;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import execution.instance.property.PropertyInstance;
import function.api.Function;
import function.impl.EnvironmentFunction;
import function.impl.RandomFunction;


public class ConditionAction extends AbstractConditionAction {
    String property;
    String operator;
    String value;

    public ConditionAction(ActionType actionType, EntityDefinition entityDefinition, String p, String o, String v) {
        super(actionType, entityDefinition);
        property = p;
        operator = o;
        value = v;
    }

    @Override
    public void invoke(Context context) {
        for (EntityInstance instance : context.getEntityInstanceManager().getInstances()) {
            if (instance.getEntityDefinitionName().equals(entityDefinition.getName())) {
                context.setPrimaryEntityInstance(instance);
                PropertyInstance propertyInstance = instance.getPropertyByName(property);
                switch (operator) {
                    case "=": {
                        String propertyInstanceValue = propertyInstance.getValue().toString();
                        conditionReturnValue = propertyInstanceValue.equals(value);
                        break;
                    }
                    case "!=": {
                        String propertyInstanceValue = propertyInstance.getValue().toString();
                        conditionReturnValue = !propertyInstanceValue.equals(value);
                        break;
                    }
                    case "bt": {
                        conditionReturnValue = generateValueFromCondition(instance, value, context);
                        break;
                    }
                    case "lt": {
                        conditionReturnValue = !generateValueFromCondition(instance, value, context);
                        break;
                    }
                }
                break;
            }
        }
    }
    private boolean generateValueFromCondition(EntityInstance instance, String expression, Context context) {
        PropertyInstance propertyInstance = instance.getPropertyByName(property); // Property AGE
        if (expression.startsWith("environment")) {
            return propertyInstanceBiggerThanEnvironment(context, expression, propertyInstance);
        } else if (expression.startsWith("random")) {
            return propertyInstanceBiggerThanRandom(propertyInstance, expression);
        } else if (instance.hasPropertyByName(expression)) {
            if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")){
                return (Integer)propertyInstance.getValue() > (Integer)instance.getPropertyByName(expression).getValue();
            }
            else {
                return (Float)propertyInstance.getValue() > (Float)instance.getPropertyByName(expression).getValue();
            }
        } else { // ערך חופשי
            if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")){
                return (Integer)propertyInstance.getValue() >Integer.parseInt(expression);
            }
            else if(propertyInstance.getValue().getClass().getSimpleName().equals("Float")){
                return (Float)propertyInstance.getValue() > Float.parseFloat(expression);
            }
            else return false;
        }
    }

    private boolean propertyInstanceBiggerThanRandom(PropertyInstance propertyInstance, String expression){
        Function envFunction = new RandomFunction(expression);
        if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")){
            return (Integer)propertyInstance.getValue() > envFunction.getRandomValue();
        }
        else {
            return (Float)propertyInstance.getValue() > envFunction.getRandomValue();
        }

    }

    private boolean propertyInstanceBiggerThanEnvironment(Context context, String expression, PropertyInstance propertyInstance){
        Function envFunction = new EnvironmentFunction(expression);
        if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")){
            return (Integer)propertyInstance.getValue() > (Integer)PropertyType.DECIMAL.convert(envFunction.getPropertyInstanceValueFromEnvironment(context));
        }
        else {
            return (Float)propertyInstance.getValue() > (Float)PropertyType.DECIMAL.convert(envFunction.getPropertyInstanceValueFromEnvironment(context));
        }
    }
}
