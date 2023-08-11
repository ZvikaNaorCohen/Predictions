package action.impl;

import action.api.AbstractAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import execution.context.Context;

public class DecreaseAction extends AbstractAction {
    private final String property;
    private final String byExpression;

    public DecreaseAction(EntityDefinition entityDefinition, String property, String byExpression) {
        super(ActionType.DECREASE, entityDefinition);
        this.property = property;
        this.byExpression = byExpression;
    }

    @Override
    public void invoke(Context context) {

    }
}
