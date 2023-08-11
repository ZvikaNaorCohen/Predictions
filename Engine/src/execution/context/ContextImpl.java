package execution.context;

import execution.instance.entity.EntityInstance;
import execution.instance.entity.manager.EntityInstanceManager;
import execution.instance.environment.api.ActiveEnvironment;
import execution.instance.property.PropertyInstance;
import rule.Termination;

public class ContextImpl implements Context {

    private EntityInstance primaryEntityInstance;
    private EntityInstanceManager entityInstanceManager;
    private ActiveEnvironment activeEnvironment;

    private Termination terminationRules;

    public ContextImpl(Termination term, EntityInstance primaryEntityInstance, EntityInstanceManager entityInstanceManager, ActiveEnvironment activeEnvironment) {
        this.primaryEntityInstance = primaryEntityInstance;
        this.entityInstanceManager = entityInstanceManager;
        this.activeEnvironment = activeEnvironment;
        this.terminationRules = term;
    }

    @Override
    public Termination getTerminationRules() {
        return terminationRules;
    }

    @Override
    public EntityInstance getPrimaryEntityInstance() {
        return primaryEntityInstance;
    }

    @Override
    public void removeEntity(EntityInstance entityInstance) {
        entityInstanceManager.killEntity(entityInstance.getId());
    }

    @Override
    public PropertyInstance getEnvironmentVariable(String name) {
        return activeEnvironment.getProperty(name);
    }
}
