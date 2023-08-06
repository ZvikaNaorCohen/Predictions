package definition.environment.impl;

import definition.environment.api.EnvVariablesManager;
import definition.property.api.PropertyDefinition;
import execution.instance.environment.api.ActiveEnvironment;
import execution.instance.environment.impl.ActiveEnvironmentImpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EnvVariableManagerImpl implements EnvVariablesManager {

    private final Map<String, PropertyDefinition> propNameToPropDefinition;

    public EnvVariableManagerImpl() {
        propNameToPropDefinition = new HashMap<>();
    }

    @Override
    public void addEnvironmentVariable(PropertyDefinition propertyDefinition) {
        propNameToPropDefinition.put(propertyDefinition.getName(), propertyDefinition);
    }

    @Override
    public ActiveEnvironment createActiveEnvironment() {
        return new ActiveEnvironmentImpl();
    }

    @Override
    public Collection<PropertyDefinition> getEnvVariables() {
        return propNameToPropDefinition.values();
    }
}
