package executionManager;

public class EntityData {
    private String name;
    private Integer count;

    public EntityData(String name, Integer count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public Integer getCount() {
        return count;
    }
}
