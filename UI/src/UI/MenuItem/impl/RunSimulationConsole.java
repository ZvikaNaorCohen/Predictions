package UI.MenuItem.impl;

import UI.MenuItem.api.RunSimulation;
import definition.property.api.PropertyDefinition;
import engine.AllData;
import execution.context.Context;
import execution.instance.environment.api.ActiveEnvironment;
import execution.instance.environment.impl.ActiveEnvironmentImpl;
import execution.instance.property.PropertyInstance;
import execution.instance.property.PropertyInstanceImpl;
import file.validate.impl.PRDEnvironmentValidator;
import generated.PRDEntity;
import generated.PRDEnvProperty;
import generated.PRDEvironment;

import java.util.Scanner;

import static engine.AllData.fromPRDToPropEnvDef;

public class RunSimulationConsole implements RunSimulation {
    public void invoke(AllData allData, Context myWorld, PRDEvironment environment){
        if(myWorld == null) {
            printInvalidChoice("Engine is not initialized. Please make sure you successfully " +
                    "read XML file before using this option. ");
        }
        else {
            if (environment.getPRDEnvProperty().size() != 0) {
                System.out.println("In order to run simulation, we need to initialize the environment properties.");
                System.out.println("If you want to random initialize an environment property, simply press enter. (Enter empty string)");
            }

            // Handle environment properties
            ActiveEnvironment activeEnvironment = handleEnvironmentPropertiesUI(environment);
            myWorld.setActiveEnvironment(activeEnvironment);

            // RUN SIMULATIONNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN
        }
    }

    protected ActiveEnvironment handleEnvironmentPropertiesUI(PRDEvironment environment){
        ActiveEnvironment allEnvironmentProps = new ActiveEnvironmentImpl();
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
                                    allEnvironmentProps.addPropertyInstance(new PropertyInstanceImpl(envPropertyDef, envPropertyDef.generateValue()));
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
                                    allEnvironmentProps.addPropertyInstance(new PropertyInstanceImpl(envPropertyDef, envPropertyDef.generateValue()));
                                    break;
                                }
                            } catch (Exception e) {
                                System.out.println("The property: " + prop.getPRDName() + "is numeric. Please enter a number. ");
                            }
                        }
                    } else { // Boolean / String NOT RANDOM
                        PropertyDefinition envPropertyDef = fromPRDToPropEnvDef(prop, userInput, false);
                        allEnvironmentProps.addPropertyInstance(new PropertyInstanceImpl(envPropertyDef, envPropertyDef.generateValue()));
                        break;
                    }
                } else {  // ToRandom
                    PropertyDefinition envPropertyDef = fromPRDToPropEnvDef(prop, userInput, true);
                    allEnvironmentProps.addPropertyInstance(new PropertyInstanceImpl(envPropertyDef, envPropertyDef.generateValue()));
                    break;
                }
            }
        }
        // scanner.close();
        return allEnvironmentProps;
    }

    public void printInvalidChoice(String reason){
        System.out.println(reason);
    }

    public void handleEnvironmentProperties(Context context){
        // Traverse through all environment property, and ask user if he wants to give value to this env.
        // If yes, insert the value and check that it's ok.
        // If not, let it have random value.
    }
}
