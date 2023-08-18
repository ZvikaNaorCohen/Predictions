package UI.MenuItem.impl;

import DTO.ContextDTO;
import UI.MenuItem.api.MenuItem;
import UI.MenuItem.api.SimulationDetail;
import UI.UIUtils;
import engine.AllData;
import execution.context.Context;

public class SimulationDetailConsole implements SimulationDetail {
    public void invoke(ContextDTO myWorld){
        if(myWorld == null) {
            UIUtils.printBadInput("Engine is not initialized. Please make sure you successfully " +
                    "read XML file before using this option. ");
        }
        else {
            System.out.println(myWorld.getWorldData());
        }
    }
}
