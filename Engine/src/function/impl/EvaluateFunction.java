package function.impl;

import execution.context.Context;
import execution.instance.entity.EntityInstance;
import function.api.AbstractFunction;

public class EvaluateFunction extends AbstractFunction {
    public EvaluateFunction(String e) {
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
        String[] parts = expression.split("\\.");
        String entityName = parts[0];
        String propertyName = parts[1];

        if(context.getPrimaryEntityInstance().getEntityDefinitionName().equals(entityName)){
            return context.getPrimaryEntityInstance().getPropertyByName(propertyName).getValue();
        }
        else {
            return context.getSecondaryEntityInstance().getPropertyByName(propertyName).getValue();
        }
    }

    @Override
    public Double getPercentFromFunction(Context context) {
        return null;
    }

    @Override
    public Integer getTicksNotUpdated(Context context) {
        return null;
    }
}
