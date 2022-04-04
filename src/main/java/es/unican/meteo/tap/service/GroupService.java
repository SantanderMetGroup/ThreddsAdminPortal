package es.unican.meteo.tap.service;

import java.util.List;

import es.unican.meteo.tap.model.Group;
import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.model.User;

public interface GroupService {
	
	Group getGroup(String groupName);
	List<Group> getGroups();
	List<Group> getGroupsByRole(String roleName);
	List<Group> getGroupsByRoles(List<String> roleNames);
	List<Group> getPagedGroups(int startIndex, int pageSize);
	List<User> getUsers(String groupName);

	List<Group> getGroupProjects();
	List<Role> getGroupRoles(String groupName);
	List<Role> getGroupDatasets(String groupName);
	boolean hasRestrictedRoles(String groupName);
	
	int getNumberOfGroups();

	void insertGroup(Group group);
	void updateGroup(Group group);
	void deleteGroup(String groupName);
	void insertRole(String groupName, String roleName);
	void deleteRole(String groupName, String roleName);
}
