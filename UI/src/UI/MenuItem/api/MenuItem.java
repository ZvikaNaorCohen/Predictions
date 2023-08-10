package UI.MenuItem.api;

import javax.xml.bind.JAXBException;

public interface MenuItem {
    void invoke() throws JAXBException;
    void printInvalidChoice(String reason);
}
