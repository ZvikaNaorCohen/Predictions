package resources.requests;

import generated.PRDWorld;
import javafx.beans.property.SimpleBooleanProperty;

public class Request {
    private PRDWorld world;
    private int numberOfRuns = 0;
    private SimpleBooleanProperty endByUser = new SimpleBooleanProperty(false);

    private int ticks = -1;
    private int seconds = -1;

    public Request(){

    }

    public Request(PRDWorld oldWorld){
        world = oldWorld;
    }

    public Request(PRDWorld newWorld, int runs, boolean userEnd, int newTicks, int newSeconds){
        world = newWorld;
        numberOfRuns = runs;
        endByUser.set(userEnd);
        ticks = newTicks;
        seconds = newSeconds;
    }

    public String getRequestWorldName(){
        return world.getName();
    }

    public void setNumberOfRuns(int newNum){
        numberOfRuns = newNum;
    }

    public int getNumberOfRuns(){
        return numberOfRuns;
    }

    public void setEndByUser(Boolean newValue){
        endByUser.set(newValue);
        if(newValue) {
            seconds = -1;
            ticks = -1;
        }
    }

    public int getEndBySeconds(){
        return seconds;
    }

    public int getEndByTicks(){
        return ticks;
    }

    public boolean getEndByUser(){
        return endByUser.get();
    }

    public SimpleBooleanProperty getPropertyFromEndByUser(){
        return endByUser;
    }

    public void setEndBySeconds(int newValue){
        seconds = newValue;
    }

    public void setEndByTicks(int newValue){
        ticks = newValue;
    }
}
