package executionManager;

import DTO.ContextDTO;
import execution.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutionManager {
    private final Map<Integer, Context> allRunningContexts = new HashMap<>();
    private Map<Integer, Context> contextsBeforeRunning = new HashMap<>();

    private ExecutorService threadExecutor = Executors.newCachedThreadPool();

    public void runSpecificContext(int contextID){
        threadExecutor.execute((Runnable) allRunningContexts.get(contextID));
    }

    public int getQueueSize(){
        return allRunningContexts.size();
    }

    public int getRunningContexts(){
        int counter = 0;
        for(Map.Entry<Integer,Context> entry : allRunningContexts.entrySet()){
            if(entry.getValue().isRunning().get()){
                counter++;
            }
        }

        return counter;
    }

    public boolean isContextRunning(int contextID){
        return allRunningContexts.get(contextID).isRunning().get();
    }

    public void addNewContext(Context context, Context copied){
        int newID = allRunningContexts.size()+1;
        contextsBeforeRunning.put(newID, copied);
        allRunningContexts.put(allRunningContexts.size()+1, context);
    }

    public int getIDForContext(){
        return allRunningContexts.size() + 1;
    }

    public Context getContextByID(int id){
        return allRunningContexts.get(id);
    }
}
