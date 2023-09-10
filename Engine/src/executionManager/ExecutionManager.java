package executionManager;

import DTO.ContextDTO;
import execution.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutionManager {
    private Map<Integer, Context> allRunningContexts = new HashMap<>();

    private ExecutorService threadExecutor = Executors.newFixedThreadPool(1);

    public void addNewContext(Context context){
        allRunningContexts.put(allRunningContexts.size()+1, context);
    }

    public int getIDForContext(){
        return allRunningContexts.size() + 1;
    }
}
