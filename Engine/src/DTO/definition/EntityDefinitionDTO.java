package DTO.definition;

import definition.entity.EntityDefinition;
import definition.property.api.PropertyDefinition;

import java.util.ArrayList;
import java.util.List;

public class EntityDefinitionDTO {
    protected EntityDefinition entityDefinition;
    protected List<PropertyDefinitionDTO> listOfPropertyDTOs;

    public EntityDefinitionDTO(EntityDefinition e){
        entityDefinition = e;
        listOfPropertyDTOs = new ArrayList<>();
        for(PropertyDefinition prop : e.getProps()){
            listOfPropertyDTOs.add(new PropertyDefinitionDTO(prop));
        }
    }

    public String getEntityDTOData(){
        String answer = "Entity name: " + entityDefinition.getName() + ". Population: " + entityDefinition.getPopulation() + ". \n";
        int counter = 1;
        for(PropertyDefinitionDTO propDTO : listOfPropertyDTOs){
            answer += "\t Action property #" + counter + ": " + propDTO.getPropertyDefinitionData();
            counter++;
        }

        return answer;
    }
}
