package file.validate.impl;

import generated.PRDEntities;
import generated.PRDEntity;
import generated.PRDProperty;
import generated.PRDWorld;

import java.util.HashSet;
import java.util.Set;

public class PRDEntitiesValidator {
    private boolean entityWithUniquePropNames = true;
    private boolean entitiesWithUniqueNames = true;

    public boolean isEntityValidatorValid(PRDWorld world){
        areEntitiesValid(world.getPRDEntities());
        return entitiesWithUniqueNames && entityWithUniquePropNames;
    }

    public void areEntitiesValid(PRDEntities entities){
        entitiesWithUniqueNames = true;
        entityWithUniquePropNames = true;

        entitiesWithUniqueNames = uniqueEntityNames(entities);
        for(PRDEntity entity : entities.getPRDEntity()){
            if(!uniquePropertyNamesForEntity(entity)){
                entityWithUniquePropNames = false;
                break;
            }
        }
    }
    private boolean uniqueEntityNames(PRDEntities entities){
        Set<String> entityNames = new HashSet<>();
        for(PRDEntity entity : entities.getPRDEntity()){
            String entityName = entity.getName();
            if (entityNames.contains(entityName)) {
                return false;
            }
            entityNames.add(entityName);
        }
        return true;
    }
    private boolean uniquePropertyNamesForEntity(PRDEntity entity){
        Set<String> propNames = new HashSet<>();
        for(PRDProperty property : entity.getPRDProperties().getPRDProperty()){
            String propName = property.getPRDName();
            if (propNames.contains(propName)) {
                return false;
            }
            propNames.add(propName);
        }
        return true;
    }
}
