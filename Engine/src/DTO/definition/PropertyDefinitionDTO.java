package DTO.definition;

import definition.property.api.PropertyDefinition;

public class PropertyDefinitionDTO {
    protected PropertyDefinition property;

    public PropertyDefinitionDTO(PropertyDefinition p){
        property = p;
    }

    public String getPropertyDefinitionData(){
        String answer = "Property name: " + property.getName() + ".\t Property type: " + property.getType().name() + ". \n";

        return answer;
    }
}
