package UI.MenuItem.impl;

import DTO.ContextDTO;
import DTO.instance.PropertyInstanceDTO;
import UI.MenuItem.api.RunSimulation;
import definition.environment.api.EnvVariablesManager;
import definition.environment.impl.EnvVariableManagerImpl;
import definition.property.api.PropertyDefinition;
import engine.AllData;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import execution.instance.environment.api.ActiveEnvironment;
import execution.instance.property.PropertyInstance;
import execution.instance.property.PropertyInstanceImpl;
import generated.PRDEnvProperty;
import generated.PRDEvironment;
import java.util.Scanner;
import java.util.Set;

import static engine.AllData.fromPRDToPropEnvDef;
import static java.lang.System.in;

public class RunSimulationConsole implements RunSimulation {
    public void invoke(AllData allData, Context myWorld, PRDEvironment environment, int id) {
        if (myWorld == null) {
            printInvalidChoice("Engine is not initialized. Please make sure you successfully " +
                    "read XML file before using this option. ");
        } else {
            if (environment.getPRDEnvProperty().size() != 0) {
                System.out.println("In order to run simulation, we need to initialize the environment properties.");
                System.out.println("If you want to random initialize an environment property, simply press enter. (Enter empty string)");
            }
            myWorld.setContextID(id);

            // Handle environment properties
            EnvVariablesManager envVariablesManager = new EnvVariableManagerImpl();
            ActiveEnvironment activeEnvironment = handleEnvironmentPropertiesUI(environment, envVariablesManager);
            allData.setEnvVariablesManager(envVariablesManager);
            myWorld.setActiveEnvironment(activeEnvironment);

            ContextDTO worldToPrint = new ContextDTO(allData);
            worldToPrint.updatePropertyInstanceDTO(allData, myWorld);



            printDataBeforeSimulation(worldToPrint);

//            System.out.println("Starting values for entity in position 3 in list: ");
//            EntityInstance e = myWorld.getEntityInstanceManager().getInstances().get(3);
//            System.out.println("Property: lung-cancer-progress: " + e.getPropertyByName("lung-cancer-progress").getValue());
//            System.out.println("Property: cigarets-per-month: " + e.getPropertyByName("cigarets-per-month").getValue());
//            System.out.println("Property: age: " + e.getPropertyByName("age").getValue());

//            System.out.println("Starting values for entity in position 3 in list: ");
//            EntityInstance e = myWorld.getEntityInstanceManager().getInstances().get(3);
//            System.out.println("Property p1 Starting value: " + e.getPropertyByName("p1").getValue() + ". ");
//            System.out.println("Property p2 Starting value: " + e.getPropertyByName("p2").getValue() + ". ");
//            System.out.println("Property p3 Starting value: " + e.getPropertyByName("p3").getValue() + ". ");
//            System.out.println("Property p4 Starting value: " + e.getPropertyByName("p4").getValue() + ". ");
            System.out.println("Number of entities before run: " + myWorld.getEntityInstanceManager().getInstances().size());
            System.out.println("Value of p1 in ent-1 before run: " + myWorld.getEntityInstanceManager().getInstances().get(0).getPropertyByName("p1").getValue());

            myWorld.runSimulation();

            System.out.println("Number of entities after run: " + myWorld.getEntityInstanceManager().getInstances().size());
//            System.out.println("Value of p1 in ent-1 after run: " + myWorld.getEntityInstanceManager().getInstances().get(30).getPropertyByName("p1").getValue());
//            System.out.println("Ending values for entity in position 3 in list: ");
//            e = myWorld.getEntityInstanceManager().getInstances().get(3);
//            System.out.println("Property p1 ending value: " + e.getPropertyByName("p1").getValue() + ". ");
//            System.out.println("Property p2 ending value: " + e.getPropertyByName("p2").getValue() + ". ");
//            System.out.println("Property p3 ending value: " + e.getPropertyByName("p3").getValue() + ". ");
//            System.out.println("Property p4 ending value: " + e.getPropertyByName("p4").getValue() + ". ");

//            System.out.println("Ending values for entity in position 3 in list: ");
//            System.out.println("Property: lung-cancer-progress: " + e.getPropertyByName("lung-cancer-progress").getValue());
//            System.out.println("Property: cigarets-per-month: " + e.getPropertyByName("cigarets-per-month").getValue());
//            System.out.println("Property: age: " + e.getPropertyByName("age").getValue());
        }
    }

    private void printDataBeforeSimulation(ContextDTO myWorld){
        System.out.println("Before we start the simulation, I want to show you the environment properties and their values. ");
        Set<PropertyInstanceDTO> mySet = myWorld.getPropertyInstanceDTOs();
        int counter = 1;
        for(PropertyInstanceDTO propInstance : mySet){
            System.out.println("Property #" + counter + ": \t Name: " + propInstance.getPropertyInstanceDTOName() + ". \t Value: " +
                    propInstance.getPropertyInstanceDTOValue().toString() + ". ");
            counter++;
        }
    }
    protected String printProperty(PRDEnvProperty prop){
        String answer = "\t Name: " + prop.getPRDName() + ". \t type: " + prop.getPRDName();
        if(prop.getPRDRange() != null){
            answer += ".\t From: " + prop.getPRDRange().getFrom() + ".\t To: " + prop.getPRDRange().getTo() + ". ";
        }

        return answer;
    }

    protected boolean envInputChoiceValid(String userInput, int counter){
        try {
            int userChoice = Integer.parseInt(userInput);

            // Check if the choice is within the valid range
            if (userChoice >= 0 && userChoice <= counter) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected void updateEnvPropertyValueFromUserInput(PRDEvironment environment, EnvVariablesManager envVariablesManager, ActiveEnvironment activeEnvironment,
                                                       String userEnvProp) {
        PRDEnvProperty prop = environment.getPRDEnvProperty().get(Integer.parseInt(userEnvProp));
        if (prop.getPRDRange() != null) {
            System.out.println("The property has range. FROM: " + prop.getPRDRange().getFrom() + ". TO: " +
                    prop.getPRDRange().getTo() + ". ");
        }
        Scanner scanner = new Scanner(in);
        while (true) {
            System.out.println("Please enter new value: ");
            String userInput = scanner.nextLine();
            if (prop.getPRDRange() != null) {
                if (prop.getType().equals("decimal")) {
                    try {
                        Integer valueFromInput = Integer.parseInt(userInput);
                        if (valueFromInput < prop.getPRDRange().getFrom() || valueFromInput > prop.getPRDRange().getTo()) {
                            System.out.println("The property: " + prop.getPRDName() + " has range FROM: " + prop.getPRDRange().getFrom() +
                                    " and TO: " + prop.getPRDRange().getTo() + ".\n The value you entered is invalid. ");
                        } else {
                            PropertyDefinition envPropertyDef = fromPRDToPropEnvDef(prop, valueFromInput, false);
                            envVariablesManager.addEnvironmentVariable(envPropertyDef);
                            activeEnvironment.addPropertyInstance(new PropertyInstanceImpl(envPropertyDef, envPropertyDef.generateValue()));
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("The property: " + prop.getPRDName() + "is numeric. Please enter a number. ");
                    }
                } else if (prop.getType().equals("float")) {
                    try {
                        Float valueFromInput = Float.parseFloat(userInput);
                        if (valueFromInput < prop.getPRDRange().getFrom() || valueFromInput > prop.getPRDRange().getTo()) {
                            System.out.println("The property: " + prop.getPRDName() + " has range FROM: " + prop.getPRDRange().getFrom() +
                                    " and TO: " + prop.getPRDRange().getTo() + ".\n The value you entered is invalid. ");
                        } else {
                            PropertyDefinition envPropertyDef = fromPRDToPropEnvDef(prop, valueFromInput, false);
                            envVariablesManager.addEnvironmentVariable(envPropertyDef);
                            activeEnvironment.addPropertyInstance(new PropertyInstanceImpl(envPropertyDef, envPropertyDef.generateValue()));
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("The property: " + prop.getPRDName() + "is numeric. Please enter a number. ");
                    }
                }
            } else { // Boolean / String NOT RANDOM
                PropertyDefinition envPropertyDef = fromPRDToPropEnvDef(prop, userInput, false);
                envVariablesManager.addEnvironmentVariable(envPropertyDef);
                activeEnvironment.addPropertyInstance(new PropertyInstanceImpl(envPropertyDef, envPropertyDef.generateValue()));
                break;
            }
        }
    }


    protected ActiveEnvironment handleEnvironmentPropertiesUI(PRDEvironment environment, EnvVariablesManager envVariablesManager){
        ActiveEnvironment activeEnvironment = envVariablesManager.createActiveEnvironment();
        Scanner scanner = new Scanner(in);
        int counter = 1;
        while(true){
            System.out.println("There are all the environment properties. Choose one to update, or enter 0 to start simulation. ");
            for (PRDEnvProperty prop : environment.getPRDEnvProperty())
            {
                System.out.println("Property #" + counter + ": ");
                System.out.println(printProperty(prop));
            }
            String userInput = scanner.nextLine();
            if(userInput.equals("0")){
                break;
            }
            if(envInputChoiceValid(userInput, counter)){
                updateEnvPropertyValueFromUserInput(environment, envVariablesManager, activeEnvironment, userInput);
            }
            else {
                System.out.println("We received an input that is different than 0 to " + counter + ". Please try again.");
            }
        }

        for(PRDEnvProperty prop : environment.getPRDEnvProperty()){
            try{
                PropertyInstance propInstance = activeEnvironment.getProperty(prop.getPRDName());
            }
            catch(Exception e){ // Property was not found in active env, so need to random the values.
                PropertyDefinition envPropertyDef = fromPRDToPropEnvDef(prop, "", true);
                envVariablesManager.addEnvironmentVariable(envPropertyDef);
                activeEnvironment.addPropertyInstance(new PropertyInstanceImpl(envPropertyDef, envPropertyDef.generateValue()));
            }
        }

        return activeEnvironment;
    }

    public void printInvalidChoice(String reason){
        System.out.println(reason);
    }
}
