package action.api;

import definition.entity.EntityDefinition;
import execution.context.Context;

public interface Action {
    void invoke(Context context);
    ActionType getActionType();
    EntityDefinition getContextEntity();
}
