package rule;

public class ActivationImpl implements Activation{
    int ticks = 1;
    double probability = 1;

    public ActivationImpl(int numOfTicks){
        ticks = numOfTicks;
    }
    public ActivationImpl(double prob){
        probability = prob;
    }

    public ActivationImpl(int numOfTicks, double prob){
        ticks = numOfTicks;
        probability = prob;
    }

    public ActivationImpl(){
        ticks = 1;
        probability = 1;
    }
    @Override
    public boolean isActive(int tickNumber) {
        return tickNumber % ticks == 0;
    }

    @Override
    public int getTicks() {
        return ticks;
    }

    @Override
    public double getProb() {
        return probability;
    }
}
