package definition.property.impl;

import definition.property.api.AbstractPropertyDefinition;
import definition.property.api.PropertyType;
import definition.value.generator.api.ValueGenerator;

public class BooleanPropertyDefinition extends AbstractPropertyDefinition<Boolean> {
    public BooleanPropertyDefinition(String name, ValueGenerator<Boolean> valueGenerator, double from, double to) {
        super(name, PropertyType.BOOLEAN, valueGenerator, from, to);
    }


    @Override
    public boolean newValueInCorrectBounds(Integer value) {
        return true;
    }

    @Override
    public boolean newValueInCorrectBounds(Float value) {
        return true;
    }
}
