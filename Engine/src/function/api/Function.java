package function.api;

import execution.context.Context;

public interface Function {
    Object getPropertyInstanceValueFromEnvironment(Context context);
    Integer getRandomValue();

}
