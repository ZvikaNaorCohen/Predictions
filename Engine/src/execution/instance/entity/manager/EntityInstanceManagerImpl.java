package execution.instance.entity.manager;

import definition.entity.EntityDefinition;
import definition.property.api.PropertyDefinition;
import execution.instance.entity.EntityInstance;
import execution.instance.entity.EntityInstanceImpl;
import execution.instance.property.PropertyInstance;
import execution.instance.property.PropertyInstanceImpl;
import javafx.util.Pair;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityInstanceManagerImpl implements EntityInstanceManager {
    private int maxRows, maxCols;

    private int count;
    private List<EntityInstance> instances;
    private EntityInstance[][] grid;

    public EntityInstanceManagerImpl(int rows, int cols) {
        count = 0;
        instances = new ArrayList<>();
        maxRows = rows;
        maxCols = cols;
        grid = new EntityInstance[maxRows][maxCols];
    }

    Pair<Integer, Integer> findEmptySpot(EntityInstance[][] grid){
        List<Pair<Integer, Integer>> myList = new ArrayList<>();
        for(int row=0; row<maxRows;row++){
            for(int col = 0; col < maxCols; col++){
                if(grid[row][col] == null){
                    myList.add(new Pair<>(row, col));
                }
            }
        }

        Random random = new Random();
        if(myList.size() == 0){
            // BUGGGGGGGG
            return null;
        }
        else{
            int index = random.nextInt(myList.size());
            return myList.get(index);
        }
    }

    @Override
    public EntityInstance create(EntityDefinition entityDefinition, EntityInstance[][] grid) {
//        Random random = new Random();
        int newRow = 1;
        int newCol = 1;
        count++;

        Pair<Integer, Integer> newPosition = findEmptySpot(grid);
        newRow = newPosition.getKey();
        newCol = newPosition.getValue();
//
//        do{
//            newRow = random.nextInt(maxRows);
//            newCol = random.nextInt(maxCols);
//            if(grid[newRow][newCol] == null){
//                break;
//            }
//        }while(true);

        EntityInstance newEntityInstance = new EntityInstanceImpl(entityDefinition, count, newRow, newCol);
        instances.add(newEntityInstance);

        for (PropertyDefinition propertyDefinition : entityDefinition.getProps()) {
            Object value = propertyDefinition.generateValue();
            PropertyInstance newPropertyInstance = new PropertyInstanceImpl(propertyDefinition, value);
            newEntityInstance.addPropertyInstance(newPropertyInstance);
        }

        return newEntityInstance;
    }

    @Override
    public List<EntityInstance> getInstances() {
        return instances;
    }

    public void createEntityInstanceByName(String name){
        for(EntityInstance temp : instances){
            if(temp.getEntityDefinitionName().equals(name)){
                create(temp.getEntityDef(), grid);
                return;
            }
        }
    }

    @Override
    public int getCurrentAliveEntitiesByName(String name){
        int counter = 0;
        for(EntityInstance instance : instances){
            if(instance.getEntityDefinitionName().equals(name)){
                counter++;
            }
        }

        return counter;
    }

    @Override
    public void killEntity(EntityInstance instance, EntityInstance[][] grid) {
//        for (EntityInstance instance : instances) {
//            if (instance.getEntityDefinitionName().equals(entityName)) {
//                instances.remove(instance);
//                return;
//            }
//        }
        grid[instance.getRow()][instance.getCol()] = null;
        instances.remove(instance);
    }
}