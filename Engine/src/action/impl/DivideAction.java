package action.impl;

import action.api.AbstractCalculationAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import definition.property.api.PropertyType;
import execution.context.Context;
import execution.instance.entity.EntityInstance;

public class DivideAction extends AbstractCalculationAction {
    String argument1, argument2;

    public DivideAction(EntityDefinition entityDefinition, String arg1, String arg2) {
        super(ActionType.DIVIDE, entityDefinition, "");
        argument1 = arg1;
        argument2 = arg2;
    }

    @Override
    public void invoke(Context context) {
        for (EntityInstance instance : context.getEntityInstanceManager().getInstances()) {
            if (instance.getEntityDefinitionName().equals(entityDefinition.getName())) {
                Object argument1Value = getArgumentValue(context, argument1);
                Object argument2Value = getArgumentValue(context, argument2);

                if (instance.getPropertyByName(resultProp).getPropertyDefinition().getType() == PropertyType.DECIMAL) {
                    Integer val1 = (Integer) argument1Value;
                    Integer val2 = (Integer) argument2Value;
                    if(instance.getPropertyByName(resultProp).getPropertyDefinition().newValueInCorrectBounds(val1/val2)) {
                        instance.getPropertyByName(resultProp).updateValue(val1 / val2);
                    }
                }
                if (instance.getPropertyByName(resultProp).getPropertyDefinition().getType() == PropertyType.FLOAT) {
                    Float val1 = (Float) argument1Value;
                    Float val2 = (Float) argument2Value;
                    if(instance.getPropertyByName(resultProp).getPropertyDefinition().newValueInCorrectBounds(val1/val2)){
                        instance.getPropertyByName(resultProp).updateValue(val1 / val2);
                    }
                }
            }
        }
    }
}
