package UI.MenuItem.impl;

import UI.MenuItem.api.MenuItem;

public class ExitProgram implements MenuItem {
    public void invoke(){
        System.out.println("Thank you for supporting PredictionsV1. Exiting program. ");
        System.exit(1);
    }

    public void printInvalidChoice(String reason){

    }
}
