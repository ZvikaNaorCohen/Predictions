package function.impl;

import execution.context.Context;
import function.api.AbstractFunction;

import java.util.Random;

public class RandomFunction extends AbstractFunction {

    public RandomFunction(String e){
        super(e);
    }

    @Override
    public Object getPropertyInstanceValueFromEnvironment(Context context) {
        return null;
    }

    @Override
    public Integer getRandomValue() {
        Random random = new Random();
        return random.nextInt(Integer.parseInt(expression));
    }
}
