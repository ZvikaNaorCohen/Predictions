package UI.MenuItem.impl;

import UI.MenuItem.api.MenuItem;
import UI.MenuItem.api.PastDetail;
import definition.entity.EntityDefinition;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import execution.instance.property.PropertyInstance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.System.in;

public class PastDetailConsole implements PastDetail {
    private Context getContextByID(int id, Map<String, Context> myPastRuns){
        for (Map.Entry<String, Context> entry : myPastRuns.entrySet()){
            if(entry.getValue().getID() == id){
                return entry.getValue();
            }
        }
        return null;
    }

    private String chooseHistogramOrCount(){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Count of Entities");
            System.out.println("2. Property Histogram");
            System.out.print("Please choose an option (1 or 2): ");

            String choice = scanner.nextLine();

            if (choice.equals("1") || choice.equals("2")) {
                return choice;
            } else {
                System.out.println("Invalid input. Please enter 1 or 2.");
            }
        }
    }

    int checkValidInputForPropertyInput(List<PropertyInstance> propertyInstances){
        int userPropertyInputAsInt = -1;
        while(true){
            Scanner scanner = new Scanner(in);
            String userPropertyInput = scanner.nextLine();
            try {
                userPropertyInputAsInt = Integer.parseInt(userPropertyInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and " + propertyInstances.size() + ".");
            }

            if (userPropertyInputAsInt >= 1 && userPropertyInputAsInt <= propertyInstances.size()) {
                return userPropertyInputAsInt;
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and " + propertyInstances.size() + ".");
            }
        }



    }

    private void runHistogram(Context context){
        // Get all unique entity names:
        Set<String> entityNames = new HashSet<>();
        for(EntityInstance instance : context.getEntityInstanceManager().getInstances())
        {
            String name = instance.getEntityDefinitionName();
            if(!entityNames.contains(name)){
                entityNames.add(name);
            }
        }

        // Choose an entity from a list
        System.out.println("There are all the alive entities: ");
        for(String entityName : entityNames){
            System.out.println(entityName);
        }
        Scanner scanner = new Scanner(in);
        do{
            System.out.println("Please enter the name of the entity you want to get information about. Or empty string to quit.");
            String userEntityNameInput = scanner.nextLine();
            if(userEntityNameInput.equals("")){
                break;
            }
            if(!entityNames.contains(userEntityNameInput)){
                System.out.println("Entity name not found. Please try again or enter an empty string to exit. ");
            }
            else {
                // Now choose a property to see values
                System.out.println("For entity: " + userEntityNameInput + " there are properties: ");
                List<PropertyInstance> listOfInstances = new ArrayList<>();
                int counter = 1;
                for(EntityInstance instance : context.getEntityInstanceManager().getInstances()){
                    if(instance.getEntityDefinitionName().equals(userEntityNameInput)){
                        for(PropertyInstance propInstance : instance.getAllPropertyInstances().values()){
                            System.out.println(counter + ". " + propInstance.getPropertyDefinition().getName());
                            counter++;
                            listOfInstances.add(propInstance);
                        }
                        break;
                    }
                }

                System.out.println("Now enter the number for the property you want histogram for: ");
                int userChoice = checkValidInputForPropertyInput(listOfInstances);
                // Now see histogram for the specific property:
                Map<Object, Integer> propertyHistogram = new HashMap<>();

                for (EntityInstance instance : context.getEntityInstanceManager().getInstances()) {
                    String propInstanceName = listOfInstances.get(userChoice-1).getPropertyDefinition().getName();
                    PropertyInstance propInstance = instance.getPropertyByName(propInstanceName);

                    if (instance.getEntityDefinitionName().equals(userEntityNameInput)) {
                        Object propValue = propInstance.getValue();
                        propertyHistogram.put(propValue, propertyHistogram.getOrDefault(propValue, 0) + 1);
                    }
                }
                for (Map.Entry<Object, Integer> entry : propertyHistogram.entrySet()) {
                    Object key = entry.getKey();
                    Integer value = entry.getValue();
                    System.out.println("Property value: " + key + ", count: " + value);
                }

                break;
            }
        } while(true);
    }

    private void runCount(Context context){
        // For every entity, show the count before and after simulation
        Set<String> printedNames = new HashSet<>();
        if(context.getEntityInstanceManager().getInstances().size() == 0){
            System.out.println("No alive entities to show. ");
        }
        for(EntityInstance instance : context.getEntityInstanceManager().getInstances())
        {
            String name = instance.getEntityDefinitionName();
            if(!printedNames.contains(name)){
                printedNames.add(name);
                int alive = context.getEntityInstanceManager().getCurrentAliveEntitiesByName(name);
                System.out.println("Entity name: " + name + ". \t Initialize count: " + instance.getEntityDef().getPopulation()
                + ". Current count: " + alive + ". ");
            }
        }
    }
    public void invoke(Map<String, Context> myPastRuns) {
        if(myPastRuns.size() == 0){
            System.out.println("No past runs found.");
            return;
        }
        printPastRuns(myPastRuns);
        runChoiceForUser(myPastRuns);
    }

    public void printInvalidChoice(String reason){
        System.out.println(reason);
    }

    private void printPastRuns(Map<String, Context> myPastRuns){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss");
        myPastRuns.entrySet()
                .stream()
                .sorted((entry1, entry2) ->
                        LocalDateTime.parse(entry1.getKey(), formatter)
                                .compareTo(LocalDateTime.parse(entry2.getKey(), formatter)))
                .forEach(entry -> {
                    String key = entry.getKey();
                    Context context = entry.getValue();
                    System.out.println("Run #" + context.getID() + ". \tStart time: " + key);
                });
    }

    private void runChoiceForUser(Map<String, Context> myPastRuns)
    {
        do{
            Scanner scanner = new Scanner(in);
            System.out.println("Please choose a valid past run number (1-" + (myPastRuns.size()) + "): ");
            String choice = scanner.nextLine();
            try {
                int selectedOption = Integer.parseInt(choice);
                if (selectedOption >= 1 && selectedOption <= myPastRuns.size()) {
                    Context selectedContext = getContextByID(selectedOption, myPastRuns);
                    String countOrHistogram = chooseHistogramOrCount();
                    // Entity count
                    if(countOrHistogram.equals("1")){
                        runCount(selectedContext);
                    }

                    // Property histogram
                    if(countOrHistogram.equals("2")){
                        runHistogram(selectedContext);
                    }
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a number between 1 and " + myPastRuns.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }


        } while(true);
    }
}
