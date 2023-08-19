package execution.instance.entity;

import definition.entity.EntityDefinition;
import execution.instance.property.PropertyInstance;

public interface EntityInstance {
    int getId();
    PropertyInstance getPropertyByName(String name);

    EntityDefinition getEntityDef();
    void addPropertyInstance(PropertyInstance propertyInstance);

    boolean hasPropertyByName(String name);

    String getEntityDefinitionName();
}
