package engine;

public class Engine {
    private AllInstances engineAllInstances;

    public Engine(AllInstances i){
        engineAllInstances = i;
    }

    public Engine(){
        engineAllInstances = new AllInstances();
    }

    public void initEngine(AllInstances i){
        engineAllInstances = i;
    }

    public AllInstances getAllInstances(){
        return engineAllInstances;
    }
}
