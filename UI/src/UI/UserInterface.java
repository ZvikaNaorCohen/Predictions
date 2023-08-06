package UI;

import UI.MenuItem.api.MenuItem;
import generated.PRDWorld;

import javax.xml.bind.JAXBException;
import java.util.List;

import static UI.UIUtils.*;

public class UserInterface {
    // private Engine myEngine = new Engine();

    public void run() throws JAXBException {
        PRDWorld prdWorld = UIUtils.getWorldFromScheme();
        // World myWorld = UIUtils.fromGeneratedToWorld(prdWorld);

        Boolean run = true;
        while(run){
            MenuItem choice = getInputFromUser();
            choice.invoke();
        }
    }
}
