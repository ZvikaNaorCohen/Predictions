package DTO;

import DTO.definition.ActionDTO;
import DTO.definition.EntityDefinitionDTO;
import DTO.rule.RuleDTO;
import DTO.rule.TerminationDTO;
import action.api.Action;
import definition.entity.EntityDefinition;
import engine.AllData;
import rule.Rule;

import java.util.*;

public class ContextDTO {
    Map<String, EntityDefinitionDTO> allEntitiesDTO;
    Set<RuleDTO> allRulesDTO;
    TerminationDTO terminationDTO;

    public ContextDTO(AllData allDefinitions){
        terminationDTO = new TerminationDTO(allDefinitions.getTerminationFromAllData());
        allEntitiesDTO = new HashMap<>();
        allRulesDTO = new HashSet<>();

        for(EntityDefinition e : allDefinitions.getMapAllEntities().values()){
            EntityDefinitionDTO temp = new EntityDefinitionDTO(e);
            allEntitiesDTO.put(e.getName(), temp);
        }

        for(Rule rule : allDefinitions.getAllRulesFromAllData()){
            List<ActionDTO> listOfActionDTOs = new ArrayList<>();
            for(Action a : rule.getActionsToPerform()){
                listOfActionDTOs.add(new ActionDTO(a));
            }
            allRulesDTO.add(new RuleDTO(rule, rule.getActivation(), listOfActionDTOs));
        }

    }

    public String getWorldData(){
        String answer = "";
        for(EntityDefinitionDTO entityDTO : allEntitiesDTO.values()){
            answer += entityDTO.getEntityDTOData();
        }

        for(RuleDTO rule : allRulesDTO){
            answer += rule.getRuleData();
        }

        answer += terminationDTO.getTerminationData();

        return answer;
    }
}
