package definition.entity;

import definition.property.api.PropertyDefinition;

import java.util.List;

public interface EntityDefinition {
    String getName();
    int getPopulation();
    List<PropertyDefinition> getProps();
    PropertyDefinition getPropDefByName(String name);
}
