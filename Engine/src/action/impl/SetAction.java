package action.impl;

import action.api.AbstractAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import execution.context.Context;

public class SetAction extends AbstractAction {
    String property, newPropertyValue;

    public SetAction(EntityDefinition entityDefinition, String inputProperty, String inputNewValue) {
        super(ActionType.SET, entityDefinition);
        property = inputProperty;
        newPropertyValue = inputNewValue;
    }


    @Override
    public void invoke(Context context) {
        // Perform Set
    }
}
