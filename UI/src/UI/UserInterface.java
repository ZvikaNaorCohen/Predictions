package UI;

import UI.MenuItem.api.PastDetail;
import UI.MenuItem.api.ReadFile;
import UI.MenuItem.api.RunSimulation;
import UI.MenuItem.impl.ExitProgram;
import UI.MenuItem.impl.PastDetailConsole;
import UI.MenuItem.impl.ReadFileConsole;
import UI.MenuItem.impl.RunSimulationConsole;
import execution.context.Context;

import javax.xml.bind.JAXBException;

import static UI.UIUtils.*;

public class UserInterface {
    private Context myRunningWorld;

    public void run() throws JAXBException {
//        PRDWorld prdWorld = getWorldFromScheme("");
//        myRunningWorld = new ContextImpl(new AllData(prdWorld));



        Boolean run = true;
        while(run){
            String choice = getInputFromUser();
            switch(choice){
                case "1": {
                    ReadFile readFile = new ReadFileConsole();
                    myRunningWorld = readFile.invoke();
                    break;
                }
                case "2": {
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
