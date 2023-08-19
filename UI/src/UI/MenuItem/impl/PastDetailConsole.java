package UI.MenuItem.impl;

import UI.MenuItem.api.MenuItem;
import UI.MenuItem.api.PastDetail;
import definition.entity.EntityDefinition;
import execution.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

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

    private void runHistogram(Context context){
        // Choose an entity
    }

    private void runCount(Context context){
        // For every entity, show the count before and after simulation


        // For test:

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
                    Context selectedContext = myPastRuns.get(getContextByID(selectedOption, myPastRuns));
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
