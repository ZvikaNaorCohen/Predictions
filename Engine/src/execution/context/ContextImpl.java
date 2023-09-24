package execution.context;

import action.api.Action;
import action.api.ActionType;
import action.impl.ProximityAction;
import definition.entity.SecondaryEntityDefinition;
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
        entityInstanceManager.killEntity(entityInstance);
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

    private void oldRun(){
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

    private List<Rule> getAllRulesThatShouldWork(){
        List<Rule> allRulesToWork = new ArrayList<>();
        Random random = new Random();
        for(Rule rule : allRules){
            double randomValue = random.nextDouble();
            if (rule.getActivation().isActive(currentTick) || randomValue < rule.getActivation().getProb()) {
                allRulesToWork.add(rule);
            }
        }

        return allRulesToWork;
    }

    private List<Action> getAllActionsThatShouldWorkWithoutKillReplace(List<Rule> allRulesToWork){
        List<Action> allActionsToWork = new ArrayList<>();
        for(Rule rule : allRulesToWork){
            for(Action action : rule.getActionsToPerform()){
                if(!action.getActionType().equals(ActionType.KILL) && !action.getActionType().equals(ActionType.REPLACE)) {
                    allActionsToWork.add(action);
                }
            }
        }

        return allActionsToWork;
    }


    private List<Action> getReplaceOrKillActionsThatShouldWork(List<Rule> allRulesToWork){
        List<Action> allActionsToWork = new ArrayList<>();
        for(Rule rule : allRulesToWork){
            for(Action action : rule.getActionsToPerform()){
                if(action.getActionType().equals(ActionType.KILL) || action.getActionType().equals(ActionType.REPLACE)) {
                    allActionsToWork.add(action);
                }
            }
        }

        return allActionsToWork;
    }

    private List<EntityInstance> getSecondEntitiesToWorkOnForProximity(Action action){
        List<EntityInstance> listToReturn = new ArrayList<>();
        ProximityAction myAction = (ProximityAction) action;
        String targetName = myAction.getTargetName();

        for(EntityInstance instance : entityInstanceManager.getInstances()) {
            if (instance.getEntityDefinitionName().equals(targetName)) {
                listToReturn.add(instance);
            }
        }

        return listToReturn;
    }

    private List<EntityInstance> getSecondEntitiesToWorkOn(Action action){
        if(action.getActionType().equals(ActionType.PROXIMITY)){
            return getSecondEntitiesToWorkOnForProximity(action);
        }
        List<EntityInstance> listToReturn = new ArrayList<>(action.getSecondEntityDefinition().getCount());
        SecondaryEntityDefinition secondaryEntityDefinition = action.getSecondEntityDefinition();
        for(EntityInstance instance : entityInstanceManager.getInstances()){
            if(instance.getEntityDefinitionName().equals(secondaryEntityDefinition.getEntityName())){
                if(listToReturn.size() < secondaryEntityDefinition.getCount()){
                    secondaryEntityDefinition.getActionToPerform().invoke(this);
                    if(secondaryEntityDefinition.getActionToPerform().getConditionReturnValue()){
                        listToReturn.add(instance);
                    }
                }
                else{
                    break;
                }
            }
        }

        return listToReturn;
    }

    private boolean newPositionEmpty(int row, int col){
        if (row >= 0 && row < maxRows && col >= 0 && col < maxCols && grid[row][col] == null) {
            return true;
        }
        return false;
    }

    private List<EntityPosition> fillValidPositions(EntityInstance instance){
        List<EntityPosition> validPositions = new ArrayList<>();
        String[] directions = {"up", "down", "left", "right"};
        int row = instance.getRow();
        int col = instance.getCol();
        for (String direction : directions) {
            if (direction.equals("up")) {
                row = (row - 1 + maxRows) % maxRows;
                if (newPositionEmpty(row, col)) {
                    validPositions.add(new EntityPosition(row, col));
                }
            } else if (direction.equals("down")) {
                row = (row + 1) % maxRows;
                if (newPositionEmpty(row, col)) {
                    validPositions.add(new EntityPosition(row, col));
                }
            } else if (direction.equals("left")) {
                col = (col - 1 + maxCols) % maxCols;
                if (newPositionEmpty(row, col)) {
                    validPositions.add(new EntityPosition(row, col));
                }
            } else if (direction.equals("right")) {
                col = (col + 1) % maxCols;
                if (newPositionEmpty(row, col)) {
                    validPositions.add(new EntityPosition(row, col));
                }
            }
        }

        return validPositions;
    }

    private void moveAllEntities() {
        for (EntityInstance instance : entityInstanceManager.getInstances()) {
            List<EntityPosition> validPositions = fillValidPositions(instance);
            if(!validPositions.isEmpty()){
                int oldRow = instance.getRow();
                int oldCol = instance.getCol();

                grid[oldRow][oldCol] = null;
                Random random = new Random();
                int index = random.nextInt(validPositions.size());

                int newRow = validPositions.get(index).getRow();
                int newCol = validPositions.get(index).getCol();
                instance.setRow(newRow);
                instance.setCol(newCol);
                grid[newRow][newCol] = instance;
            }
        }
    }

    @Override
    public void run(){
        startTime = System.currentTimeMillis();
        while(!shouldSimulationTerminate(currentTick, secondsPassed) && keepRunning.get()){
            try{
                if(paused.get()){
                    pauseTime = System.currentTimeMillis();
                    sleepForAWhile();
                }
                else{
                    sleepForAWhile();
                    // Move all entities
                    moveAllEntities();

                    // Check all rules and check which should work
                    List<Rule> allRulesToWork = getAllRulesThatShouldWork();
                    // put their actions in a list.
                    List<Action> allActionsExceptKillReplace = getAllActionsThatShouldWorkWithoutKillReplace(allRulesToWork);
                    List<Action> allActionsWithKillReplace = getReplaceOrKillActionsThatShouldWork(allRulesToWork);

                    List<EntityInstance> copyOfInstancesFirst = new ArrayList<>(entityInstanceManager.getInstances());
                    for(EntityInstance instance : copyOfInstancesFirst){
                        for(Action action : allActionsExceptKillReplace){
                            if(action.getContextEntity().equals(instance.getEntityDef())){
                                primaryEntityInstance = instance;
                                if(action.hasSecondEntity() || action.getActionType().equals(ActionType.PROXIMITY)){
                                    List<EntityInstance> secondEntitiesToWorkOn = getSecondEntitiesToWorkOn(action);
                                    for(EntityInstance secondEntity : secondEntitiesToWorkOn){
                                        secondaryEntityInstance = secondEntity;
                                        action.invoke(this);
                                    }
                                }
                                else{
                                    secondaryEntityInstance = null;
                                    action.invoke(this);
                                }
                            }
                        }
                    }

                    List<EntityInstance> copyOfInstancesSecond = new ArrayList<>(copyOfInstancesFirst);
                    for(EntityInstance instance : copyOfInstancesSecond){
                        for(Action action : allActionsWithKillReplace){
                            if(action.getContextEntity().equals(instance.getEntityDef())){
                                primaryEntityInstance = instance;
                                action.invoke(this);
                            }
                        }
                    }


                    // Then, when you finished with all the rules, now do the following:
                        // For every entity instance in the world:
                            // 1. Go through all actions
                                // 1.1. If action doesn't work on current entity
                                    //  1.1.1. Go to next action.
                                // 1.2. If action works on current entity:
                                    // 1.2.1. Check if there is second entity for the action.
                                        // 1.2.2. If not, do action on current entity and move on. V
                                        // 1.2.3. If yes, go through all the second entities and put them in a list.
                                        //        Iterate all second entities, and send both of the entities to the action.



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

    class EntityPosition {
        private int row;
        private int col;

        public EntityPosition(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }
}
