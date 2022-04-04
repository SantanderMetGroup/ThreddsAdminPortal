package es.unican.meteo.tap.model;

public class Group {
	private String groupName;
	private String description;
	private boolean isProject;
	private String coordinator;
	
	public Group(){}
	
	public Group(String groupName){
		super();
		this.groupName = groupName;
	}
	public Group(String groupName, String description, boolean isProject,
			String coordinator) {
		super();
		this.groupName = groupName;
		this.description = description;
		this.isProject = isProject;
		this.coordinator = coordinator;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean getIsProject(){
		return isProject;
	}

	public boolean isProject() {
		return isProject;
	}

	public void setIsProject(boolean isProject) {
		this.isProject = isProject;
	}

	public String getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(String coordinator) {
		if(!coordinator.equals(""))
			this.coordinator = coordinator;
	}

	@Override
	public String toString() {
		return groupName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((coordinator == null) ? 0 : coordinator.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + (isProject ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this.groupName.equals(((Group)obj).getGroupName()))
			return true;
		return false;
	}
}
