package function.impl;

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

    @Override
    public Double getPercentFromFunction(Context context) {
        return 1.0;
    }

    @Override
    public Float getTicksNotUpdated(Context context) {
        return null;
    }
}
