package action.impl;

import action.api.AbstractAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import execution.context.Context;

public class KillAction extends AbstractAction {

    public KillAction(EntityDefinition entityDefinition) {
        super(ActionType.KILL, entityDefinition);
    }

    @Override
    public void invoke(Context context) {
        context.removeEntity(context.getPrimaryEntityInstance());
    }

}
