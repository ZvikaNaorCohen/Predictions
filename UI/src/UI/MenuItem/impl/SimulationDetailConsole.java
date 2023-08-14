package UI.MenuItem.impl;

import UI.MenuItem.api.MenuItem;
import execution.context.Context;

public class SimulationDetailConsole implements MenuItem {
    public void invoke(Context myWorld){
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
