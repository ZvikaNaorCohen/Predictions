package function.impl;

import execution.context.Context;
import execution.instance.entity.EntityInstance;
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
        int startIndex = expression.indexOf("(");
        int endIndex = expression.indexOf(")");

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            // Extract the substring between the parentheses
            String numberString = expression.substring(startIndex + 1, endIndex);

            // Convert the number to an integer if needed
            int number = Integer.parseInt(numberString);
            return random.nextInt(number);
        }
        return 1;
    }

    @Override
    public Object getValueFromEvaluate(Context context) {
        return null;
    }

    @Override
    public Double getPercentFromFunction(Context context) {
        return null;
    }

    @Override
    public Float getTicksNotUpdated(Context context) {
        return null;
    }
}
