package UI.MenuItem.impl;

import UI.MenuItem.api.MenuItem;
import UI.UserInterface;

public class SimulationDetail implements MenuItem {
    public void invoke(){
        if(true) {
            printInvalidChoice("Engine is not initialized. Please make sure you successfully " +
                    "read XML file before using this option. ");
        }
        else {
            // UserInterface.printWorld(engine.getMyWorld());
        }
    }

    public void printInvalidChoice(String reason){
        System.out.println(reason);
    }
}
