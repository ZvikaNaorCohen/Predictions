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
import execution.instance.property.PropertyInstanceImpl;
import generated.PRDEnvProperty;
import generated.PRDEvironment;

import javax.swing.text.html.parser.Entity;
import java.util.Scanner;
import java.util.Set;

import static engine.AllData.fromPRDToPropEnvDef;

public class RunSimulationConsole implements RunSimulation {
    public void invoke(AllData allData, Context myWorld, PRDEvironment environment) {
        if (myWorld == null) {
            printInvalidChoice("Engine is not initialized. Please make sure you successfully " +
                    "read XML file before using this option. ");
        } else {
            if (environment.getPRDEnvProperty().size() != 0) {
                System.out.println("In order to run simulation, we need to initialize the environment properties.");
                System.out.println("If you want to random initialize an environment property, simply press enter. (Enter empty string)");
            }

            // Handle environment properties
            EnvVariablesManager envVariablesManager = new EnvVariableManagerImpl();
            ActiveEnvironment activeEnvironment = handleEnvironmentPropertiesUI(environment, envVariablesManager);
            allData.setEnvVariablesManager(envVariablesManager);
            myWorld.setActiveEnvironment(activeEnvironment);

            ContextDTO worldToPrint = new ContextDTO(allData);
            worldToPrint.updatePropertyInstanceDTO(allData, myWorld);



            printDataBeforeSimulation(worldToPrint);

            System.out.println("Starting values for entity in position 3 in list: ");
            EntityInstance e = myWorld.getEntityInstanceManager().getInstances().get(3);
            System.out.println("Property: lung-cancer-progress: " + e.getPropertyByName("lung-cancer-progress").getValue());
            System.out.println("Property: cigarets-per-month: " + e.getPropertyByName("cigarets-per-month").getValue());
            System.out.println("Property: age: " + e.getPropertyByName("age").getValue());


            myWorld.runSimulation();

            System.out.println("Ending values for entity in position 3 in list: ");
            System.out.println("Property: lung-cancer-progress: " + e.getPropertyByName("lung-cancer-progress").getValue());
            System.out.println("Property: cigarets-per-month: " + e.getPropertyByName("cigarets-per-month").getValue());
            System.out.println("Property: age: " + e.getPropertyByName("age").getValue());

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

    protected ActiveEnvironment handleEnvironmentPropertiesUI(PRDEvironment environment, EnvVariablesManager envVariablesManager){
        ActiveEnvironment activeEnvironment = envVariablesManager.createActiveEnvironment();
        Scanner scanner = new Scanner(System.in);
        for (PRDEnvProperty prop : environment.getPRDEnvProperty()) {
            while (true) {
                System.out.println("Now we want to take care of environment property: " + prop.getPRDName() + ". ");
                System.out.println("The type of the environment property is: " + prop.getType());
                if (prop.getPRDRange() != null){
                    System.out.println("The property has range. FROM: " + prop.getPRDRange().getFrom() + ". TO: " +
                            prop.getPRDRange().getTo() + ". ");
                }
                String userInput = scanner.nextLine();
                if (!userInput.isEmpty()) { // Check if user input is not empty
                    if (prop.getPRDRange() != null) {
                        if(prop.getType().equals("decimal")){
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
                        }
                        else if (prop.getType().equals("float")) {
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
                } else {  // ToRandom
                    PropertyDefinition envPropertyDef = fromPRDToPropEnvDef(prop, userInput, true);
                    envVariablesManager.addEnvironmentVariable(envPropertyDef);
                    activeEnvironment.addPropertyInstance(new PropertyInstanceImpl(envPropertyDef, envPropertyDef.generateValue()));
                    break;
                }
            }
        }
        // scanner.close();
        return activeEnvironment;
    }

    public void printInvalidChoice(String reason){
        System.out.println(reason);
    }
}
