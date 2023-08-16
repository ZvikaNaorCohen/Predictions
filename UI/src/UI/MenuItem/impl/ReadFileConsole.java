package UI.MenuItem.impl;

import UI.MenuItem.api.ReadFile;
import UI.UIUtils;
import engine.AllData;
import execution.context.Context;
import execution.context.ContextImpl;
import file.validate.impl.PRDWorldValid;
import generated.PRDWorld;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.Scanner;

import static file.read.XMLRead.getWorldFromScheme;

public class ReadFileConsole implements ReadFile {
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
    @Override
    public PRDWorld invoke() throws JAXBException {
        // String path = getPathFromUser();
        // For testing:
        String path = "Engine/src/resources/cigarets1.xml";
        // File file = new File(path);

        PRDWorld inputWorld = getWorldFromScheme(path);
        PRDWorldValid worldValidator = new PRDWorldValid();
        if(!worldValidator.isWorldValid(inputWorld)){
            UIUtils.printBadInput(worldValidator.getErrorMessage());
            return null;
        }

        // return new ContextImpl(new AllData(inputWorld));
        return inputWorld;
    }
}
