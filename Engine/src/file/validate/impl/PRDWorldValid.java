package file.validate.impl;

import generated.PRDRule;
import generated.PRDWorld;

public class PRDWorldValid {

    String errorMessage = "";
    public boolean isWorldValid(PRDWorld world){
        errorMessage = "";
        PRDEntitiesValidator entitiesValidator = new PRDEntitiesValidator();
        PRDEnvironmentValidator prdEnvironmentValidator = new PRDEnvironmentValidator();
        PRDRulesValidator rulesValidator = new PRDRulesValidator();

        if(!entitiesValidator.isEntityValidatorValid(world)){
            errorMessage = "Problem: Found entities with duplicated names, or duplicated property names for one entity. \n";
            return false;
        }

        if(!prdEnvironmentValidator.isEnvironmentValid(world.getPRDEvironment())){
            errorMessage = "Problem: Environment variables with duplicated names found. \n";
            return false;
        }

        if(!rulesValidator.areRulesValid(world)){
            errorMessage = rulesValidator.getErrorMessage();
            return false;
        }

        return true;
    }

    public String getErrorMessage(){return errorMessage;}
}
