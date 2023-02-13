package stackoverflowapp.model;

public enum RequiredLocation {
    ROMANIA("Romania"),
    MOLDOVA("Moldova");

    private String value;

    RequiredLocation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
