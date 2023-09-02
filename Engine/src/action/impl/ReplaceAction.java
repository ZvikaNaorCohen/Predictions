package action.impl;

import action.api.AbstractAction;
import action.api.Action;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import execution.instance.entity.EntityInstanceImpl;

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
        for (EntityInstance instance : context.getEntityInstanceManager().getInstances()) {
            if (instance.getEntityDefinitionName().equals(entityDefinition.getName())) {
                context.setPrimaryEntityInstance(instance);
                if (mode.equals("scratch")) {
                    handleScratchMode(context);
                } else {
                    handleDerivedMode(context);
                }
            }
        }
    }

    private void handleScratchMode(Context context){
        Action kill = new KillAction(context.getPrimaryEntityInstance().getEntityDef());
        kill.invoke(context);
        context.getEntityInstanceManager().createEntityInstanceByName(entityToCreate);
    }

    private void handleDerivedMode(Context context){

    }
}
