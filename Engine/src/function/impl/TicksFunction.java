package function.impl;

import execution.context.Context;
import function.api.AbstractFunction;

public class TicksFunction extends AbstractFunction {
    public TicksFunction(String e) {
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

    @Override
    public Double getPercentFromFunction(Context context) {
        return null;
    }

    @Override
    public Float getTicksNotUpdated(Context context) {
        int openParenIndex = expression.indexOf("(");
        int dotIndex = expression.indexOf(".");

        if (openParenIndex != -1 && dotIndex != -1) {
            String entityName = expression.substring(openParenIndex + 1, dotIndex);
            String propertyName = expression.substring(dotIndex + 1, expression.length() - 1);

            if(context.getPrimaryEntityInstance().getEntityDefinitionName().equals(entityName)){
                return context.getPrimaryEntityInstance().getPropertyByName(propertyName).getTicksNotChanged();
            }
            else {
                return context.getSecondaryEntityInstance().getPropertyByName(propertyName).getTicksNotChanged();
            }
        }

        return 0f;
    }
}
