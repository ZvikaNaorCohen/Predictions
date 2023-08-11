package action.impl;

import action.api.AbstractAction;
import action.api.AbstractCalculationAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import execution.context.Context;

public class MultiplyAction extends AbstractCalculationAction {
    String argument1, argument2;
    public MultiplyAction(EntityDefinition entityDefinition, String arg1, String arg2) {
        super(ActionType.DIVIDE, entityDefinition, "");
        argument1 = arg1;
        argument2 = arg2;
    }

    @Override
    public void invoke(Context context) {
        // Perform multiply
    }
}
