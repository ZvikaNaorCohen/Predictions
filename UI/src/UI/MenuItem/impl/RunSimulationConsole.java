package UI.MenuItem.impl;

import UI.MenuItem.api.MenuItem;
import UI.MenuItem.api.RunSimulation;
import execution.context.Context;

public class RunSimulationConsole implements RunSimulation {
    public void invoke(Context myWorld){
        if(true) {
            printInvalidChoice("Engine is not initialized. Please make sure you successfully " +
                    "read XML file before using this option. ");
        }
    }

    public void printInvalidChoice(String reason){
        System.out.println(reason);
    }
}
