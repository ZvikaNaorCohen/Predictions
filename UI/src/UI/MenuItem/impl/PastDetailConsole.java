package UI.MenuItem.impl;

import UI.MenuItem.api.MenuItem;
import UI.MenuItem.api.PastDetail;
import execution.context.Context;

public class PastDetailConsole implements PastDetail {
    public void invoke(Context myWorld){

    }

    public void printInvalidChoice(String reason){
        System.out.println(reason);
    }
}
