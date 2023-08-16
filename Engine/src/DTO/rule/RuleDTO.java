package DTO.rule;

import DTO.definition.ActionDTO;
import rule.Activation;
import rule.Rule;

import java.util.List;

public class RuleDTO {
    protected Rule rule;
    protected ActivationDTO activation;
    protected List<ActionDTO> actionDTOList;

    public RuleDTO(Rule input, Activation act, List<ActionDTO> actionDTOListInput){
        rule = input;
        activation = new ActivationDTO(act);
        actionDTOList = actionDTOListInput;
    }

    public String getRuleDTOName(){
        return rule.getName();
    }

    public List<ActionDTO> getActionDTOList(){
        return actionDTOList;
    }

    public String getRuleData(){
        String answer = "Rule name: " + rule.getName() + ". \n";
        answer += "Rule activation: \t Ticks: " + activation.getActivationDTOTicks() + ". \t Probability: " + activation.getActivationDTOProb() + ". \n";
        answer += "Rule has " + actionDTOList.size() + " actions: \n";
        int counter = 1;
        for(ActionDTO action : actionDTOList){
            answer += "\t Action #" + counter + ": Type: " + action.getActionDTOActionTypeName() + ". \n";
            counter++;
        }

        return answer;
    }


}
