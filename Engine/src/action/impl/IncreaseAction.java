package action.impl;

import action.api.AbstractAction;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import definition.property.api.PropertyType;
import execution.context.Context;
import execution.instance.property.PropertyInstance;

public class IncreaseAction extends AbstractAction {

    private final String property;
    private final String byExpression;

    public IncreaseAction(EntityDefinition entityDefinition, String property, String byExpression) {
        super(ActionType.INCREASE, entityDefinition);
        this.property = property;
        this.byExpression = byExpression;
    }

    @Override
    public void invoke(Context context) { // <PRD-action entity="Smoker" type="increase" property="age" by="1"/>
        PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(property); // Property AGE
        if (!verifyNumericPropertyTYpe(propertyInstance)) {
            throw new IllegalArgumentException("increase action can't operate on a none number property [" + property);
        }

        Integer oldValue = PropertyType.DECIMAL.convert(propertyInstance.getValue());
        // Here I need to extract the value from byExpression
        Integer newValue = oldValue + (Integer)PropertyType.DECIMAL.convert(byExpression);



        // something that evaluates expression to a number, say the result is 5...
        // now you can also access the environment variables through the active environment...
        // PropertyInstance blaPropertyInstance = activeEnvironment.getProperty("bla");
        // int x = 5;

        // actual calculation
        // int result = x + v;

        // updating result on the property
        propertyInstance.updateValue(newValue);
    }

    private boolean verifyNumericPropertyTYpe(PropertyInstance propertyValue) {
        return
                PropertyType.DECIMAL.equals(propertyValue.getPropertyDefinition().getType()) || PropertyType.FLOAT.equals(propertyValue.getPropertyDefinition().getType());
    }
}
