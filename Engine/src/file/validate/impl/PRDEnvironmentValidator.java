package file.validate.impl;

import generated.PRDEnvProperty;
import generated.PRDEnvironment;
import generated.PRDEvironment;
import java.util.HashSet;
import java.util.Set;

public class PRDEnvironmentValidator{
    Set<String> uniqueNames = new HashSet<>();
    public boolean isEnvironmentValid(PRDEnvironment environment) {
        uniqueNames.clear();
        for (PRDEnvProperty envProperty : environment.getPRDEnvProperty()) {
            String propertyName = envProperty.getPRDName();
            if (uniqueNames.contains(propertyName)) {
                return false;
            }
            uniqueNames.add(propertyName);
        }

        return true;
    }
}
