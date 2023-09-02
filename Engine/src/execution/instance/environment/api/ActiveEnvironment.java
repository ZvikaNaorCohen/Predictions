package execution.instance.environment.api;

import execution.instance.property.PropertyInstance;

import java.util.Map;

public interface ActiveEnvironment { // משתני הסביבה שרלוונטיים להרצה הנוכחית
    PropertyInstance getProperty(String name);
    Map<String, PropertyInstance> getEnvVariables();
    void addPropertyInstance(PropertyInstance propertyInstance);
}
