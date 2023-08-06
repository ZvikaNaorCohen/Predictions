package execution.instance.environment.api;

import execution.instance.property.PropertyInstance;

public interface ActiveEnvironment {
    PropertyInstance getProperty(String name);
    void addPropertyInstance(PropertyInstance propertyInstance);
}
