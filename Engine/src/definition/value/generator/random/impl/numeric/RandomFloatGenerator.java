package definition.value.generator.random.impl.numeric;

public class RandomFloatGenerator extends AbstractNumericRandomGenerator<Float>{
    public RandomFloatGenerator(float from, float to) {
        super(from, to);
    }

    @Override
    public Float generateValue() {
        return from + random.nextFloat() * (to - from);
    }
}
