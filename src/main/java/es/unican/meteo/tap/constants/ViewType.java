package es.unican.meteo.tap.constants;

/* 
 * Indicates if a JSP is shown in a Window, in a context menu...
 */
public enum ViewType {
	
	VIEW_DEFAULT("VIEW_DEFAULT"),
    VIEW_ADMIN("VIEW_WINDOW");

    private final String label;

    private ViewType(String label) {
        this.label = label;
    }

    public String toString() {
        return this.label;
    }
}