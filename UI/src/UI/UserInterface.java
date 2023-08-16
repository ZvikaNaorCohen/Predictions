package UI;

import DTO.ContextDTO;
import UI.MenuItem.api.PastDetail;
import UI.MenuItem.api.ReadFile;
import UI.MenuItem.api.RunSimulation;
import UI.MenuItem.api.SimulationDetail;
import UI.MenuItem.impl.*;
import engine.AllData;
import execution.context.Context;
import execution.context.ContextImpl;
import generated.PRDWorld;

import javax.xml.bind.JAXBException;

import static UI.UIUtils.*;

public class UserInterface {
    private Context myRunningWorld;
    private ContextDTO runningWorldDTO;

    private AllData allData;
    // Path to valid file: Engine/src/Resources/world.xml
    // Path to invalid file: Engine/src/Resources/ex1-error-4.xml

    public void run() throws JAXBException {
        Boolean run = true;
        while(run){
            String choice = getInputFromUser();
            switch(choice){
                case "1": {
                    ReadFile readFile = new ReadFileConsole();
                    PRDWorld validWorld = readFile.invoke();
                    if(validWorld != null){
                        allData = new AllData(validWorld);
                        runningWorldDTO = new ContextDTO(allData);
                        myRunningWorld = new ContextImpl(allData);
                    }
                    break;
                }
                case "2": {
                    SimulationDetail simulationDetail = new SimulationDetailConsole();
                    simulationDetail.invoke(allData, myRunningWorld);
                    break;
                }
                case "3": {
                    RunSimulation runSimulation = new RunSimulationConsole();
                    // runSimulation.invoke();
                    break;
                }
                case "4": {
                    PastDetail pastDetail = new PastDetailConsole();
                    // pastDetail.invoke();
                    break;
                }
                case "5": {
                    ExitProgram exitProgram = new ExitProgram();
                    exitProgram.invoke();
                    break;
                }
            }
        }
    }
}
