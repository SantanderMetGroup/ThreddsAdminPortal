package es.unican.meteo.tap.service;

import java.util.List;

import es.unican.meteo.tap.model.Group;
import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.model.User;
import es.unican.meteo.tap.model.UserRole;

public interface UserService {
	
	User getUserByUsername(String username);
	User getUserByEmail(String email);
	User getUserByToken(String token);
	User getUserByOpenID(String openID);
	List<User> getUsers();
	List<User> getAdminUsers();
	List<User> getPagedUsers(int startIndex, int pageSize, String order, String text, String column);
	List<UserRole> getUsersRolesByPolicy(String policyName);
	int getNumberOfUsers();
	void insertUser(User user);
	void updateUser(User user);
	void deleteUser(String username);
	void resetUserToken(String username);
	void activateUser(String username, boolean activate);
	
	void insertRole(String username, String roleName, boolean authorized, boolean accepted, boolean isSingle);
	void updateRole(String username, String roleName, boolean authorized, boolean accepted, boolean isSingle);
	void deleteRole(String username, String roleName);
	void deleteRole(String username, String roleName, String groupName);
	void acceptRole(String username, String roleName, boolean accepted);
	void authorizeRole(String username, String roleName, boolean authorized);
	List<Role> getRoles(String username);
	List<Role> getSingleRoles(String username);
	List<Role> getDatasets(String username);
	List<Role> getAllRoles(String username);
	List<Role> getPendingOfAcceptanceRoles(String username);
	List<Role> getPendingOfAuthRoles(String username);
	
	void insertGroup(String username, String groupName, boolean authorized, boolean accepted);
	void deleteGroup(String username,String groupName);
	void authorizeGroup(String username, String groupName, boolean authorized);
	List<Group> getGroups(String username);
	List<Group> getPendingOfAuthGroups(String username);
	List<User> disableRole(List<User> usersToModify);
	void changeUserPassword(String username, String password);
}
