package definition.property.impl;

import definition.property.api.AbstractPropertyDefinition;
import definition.property.api.PropertyType;
import definition.value.generator.api.ValueGenerator;

public class FloatPropertyDefinition extends AbstractPropertyDefinition<Float> {
    public FloatPropertyDefinition(String name, ValueGenerator<Float> valueGenerator, double from, double to) {
        super(name, PropertyType.FLOAT, valueGenerator, from, to);
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
