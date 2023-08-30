package action.impl;

import action.api.AbstractAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import execution.context.Context;

public class ReplaceAction extends AbstractAction {
    String entityToCreate;
    String mode;

    public ReplaceAction(ActionType actionType, EntityDefinition entityDefinition) {
        super(actionType, entityDefinition);
    }

    public ReplaceAction(ActionType actionType, EntityDefinition entityDefinition, String entity, String m) {
        super(actionType, entityDefinition);
        entityToCreate = entity;
        mode = m;
    }


    @Override
    public void invoke(Context context) {

    }
}
