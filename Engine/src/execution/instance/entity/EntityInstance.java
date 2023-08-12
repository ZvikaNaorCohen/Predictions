package execution.instance.entity;

import execution.instance.property.PropertyInstance;

public interface EntityInstance {
    int getId();
    PropertyInstance getPropertyByName(String name);
    void addPropertyInstance(PropertyInstance propertyInstance);

    boolean hasPropertyByName(String name);
}
