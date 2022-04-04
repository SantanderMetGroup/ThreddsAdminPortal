package es.unican.meteo.tap.service.impl;


import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import es.unican.meteo.tap.dao.UserMapper;
import es.unican.meteo.tap.model.Group;
import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.model.User;
import es.unican.meteo.tap.model.UserRole;
import es.unican.meteo.tap.service.ContextHelper;
import es.unican.meteo.tap.service.GroupService;
import es.unican.meteo.tap.service.MailService;
import es.unican.meteo.tap.service.RoleService;
import es.unican.meteo.tap.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RoleService roleService;

	@Autowired
	private GroupService groupService;
	
	@Autowired
	private ContextHelper contextHelper;
	
	@Autowired
	private MailService mailService;

	public User getUserByUsername(String username) {
		return userMapper.getUserByUsername(username);
	}

	public User getUserByEmail(String email){
		return userMapper.getUserByEmail(email);
	}
	public List<User> getUsers() {
		return  userMapper.getUsers();
	}
	
	public List<User> getAdminUsers() {
		return userMapper.getAdminUsers();
	}
	
	public int getNumberOfUsers(){
		return userMapper.getNumberOfUsers();
	}
	
	public List<User> getPagedUsers(int startIndex, int pageSize, String order, String text, String column) {
		if(!StringUtils.isEmpty(text) & !StringUtils.isEmpty(column) & !StringUtils.isEmpty(order))
			return userMapper.getPaggedUsersOrderedAndFiltered(startIndex, pageSize, order.split("\\s+")[0], order.split("\\s+")[1], text, column);
		if(!StringUtils.isEmpty(text) & !StringUtils.isEmpty(column))
			return userMapper.getPaggedUsersFiltered(startIndex, pageSize, text, column);
		if(StringUtils.isEmpty(order))
			return userMapper.getPaggedUsers(startIndex, pageSize);
		else
			return userMapper.getPaggedUsersOrdered(startIndex, pageSize, order.split("\\s+")[0], order.split("\\s+")[1]);
	}
	

	public void insertUser(User user) {
		PasswordEncoder encoder = new Md5PasswordEncoder();
		String passwordEncoded = encoder.encodePassword(user.getPassword(), null);
		user.setPassword(passwordEncoded);
		user.setToken(encoder.encodePassword(user.getEmail() + Calendar.getInstance().getTimeInMillis(), null));
		userMapper.insertUser(user);
	}
	
	public void updateUser(User user) {
		User storedUser = getUserByUsername(user.getUsername());
		user.setMetadata(storedUser.getMetadata());
		if(storedUser != null && !user.getPassword().equals(storedUser.getPassword())){
			PasswordEncoder encoder = new Md5PasswordEncoder();
			String passwordEncoded = encoder.encodePassword(user.getPassword(), null);
			user.setPassword(passwordEncoded);
		}
		if(user.getOpenid()==null)
			user.setOpenid("");
		userMapper.updateUser(user);
	}
	
	public void deleteUser(String username) {
		userMapper.deleteRoles(username);
		userMapper.deleteGroups(username);
		userMapper.deleteUser(username);
	}
	
	public void resetUserToken(String username) {
		PasswordEncoder encoder = new Md5PasswordEncoder();
		String token = encoder.encodePassword(username + Calendar.getInstance().getTimeInMillis(), null);
		userMapper.resetUserToken(username, token);
	}
	
	public void activateUser(String username, boolean activate) {
		userMapper.activateUser(username, activate);
		
	}

	/**
	 * Roles
	 * 
	 */

	public void insertRole(String username, String roleName, boolean authorized, boolean accepted, boolean isSingle) {
		if(!getAllRoles(username).contains(new Role(roleName)))
			userMapper.insertRole(username, roleName, authorized, accepted, isSingle);
		else if(getPendingOfAcceptanceRoles(username).contains(new Role(roleName)))
			updateRole(username, roleName, true, accepted, isSingle);
		else if(getPendingOfAuthRoles(username).contains(new Role(roleName)))
			updateRole(username, roleName, authorized, true, isSingle);
	}

	public void updateRole(String username, String roleName, boolean authorized, boolean accepted, boolean isSingle) {
		userMapper.updateRole(username, roleName, authorized, accepted, isSingle);
	}
	
	public void deleteRole(String username, String roleName) {
		userMapper.deleteRole(username, roleName);
	}
	
	public void deleteRole(String username, String roleName, String groupName){
		List<Group> roleGroups = Lists.newArrayList(groupService.getGroupsByRole(roleName));
		List<Group> userGroups = Lists.newArrayList(getGroups(username));
		roleGroups.remove(new Group(groupName));
		userGroups.contains(roleGroups);
		if(!userGroups.removeAll(roleGroups))
			if(!getSingleRoles(username).contains(new Role(roleName)))
				deleteRole(username, roleName);
	}
	

	public void acceptRole(String username, String roleName, boolean accepted) {
		userMapper.acceptRole(username, roleName, accepted);
	}

	public void authorizeRole(String username, String roleName, boolean authorized) {
		userMapper.authorizeRole(username, roleName, authorized);
	}
	
	public List<Role> getRoles(String username) {
		return userMapper.getRoles(username);
	}
	
	public List<Role> getSingleRoles(String username) {
		return userMapper.getSingleRoles(username);
	}
	
	public List<Role> getDatasets(String username) {
		return userMapper.getDatasets(username);
	}
	
	public List<Role> getAllRoles(String username) {
		return userMapper.getAllRoles(username);
	}

	public List<Role> getPendingOfAcceptanceRoles(String username) {
		return userMapper.getPendingOfAcceptanceRoles(username);
	}

	public List<Role> getPendingOfAuthRoles(String username) {
		return userMapper.getPendingOfAuthRoles(username);
	}

	/**
	 * GROUPS
	 */

	public void insertGroup(String username, String groupName, boolean authorized, boolean accepted) {
		List<Role> groupRoles = groupService.getGroupRoles(groupName);
		List<Role> rolesToInsert = Lists.newArrayList(groupRoles);
		for(Role role: rolesToInsert)
			insertRole(username, role.getRoleName(), authorized, accepted, false);
		userMapper.insertGroup(username, groupName, authorized);
	}

	public void deleteGroup(String username, String groupName) {
		List<Group> userGroups = Lists.newArrayList(userMapper.getAllGroups(username));
		List<Role> rolesToDelete = Lists.newArrayList(groupService.getGroupRoles(groupName));
		for(Group group : userGroups){
			if(!group.getGroupName().equals(groupName)){ //If the role belongs to another assigned group do not remove
				rolesToDelete.removeAll(groupService.getGroupRoles(group.getGroupName()));
			}
		}
		rolesToDelete.removeAll(getSingleRoles(username));//If the role is single do not remove
		for(Role role : rolesToDelete){
			deleteRole(username, role.getRoleName());
		}
		userMapper.deleteGroup(username, groupName);
	}

	public List<Group> getGroups(String username) {
		return userMapper.getGroups(username);
	}

	public List<Group> getPendingOfAuthGroups(String username) {
		return userMapper.getPendingOfAuthGroups(username);
	}

	public void authorizeGroup(String username, String groupName, boolean authorized) {
		List<Role> groupRoles = groupService.getGroupRoles(groupName);
		List<Role> allRoles = getAllRoles(username);
		for(Role role : groupRoles){
			if(allRoles.contains(role))
				authorizeRole(username, role.getRoleName(), true);
		}
		userMapper.authorizeGroup(username, groupName, authorized);
	}

	public User getUserByToken(String token) {
		return userMapper.getUserByToken(token);
	}

	public List<UserRole> getUsersRolesByPolicy(String policyName) {
		return userMapper.getUsersByPolicy(policyName);
	}
	
	public User getUserByOpenID(String openID){
		return userMapper.getUserByOpenID(openID);
	}

	public List<User> disableRole(List<User> usersToModify) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void changeUserPassword(String username, String password){
		User user = getUserByUsername(username);
		PasswordEncoder encoder = new Md5PasswordEncoder();
		String passwordEncoded = encoder.encodePassword(password, null);
		user.setPassword(passwordEncoded);
		userMapper.updateUser(user);
	}
}
