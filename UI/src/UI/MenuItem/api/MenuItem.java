package UI.MenuItem.api;

import execution.context.Context;

import javax.xml.bind.JAXBException;

public interface MenuItem {
    void invoke(Context myWorld) throws JAXBException;
    void printInvalidChoice(String reason);
}
