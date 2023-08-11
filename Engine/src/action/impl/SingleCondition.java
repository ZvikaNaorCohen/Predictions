package action.impl;

import action.api.AbstractConditionAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import execution.context.Context;

public class SingleCondition extends AbstractConditionAction {
    public SingleCondition(ActionType actionType, EntityDefinition entityDefinition) {
        super(actionType, entityDefinition);
    }

    @Override
    public void invoke(Context context) {
    // implement
    }
}
