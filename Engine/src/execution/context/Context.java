package execution.context;

import execution.instance.entity.EntityInstance;
import execution.instance.entity.manager.EntityInstanceManager;
import execution.instance.environment.api.ActiveEnvironment;
import execution.instance.property.PropertyInstance;
import javafx.beans.property.SimpleBooleanProperty;
import rule.Rule;
import rule.Termination;

import java.util.Map;
import java.util.Set;

public interface Context {
    Termination getTerminationRules();
    void setContextID(int id);

    Map<Integer, Integer> getDataForGraph();

    float getProgressBarPercent();

    EntityInstance[][] getGrid();

    Set<Rule> getAllRules();
    int getID();
    int getCurrentTick();

    int getSecondsPassed();
    SimpleBooleanProperty isRunning();
    SimpleBooleanProperty isPaused();
    int getThreadsCount();
    Set<String> getAliveEntityNames();
    Set<String> getSpecificEntityProperties(String entityName);
    void stopRun();
    void resumeRun();
    void pauseRun();
    EntityInstance getPrimaryEntityInstance();
    EntityInstance getSecondaryEntityInstance();
    ActiveEnvironment getActiveEnvironment();
    boolean shouldSimulationTerminate(int ticks, int seconds);
    void singleSimulationRun(int ticks);

   // String runSimulation();
    void removeEntity(EntityInstance entityInstance);
    PropertyInstance getEnvironmentVariable(String name);
    void setPrimaryEntityInstance(EntityInstance instance);
    EntityInstanceManager getEntityInstanceManager();
    void setActiveEnvironment(ActiveEnvironment e);
}
