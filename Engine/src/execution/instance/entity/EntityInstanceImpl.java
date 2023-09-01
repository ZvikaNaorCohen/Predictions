package execution.instance.entity;

import definition.entity.EntityDefinition;
import execution.instance.property.PropertyInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EntityInstanceImpl implements EntityInstance {

    private final EntityDefinition entityDefinition;
    private final int id;

    private int row, col;
    private Map<String, PropertyInstance> properties;

    public EntityInstanceImpl(EntityDefinition entityDefinition, int id, int newRow, int newCol) {
        this.entityDefinition = entityDefinition;
        this.id = id;
        properties = new HashMap<>();
        row = newRow;
        col = newCol;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getCol() {
        return col;
    }

    @Override
    public void setRow(int newRow) {
        row = newRow;
    }

    @Override
    public void setCol(int newCol) {
        col = newCol;
    }

    @Override
    public PropertyInstance getPropertyByName(String name) {
        if (!properties.containsKey(name)) {
            throw new IllegalArgumentException("for entity of type " + entityDefinition.getName() + " has no property named " + name);
        }

        return properties.get(name);
    }

    @Override
    public Map<String, PropertyInstance> getAllPropertyInstances(){
        return properties;
    }

    public EntityDefinition getEntityDef(){
        return entityDefinition;
    }

    @Override
    public void addPropertyInstance(PropertyInstance propertyInstance) {
        properties.put(propertyInstance.getPropertyDefinition().getName(), propertyInstance);
    }

    public boolean hasPropertyByName(String name){
        return properties.containsKey(name);
    }

    @Override
    public String getEntityDefinitionName() {
        return entityDefinition.getName();
    }
}
