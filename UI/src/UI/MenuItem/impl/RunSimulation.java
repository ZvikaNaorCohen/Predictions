package UI.MenuItem.impl;

import UI.MenuItem.api.MenuItem;

public class RunSimulation implements MenuItem {
    public void invoke(){
        if(true) {
            printInvalidChoice("Engine is not initialized. Please make sure you successfully " +
                    "read XML file before using this option. ");
        }
    }

    public void printInvalidChoice(String reason){
        System.out.println(reason);
    }
}
