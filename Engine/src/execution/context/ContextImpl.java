package execution.context;

import engine.AllData;
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

    public ContextImpl(AllData definitions) {
        terminationRules = definitions.fromAllDataToAllInstances().getEngineTermination();
        primaryEntityInstance = definitions.fromAllDataToAllInstances().getAllEntities().getInstances().get(0);
        entityInstanceManager = definitions.fromAllDataToAllInstances().getAllEntities();
        activeEnvironment = definitions.fromAllDataToAllInstances().getActiveEnvironment();
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
