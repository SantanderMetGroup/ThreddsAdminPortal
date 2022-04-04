package es.unican.meteo.tap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.unican.meteo.tap.dao.RoleMapper;
import es.unican.meteo.tap.model.Policy;
import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.model.User;
import es.unican.meteo.tap.service.RoleService;

@Service(value="roleService")
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleMapper roleMapper;

	public void setUserMapper(RoleMapper roleMapper) {
		this.roleMapper = roleMapper;
	}
	
	public Role getRoleByName(String roleName) {
		return roleMapper.getRoleByName(roleName);
	}
	

	public boolean hasPolicies(String roleName) {
		return getRolesWithPolicies().contains(new Role(roleName));
	}

	public List<Role> getRoles() {
		return roleMapper.getRoles();
	}
	
	public List<User> getRoleUsers(String roleName) {
		return roleMapper.getRoleUsers(roleName);
	}
	
	public List<Role> getPaggedRoles(int startIndex, int pageSize) {
		return roleMapper.getPaggedRoles(startIndex, pageSize);
	}
	
	public List<User> getPendingOfAuthUsers() {
		return roleMapper.getPendingOfAuthUsers();
	}

	public int getNumberOfRoles() {
		return roleMapper.getNumberOfRoles();
	}

	public void insertRole(Role role) {
		roleMapper.insertRole(role);
	}

	public void updateRole(Role role) {
		roleMapper.updateRole(role);
	}

	public void deleteRole(String roleName) {
		roleMapper.deleteRolePolicyDependencies(roleName);
		roleMapper.deleteRoleUserDependencies(roleName);
		roleMapper.deleteRole(roleName);
	}

	public List<Policy> getPolicies(String roleName) {
		return roleMapper.getPolicies(roleName);
	}

	public void insertPolicy(String roleName, String policyName) {
		roleMapper.insertPolicy(roleName, policyName);
	}

	public void removePolicy(String roleName, String policyName) {
		roleMapper.deletePolicy(roleName, policyName);
	}

	public List<Role> getRolesWithPolicies() {
		return roleMapper.getRolesWithPolicies();
	}

	public List<Role> getDatasets() {
		return roleMapper.getDatasets();
	}

}
