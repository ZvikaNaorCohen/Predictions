package file.read;

import generated.PRDWorld;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class XMLRead {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "generated";
    public static PRDWorld getWorldFromScheme(String path) throws JAXBException {
        try {
            // path = "Engine/src/Resources/world.xml"; // Just for debug
            InputStream inputStream = new FileInputStream(new File(path));
            PRDWorld world = deserializeFrom(inputStream);
            return world;
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }
}
