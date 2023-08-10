package engine;

import definition.entity.EntityDefinition;
import definition.property.api.PropertyDefinition;
import file.generator.PRDtoWorld;
import generated.PRDWorld;
import rule.Rule;
import rule.Termination;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static file.generator.PRDtoWorld.*;

public class AllData {
    Termination terminationRules;
    Map<String, EntityDefinition> allEntityDefinitions;
    Set<Rule> allRules;

    public AllData(PRDWorld oldWorld){
        terminationRules = getTerminationRules(oldWorld.getPRDTermination());
        allEntityDefinitions = getAllEntityDefinitions(oldWorld.getPRDEntities());
        allRules = getAllRules(oldWorld.getPRDRules());
    }

}
