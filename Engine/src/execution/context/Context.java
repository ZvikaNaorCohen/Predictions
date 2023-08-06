package execution.context;

import execution.instance.entity.EntityInstance;
import execution.instance.property.PropertyInstance;

public interface Context {
    EntityInstance getPrimaryEntityInstance();
    void removeEntity(EntityInstance entityInstance);
    PropertyInstance getEnvironmentVariable(String name);
}
