package engine;

import definition.entity.EntityDefinition;
import definition.environment.api.EnvVariablesManager;
import definition.environment.impl.EnvVariableManagerImpl;
import definition.property.api.PropertyDefinition;
import definition.property.impl.BooleanPropertyDefinition;
import definition.property.impl.FloatPropertyDefinition;
import definition.property.impl.IntegerPropertyDefinition;
import definition.property.impl.StringPropertyDefinition;
import definition.value.generator.fixed.FixedValueGenerator;
import definition.value.generator.random.impl.bool.RandomBooleanValueGenerator;
import definition.value.generator.random.impl.numeric.RandomFloatGenerator;
import definition.value.generator.random.impl.numeric.RandomIntegerGenerator;
import definition.value.generator.random.impl.string.RandomStringGenerator;
import execution.instance.entity.manager.EntityInstanceManager;
import execution.instance.entity.manager.EntityInstanceManagerImpl;
import execution.instance.environment.api.ActiveEnvironment;
import execution.instance.environment.impl.ActiveEnvironmentImpl;
import generated.PRDEnvProperty;
import generated.PRDEvironment;
import generated.PRDWorld;
import rule.Rule;
import rule.Termination;

import java.util.Collection;
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
        allRules = getAllRules(allEntityDefinitions, oldWorld.getPRDRules());
        envVariablesManager = new EnvVariableManagerImpl();
    }

    public void setEnvVariablesManager(EnvVariablesManager e){
        envVariablesManager = e;
    }

    public EnvVariablesManager getEnvVariablesManager() {return envVariablesManager;}

    public Map<String, EntityDefinition> getMapAllEntities(){return allEntityDefinitions;}
    public Set<Rule> getAllRulesFromAllData(){return allRules;}
    public Termination getTerminationFromAllData(){return  terminationRules;}

    public Collection<PropertyDefinition> getEnvVariables(){return envVariablesManager.getEnvVariables();}

//     public static EnvVariablesManager getAllEnvProperties(PRDEvironment environment){
//        EnvVariablesManager varManager = new EnvVariableManagerImpl();
//        for(int i=0; i<environment.getPRDEnvProperty().size(); i++){
//
//        }
//        for(PRDEnvProperty prop : environment.getPRDEnvProperty()){
//
//            PropertyDefinition newProp = fromPRDToPropEnvDef(prop);
//            varManager.addEnvironmentVariable(newProp);
//        }
//
//      return varManager;
//     }

    public static PropertyDefinition fromPRDToPropEnvDef(PRDEnvProperty prop, Object value, boolean toRandom) {
        PropertyDefinition newProp = null;
        switch (prop.getType()) {
            case "decimal": {
                int from = (int) prop.getPRDRange().getFrom();
                int to = (int) prop.getPRDRange().getTo();
                if(toRandom){
                    newProp = new IntegerPropertyDefinition(prop.getPRDName(), new RandomIntegerGenerator(from, to), from, to);
                }
                else{
                    newProp = new IntegerPropertyDefinition(prop.getPRDName(), new FixedValueGenerator<>((Integer)value), from, to);
                }
            }
            break;
            case "float": {
                float from = (float) prop.getPRDRange().getFrom();
                float to = (float) prop.getPRDRange().getTo();
                if (toRandom) {
                    newProp = new FloatPropertyDefinition(prop.getPRDName(), new RandomFloatGenerator(from, to), from, to);
                } else {
                    newProp = new FloatPropertyDefinition(prop.getPRDName(), new FixedValueGenerator<>((Float) value), from, to);
                }
                break;
            }
            case "string": {
                if(toRandom){
                    newProp = new StringPropertyDefinition(prop.getPRDName(), new RandomStringGenerator(),0 ,0);
                }
                else {
                    newProp = new StringPropertyDefinition(prop.getPRDName(), new FixedValueGenerator<>((String)value),0 ,0);
                }
                break;
            }
            case "boolean": {
                if(toRandom){
                    newProp = new BooleanPropertyDefinition(prop.getPRDName(), new RandomBooleanValueGenerator(),0 ,0);
                }
                else {
                    Boolean realValue = true;
                    if(value.toString().equals("false")){
                        realValue = false;
                    }
                    newProp = new BooleanPropertyDefinition(prop.getPRDName(), new FixedValueGenerator<>(realValue),0,0);
                }
            }
            break;
        }
        return newProp;
    }

        public AllInstances fromAllDataToAllInstances(){
        EntityInstanceManager allEntities = new EntityInstanceManagerImpl();

        for (Map.Entry<String, EntityDefinition> entry : allEntityDefinitions.entrySet()) {
            for(int i=0; i<entry.getValue().getPopulation();i++)
            {
                allEntities.create(entry.getValue());
            }
        }
        return new AllInstances(terminationRules, allEntities, allRules);
    }
}
