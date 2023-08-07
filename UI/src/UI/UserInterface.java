package UI;

import UI.MenuItem.api.MenuItem;
import file.generator.PRDtoWorld;
import generated.PRDWorld;
import rule.Termination;

import javax.xml.bind.JAXBException;

import static UI.UIUtils.*;
import static file.read.XMLRead.getWorldFromScheme;

public class UserInterface {
    // private Engine myEngine = new Engine();

    public void run() throws JAXBException {
        PRDWorld prdWorld = getWorldFromScheme("");
        Termination test = PRDtoWorld.getTerminationRules(prdWorld.getPRDTermination());
        // World myWorld = UIUtils.fromGeneratedToWorld(prdWorld);

        Boolean run = true;
        while(run){
            MenuItem choice = getInputFromUser();
            choice.invoke();
        }
    }
}
