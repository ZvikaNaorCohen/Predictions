package rule;

public interface Activation {
    boolean isActive(int tickNumber);
    int getTicks();
    double getProb();
}
