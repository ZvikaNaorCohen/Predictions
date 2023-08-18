package DTO.instance;

import definition.property.api.PropertyType;

public class PropertyInstanceDTO {
    protected String name;
    protected PropertyType type;
    protected Object value;

    public PropertyInstanceDTO(String n, PropertyType t, Object v){
        type = t;
        value = v;
        name = n;
    }

    public String getPropertyInstanceDTOName(){
        return name;
    }

    public PropertyType getPropertyInstanceDTOType(){
        return type;
    }
    public Object getPropertyInstanceDTOValue(){
        return value;
    }

}
