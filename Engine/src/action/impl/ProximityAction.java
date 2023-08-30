package action.impl;

import action.api.AbstractAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import execution.context.Context;

public class ProximityAction extends AbstractAction {

    public ProximityAction(ActionType actionType, EntityDefinition entityDefinition) {
        super(actionType, entityDefinition);
    }

    @Override
    public void invoke(Context context) {

    }
}
