package UI.MenuItem.api;

import execution.context.Context;

import java.util.Map;

public interface PastDetail {
    void invoke(Map<String, Context> myPastRuns);
}
