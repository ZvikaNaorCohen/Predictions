package action.api;

import definition.entity.EntityDefinition;
import definition.property.api.PropertyType;
import execution.context.Context;
import function.api.Function;
import function.impl.*;

import static definition.value.generator.transformer.Transformer.StringToFloat;
import static definition.value.generator.transformer.Transformer.StringToInteger;

public abstract class AbstractCalculationAction extends AbstractAction {
    protected String resultProp;

    protected AbstractCalculationAction(ActionType type, EntityDefinition entityDefinition, String result) {
        super(type, entityDefinition);
        if (!result.equals("")) {
            resultProp = result;
        }
    }

    @Override
    public void invoke(Context context) {

    }

    protected Object getArgumentValue(Context context, String arg) {
        if (arg.startsWith("environment")) {
            Function func1 = new EnvironmentFunction(arg);
            return func1.getPropertyInstanceValueFromEnvironment(context);
        } else if (arg.startsWith("random")) {
            Function func1 = new RandomFunction(arg);
            return func1.getRandomValue();
        } else if (arg.startsWith("evaluate")) {
            Function func1 = new EvaluateFunction(arg);
            return func1.getValueFromEvaluate(context);
        } else if (arg.startsWith("percent")) {
            Function func1 = new PercentFunction(arg);
            return func1.getPercentFromFunction(context);
        } else if (arg.startsWith("ticks")) {
            Function func1 = new TicksFunction(arg);
            return func1.getTicksNotUpdated(context);
        } else if (context.getPrimaryEntityInstance().hasPropertyByName(arg)) {
            return context.getPrimaryEntityInstance().getPropertyByName(arg).getValue();
        } else if (context.getSecondaryEntityInstance() != null && context.getSecondaryEntityInstance().hasPropertyByName(arg)) {
            return context.getSecondaryEntityInstance().getPropertyByName(arg).getValue();
        } else {
            return StringToFloat(arg);
        }
    }
}
