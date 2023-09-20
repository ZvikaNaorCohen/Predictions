package definition.entity;

import action.api.AbstractConditionAction;
import action.impl.ConditionAction;
import generated.PRDAction;

public class SecondaryEntityDefinition{
    String entityName;
    int count;
    AbstractConditionAction actionToPerform;

    public SecondaryEntityDefinition(PRDAction.PRDSecondaryEntity secondEntity){
        entityName = secondEntity.getEntity();
        count = Integer.parseInt(secondEntity.getPRDSelection().getCount());
    }

    public void setActionToPerform(AbstractConditionAction action){
        actionToPerform = action;
    }
}
