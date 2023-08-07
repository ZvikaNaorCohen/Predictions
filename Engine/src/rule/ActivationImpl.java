package rule;

public class ActivationImpl implements Activation{
    int ticks;

    public ActivationImpl(int numOfTicks){
        ticks = numOfTicks;
    }
    @Override
    public boolean isActive(int tickNumber) {
        return tickNumber % ticks == 0;
    }
}
