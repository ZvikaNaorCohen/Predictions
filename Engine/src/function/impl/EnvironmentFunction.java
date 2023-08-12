package function.impl;

import definition.property.api.PropertyDefinition;
import definition.property.api.PropertyType;
import execution.context.Context;
import execution.instance.property.PropertyInstance;
import function.api.AbstractFunction;
import function.api.Function;

public class EnvironmentFunction extends AbstractFunction{

    public EnvironmentFunction(String e) {
        super(e);
    }

    private String getVarNameFromExpression(){
        String input = expression;
        String answer = "";
        int startIndex = input.indexOf("(");
        int endIndex = input.indexOf(")");
        answer = input.substring(startIndex + 1, endIndex);

        return answer;
    }
    public Object getPropertyInstanceValueFromEnvironment(Context context){
        String envVarName = "";
        envVarName = getVarNameFromExpression();
        return context.getPrimaryEntityInstance().getPropertyByName(envVarName).getValue(); // Assuming property name exists
    }

    @Override
    public Integer getRandomValue() {
        return null;
    }

}
