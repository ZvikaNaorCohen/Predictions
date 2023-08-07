package rule;

public class Termination {
    private int endByTicks = -1;
    private int endBySeconds = -1;

    public int getEndByTicks(){
        return endByTicks;
    }

    public int getEndBySeconds(){
        return endBySeconds;
    }

    public Termination(int ticks, int seconds){
        endBySeconds = seconds;
        endByTicks = ticks;
    }
}
