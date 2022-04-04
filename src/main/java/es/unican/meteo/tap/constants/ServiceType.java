package es.unican.meteo.tap.constants;

public enum ServiceType {
	
	REST("REST"),
    OPENID("OPENID");

    private final String label;

    private ServiceType(String label) {
        this.label = label;
    }

    public String toString() {
        return this.label;
    }
}