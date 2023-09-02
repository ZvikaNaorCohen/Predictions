package execution.instance.entity.manager;

import definition.entity.EntityDefinition;
import execution.instance.entity.EntityInstance;

import java.util.List;

public interface EntityInstanceManager {
    EntityInstance create(EntityDefinition entityDefinition, EntityInstance[][] grid);
    List<EntityInstance> getInstances();
    void killEntity(String entityName);

    int getCurrentAliveEntitiesByName(String name);

    void createEntityInstanceByName(String name);
}
