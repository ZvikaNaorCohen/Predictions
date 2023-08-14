package UI;

import UI.MenuItem.api.MenuItem;
import UI.MenuItem.impl.*;
import generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import static java.lang.System.in;

public class UIUtils {

    public static void PrintMenu() {
        System.out.println("Please choose an option. Just press the digit of the command you want to use. ");
        System.out.println("1. Read an XML file. ");
        System.out.println("2. Show simulation details. ");
        System.out.println("3. Run Simulation. ");
        System.out.println("4. Show full details of past run. ");
        System.out.println("5. Exit. ");
    }

    public static Boolean validOptionFromMainMenu(String userInput) {
        userInput = userInput.trim();
        return userInput.equals("1") || userInput.equals("2") || userInput.equals("3") || userInput.equals("4") || userInput.equals("5");
    }

    public static void printBadInput(String reason) {
        System.out.println("Your input was incorrect. Reason: " + reason);
    }

    public static String getInputFromUser() {
        Scanner scanner = new Scanner(in);
        do {
            PrintMenu();
            String choice = scanner.nextLine();
            if (!validOptionFromMainMenu(choice)) {
                UIUtils.printBadInput("Invalid option received. Please enter a digit from 1 to 5.");
            } else {
                System.out.println("Running option: " + choice);
                return choice;
            }
        } while (true);
    }

//    public static World fromGeneratedToWorld(PRDWorld prdWorld){
//        World myWorld = new World();
//        extractPRDEnv(prdWorld, myWorld);
//        extractPRDEntities(prdWorld, myWorld);
//        extractPRDRules(prdWorld, myWorld);
//        extractPRDTermination(prdWorld, myWorld);
//
//        return myWorld;
//    }



//    private static void extractPRDEnv(PRDWorld prdWorld, World myWorld) {
//        PRDEvironment tempEnv = prdWorld.getPRDEvironment();
//        // Environment myEnv = new Environment();
//
//        if(validityChecksPRDEnv(tempEnv)){
//            for(PRDEnvProperty prop : tempEnv.getPRDEnvProperty()){
//                String name = prop.getPRDName();
//                double from = prop.getPRDRange().getFrom();
//                double to = prop.getPRDRange().getTo();
//
//                // PropType type = PropType.getPropTypeFromPRDEnvProp(prop);
//                // EnvPropertyDefinition definition = new EnvPropertyDefinition(name, type, from, to);
//                // myEnv.insertNewEnvProperty(definition);
//            }
//        }
//    }

//    private static void extractPRDEntities(PRDWorld prdWorld, World myWorld){
//
//    }
//
//    private static void extractPRDRules(PRDWorld prdWorld, World myWorld){
//
//    }
//
//    private static void extractPRDTermination(PRDWorld prdWorld, World myWorld){
//
//    }

    private static boolean validityChecksPRDEnv(PRDEvironment env){

        return true;
    }

    private static boolean validityChecksPRDEntities(PRDEntities env){

        return true;
    }

    private static boolean validityChecksPRDRules(PRDRules env){

        return true;
    }

    private static boolean validityChecksPRDActions(PRDActions env){

        return true;
    }
}
