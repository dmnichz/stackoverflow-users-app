package stackoverflowapp.model;

public enum Tag {
    JAVA("java"),
    NET(".net"),
    DOCKER("docker"),
    C("c#");

    private final String value;

    Tag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
