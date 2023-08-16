package DTO.rule;

import rule.Termination;

public class TerminationDTO {
    protected Termination finishRule;

    public TerminationDTO(Termination r){
        finishRule = r;
    }

    public int getTerminationEndByTicks(){
        return finishRule.getEndByTicks();
    }

    public int getTerminationEndBySeconds(){
        return finishRule.getEndBySeconds();
    }

    public String getTerminationData(){
        return "Termination seconds: " + finishRule.getEndBySeconds() + ". Termination ticks: " + finishRule.getEndByTicks();
    }
}
