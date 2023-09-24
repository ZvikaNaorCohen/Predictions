package action.impl;

import action.api.AbstractConditionAction;
import action.api.Action;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import execution.context.Context;

import java.util.List;

public class MultipleConditions extends AbstractConditionAction {
    List<AbstractConditionAction> innerConditionActions;
    String logical;
    Boolean runThen = false;
    Boolean runningOnSecondEntity = false;

    public MultipleConditions(ActionType actionType, EntityDefinition entityDefinition, String l, List<AbstractConditionAction> innerActions) {
        super(actionType, entityDefinition);
        innerConditionActions = innerActions;
        logical = l;
    }

    @Override
    public void invoke(Context context) {
        for(AbstractConditionAction conditionAction : innerConditionActions){
            conditionAction.setConditionReturnValue(false);
            conditionAction.invoke(context);
        }

        updateRunThenOrNot(context);
        if(!runningOnSecondEntity){
            if(runThen){
                for(Action action : listOfThenActions){
                    action.invoke(context);
                }
            }
            else {
                for(Action action : listOfElseActions){
                    action.invoke(context);
                }
            }
        }

        runThen = false;
    }

    public void updateRunThenOrNot(Context context){
        Boolean val;
        if(logical.equals("and")){
            val = true;
            for(AbstractConditionAction action : innerConditionActions){
                action.invoke(context);
                val = val && action.getConditionReturnValue();
            }

            runThen = val;
        }
        else {
            val = false;
            for(AbstractConditionAction action : innerConditionActions){
                action.invoke(context);
                val = val || action.getConditionReturnValue();
            }

            runThen = val;
        }
    }

    public Boolean getRunThen(){return runThen;}
}
