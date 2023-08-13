package definition.value.generator.fixed;

import definition.value.generator.api.ValueGenerator;

public class FixedValueGenerator<T> implements ValueGenerator<T> { // Give this fixed value. For example: 8, "string",..

    private final T fixedValue;

    public FixedValueGenerator(T fixedValue) {
        this.fixedValue = fixedValue;
    }

    @Override
    public T generateValue() {
        return fixedValue;
    }

}
