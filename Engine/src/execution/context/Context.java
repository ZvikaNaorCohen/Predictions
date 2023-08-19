package execution.context;

import execution.instance.entity.EntityInstance;
import execution.instance.entity.manager.EntityInstanceManager;
import execution.instance.environment.api.ActiveEnvironment;
import execution.instance.property.PropertyInstance;
import rule.Termination;

public interface Context {
    Termination getTerminationRules();
    void setContextID(int id);
    int getID();
    EntityInstance getPrimaryEntityInstance();
    ActiveEnvironment getActiveEnvironment();
    boolean shouldSimulationTerminate(int ticks, int seconds);
    void singleSimulationRun(int ticks);
    void runSimulation();
    void removeEntity(EntityInstance entityInstance);
    PropertyInstance getEnvironmentVariable(String name);
    void setPrimaryEntityInstance(EntityInstance instance);
    EntityInstanceManager getEntityInstanceManager();
    void setActiveEnvironment(ActiveEnvironment e);
}
