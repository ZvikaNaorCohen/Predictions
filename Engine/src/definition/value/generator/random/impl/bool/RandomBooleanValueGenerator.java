package definition.value.generator.random.impl.bool;

import definition.value.generator.random.api.AbstractRandomValueGenerator;

public class RandomBooleanValueGenerator extends AbstractRandomValueGenerator<Boolean> {

    @Override
    public Boolean generateValue() {
        return random.nextBoolean();
    }
}
