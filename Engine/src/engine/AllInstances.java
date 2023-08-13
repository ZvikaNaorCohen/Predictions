package engine;

import execution.instance.entity.EntityInstance;
import execution.instance.entity.manager.EntityInstanceManager;
import execution.instance.entity.manager.EntityInstanceManagerImpl;
import execution.instance.environment.api.ActiveEnvironment;
import execution.instance.environment.impl.ActiveEnvironmentImpl;
import generated.PRDWorld;
import rule.Rule;
import rule.Termination;

import java.util.Set;

public class AllInstances {
    private Termination terminationRules;
    private EntityInstanceManager allEntities;
    private ActiveEnvironment allEnvironmentProps;

    private Set<Rule> allRules;

    public AllInstances(){
        terminationRules = new Termination(-1, -1);
        allEntities = new EntityInstanceManagerImpl();
        allEnvironmentProps = new ActiveEnvironmentImpl();
    }

    public AllInstances(Termination t, EntityInstanceManager e, ActiveEnvironment a, Set<Rule> ar){
        terminationRules = t;
        allEntities = e;
        allEnvironmentProps = a;
        allRules = ar;
    }

    public Termination getEngineTermination(){
        return terminationRules;
    }

    public EntityInstanceManager getAllEntities(){
        return allEntities;
    }

    public ActiveEnvironment getActiveEnvironment(){
        return allEnvironmentProps;
    }

    public Set<Rule> getAllRules(){return allRules;}
}
