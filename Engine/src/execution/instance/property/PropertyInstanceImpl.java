package execution.instance.property;

import definition.property.api.PropertyDefinition;

import java.util.Map;

public class PropertyInstanceImpl implements PropertyInstance {

    private PropertyDefinition propertyDefinition;
    private Object value;

    private int lastTickChanged = 0;

    private int ticksNotChanged = 0;
    private int timesValueChanged = 0;

    public PropertyInstanceImpl(PropertyDefinition propertyDefinition, Object value) {
        this.propertyDefinition = propertyDefinition;
        this.value = value;
    }

    @Override
    public PropertyDefinition getPropertyDefinition() {
        return propertyDefinition;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void updateValue(Object val) {
        if(this.value != val){
            timesValueChanged++;
        }
        this.value = val;
        ticksNotChanged = 0;
    }

    @Override
    public int getTicksNotChanged(){
        return ticksNotChanged;
    }

    @Override
    public int getTimesValueChanged(){
        return timesValueChanged;
    }
}
