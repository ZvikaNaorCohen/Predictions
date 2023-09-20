package action.api;

import definition.entity.EntityDefinition;
import execution.context.Context;

public abstract class AbstractAction implements Action {

    protected final ActionType actionType;
    protected final EntityDefinition entityDefinition;

    protected AbstractAction(ActionType actionType, EntityDefinition entityDefinition) {
        this.actionType = actionType;
        this.entityDefinition = entityDefinition;
    }

    @Override
    public Object getValueFromEvaluate(Context context, String input) {
        int openParenIndex = input.indexOf("(");
        int dotIndex = input.indexOf(".");

        if (openParenIndex != -1 && dotIndex != -1) {
            String entityName = input.substring(openParenIndex + 1, dotIndex);
            String propertyName = input.substring(dotIndex + 1, input.length() - 1);

            if(context.getPrimaryEntityInstance().getEntityDefinitionName().equals(entityName)){
                return context.getPrimaryEntityInstance().getPropertyByName(propertyName).getValue();
            }
            else {
                return context.getSecondaryEntityInstance().getPropertyByName(propertyName).getValue();
            }
        }

        return 0;
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
