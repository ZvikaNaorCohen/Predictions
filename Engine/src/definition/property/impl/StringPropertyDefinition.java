package definition.property.impl;

import definition.property.api.AbstractPropertyDefinition;
import definition.property.api.PropertyType;
import definition.value.generator.api.ValueGenerator;

public class StringPropertyDefinition extends AbstractPropertyDefinition<String> {

    public StringPropertyDefinition(String name, ValueGenerator<String> valueGenerator, double from, double to) {
        super(name, PropertyType.STRING, valueGenerator, from, to);
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