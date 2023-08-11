package execution.context;

import execution.instance.entity.EntityInstance;
import execution.instance.property.PropertyInstance;
import rule.Termination;

public interface Context {
    Termination getTerminationRules();
    EntityInstance getPrimaryEntityInstance();
    void removeEntity(EntityInstance entityInstance);
    PropertyInstance getEnvironmentVariable(String name);
}
