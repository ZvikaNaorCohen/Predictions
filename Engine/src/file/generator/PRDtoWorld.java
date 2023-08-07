package file.generator;

import action.api.Action;
import definition.entity.EntityDefinition;
import definition.entity.EntityDefinitionImpl;
import definition.property.api.AbstractPropertyDefinition;
import definition.property.api.PropertyDefinition;
import definition.property.api.PropertyType;
import execution.instance.property.PropertyInstance;
import execution.instance.property.PropertyInstanceImpl;
import generated.*;
import rule.*;

import java.util.*;

public class PRDtoWorld {

    public static Termination getTerminationRules(PRDTermination inputTermination){
        int seconds = -1;
        int ticks = -1;
        if(inputTermination.getPRDByTicksOrPRDBySecond().get(0) != null) {
            ticks = ((PRDByTicks) inputTermination.getPRDByTicksOrPRDBySecond().get(0)).getCount();
        }
        if(inputTermination.getPRDByTicksOrPRDBySecond().get(1) != null){
            seconds = ((PRDBySecond)inputTermination.getPRDByTicksOrPRDBySecond().get(1)).getCount();
        }

        return new Termination(ticks, seconds);
    }
    public static List<Action> getAllRuleActions(PRDRule rule){
        return null;
    }

    public static Activation getActivationRule(PRDActivation aRule){
        return new ActivationImpl(aRule.getTicks());
    }
    public static Set<Rule> getAllRules(PRDRules schemaRules){
        Set<Rule> generatedRules = new HashSet<Rule>();

        for(PRDRule rule : schemaRules.getPRDRule()){
            String ruleName = rule.getName();
            List<Action> ruleActions = getAllRuleActions(rule);
            Activation activationRule = getActivationRule(rule.getPRDActivation());

            RuleImpl newRule = new RuleImpl(ruleName);
            newRule.setListOfActions(ruleActions);
            newRule.setActivation(activationRule);

            generatedRules.add(newRule);
        }
        return generatedRules;
    }

    public static Map<String, EntityDefinition> getAllEntityDefinitions(PRDEntities entities){
        Map<String, EntityDefinition> myList = new HashMap<>();
        for(PRDEntity entity : entities.getPRDEntity()){
            EntityDefinitionImpl entityImpl = new EntityDefinitionImpl(entity.getName(), entity.getPRDPopulation());
            for(PRDProperty prop : entity.getPRDProperties().getPRDProperty()){
                // WHAT DO I DO HERE? I want all properties
            }
            myList.put(entityImpl.getName(), entityImpl);
        }
        return myList;
    }
}
