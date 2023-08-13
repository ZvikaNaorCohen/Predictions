package action.api;

import definition.entity.EntityDefinition;

public abstract class AbstractAction implements Action {

    protected final ActionType actionType;
    protected final EntityDefinition entityDefinition;

    protected AbstractAction(ActionType actionType, EntityDefinition entityDefinition) {
        this.actionType = actionType;
        this.entityDefinition = entityDefinition;
    }

    @Override
    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public EntityDefinition getContextEntity() {
        return entityDefinition;
    }


}
