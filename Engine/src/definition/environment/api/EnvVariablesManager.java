package definition.environment.api;

import definition.property.api.PropertyDefinition;
import execution.instance.environment.api.ActiveEnvironment;

import java.util.Collection;

public interface EnvVariablesManager {
    void addEnvironmentVariable(PropertyDefinition propertyDefinition);
    ActiveEnvironment createActiveEnvironment();
    PropertyDefinition getPropertyDefinitionByName(String name);

    Collection<PropertyDefinition> getEnvVariables();
}
