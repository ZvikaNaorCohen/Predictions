package UI;

import javax.xml.bind.JAXBException;

public class UIMain {
    private static UserInterface ui = new UserInterface();
    public static void main(String[] args) throws JAXBException {
        ui.run();
    }
}
