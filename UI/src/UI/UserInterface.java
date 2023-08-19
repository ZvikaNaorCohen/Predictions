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
import java.time.format.DateTimeFormatter;

import javax.xml.bind.JAXBException;

import java.time.LocalDateTime;
import java.util.*;

import static UI.UIUtils.*;

public class UserInterface {
    private Context myRunningWorld;
    private ContextDTO runningWorldDTO;
    private PRDWorld oldWorld;

    private AllData allData;

    private Map<String, Context> pastRuns;
    // Path to valid file: Engine/src/Resources/world.xml
    // Path to invalid file: Engine/src/Resources/ex1-error-4.xml

    public void run() throws JAXBException {
        Boolean run = true;
        pastRuns = new HashMap<>();
        while(run){
            String choice = getInputFromUser();
            switch(choice){
                case "1": {
                    ReadFile readFile = new ReadFileConsole();
                    PRDWorld validWorld = readFile.invoke();
                    if(validWorld != null){
                        pastRuns.clear();
                        oldWorld = validWorld;
                        allData = new AllData(validWorld);
                        myRunningWorld = new ContextImpl(allData);
                        runningWorldDTO = new ContextDTO(allData);
                    }
                    break;
                }
                case "2": {
                    SimulationDetail simulationDetail = new SimulationDetailConsole();
                    simulationDetail.invoke(runningWorldDTO);
                    break;
                }
                case "3": {
                    myRunningWorld = new ContextImpl(allData);
                    RunSimulation runSimulation = new RunSimulationConsole();
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss");
                    String formattedDateTime = now.format(formatter);
                    runSimulation.invoke(allData, myRunningWorld, oldWorld.getPRDEvironment(), pastRuns.size()+1);
                    pastRuns.put(formattedDateTime, myRunningWorld);
                    break;
                }
                case "4": {
                    PastDetail pastDetail = new PastDetailConsole();
                    pastDetail.invoke(pastRuns);
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
