package execution.instance.entity;

import definition.entity.EntityDefinition;
import execution.instance.property.PropertyInstance;

import java.util.HashMap;
import java.util.Map;

public class EntityInstanceImpl implements EntityInstance {

    private final EntityDefinition entityDefinition;
    private final int id;
    private Map<String, PropertyInstance> properties;

    public EntityInstanceImpl(EntityDefinition entityDefinition, int id) {
        this.entityDefinition = entityDefinition;
        this.id = id;
        properties = new HashMap<>();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public PropertyInstance getPropertyByName(String name) {
        if (!properties.containsKey(name)) {
            throw new IllegalArgumentException("for entity of type " + entityDefinition.getName() + " has no property named " + name);
        }

        return properties.get(name);
    }

    public EntityDefinition getEntityDef(){
        return entityDefinition;
    }

    @Override
    public void addPropertyInstance(PropertyInstance propertyInstance) {
        properties.put(propertyInstance.getPropertyDefinition().getName(), propertyInstance);
    }

    public boolean hasPropertyByName(String name){
        return properties.containsKey(name);
    }

    @Override
    public String getEntityDefinitionName() {
        return entityDefinition.getName();
    }
}
