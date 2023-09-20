package execution.context;

import engine.AllData;
import execution.instance.entity.EntityInstance;
import execution.instance.entity.EntityInstanceImpl;
import execution.instance.entity.manager.EntityInstanceManager;
import execution.instance.environment.api.ActiveEnvironment;
import execution.instance.property.PropertyInstance;
import javafx.beans.property.SimpleBooleanProperty;
import rule.Rule;
import rule.Termination;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.*;

public class ContextImpl implements Runnable, Context {

    int contextID;
    private EntityInstance primaryEntityInstance;
    private EntityInstance secondaryEntityInstance;
    private EntityInstanceManager entityInstanceManager;
    private ActiveEnvironment activeEnvironment;
    private Set<Rule> allRules;
    private int currentTick = 0;
    private int secondsPassed = 0;

    private long startTime = 0;
    private long pauseStartTime = 0;
    private long totalPauseTime = 0;

    private long pauseTime = 0;


    private int maxRows, maxCols;
    private final SimpleBooleanProperty keepRunning = new SimpleBooleanProperty(true);
    private float progressBarPercent = 0;
    private final SimpleBooleanProperty paused = new SimpleBooleanProperty(false);
    private final Map<Integer, Integer> aliveEntitiesPerTick = new HashMap<>();

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

    public Set<Rule> getAllRules(){
        return allRules;
    }

    public void setContextID(int id){
        contextID = id;
    }

    public int getID(){return contextID;}

    public EntityInstance[][] getGrid(){return grid;}

    public ActiveEnvironment getActiveEnvironment(){
        return activeEnvironment;
    }

    private void updateProgressPercent(int ticks, int seconds){
        float percent = 0;
        if(terminationRules.getEndByTicks() != -1){
            percent = (float) ticks / terminationRules.getEndByTicks();
        }
        if(terminationRules.getEndBySeconds() != -1){
            percent = Math.max(percent, (float) seconds / terminationRules.getEndBySeconds());
        }

        progressBarPercent = Float.parseFloat(String.format("%.2f", percent*100));
    }

    public boolean shouldSimulationTerminate(int ticks, int seconds){
        updateProgressPercent(ticks, seconds);
        if(terminationRules.getEndBySeconds() != -1 || terminationRules.getEndByTicks() != -1){
            return ticks >= terminationRules.getEndByTicks() || seconds >= terminationRules.getEndBySeconds();
        }
        else{
            return false;
        }
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

    private void sleepForAWhile() throws InterruptedException {
        Thread.sleep(100);
    }

    @Override
    public SimpleBooleanProperty isRunning(){
        return keepRunning;
    }

    @Override
    public void resumeRun() {
        paused.set(false);
        totalPauseTime += System.currentTimeMillis() - pauseStartTime;
    }

    @Override
    public void stopRun(){keepRunning.set(false);}

    @Override
    public void pauseRun(){
        paused.set(true);
        pauseStartTime = System.currentTimeMillis();
    }

    @Override
    public SimpleBooleanProperty isPaused(){
        return paused;
    }

    @Override
    public int getCurrentTick(){
        return currentTick;
    }

    @Override
    public int getSecondsPassed(){return secondsPassed;}

    @Override
    public void run(){
        startTime = System.currentTimeMillis();
        Random random = new Random();
        while(!shouldSimulationTerminate(currentTick, secondsPassed) && keepRunning.get()){
            try{
                if(paused.get()){
                    pauseTime = System.currentTimeMillis();
                    sleepForAWhile();
                }
                else{
                    sleepForAWhile();
                    for(Rule rule : allRules){
                        double randomValue = random.nextDouble();
                        if (rule.getActivation().isActive(currentTick) || randomValue < rule.getActivation().getProb()) {
                            rule.getActionsToPerform().forEach(action -> action.invoke(this));
                        }
                    }
                    updateAliveEntitiesPerTick(currentTick);
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - startTime - totalPauseTime;
                    secondsPassed = (int) (elapsedTime / 1000);
                    currentTick++;
                }
            }catch (InterruptedException exception) {
                keepRunning.set(false);
                exception.printStackTrace();
            }
        }

        keepRunning.set(false);
    }

    private void updateAliveEntitiesPerTick(int currentTick)
    {
        aliveEntitiesPerTick.put(currentTick, entityInstanceManager.getInstances().size());
    }

    @Override
    public float getProgressBarPercent(){
        return progressBarPercent;
    }

    @Override
    public Set<String> getAliveEntityNames(){
        Set<String> entityNames = new HashSet<>();
        for(EntityInstance instance : entityInstanceManager.getInstances())
        {
            String name = instance.getEntityDefinitionName();
            if(!entityNames.contains(name)){
                entityNames.add(name);
            }
        }

        return entityNames;
    }

    @Override
    public Map<Integer, Integer> getDataForGraph(){
        return aliveEntitiesPerTick;
    }

    @Override
    public Set<String> getSpecificEntityProperties(String entityName){
        Set<String> properties = new HashSet<>();
        for(EntityInstance instance : entityInstanceManager.getInstances())
        {
            if(instance.getEntityDefinitionName().equals(entityName)){
                for(PropertyInstance propInstance : instance.getAllPropertyInstances().values())
                {
                    properties.add(propInstance.getPropertyDefinition().getName());
                }

                break;
            }
        }

        return properties;
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
