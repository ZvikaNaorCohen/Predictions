package UI;

import UI.MenuItem.api.MenuItem;
import engine.AllData;
import engine.AllInstances;
import engine.Engine;
import execution.context.Context;
import execution.context.ContextImpl;
import execution.instance.entity.EntityInstance;
import execution.instance.entity.manager.EntityInstanceManager;
import execution.instance.environment.api.ActiveEnvironment;
import file.generator.PRDtoWorld;
import generated.PRDWorld;
import rule.Termination;

import javax.xml.bind.JAXBException;

import static UI.UIUtils.*;
import static file.read.XMLRead.getWorldFromScheme;

public class UserInterface {
    private Engine myEngine = new Engine();
    private Context myRunningWorld;

    public void run() throws JAXBException {
        PRDWorld prdWorld = getWorldFromScheme("");
        AllData myAllData = new AllData(prdWorld);
        Termination tempTerm = myAllData.fromAllDataToAllInstances().getEngineTermination();
        EntityInstance primaryInstance = myAllData.fromAllDataToAllInstances().getAllEntities().getInstances().get(0);
        EntityInstanceManager manager = myAllData.fromAllDataToAllInstances().getAllEntities();
        ActiveEnvironment activeEnv = myAllData.fromAllDataToAllInstances().getActiveEnvironment();
        myEngine.initEngine(myAllData.fromAllDataToAllInstances());
        myRunningWorld = new ContextImpl(tempTerm, primaryInstance, manager, activeEnv);



        Boolean run = true;
        while(run){
            MenuItem choice = getInputFromUser();
            choice.invoke();
        }
    }
}
