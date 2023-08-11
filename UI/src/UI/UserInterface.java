package UI;

import UI.MenuItem.api.MenuItem;
import engine.AllData;
import execution.context.Context;
import execution.context.ContextImpl;
import execution.instance.entity.EntityInstance;
import execution.instance.entity.manager.EntityInstanceManager;
import execution.instance.environment.api.ActiveEnvironment;
import generated.PRDWorld;
import rule.Termination;

import javax.xml.bind.JAXBException;

import static UI.UIUtils.*;
import static file.read.XMLRead.getWorldFromScheme;

public class UserInterface {
    private Context myRunningWorld;

    public void run() throws JAXBException {
        PRDWorld prdWorld = getWorldFromScheme("");
        myRunningWorld = new ContextImpl(new AllData(prdWorld));



        Boolean run = true;
        while(run){
            MenuItem choice = getInputFromUser();
            choice.invoke();
        }
    }
}
