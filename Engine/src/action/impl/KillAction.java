package action.impl;

import action.api.AbstractAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import execution.context.Context;
import execution.instance.entity.EntityInstance;

public class KillAction extends AbstractAction {

    public KillAction(EntityDefinition entityDefinition) {
        super(ActionType.KILL, entityDefinition);
    }

    @Override
    public void invoke(Context context) {
        for (EntityInstance instance : context.getEntityInstanceManager().getInstances()) {
            if (instance.getEntityDefinitionName().equals(entityDefinition.getName())) {
                context.removeEntity(instance);
                return;
            }
        }
    }
}
