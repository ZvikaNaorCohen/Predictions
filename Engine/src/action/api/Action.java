package action.api;

import definition.entity.EntityDefinition;
import definition.entity.SecondaryEntityDefinition;
import execution.context.Context;

public interface Action {
    void invoke(Context context);
    ActionType getActionType();
    EntityDefinition getContextEntity();
    Object getValueFromEvaluate(Context context, String input);

    boolean hasSecondEntity();

    SecondaryEntityDefinition getSecondEntityDefinition();
}
