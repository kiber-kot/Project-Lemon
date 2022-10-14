package enumirated;

public enum SearchValue {

    WIKI("wiki");

    private final String value;

    public String getValue() {
        return value;
    }

    SearchValue(String value) {
        this.value = value;
    }

}
