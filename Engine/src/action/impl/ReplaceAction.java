package action.impl;

import action.api.AbstractAction;
import action.api.Action;
import action.api.ActionType;
import definition.entity.EntityDefinition;
import definition.property.api.PropertyDefinition;
import definition.property.api.PropertyType;
import execution.context.Context;
import execution.instance.entity.EntityInstance;
import execution.instance.entity.EntityInstanceImpl;
import execution.instance.property.PropertyInstance;

import java.util.HashMap;
import java.util.Map;

public class ReplaceAction extends AbstractAction {
    String entityToCreate;
    String mode;

    public ReplaceAction(ActionType actionType, EntityDefinition entityDefinition) {
        super(actionType, entityDefinition);
    }

    public ReplaceAction(ActionType actionType, EntityDefinition entityDefinition, String entity, String m) {
        super(actionType, entityDefinition);
        entityToCreate = entity;
        mode = m;
    }


    @Override
    public void invoke(Context context) {
//        for (EntityInstance instance : context.getEntityInstanceManager().getInstances()) {
//            if (instance.getEntityDefinitionName().equals(entityDefinition.getName())) {
//                context.setPrimaryEntityInstance(instance);
                if (mode.equals("scratch")) {
                    handleScratchMode(context);
                } else {
                    handleDerivedMode(context);
                }
            }
//        }
//    }

    private void handleScratchMode(Context context){
        Action kill = new KillAction(context.getPrimaryEntityInstance().getEntityDef());
        kill.invoke(context);
        context.getEntityInstanceManager().createEntityInstanceByName(entityToCreate);
    }

    private void handleDerivedMode(Context context) {
        EntityDefinition entityDefinitionToKill = context.getPrimaryEntityInstance().getEntityDef();
        EntityDefinition entityDefinitionToCreate = context.getSecondaryEntityInstance().getEntityDef();

        if (!entitiesWithSameProperties(entityDefinitionToKill, entityDefinitionToCreate)) {
            return;
        }

        EntityInstance entityInstanceToLill = context.getPrimaryEntityInstance();

        EntityInstance instanceToCreate = context.getEntityInstanceManager().create(entityDefinitionToCreate, context.getGrid());
        for (Map.Entry<String, PropertyInstance> entry : entityInstanceToLill.getAllPropertyInstances().entrySet()) {
            Object newValue = entry.getValue().getValue();
            instanceToCreate.getPropertyByName(entry.getKey()).updateValue(newValue);
        }

        Action kill = new KillAction(context.getPrimaryEntityInstance().getEntityDef());
        kill.invoke(context);
        context.getEntityInstanceManager().createEntityInstanceByName(entityToCreate);
    }

    private boolean entitiesWithSameProperties(EntityDefinition toKill, EntityDefinition toCreate){
        Map<String, PropertyType> deadMap = new HashMap<>();
        for(PropertyDefinition prop : toKill.getProps()){
            deadMap.put(prop.getName(), prop.getType());
        }

        for(PropertyDefinition prop : toCreate.getProps()){
            String propName = prop.getName();
            PropertyType propType = prop.getType();

            if (deadMap.containsKey(propName)) {
                PropertyType deadPropType = deadMap.get(propName);

                if (propType != deadPropType) {
                    return false;
                }
            } else {
                return false;
            }
        }

        return true;
    }
}
