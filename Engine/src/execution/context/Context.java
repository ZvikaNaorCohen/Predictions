package execution.context;

import execution.instance.entity.EntityInstance;
import execution.instance.entity.manager.EntityInstanceManager;
import execution.instance.environment.api.ActiveEnvironment;
import execution.instance.property.PropertyInstance;
import rule.Rule;
import rule.Termination;

import java.util.Set;

public interface Context {
    Termination getTerminationRules();
    void setContextID(int id);

    EntityInstance[][] getGrid();

    Set<Rule> getAllRules();
    int getID();
    EntityInstance getPrimaryEntityInstance();
    EntityInstance getSecondaryEntityInstance();
    ActiveEnvironment getActiveEnvironment();
    boolean shouldSimulationTerminate(int ticks, int seconds);
    void singleSimulationRun(int ticks);

    String runSimulation();
    void removeEntity(EntityInstance entityInstance);
    PropertyInstance getEnvironmentVariable(String name);
    void setPrimaryEntityInstance(EntityInstance instance);
    EntityInstanceManager getEntityInstanceManager();
    void setActiveEnvironment(ActiveEnvironment e);
}
