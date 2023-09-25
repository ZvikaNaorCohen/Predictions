package action.impl;

import action.api.AbstractCalculationAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import definition.property.api.PropertyType;
import execution.context.Context;
import execution.instance.entity.EntityInstance;

public class MultiplyAction extends AbstractCalculationAction {
    String argument1, argument2;

    public MultiplyAction(EntityDefinition entityDefinition, String arg1, String arg2, String resultprop) {
        super(ActionType.MULTIPLY, entityDefinition, resultprop);
        argument1 = arg1;
        argument2 = arg2;
    }

    private int getIntFromObject(Object input) {
        if (input instanceof Integer) {
            return (int) input;
        } else {
            return Math.round((float) input);
        }
    }

    private float getFloatFromObject(Object input) {
        if (input instanceof Integer) {
            return Math.round((float) input);
        } else {
            return (float) input;
        }
    }

    @Override
    public void invoke(Context context) {
        EntityInstance primaryInstance = context.getPrimaryEntityInstance();
        EntityInstance secondaryInstance = context.getSecondaryEntityInstance();
        Object argument1Value = getArgumentValue(context, argument1);
        Object argument2Value = getArgumentValue(context, argument2);
        if(primaryInstance.getPropertyByName(resultProp) != null) {
            if (primaryInstance.getPropertyByName(resultProp).getPropertyDefinition().getType() == PropertyType.FLOAT) {
                float val1 = getFloatFromObject(argument1Value);
                float val2 = getFloatFromObject(argument2Value);
                if (primaryInstance.getPropertyByName(resultProp).getPropertyDefinition().newValueInCorrectBounds(val1 * val2)) {
                    primaryInstance.getPropertyByName(resultProp).updateValue(val1 * val2);
                }
            }
        }
        else{
            if (secondaryInstance != null && secondaryInstance.getPropertyByName(resultProp).getPropertyDefinition().getType() == PropertyType.FLOAT) {
                float val1 = getFloatFromObject(argument1Value);
                float val2 = getFloatFromObject(argument2Value);
                if (secondaryInstance.getPropertyByName(resultProp).getPropertyDefinition().newValueInCorrectBounds(val1 * val2)) {
                    secondaryInstance.getPropertyByName(resultProp).updateValue(val1 * val2);
                }
            }
        }
    }
}
