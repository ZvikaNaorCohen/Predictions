package action.impl;

import action.api.AbstractAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import execution.instance.property.PropertyInstance;
import function.api.Function;
import function.impl.EnvironmentFunction;
import function.impl.RandomFunction;

import static definition.value.generator.transformer.Transformer.StringToFloat;
import static definition.value.generator.transformer.Transformer.StringToInteger;

public class SetAction extends AbstractAction {
    String property, newPropertyValue;

    public SetAction(EntityDefinition entityDefinition, String inputProperty, String inputNewValue) {
        super(ActionType.SET, entityDefinition);
        property = inputProperty;
        newPropertyValue = inputNewValue;
    }


    @Override
    public void invoke(Context context) {
        for (EntityInstance instance : context.getEntityInstanceManager().getInstances()) {
            if (instance.getEntityDefinitionName().equals(entityDefinition.getName())) {
                PropertyInstance propertyInstance = instance.getPropertyByName(property); // Property AGE
                if(newPropertyValue.startsWith("environment"))
                {
                    Function envFunction = new EnvironmentFunction(newPropertyValue);
                    String className = envFunction.getPropertyInstanceValueFromEnvironment(context).getClass().getName();
                    if(className.equals("Integer") &&
                            propertyInstance.getPropertyDefinition().newValueInCorrectBounds((Integer)envFunction.getPropertyInstanceValueFromEnvironment(context))){
                        instance.getPropertyByName(property).updateValue(envFunction.getPropertyInstanceValueFromEnvironment(context));
                    }
                    if(className.equals("Float") &&
                            propertyInstance.getPropertyDefinition().newValueInCorrectBounds((Float)envFunction.getPropertyInstanceValueFromEnvironment(context))){
                        instance.getPropertyByName(property).updateValue(envFunction.getPropertyInstanceValueFromEnvironment(context));
                    }

                }
                else if(newPropertyValue.startsWith("random")){
                    Function randomFunction = new RandomFunction(newPropertyValue);
                    String className = randomFunction.getPropertyInstanceValueFromEnvironment(context).getClass().getName();
                    if(className.equals("Integer") && propertyInstance.getPropertyDefinition().newValueInCorrectBounds((Integer)randomFunction.getRandomValue())){
                        instance.getPropertyByName(property).updateValue(randomFunction.getRandomValue());
                    }
                }
                else if(instance.hasPropertyByName(newPropertyValue)){
                    propertyInstance.updateValue(instance.getPropertyByName(newPropertyValue));
                }
                else { // ערך חופשי
                    updatePropertyInstanceValueByFreeValue(propertyInstance);
                }
            }
        }
    }

    private void updatePropertyInstanceValueByFreeValue(PropertyInstance propertyInstance) {
        if(propertyInstance.getValue().getClass().getName().equals("Integer")){
            Integer newValue = StringToInteger(newPropertyValue);
            if(propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue)){
                propertyInstance.updateValue(newValue);
            }
        }
        else if(propertyInstance.getValue().getClass().getName().equals("Float")) {
            Float newValue = StringToFloat(newPropertyValue);
            if (propertyInstance.getPropertyDefinition().newValueInCorrectBounds(newValue))
            {
                propertyInstance.updateValue(newValue);
            }
        } else if(propertyInstance.getValue().getClass().getName().equals("Boolean")) {
            Boolean newValue = newPropertyValue.equals("true");
            propertyInstance.updateValue(newValue);
        }
        else {
            propertyInstance.updateValue(newPropertyValue);
        }
    }
}