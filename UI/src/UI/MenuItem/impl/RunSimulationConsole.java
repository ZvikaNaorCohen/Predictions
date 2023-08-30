package UI.MenuItem.impl;

import DTO.ContextDTO;
import DTO.instance.PropertyInstanceDTO;
import UI.MenuItem.api.RunSimulation;
import definition.environment.api.EnvVariablesManager;
import definition.environment.impl.EnvVariableManagerImpl;
import definition.property.api.PropertyDefinition;
import engine.AllData;
import execution.context.Context;
import execution.instance.environment.api.ActiveEnvironment;
import execution.instance.property.PropertyInstance;
import execution.instance.property.PropertyInstanceImpl;
import generated.PRDEnvProperty;
import generated.PRDEnvironment;
import generated.PRDEvironment;

import java.util.List;
import java.util.Scanner;

import static engine.AllData.fromPRDToPropEnvDef;
import static java.lang.System.in;

public class RunSimulationConsole implements RunSimulation {
    public void invoke(AllData allData, Context myWorld, PRDEnvironment environment, int id) {
        if (myWorld == null) {
            printInvalidChoice("Engine is not initialized. Please make sure you successfully " +
                    "read XML file before using this option. ");
        } else {
            if (environment.getPRDEnvProperty().size() != 0) {
                System.out.println("In order to run simulation, we need to initialize the environment properties.");
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

            String endReason = myWorld.runSimulation();
            System.out.println("End reason: " + endReason);
            System.out.println("Simulation ID: " + myWorld.getID());
        }
    }

    private void printDataBeforeSimulation(ContextDTO myWorld){
        System.out.println("Before we start the simulation, I want to show you the environment properties and their values. ");
        List<PropertyInstanceDTO> myList = myWorld.getPropertyInstanceDTOs();
        for(int counter = 1; counter <= myList.size(); counter++){
            PropertyInstanceDTO propInstance = myList.get(counter-1);
            System.out.println("Property #" + counter + ": \t Name: " + propInstance.getPropertyInstanceDTOName() + ". \t Value: " +
                    propInstance.getPropertyInstanceDTOValue().toString() + ". ");
        }
    }
    protected String printProperty(PRDEnvProperty prop){
        String answer = "\t Name: " + prop.getPRDName() + ". \t type: " + prop.getType();
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

    protected void updateEnvPropertyValueFromUserInput(PRDEnvironment environment, EnvVariablesManager envVariablesManager, ActiveEnvironment activeEnvironment,
                                                       String userEnvProp) {
        PRDEnvProperty prop = environment.getPRDEnvProperty().get(Integer.parseInt(userEnvProp)-1);
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
            } else if(prop.getType().equals("boolean")) {
                if(!userInput.equals("true") && !userInput.equals("false")){
                    System.out.println("The property " + prop.getPRDName() + " is type boolean. Please enter the " +
                            "word 'false' or word 'true'");
                }
                else {
                    PropertyDefinition envPropertyDef = fromPRDToPropEnvDef(prop, userInput, false);
                    envVariablesManager.addEnvironmentVariable(envPropertyDef);
                    activeEnvironment.addPropertyInstance(new PropertyInstanceImpl(envPropertyDef, envPropertyDef.generateValue()));
                    break;
                }
            }
            else{// Boolean / String NOT RANDOM
                PropertyDefinition envPropertyDef = fromPRDToPropEnvDef(prop, userInput, false);
                envVariablesManager.addEnvironmentVariable(envPropertyDef);
                activeEnvironment.addPropertyInstance(new PropertyInstanceImpl(envPropertyDef, envPropertyDef.generateValue()));
                break;
            }
        }
    }


    protected ActiveEnvironment handleEnvironmentPropertiesUI(PRDEnvironment environment, EnvVariablesManager envVariablesManager){
        ActiveEnvironment activeEnvironment = envVariablesManager.createActiveEnvironment();
        Scanner scanner = new Scanner(in);
        while(true){
            System.out.println("There are all the environment properties. Choose one to update, or enter 0 to start simulation. ");
            int counter = 1;
            for (PRDEnvProperty prop : environment.getPRDEnvProperty())
            {
                System.out.println("Property #" + counter + ": ");
                System.out.println(printProperty(prop));
                counter++;
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
