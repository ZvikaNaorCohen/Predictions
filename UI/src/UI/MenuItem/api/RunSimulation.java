package UI.MenuItem.api;

import engine.AllData;
import execution.context.Context;
import generated.PRDEvironment;

public interface RunSimulation {
    void invoke(AllData allData, Context myWorld, PRDEvironment environment, int id);
}
