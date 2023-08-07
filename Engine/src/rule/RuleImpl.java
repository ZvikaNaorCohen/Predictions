package rule;

import action.api.Action;

import java.util.ArrayList;
import java.util.List;

public class RuleImpl implements Rule {

    private final String name;
    private Activation activation;
    private final List<Action> actions;

    public RuleImpl(String name) {
        this.name = name;
        actions = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Activation getActivation() {
        return activation;
    }

    @Override
    public List<Action> getActionsToPerform() {
        return actions;
    }

    @Override
    public void addAction(Action action) {
        actions.add(action);
    }

    public void setListOfActions(List<Action> inputActions){
        for(Action action : inputActions){
            actions.add(action);
        }
    }

    public void setActivation(Activation temp){
        activation = temp;
    }

    public boolean toRunRule(int tick){
        return activation.isActive(tick);
    }
}
