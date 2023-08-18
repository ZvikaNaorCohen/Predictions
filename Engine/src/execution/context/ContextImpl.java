package execution.context;

import engine.AllData;
import execution.instance.entity.EntityInstance;
import execution.instance.entity.manager.EntityInstanceManager;
import execution.instance.environment.api.ActiveEnvironment;
import execution.instance.property.PropertyInstance;
import rule.Rule;
import rule.Termination;

import java.util.Random;
import java.util.Set;

public class ContextImpl implements Context {

    private EntityInstance primaryEntityInstance;
    private EntityInstanceManager entityInstanceManager;
    private ActiveEnvironment activeEnvironment;
    private Set<Rule> allRules;

    private Termination terminationRules;

    public ContextImpl(AllData definitions) {
        terminationRules = definitions.fromAllDataToAllInstances().getEngineTermination();
        primaryEntityInstance = definitions.fromAllDataToAllInstances().getAllEntities().getInstances().get(0);
        entityInstanceManager = definitions.fromAllDataToAllInstances().getAllEntities();
        allRules = definitions.fromAllDataToAllInstances().getAllRules();
    }

    public ActiveEnvironment getActiveEnvironment(){
        return activeEnvironment;
    }

    public boolean shouldSimulationTerminate(int ticks, int seconds){
        return ticks >= terminationRules.getEndByTicks() || seconds >= terminationRules.getEndBySeconds();
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

    @Override
    public void runSimulation(){
        int ticks = 0;
        int seconds = 1;
        Random random = new Random();
        while(!shouldSimulationTerminate(ticks, seconds)){
            for(Rule rule : allRules){
                Long randomValue = random.nextLong();
                if (rule.getActivation().isActive(ticks) || randomValue < rule.getActivation().getProb()) {
                    rule.getActionsToPerform().forEach(action -> action.invoke(this));
                }
            }
            ticks++;
        }
    }

    @Override
    public void singleSimulationRun(int ticks){

    }


    @Override
    public void setActiveEnvironment(ActiveEnvironment e){
        activeEnvironment = e;
    }

    @Override
    public void setPrimaryEntityInstance(EntityInstance instance){
        primaryEntityInstance = instance;
    }

    public EntityInstanceManager getEntityInstanceManager(){return entityInstanceManager;}

    public Set<Rule> getALlRules(){return allRules;}
}
