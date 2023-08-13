package definition.property.api;

public interface PropertyDefinition {
    String getName();
    PropertyType getType();
    Object generateValue();

    boolean newValueInCorrectBounds(Integer value);
    boolean newValueInCorrectBounds(Float value);
}