package file.validate.impl;

import generated.PRDEnvProperty;
import generated.PRDRule;
import generated.PRDRules;
import generated.PRDWorld;

import java.util.HashSet;
import java.util.Set;

public class PRDRulesValidator {
    private boolean rulesWithUniqueNames = true;
    private String errorMessage = "";

    public boolean areRulesValid(PRDWorld world){
        rulesWithUniqueNames = true;
        errorMessage = "";

        // Validate all actions:
        for(PRDRule rule : world.getPRDRules().getPRDRule()){
            PRDActionValidator actionValidator = new PRDActionValidator();
            boolean allActionsValid = actionValidator.allActionsAreValid(rule.getPRDActions(), world);
            if(!allActionsValid){
                errorMessage = actionValidator.getErrorMessage();
                return false;
            }
        }
        return true;
    }

    public String getErrorMessage(){return errorMessage;}
}
