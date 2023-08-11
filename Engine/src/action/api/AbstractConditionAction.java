package action.api;

import definition.entity.EntityDefinition;

public abstract class AbstractConditionAction extends AbstractAction{

    protected AbstractConditionAction(ActionType actionType, EntityDefinition entityDefinition) {
        super(actionType, entityDefinition);
    }
}
