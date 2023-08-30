package file.validate.impl;

import generated.PRDWorld;

public class PRDWorldValid {

    String errorMessage = "";
    public boolean isWorldValid(PRDWorld world){
        errorMessage = "";
        PRDEntitiesValidator entitiesValidator = new PRDEntitiesValidator();
        PRDEnvironmentValidator prdEnvironmentValidator = new PRDEnvironmentValidator();
        PRDRulesValidator rulesValidator = new PRDRulesValidator();

        if(world.getPRDGrid().getColumns() < 10 || world.getPRDGrid().getColumns() >= 101 ||
           world.getPRDGrid().getRows() < 10 || world.getPRDGrid().getRows() >= 101){
            errorMessage = "Problem: Grid size is not good! Rows and cols have to be between 0 and 100. \n";
            return false;
        }

        if(!entitiesValidator.isEntityValidatorValid(world)){
            errorMessage = "Problem: Found entities with duplicated names, or duplicated property names for one entity. \n";
            return false;
        }

        if(!prdEnvironmentValidator.isEnvironmentValid(world.getPRDEnvironment())){
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
