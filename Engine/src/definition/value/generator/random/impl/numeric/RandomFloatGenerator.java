package definition.value.generator.random.impl.numeric;

public class RandomFloatGenerator extends AbstractNumericRandomGenerator<Float>{
    public RandomFloatGenerator(float from, float to) {
        super(from, to);
    }

    @Override
    public Float generateValue() {

        int range = (int) (to - from + 1);
        return random.nextInt(range) + from;
    }
}
