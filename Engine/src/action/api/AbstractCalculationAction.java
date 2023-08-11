package action.api;

import definition.entity.EntityDefinition;
import execution.context.Context;

public class AbstractCalculationAction extends AbstractAction{
    //    private final ActionType actionType;
    //    private final EntityDefinition entityDefinition;
        private final String resultProp;

    protected AbstractCalculationAction(ActionType type, EntityDefinition entityDefinition, String result) {
        super(type, entityDefinition);
        resultProp = result;
    }

    @Override
    public void invoke(Context context) {

    }
}
