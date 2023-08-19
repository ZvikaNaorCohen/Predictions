package file.generator;

import action.api.AbstractConditionAction;
import action.api.Action;
import action.api.ActionType;
import action.impl.*;
import definition.entity.EntityDefinition;
import definition.entity.EntityDefinitionImpl;
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
import generated.*;
import rule.*;

import java.util.*;
import java.util.concurrent.locks.Condition;

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
    private static Action getActionFromPRDAction(Map<String, EntityDefinition> allEntityDefinitions, PRDAction action){
        EntityDefinition entityDef = allEntityDefinitions.get(action.getEntity());
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
                    return new MultiplyAction(entityDef, arg1, arg2, action.getResultProp());
                }
                else {
                    String arg1 = action.getPRDDivide().getArg1();
                    String arg2 = action.getPRDDivide().getArg2();
                    return new DivideAction(entityDef, arg1, arg2, action.getResultProp());
                }
            }

            case "condition":{
                PRDCondition condition = action.getPRDCondition();
                AbstractConditionAction tempAction;
                if(condition.getSingularity().equals("single")){
                    String prop = condition.getProperty();
                    String oper = condition.getOperator();
                    String val = condition.getValue();
                    tempAction = new ConditionAction(ActionType.SINGLECONDITION, entityDef, prop, oper, val);
                }
                else {
                    tempAction = getMultipleConditionAction(entityDef, condition);
                }

                if(action.getPRDThen() != null){
                    for(PRDAction thenAction : action.getPRDThen().getPRDAction()){
                        tempAction.addToListOfThen(getActionFromPRDAction(allEntityDefinitions, thenAction));
                    }
                }
                if(action.getPRDElse() != null){
                    for(PRDAction elseAction : action.getPRDElse().getPRDAction()){
                        tempAction.addToListOfElse(getActionFromPRDAction(allEntityDefinitions, elseAction));
                    }
                }

                return tempAction;
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

    private static AbstractConditionAction getMultipleConditionAction(EntityDefinition entityDef, PRDCondition condition){
        if(condition.getSingularity().equals("single")){
            return new ConditionAction(ActionType.SINGLECONDITION, entityDef, condition.getProperty(), condition.getOperator(), condition.getValue());
        }
        else {
            List<AbstractConditionAction> innerConditionActions = new ArrayList<>();
            for(PRDCondition inner : condition.getPRDCondition()){
                if(inner.getSingularity().equals("single")){
                    String prop = inner.getProperty();
                    String oper = inner.getOperator();
                    String val = inner.getValue();
                    innerConditionActions.add(new ConditionAction(ActionType.SINGLECONDITION, entityDef, prop, oper, val));
                }
                else {
                    innerConditionActions.add(getMultipleConditionAction(entityDef, inner));
                }
            }
            return new MultipleConditions(ActionType.MULTIPLECONDITION, entityDef, condition.getLogical(), innerConditionActions);
        }
    }

    public static List<Action> getAllRuleActions(Map<String, EntityDefinition> allEntityDefinitions, PRDRule rule){
        List<Action> listOfActions = new ArrayList<>();
        for(PRDAction action : rule.getPRDActions().getPRDAction()){
            Action newAction = getActionFromPRDAction(allEntityDefinitions, action);
            listOfActions.add(newAction);
        }
        return listOfActions;
    }

    public static Activation getActivationRule(PRDActivation aRule){
        if(aRule == null){
            return new ActivationImpl();
        }
        else{
            if(aRule.getTicks() == null){
                return new ActivationImpl(aRule.getProbability());
            } else if (aRule.getProbability() == null){
                return new ActivationImpl(aRule.getTicks());
            }
            else {
                return new ActivationImpl(aRule.getTicks(), aRule.getProbability());
            }
        }
    }
    public static Set<Rule> getAllRules(Map<String, EntityDefinition> allEntityDefinitions, PRDRules schemaRules){
        Set<Rule> generatedRules = new HashSet<Rule>();

        for(PRDRule rule : schemaRules.getPRDRule()){
            String ruleName = rule.getName();
            List<Action> ruleActions = getAllRuleActions(allEntityDefinitions, rule);
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
                int from = (int) prop.getPRDRange().getFrom();
                int to = (int) prop.getPRDRange().getTo();
                if (prop.getPRDValue().isRandomInitialize()) {
                    newProp = new IntegerPropertyDefinition(prop.getPRDName(), new RandomIntegerGenerator(from, to), from, to);
                } else {
                    int fixedValue = Integer.parseInt(prop.getPRDValue().getInit());
                    newProp = new IntegerPropertyDefinition(prop.getPRDName(), new FixedValueGenerator<>(fixedValue), from, to);
                }
                break;
            }
            case "float": {
                float from = (float) prop.getPRDRange().getFrom();
                float to = (float) prop.getPRDRange().getTo();
                if (prop.getPRDValue().isRandomInitialize()) {
                    newProp = new FloatPropertyDefinition(prop.getPRDName(), new RandomFloatGenerator(from, to), from, to);
                } else {
                    float fixedValue = Float.parseFloat(prop.getPRDValue().getInit());
                    newProp = new FloatPropertyDefinition(prop.getPRDName(), new FixedValueGenerator<>(fixedValue), from, to);
                }
                break;
            }
            case "string": {
                if (prop.getPRDValue().isRandomInitialize()) {
                    newProp = new StringPropertyDefinition(prop.getPRDName(), new RandomStringGenerator(), 0, 0);
                } else {
                    String fixedValue = (prop.getPRDValue().getInit());
                    newProp = new StringPropertyDefinition(prop.getPRDName(), new FixedValueGenerator<>(fixedValue),0 ,0);
                }
                break;
            }
            case "boolean": {
                if (prop.getPRDValue().isRandomInitialize()) {
                    newProp = new BooleanPropertyDefinition(prop.getPRDName(), new RandomBooleanValueGenerator(), 0,0);
                } else {
                    boolean fixedValue = Boolean.parseBoolean(prop.getPRDValue().getInit());
                    newProp = new BooleanPropertyDefinition(prop.getPRDName(), new FixedValueGenerator<>(fixedValue),0 ,0);
                }
                break;
            }
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
}
