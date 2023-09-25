package action.impl;

import action.api.AbstractAction;
import action.api.Action;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import definition.entity.SecondaryEntityDefinition;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import file.generator.PRDtoWorld;
import function.impl.EnvironmentFunction;
import function.impl.PercentFunction;
import function.impl.RandomFunction;
import function.impl.TicksFunction;
import generated.PRDAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProximityAction extends AbstractAction {
    String inputDepth = "";
    String sourceEntity = "";
    String targetEntity = "";
    float depth = 0;
    List<Action> actionsForProximity = new ArrayList<>();

    public String getSourceName(){
        return sourceEntity;
    }

    public String getTargetName(){
        return targetEntity;
    }

    @Override
    public EntityDefinition getContextEntity() {
        return entityDefinition;
    }

    public ProximityAction(Map<String, EntityDefinition> allEntityDefinitions, ActionType actionType, EntityDefinition entityDefinition, PRDAction action) {
        super(actionType, entityDefinition);
        inputDepth = action.getPRDEnvDepth().getOf();
        sourceEntity = action.getPRDBetween().getSourceEntity();
        targetEntity = action.getPRDBetween().getTargetEntity();
        actionsForProximity = getActionsForProximity(allEntityDefinitions, action);
    }

    private List<Action> getActionsForProximity(Map<String, EntityDefinition> allEntityDefinitions, PRDAction action){
        List<Action> actions = new ArrayList<>();
        for(PRDAction actionForProximity : action.getPRDActions().getPRDAction()){
            actions.add(PRDtoWorld.getActionFromPRDAction(allEntityDefinitions, actionForProximity));
        }

        return actions;
    }

    public void updateDepthOfProximityAction(Context context){
        if(inputDepth.startsWith("environment")){
            depth = (float) new EnvironmentFunction(inputDepth).getPropertyInstanceValueFromEnvironment(context);
        } else if(inputDepth.startsWith("random")){
            depth = new RandomFunction(inputDepth).getRandomValue();
        } else if(inputDepth.startsWith("evaluate")){
            depth = (float) getValueFromEvaluate(context, inputDepth);
        } else if(inputDepth.startsWith("percent")){
            depth = new PercentFunction(inputDepth).getPercentFromFunction(context).floatValue();
        } else if (inputDepth.startsWith("ticks")) {
            depth = new TicksFunction(inputDepth).getTicksNotUpdated(context);
        }
        else{
            try{
                depth = Integer.parseInt(inputDepth);
            }
            catch(Exception ignored){ }
            finally{
                depth = 1;
            }
        }
    }

    private int calculateMinimumSteps(int[] coordinate1, int[] coordinate2) {
        int rowDiff = Math.abs(coordinate1[0] - coordinate2[0]);
        int colDiff = Math.abs(coordinate1[1] - coordinate2[1]);

        return Math.max(rowDiff, colDiff);
    }

    private boolean areTwoInstancesClose(EntityInstance source, EntityInstance target){
        int[] cor1 = new int[2];
        int[] cor2 = new int[2];
        cor1[0] = source.getRow();
        cor1[1] = source.getCol();


        cor2[0] = target.getRow();
        cor2[1] = target.getCol();

        return depth > calculateMinimumSteps(cor1, cor2);
    }

    @Override
    public void invoke(Context context) {
        updateDepthOfProximityAction(context);
        EntityInstance source = context.getPrimaryEntityInstance();
        EntityInstance target = context.getSecondaryEntityInstance();
        if(areTwoInstancesClose(source, target)){
            for(Action action : actionsForProximity){
                action.invoke(context);
            }
        }
    }
}
