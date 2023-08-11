package file.generator;

import action.api.Action;
import action.api.ActionType;
import action.impl.*;
import definition.entity.EntityDefinition;
import definition.entity.EntityDefinitionImpl;
import definition.environment.api.EnvVariablesManager;
import definition.environment.impl.EnvVariableManagerImpl;
import definition.property.api.AbstractPropertyDefinition;
import definition.property.api.PropertyDefinition;
import definition.property.api.PropertyType;
import definition.property.impl.BooleanPropertyDefinition;
import definition.property.impl.FloatPropertyDefinition;
import definition.property.impl.IntegerPropertyDefinition;
import definition.property.impl.StringPropertyDefinition;
import definition.value.generator.fixed.FixedValueGenerator;
import definition.value.generator.random.impl.bool.RandomBooleanValueGenerator;
import definition.value.generator.random.impl.numeric.RandomFloatGenerator;
import definition.value.generator.random.impl.numeric.RandomIntegerGenerator;
import definition.value.generator.random.impl.string.RandomStringGenerator;
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
    private static Action getActionFromPRDAction(EntityDefinition entityDef, PRDAction action){
        switch(action.getType()){
            case "increase":{
                return new IncreaseAction(entityDef, action.getProperty(), action.getBy());
            }

            case "decrease":{
                return new DecreaseAction(entityDef, action.getProperty(), action.getBy());
            }

            case "calculation":{
                if(action.getPRDMultiply() != null){
                    String arg1 = action.getPRDMultiply().getArg1();
                    String arg2 = action.getPRDMultiply().getArg2();
                    return new MultiplyAction(entityDef, arg1, arg2);
                }
                else {
                    String arg1 = action.getPRDDivide().getArg1();
                    String arg2 = action.getPRDDivide().getArg2();
                    return new DivideAction(entityDef, arg1, arg2);
                }
            }

            case "condition":{
                if(action.getPRDCondition().getSingularity().equals("single")){
                    return new SingleCondition(ActionType.SINGLECONDITION, entityDef);
                }
                else {
                    return new MultipleConditions(ActionType.MULTIPLECONDITION, entityDef);
                }
            }

            case "set":{
                return new SetAction(entityDef, action.getProperty(), action.getValue());
            }

            case "kill":{
                return new KillAction(entityDef);
            }
        }
        return null;
    }
    public static List<Action> getAllRuleActions(EntityDefinition entityDef, PRDRule rule){
        List<Action> listOfActions = new ArrayList<>();
        for(PRDAction action : rule.getPRDActions().getPRDAction()){
            Action newAction = getActionFromPRDAction(entityDef, action);
            listOfActions.add(newAction);
        }
        return listOfActions;
    }

    public static Activation getActivationRule(PRDActivation aRule){
        return new ActivationImpl(aRule.getTicks());
    }
    public static Set<Rule> getAllRules(EntityDefinition entityDef, PRDRules schemaRules){
        Set<Rule> generatedRules = new HashSet<Rule>();

        for(PRDRule rule : schemaRules.getPRDRule()){
            String ruleName = rule.getName();
            List<Action> ruleActions = getAllRuleActions(entityDef, rule);
            Activation activationRule = getActivationRule(rule.getPRDActivation());

            RuleImpl newRule = new RuleImpl(ruleName);
            newRule.setListOfActions(ruleActions);
            newRule.setActivation(activationRule);

            generatedRules.add(newRule);
        }
        return generatedRules;
    }

    private static PropertyDefinition fromPRDToPropDef(PRDProperty prop) {
        PropertyDefinition newProp = null;
        switch (prop.getType()) {
            case "decimal": {
                if (prop.getPRDValue().isRandomInitialize()) {
                    int from = (int) prop.getPRDRange().getFrom();
                    int to = (int) prop.getPRDRange().getTo();
                    newProp = new IntegerPropertyDefinition(prop.getPRDName(), new RandomIntegerGenerator(from, to));
                } else {
                    int fixedValue = Integer.parseInt(prop.getPRDValue().getInit());
                    newProp = new IntegerPropertyDefinition(prop.getPRDName(), new FixedValueGenerator<>(fixedValue));
                }
                break;
            }
            case "float": {
                if (prop.getPRDValue().isRandomInitialize()) {
                    float from = (float) prop.getPRDRange().getFrom();
                    float to = (float) prop.getPRDRange().getTo();
                    newProp = new FloatPropertyDefinition(prop.getPRDName(), new RandomFloatGenerator(from, to));
                } else {
                    float fixedValue = Float.parseFloat(prop.getPRDValue().getInit());
                    newProp = new FloatPropertyDefinition(prop.getPRDName(), new FixedValueGenerator<>(fixedValue));
                }
                break;
            }
            case "string": {
                if (prop.getPRDValue().isRandomInitialize()) {
                    newProp = new StringPropertyDefinition(prop.getPRDName(), new RandomStringGenerator());
                } else {
                    String fixedValue = (prop.getPRDValue().getInit());
                    newProp = new StringPropertyDefinition(prop.getPRDName(), new FixedValueGenerator<>(fixedValue));
                }
                break;
            }
            case "boolean": {
                if (prop.getPRDValue().isRandomInitialize()) {
                    newProp = new BooleanPropertyDefinition(prop.getPRDName(), new RandomBooleanValueGenerator());
                } else {
                    boolean fixedValue = Boolean.parseBoolean(prop.getPRDValue().getInit());
                    newProp = new BooleanPropertyDefinition(prop.getPRDName(), new FixedValueGenerator<>(fixedValue));
                }
                break;
            }
        }
        return newProp;
    }

    private static PropertyDefinition fromPRDToPropEnvDef(PRDEnvProperty prop) {
        PropertyDefinition newProp = null;
        switch (prop.getType()) {
            case "decimal": {
                int from = (int) prop.getPRDRange().getFrom();
                int to = (int) prop.getPRDRange().getTo();
                newProp = new IntegerPropertyDefinition(prop.getPRDName(), new RandomIntegerGenerator(from, to));
            }
            break;
            case "float": {
                float from = (float) prop.getPRDRange().getFrom();
                float to = (float) prop.getPRDRange().getTo();
                newProp = new FloatPropertyDefinition(prop.getPRDName(), new RandomFloatGenerator(from, to));
            }
                break;
            case "string": {
                newProp = new StringPropertyDefinition(prop.getPRDName(), new RandomStringGenerator());
                break;
            }
            case "boolean": {
                newProp = new BooleanPropertyDefinition(prop.getPRDName(), new RandomBooleanValueGenerator());
            }
                break;
            }
        return newProp;
    }

    public static Map<String, EntityDefinition> getAllEntityDefinitions(PRDEntities entities) {
        Map<String, EntityDefinition> myList = new HashMap<>();
        for (PRDEntity entity : entities.getPRDEntity()) {
            EntityDefinitionImpl entityImpl = new EntityDefinitionImpl(entity.getName(), entity.getPRDPopulation());
            for (PRDProperty prop : entity.getPRDProperties().getPRDProperty()) {
                PropertyDefinition newProp = fromPRDToPropDef(prop);
                entityImpl.addPropertyDefinition(newProp);
            }
            myList.put(entityImpl.getName(), entityImpl);
        }
        return myList;
    }

    public static EnvVariablesManager getAllEnvProperties(PRDEvironment environment){
        EnvVariablesManager varManager = new EnvVariableManagerImpl();
        for(PRDEnvProperty prop : environment.getPRDEnvProperty()){
            PropertyDefinition newProp = fromPRDToPropEnvDef(prop);
            varManager.addEnvironmentVariable(newProp);
        }

        return varManager;
    }
}
