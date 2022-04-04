package es.unican.meteo.tap.constants;

public enum Variables {
	
	URL("url"),
    TYPE("type"),
    IS_PRIVATE("isPrivate"),
	LABEL("label");

    private final String label;

    private Variables(String label) {
        this.label = label;
    }

    public String toString() {
        return this.label;
    }
}