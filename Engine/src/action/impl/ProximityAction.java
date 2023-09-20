package action.impl;

import action.api.AbstractAction;
import action.api.Action;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import function.impl.EnvironmentFunction;
import function.impl.PercentFunction;
import function.impl.RandomFunction;
import function.impl.TicksFunction;
import generated.PRDAction;

import java.util.ArrayList;
import java.util.List;

public class ProximityAction extends AbstractAction {
    String inputDepth = "";
    String sourceEntity = "";
    String targetEntity = "";
    double depth = 0;
    List<Action> actionsForProximity = new ArrayList<>();

    public ProximityAction(ActionType actionType, EntityDefinition entityDefinition, PRDAction action) {
        super(actionType, entityDefinition);
        inputDepth = action.getPRDEnvDepth().getOf();
        sourceEntity = action.getPRDBetween().getSourceEntity();
        targetEntity = action.getPRDBetween().getTargetEntity();
    }

    public void updateDepthOfProximityAction(Context context){
        if(inputDepth.startsWith("environment")){
            depth = (double) new EnvironmentFunction(inputDepth).getPropertyInstanceValueFromEnvironment(context);
        } else if(inputDepth.startsWith("random")){
            depth = new RandomFunction(inputDepth).getRandomValue();
        } else if(inputDepth.startsWith("evaluate")){
            depth = (double) getValueFromEvaluate(context, inputDepth);
        } else if(inputDepth.startsWith("percent")){
            depth = new PercentFunction(inputDepth).getPercentFromFunction(context);
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

    private boolean areTwoInstancesClose(Context context, EntityInstance source, EntityInstance target){
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
        EntityInstance source = context.getPrimaryEntityInstance();
        EntityInstance target = context.getSecondaryEntityInstance();
        if(areTwoInstancesClose(context, source, target)){
            // do the actions
        }
    }
}
