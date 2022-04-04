package es.unican.meteo.tap.service;

import java.util.List;

import es.unican.meteo.tap.model.Policy;
import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.model.User;

public interface RoleService {
	Role getRoleByName(String roleName);
	List<User> getRoleUsers(String roleName);
	boolean hasPolicies(String roleName);
	List<Role> getRoles();
	List<Role> getDatasets();
	List<Role> getPaggedRoles(int startIndex, int pageSize);
	List<Role> getRolesWithPolicies();
	List<User> getPendingOfAuthUsers();
	int getNumberOfRoles();
	void insertRole(Role role);
	void updateRole(Role role);
	void deleteRole(String roleName);
	List<Policy> getPolicies(String roleName);
	void insertPolicy(String roleName, String policyName);
	void removePolicy(String roleName, String policyName);
}
