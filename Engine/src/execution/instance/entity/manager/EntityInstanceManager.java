package execution.instance.entity.manager;

import definition.entity.EntityDefinition;
import execution.instance.entity.EntityInstance;

import java.util.List;

public interface EntityInstanceManager {
    EntityInstance create(EntityDefinition entityDefinition);
    List<EntityInstance> getInstances();
    void killEntity(String entityName);

    int getCurrentAliveEntitiesByName(String name);
}
