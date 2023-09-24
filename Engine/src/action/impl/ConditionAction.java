package action.impl;

import action.api.AbstractConditionAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import definition.entity.SecondaryEntityDefinition;
import definition.property.api.PropertyType;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import execution.instance.property.PropertyInstance;
import function.api.Function;
import function.impl.EnvironmentFunction;
import function.impl.PercentFunction;
import function.impl.RandomFunction;
import function.impl.TicksFunction;


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

    private String handleExpressionInString(Context context, String input){
        if(input.startsWith("environment")){
            return new EnvironmentFunction(input).getPropertyInstanceValueFromEnvironment(context).toString();
        } else if(input.startsWith("random")){
            return new RandomFunction(input).getRandomValue().toString();
        } else if(input.startsWith("evaluate")){
            return getValueFromEvaluate(context, input).toString();
        } else if(input.startsWith("percent")){
            return new PercentFunction(input).getPercentFromFunction(context).toString();
        } else if (input.startsWith("ticks")) {
            return new TicksFunction(input).getTicksNotUpdated(context).toString();
        }
        else{
            return input;
        }
    }

    @Override
    public void invoke(Context context) {
                EntityInstance primary = context.getPrimaryEntityInstance();
                EntityInstance secondary = context.getSecondaryEntityInstance();
                String propertyInstanceValue = "";
                String valueFromValue = "";
                if(property.startsWith("environment") || property.startsWith("random") || property.startsWith("evaluate")
                || property.startsWith("percent") || property.startsWith("ticks")){
                    propertyInstanceValue = handleExpressionInString(context, property);
                }
                else{
                    if(primary.getEntityDefinitionName().equals(entityDefinition.getName())){
                        propertyInstanceValue = primary.getPropertyByName(property).getValue().toString();
                    }
                    else{
                        propertyInstanceValue = secondary.getPropertyByName(property).getValue().toString();
                    }

                }

                valueFromValue = handleExpressionInString(context, value);

                switch (operator) {
                    case "=": {
                        conditionReturnValue = propertyInstanceValue.equals(value);
                        break;
                    }
                    case "!=": {
                        conditionReturnValue = !propertyInstanceValue.equals(value);
                        break;
                    }
                    case "bt": {
                        conditionReturnValue = Float.parseFloat(propertyInstanceValue) > Float.parseFloat(valueFromValue);
                        break;
                    }
                    case "lt": {
                        conditionReturnValue = Float.parseFloat(propertyInstanceValue) < Float.parseFloat(valueFromValue);
                        break;
                    }
                }
            }
//        }
//    }

//    private boolean generateValueFromCondition(EntityInstance instance, String expression, Context context) {
//        PropertyInstance propertyInstance = instance.getPropertyByName(property); // Property AGE
//        if (expression.startsWith("environment")) {
//            return propertyInstanceBiggerThanEnvironment(context, expression, propertyInstance);
//        } else if (expression.startsWith("random")) {
//            return propertyInstanceBiggerThanRandom(propertyInstance, expression);
//        } else if (expression.startsWith("evaluate")){
//            return propertyInstanceBiggerThanEvaluate(context, propertyInstance, expression);
//        }
//        else if (expression.startsWith("percent")){
//            return propertyInstanceBiggerThanPercent(context, propertyInstance, expression);
//        }
//        else if (expression.startsWith("ticks")){
//            return propertyInstanceBiggerThanTicks(context, propertyInstance, expression);
//        }
//        else if (instance.hasPropertyByName(expression)) {
//            if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")){
//                return (Integer)propertyInstance.getValue() > (Integer)instance.getPropertyByName(expression).getValue();
//            }
//            else {
//                return (Float)propertyInstance.getValue() > (Float)instance.getPropertyByName(expression).getValue();
//            }
//        } else { // ערך חופשי
//            if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")){
//                return (Integer)propertyInstance.getValue() >Integer.parseInt(expression);
//            }
//            else if(propertyInstance.getValue().getClass().getSimpleName().equals("Float")){
//                return (Float)propertyInstance.getValue() > Float.parseFloat(expression);
//            }
//            else return false;
//        }
//    }
//
//    private boolean propertyInstanceBiggerThanRandom(PropertyInstance propertyInstance, String expression){
//        Function envFunction = new RandomFunction(expression);
//        if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")){
//            return (Integer)propertyInstance.getValue() > envFunction.getRandomValue();
//        }
//        else {
//            return (Float)propertyInstance.getValue() > envFunction.getRandomValue();
//        }
//
//    }
//
//    private boolean propertyInstanceBiggerThanTicks(Context context, PropertyInstance propertyInstance, String expression){
//        Float oldValue = PropertyType.FLOAT.convert(propertyInstance.getValue());
//        Float valueFromTicks = new TicksFunction(expression).getTicksNotUpdated(context);
//
//        return oldValue > valueFromTicks;
//    }
//
//    private boolean propertyInstanceBiggerThanPercent(Context context, PropertyInstance propertyInstance, String expression){
//        Float oldValue = PropertyType.FLOAT.convert(propertyInstance.getValue());
//        Double valueFromTicks = new PercentFunction(expression).getPercentFromFunction(context);
//
//        return oldValue > valueFromTicks;
//    }
//
//
//    private boolean propertyInstanceBiggerThanEvaluate(Context context, PropertyInstance propertyInstance, String expression){
//        Float oldValue = PropertyType.FLOAT.convert(propertyInstance.getValue());
//        Float valueFromEvaluate = (Float)getValueFromEvaluate(context, expression);
//
//        return oldValue > valueFromEvaluate;
//    }
//
//    private boolean propertyInstanceBiggerThanEnvironment(Context context, String expression, PropertyInstance propertyInstance){
//        Function envFunction = new EnvironmentFunction(expression);
//        if(propertyInstance.getValue().getClass().getSimpleName().equals("Integer")){
//            return (Integer)propertyInstance.getValue() > (Integer)PropertyType.DECIMAL.convert(envFunction.getPropertyInstanceValueFromEnvironment(context));
//        }
//        else {
//            return (Float)propertyInstance.getValue() > (Float)PropertyType.DECIMAL.convert(envFunction.getPropertyInstanceValueFromEnvironment(context));
//        }
//    }
}
