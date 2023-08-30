package UI.MenuItem.api;

import engine.AllData;
import execution.context.Context;
import generated.PRDEnvironment;
import generated.PRDEvironment;

public interface RunSimulation {
    void invoke(AllData allData, Context myWorld, PRDEnvironment environment, int id);
}
