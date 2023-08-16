package DTO.definition;

import action.api.Action;

public class ActionDTO {
    protected Action action;

    public ActionDTO(Action a){
        action = a;
    }

    public String getActionDTOActionTypeName(){return action.getActionType().name();}
}
