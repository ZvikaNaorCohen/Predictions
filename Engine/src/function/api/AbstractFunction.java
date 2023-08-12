package function.api;

import execution.context.Context;
import execution.instance.property.PropertyInstance;

public abstract class AbstractFunction implements Function {
    protected String expression;

    public AbstractFunction(String e){
        expression = e;
    }
}
