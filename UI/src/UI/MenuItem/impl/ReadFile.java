package UI.MenuItem.impl;

import UI.MenuItem.api.MenuItem;
import UI.UIUtils;

import java.io.File;
import java.util.Scanner;

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
    public void invoke(){
        String path = getPathFromUser();
        File file = new File(path);

    }

    public void printInvalidChoice(String reason){
        System.out.println(reason);
    }
}
