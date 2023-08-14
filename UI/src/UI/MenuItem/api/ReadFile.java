package UI.MenuItem.api;

import execution.context.Context;

import javax.xml.bind.JAXBException;

public interface ReadFile {

    Context invoke() throws JAXBException;
}
