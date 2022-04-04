package es.unican.meteo.tap.service.impl;

import java.util.List;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.unican.meteo.tap.dao.GroupMapper;
import es.unican.meteo.tap.model.Group;
import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.model.User;
import es.unican.meteo.tap.service.GroupService;
import es.unican.meteo.tap.service.MailService;
import es.unican.meteo.tap.service.UserService;

@Service
public class GroupServiceImpl implements GroupService{

	@Autowired
	private GroupMapper groupMapper;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private UserService userService;

	public Group getGroup(String groupName) {
		return groupMapper.getGroupByName(groupName);
	}

	public List<Group> getGroups() {
		return groupMapper.getGroups();
	}

	public List<Group> getGroupsByRole(String roleName) {
		return groupMapper.getGroupsByRole(roleName);
	}
	
	public List<Group> getGroupsByRoles(final List<String> roleNames) {
		String parsedRoleNames ="";
		for(String roleName : roleNames)
			parsedRoleNames += "'"+roleName+"',";
		parsedRoleNames = parsedRoleNames.substring(0, parsedRoleNames.length()-1);
		return groupMapper.getGroupsByRoles(parsedRoleNames, roleNames.size());
	}
	
	public List<Group> getGroupProjects() {
		return groupMapper.getGroupProjects();
	}
	
	public List<Role> getGroupRoles(String groupName) {
		return groupMapper.getGroupRoles(groupName);
	}
	
	public List<Role> getGroupDatasets(String groupName) {
		return groupMapper.getGroupDatasets(groupName);
	}

	public boolean hasRestrictedRoles(String groupName) {
		for(Role role : groupMapper.getGroupRoles(groupName)){
			if(role.isRestricted())
				return true;
		}
		return false;
	}

	public List<Group> getPagedGroups(int startIndex, int pageSize) {
		return groupMapper.getPaggedGroups(startIndex, pageSize);
	}

	public int getNumberOfGroups() {
		return groupMapper.getNumberOfGroups();
	}

	public void insertGroup(Group group) {
		groupMapper.insertGroup(group);
	}

	public void updateGroup(Group group) {
		groupMapper.updateGroup(group);
	}

	public void deleteGroup(String groupName) {
		List<Role> groupRoles = getGroupRoles(groupName);
		for(Role role: groupRoles)
			deleteRole(groupName, role.getRoleName());
		for(User user: getUsers(groupName))
			userService.deleteGroup(user.getUsername(), groupName);
		groupMapper.deleteGroup(groupName);
	}

	public List<User> getUsers(String groupName) {
		return groupMapper.getUsers(groupName);
	}

	public void insertRole(String groupName, String roleName) {
		groupMapper.insertRole(groupName, roleName);
	}
	
	public void deleteRole(String groupName, String roleName) {
		for(User user: getUsers(groupName)){
			userService.deleteRole(user.getUsername(), roleName, groupName);
		}
		groupMapper.deleteRole(groupName,roleName);
	}
}