package execution.instance.entity;

import definition.entity.EntityDefinition;
import execution.instance.property.PropertyInstance;

import java.util.List;
import java.util.Map;

public interface EntityInstance {
    int getId();
    PropertyInstance getPropertyByName(String name);
    Map<String, PropertyInstance> getAllPropertyInstances();
    EntityDefinition getEntityDef();
    void addPropertyInstance(PropertyInstance propertyInstance);

    boolean hasPropertyByName(String name);

    String getEntityDefinitionName();
}
