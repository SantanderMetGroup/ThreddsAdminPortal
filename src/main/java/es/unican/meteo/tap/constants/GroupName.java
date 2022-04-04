package es.unican.meteo.tap.constants;

public enum GroupName {
	
    GROUP_USER("TAP_USER"),
    GROUP_NEWSLETTER("NEWSLETTER"),
    GROUP_ADMIN("TAP_ADMIN");

    private final String label;

    private GroupName(String label) {
        this.label = label;
    }

    public String toString() {
        return this.label;
    }
}