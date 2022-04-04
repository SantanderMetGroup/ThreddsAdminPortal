package es.unican.meteo.tap.model;


import org.hibernate.validator.constraints.NotEmpty;


public class Role {

	@NotEmpty
	private String roleName;

	private String description;
	
	private boolean isRestricted;
	
	private boolean isDataset;
	
	private String metadata;
	
	public Role(){}
	
	public Role(String roleName){
		this.roleName = roleName;
		this.description = "";
		this.isRestricted = false;
		this.isDataset = false;
		this.metadata = "";
	}

	public Role(String roleName, String description, boolean isRestricted,
			boolean isDataset, String metadata) {
		super();
		this.roleName = roleName;
		this.description = description;
		this.isRestricted = isRestricted;
		this.isDataset = isDataset;
		this.metadata = metadata;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isRestricted() {
		return isRestricted;
	}
	
	public boolean getIsRestricted(){
		return isRestricted();
	}
	
	public void setIsRestricted(boolean isRestricted){
		setRestricted(isRestricted);
	}

	public void setRestricted(boolean isRestricted) {
		this.isRestricted = isRestricted;
	}

	public boolean getIsDataset() {
		return isDataset;
	}

	public void setIsDataset(boolean isDataset) {
		this.isDataset = isDataset;
	}
	

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (isDataset ? 1231 : 1237);
		result = prime * result + (isRestricted ? 1231 : 1237);
		result = prime * result
				+ ((roleName == null) ? 0 : roleName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return roleName.equals(((Role)obj).getRoleName());
	}
	
	
	@Override
	public String toString() {
		return roleName;
	}
}