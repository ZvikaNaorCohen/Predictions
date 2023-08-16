package UI.MenuItem.api;

import execution.context.Context;
import generated.PRDWorld;

import javax.xml.bind.JAXBException;

public interface ReadFile {

    PRDWorld invoke() throws JAXBException;
}
