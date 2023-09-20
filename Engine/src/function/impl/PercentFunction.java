package function.impl;

import action.api.AbstractAction;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import function.api.AbstractFunction;

public class PercentFunction extends AbstractFunction {
    public PercentFunction(String e) {
        super(e);
    }

    @Override
    public Object getPropertyInstanceValueFromEnvironment(Context context) {
        return null;
    }

    @Override
    public Integer getRandomValue() {
        return null;
    }

    @Override
    public Object getValueFromEvaluate(Context context) {
        return null;
    }

    private Object getValueFromEvaluate(Context context, String input) {
        int openParenIndex = input.indexOf("(");
        int dotIndex = input.indexOf(".");

        if (openParenIndex != -1 && dotIndex != -1) {
            String entityName = input.substring(openParenIndex + 1, dotIndex);
            String propertyName = input.substring(dotIndex + 1, input.length() - 1);

            if(context.getPrimaryEntityInstance().getEntityDefinitionName().equals(entityName)){
                return context.getPrimaryEntityInstance().getPropertyByName(propertyName).getValue();
            }
            else {
                return context.getSecondaryEntityInstance().getPropertyByName(propertyName).getValue();
            }
        }

        return 0;
    }

    private String[] splitString(String input) {
        int firstOpeningParenthesisIndex = input.indexOf("(");
        int commaIndex = input.indexOf(",");
        int lastClosingParenthesisIndex = input.lastIndexOf(")");

        if (firstOpeningParenthesisIndex != -1 && commaIndex != -1 && lastClosingParenthesisIndex != -1) {
            String part1 = input.substring(firstOpeningParenthesisIndex + 1, commaIndex).trim();
            String part2 = input.substring(commaIndex + 1, lastClosingParenthesisIndex).trim();
            return new String[]{part1, part2};
        } else {
            return null; // Parts not found in input
        }
    }

    private Object getValueFromExpression(Context context, String input){
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
    public Double getPercentFromFunction(Context context) {
        String[] result = splitString(expression);
        if(result == null){
            return 1.0;
        }
        double whole = Double.parseDouble((String) getValueFromExpression(context, result[0]));
        double percentage = Double.parseDouble((String) getValueFromExpression(context, result[1]));

        return (percentage / 100) * whole;
    }

    @Override
    public Integer getTicksNotUpdated(Context context) {
        return null;
    }
}
