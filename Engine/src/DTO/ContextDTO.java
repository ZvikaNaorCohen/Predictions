package DTO;

import DTO.definition.ActionDTO;
import DTO.definition.EntityDefinitionDTO;
import DTO.instance.PropertyInstanceDTO;
import DTO.rule.RuleDTO;
import DTO.rule.TerminationDTO;
import action.api.Action;
import definition.entity.EntityDefinition;
import definition.property.api.PropertyDefinition;
import engine.AllData;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import execution.instance.property.PropertyInstance;
import rule.Rule;

import java.util.*;

public class ContextDTO {
    Map<String, EntityDefinitionDTO> allEntitiesDTO;
    Map<Integer, EntityInstance> aliveEntityInstances;
    Map<String, Integer> entityNameToAliveCount;
    Set<RuleDTO> allRulesDTO;
    TerminationDTO terminationDTO;
    List<PropertyInstanceDTO> propertyInstanceDTOs;

    public Map<String, Integer> getAliveCountMap(){return entityNameToAliveCount;}

    public ContextDTO(Context context){
        aliveEntityInstances = new HashMap<>();
        entityNameToAliveCount = new HashMap<>();
        for(EntityInstance instance : context.getEntityInstanceManager().getInstances()){
            aliveEntityInstances.put(instance.getId(), instance);
            if(entityNameToAliveCount.get(instance.getEntityDefinitionName()) != null){
                int aliveInstances = entityNameToAliveCount.get(instance.getEntityDefinitionName())+1;
                entityNameToAliveCount.put(instance.getEntityDefinitionName(), aliveInstances);
            }
            else{
                entityNameToAliveCount.put(instance.getEntityDefinitionName(), 1);
            }
        }
    }


    public ContextDTO(AllData allDefinitions){
        terminationDTO = new TerminationDTO(allDefinitions.getTerminationFromAllData());
        allEntitiesDTO = new HashMap<>();
        allRulesDTO = new HashSet<>();
        propertyInstanceDTOs = new ArrayList<>();


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

    public void updatePropertyInstanceDTO(AllData allDefinitions, Context context){
        for(PropertyDefinition propDef : allDefinitions.getEnvVariables()){
            PropertyInstance propInstance = context.getActiveEnvironment().getProperty(propDef.getName());
            PropertyInstanceDTO propInstanceDTO = new PropertyInstanceDTO(propDef.getName(), propDef.getType(), propInstance.getValue());
            propertyInstanceDTOs.add(propInstanceDTO);
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

    public List<PropertyInstanceDTO> getPropertyInstanceDTOs(){return propertyInstanceDTOs;}
}
