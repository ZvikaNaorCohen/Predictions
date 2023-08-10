package execution.instance.environment.api;

import execution.instance.property.PropertyInstance;

public interface ActiveEnvironment { // משתני הסביבה שרלוונטיים להרצה הנוכחית
    PropertyInstance getProperty(String name);
    void addPropertyInstance(PropertyInstance propertyInstance);
}
