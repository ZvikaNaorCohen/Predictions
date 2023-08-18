package definition.property.impl;

import definition.property.api.AbstractPropertyDefinition;
import definition.property.api.PropertyType;
import definition.value.generator.api.ValueGenerator;

public class IntegerPropertyDefinition extends AbstractPropertyDefinition<Integer> {

    public IntegerPropertyDefinition(String name, ValueGenerator<Integer> valueGenerator, double from, double to) {
        super(name, PropertyType.DECIMAL, valueGenerator, from, to);
    }


    @Override
    public boolean newValueInCorrectBounds(Integer value) {
        return value <= to && value >= from;
    }

    @Override
    public boolean newValueInCorrectBounds(Float value) {
        return value <= to && value >= from;
    }
}
