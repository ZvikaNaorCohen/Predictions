package definition.property.api;

import definition.property.impl.IntegerPropertyDefinition;
import definition.value.generator.api.ValueGenerator;

public abstract class AbstractPropertyDefinition<T> implements PropertyDefinition {

    private final String name;
    private final PropertyType propertyType;
    protected final ValueGenerator<T> valueGenerator;

    protected double from;
    protected double to;

    public AbstractPropertyDefinition(String name, PropertyType propertyType, ValueGenerator<T> valueGenerator, double f, double t) {
        this.name = name;
        this.propertyType = propertyType;
        this.valueGenerator = valueGenerator;
        this.from = f;
        this.to = t;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PropertyType getType() {
        return propertyType;
    }

    @Override
    public T generateValue() {
        return valueGenerator.generateValue();
    }
}
