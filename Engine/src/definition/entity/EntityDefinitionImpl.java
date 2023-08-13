package definition.entity;

import definition.property.api.PropertyDefinition;

import java.util.ArrayList;
import java.util.List;

public class EntityDefinitionImpl implements EntityDefinition {

    private final String name;
    private final int population;
    private final List<PropertyDefinition> properties;

    public EntityDefinitionImpl(String name, int population) {
        this.name = name;
        this.population = population;
        properties = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPopulation() {
        return population;
    }

    @Override
    public List<PropertyDefinition> getProps() {
        return properties;
    }

    public void addPropertyDefinition(PropertyDefinition pd){properties.add(pd);}

    public PropertyDefinition getPropDefByName(String name){
        for(PropertyDefinition p : properties){
            if(p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }

}
