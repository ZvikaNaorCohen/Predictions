package action.api;

import definition.entity.EntityDefinition;
import execution.context.Context;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractConditionAction extends AbstractAction{
    protected Boolean conditionReturnValue;
    protected List<Action> listOfElseActions = new ArrayList<>();
    protected List<Action> listOfThenActions = new ArrayList<>();
    protected AbstractConditionAction(ActionType actionType, EntityDefinition entityDefinition) {
        super(actionType, entityDefinition);
        conditionReturnValue = false;
    }

    public void setConditionReturnValue(Boolean val){conditionReturnValue = val;}
    public Boolean getConditionReturnValue(){return conditionReturnValue;}

    public void addToListOfThen(Action action){listOfThenActions.add(action);}
    public void addToListOfElse(Action action){listOfElseActions.add(action);}
}
