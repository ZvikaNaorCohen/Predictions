package function.api;

import execution.context.Context;
import execution.instance.entity.EntityInstance;

public interface Function {
    Object getPropertyInstanceValueFromEnvironment(Context context);
    Integer getRandomValue();

    Object getValueFromEvaluate(Context context);

    Double getPercentFromFunction(Context context);

    Float getTicksNotUpdated(Context context);

}
