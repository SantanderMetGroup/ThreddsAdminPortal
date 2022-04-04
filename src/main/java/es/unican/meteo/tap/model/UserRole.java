package es.unican.meteo.tap.model;

public class UserRole {
	private String username;
	private String roleName;
	
	public UserRole() {
	}
	public UserRole(String username, String roleName) {
		super();
		this.username = username;
		this.roleName = roleName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}

	
