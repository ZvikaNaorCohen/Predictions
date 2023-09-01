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

    int contextID;
    private EntityInstance primaryEntityInstance;
    private EntityInstance secondaryEntityInstance;
    private EntityInstanceManager entityInstanceManager;
    private ActiveEnvironment activeEnvironment;
    private Set<Rule> allRules;

    private int maxRows, maxCols;

    private Termination terminationRules;

    private EntityInstance[][] grid;

    public ContextImpl(AllData definitions) {
        maxRows = definitions.getMaxRows();
        maxCols = definitions.getMaxCols();
        terminationRules = definitions.fromAllDataToAllInstances().getEngineTermination();
        primaryEntityInstance = definitions.fromAllDataToAllInstances().getAllEntities().getInstances().get(0);
        entityInstanceManager = definitions.fromAllDataToAllInstances().getAllEntities();
        allRules = definitions.fromAllDataToAllInstances().getAllRules();
        grid = definitions.getGrid();
    }

    public void setContextID(int id){
        contextID = id;
    }

    public int getID(){return contextID;}

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
    public EntityInstance getSecondaryEntityInstance(){
        return secondaryEntityInstance;
    }

    @Override
    public void removeEntity(EntityInstance entityInstance) {
        entityInstanceManager.killEntity(entityInstance.getEntityDefinitionName());
    }

    @Override
    public PropertyInstance getEnvironmentVariable(String name) {
        return activeEnvironment.getProperty(name);
    }

    @Override
    public String runSimulation(){
        int ticks = 0;
        int seconds = 0;
        long startTime = System.currentTimeMillis();
        Random random = new Random();
        while(!shouldSimulationTerminate(ticks, seconds)){
            for(Rule rule : allRules){
                double randomValue = random.nextDouble();
                if (rule.getActivation().isActive(ticks) || randomValue < rule.getActivation().getProb()) {
                    rule.getActionsToPerform().forEach(action -> action.invoke(this));
                }
            }
            long currentTime = System.currentTimeMillis();
            seconds = (int) ((currentTime - startTime) / 1000);
            ticks++;
        }
        if(ticks >= terminationRules.getEndByTicks()){
            return "ticks";
        }
        else{
            return "seconds";
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
