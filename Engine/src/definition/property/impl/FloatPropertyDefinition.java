package definition.property.impl;

import definition.property.api.AbstractPropertyDefinition;
import definition.property.api.PropertyType;
import definition.value.generator.api.ValueGenerator;

public class FloatPropertyDefinition extends AbstractPropertyDefinition<Float> {
    public FloatPropertyDefinition(String name, ValueGenerator<Float> valueGenerator) {
        super(name, PropertyType.FLOAT, valueGenerator);
    }


}
