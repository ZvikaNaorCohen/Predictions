package engine;

import definition.entity.EntityDefinition;
import definition.environment.api.EnvVariablesManager;
import definition.property.api.PropertyDefinition;
import execution.instance.entity.manager.EntityInstanceManager;
import execution.instance.entity.manager.EntityInstanceManagerImpl;
import execution.instance.environment.api.ActiveEnvironment;
import execution.instance.environment.impl.ActiveEnvironmentImpl;
import execution.instance.property.PropertyInstance;
import execution.instance.property.PropertyInstanceImpl;
import file.generator.PRDtoWorld;
import generated.PRDWorld;
import rule.Rule;
import rule.Termination;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static file.generator.PRDtoWorld.*;

public class AllData {
    private Termination terminationRules;
    private Map<String, EntityDefinition> allEntityDefinitions;
    private Set<Rule> allRules;
    private EnvVariablesManager envVariablesManager;

    public AllData(PRDWorld oldWorld){
        terminationRules = getTerminationRules(oldWorld.getPRDTermination());
        allEntityDefinitions = getAllEntityDefinitions(oldWorld.getPRDEntities());
        // allRules = getAllRules(oldWorld.getPRDRules());
        envVariablesManager = getAllEnvProperties(oldWorld.getPRDEvironment());
    }

    public AllInstances fromAllDataToAllInstances(){
        EntityInstanceManager allEntities = new EntityInstanceManagerImpl();
        ActiveEnvironment allEnvironmentProps = new ActiveEnvironmentImpl();

        for (Map.Entry<String, EntityDefinition> entry : allEntityDefinitions.entrySet()) {
            for(int i=0; i<entry.getValue().getPopulation();i++)
            {
                allEntities.create(entry.getValue());
            }
        }

        for(PropertyDefinition prop : envVariablesManager.getEnvVariables()){
            PropertyInstance newProp = new PropertyInstanceImpl(prop, prop.generateValue());
            allEnvironmentProps.addPropertyInstance(newProp);
        }


        return new AllInstances(terminationRules, allEntities, allEnvironmentProps);
    }

}
