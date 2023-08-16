package DTO.rule;

import rule.Activation;

public class ActivationDTO {
    protected Activation ruleActivation;

    public ActivationDTO(Activation act){
        ruleActivation = act;
    }

    public int getActivationDTOTicks(){
        return ruleActivation.getTicks();
    }

    public double getActivationDTOProb(){
        return ruleActivation.getProb();
    }
}
