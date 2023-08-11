package UI.MenuItem.impl;

import UI.MenuItem.api.MenuItem;
import UI.UIUtils;
import engine.AllData;
import engine.AllInstances;
import file.generator.PRDtoWorld;
import generated.PRDWorld;
import rule.Termination;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.Scanner;

import static file.generator.PRDtoWorld.*;
import static file.read.XMLRead.getWorldFromScheme;

public class ReadFile implements MenuItem {
    public String getPathFromUser(){
        Scanner scanner = new Scanner(System.in);
        String path = "";
        while(true){
            System.out.println("Please enter the full path to the XML file. \nFor example: C:\\Temp\\ex1.xml");
            path = scanner.nextLine();
            path = path.trim();
            File file = new File(path);
            if(!file.exists()){
                UIUtils.printBadInput("File not found.");
            }
            else {
                return path;
            }
        }
    }
    public void invoke() throws JAXBException {
        String path = getPathFromUser();
        // For testing:
        path = "Engine/src/Resources/world.xml";
        // File file = new File(path);
        PRDWorld inputWorld = getWorldFromScheme(path);
        AllData myDefinitions = new AllData(inputWorld);
        AllInstances myInstances = myDefinitions.fromAllDataToAllInstances();

    }

    public void printInvalidChoice(String reason){
        System.out.println(reason);
    }
}
