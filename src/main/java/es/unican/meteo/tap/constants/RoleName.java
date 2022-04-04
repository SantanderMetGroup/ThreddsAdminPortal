package es.unican.meteo.tap.constants;

public enum RoleName {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MANAGER("ROLE_MANAGER"),
    ROLE_USER("ROLE_USER"),
    ROLE_RESTRICTED("restrictedDatasetUser");

    private final String label;

    private RoleName(String label) {
        this.label = label;
    }

    public String toString() {
        return this.label;
    }
}