package es.unican.meteo.tap.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import es.unican.meteo.tap.model.Group;
import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.model.User;

public interface GroupMapper {
	
	@Select("SELECT * FROM groups")
	List<Group> getGroups();
	
	@Select("SELECT * FROM groups WHERE groupname IN (select groupname from GROUPS_ROLES where rolename = '${roleName}')")
	List<Group> getGroupsByRole(@Param("roleName")String roleName);
	
	@Select("select * from GROUPS where groupname IN (select groupname from GROUPS_ROLES WHERE rolename IN(${roleNamesParsedList}) GROUP BY groupname HAVING COUNT(groupname) = ${size})")
	List<Group> getGroupsByRoles(@Param("roleNamesParsedList")String roleNamesParsedList, @Param("size")int roleNamesSize);
	
	@Select("SELECT * FROM groups OFFSET ${startIndex} ROWS FETCH NEXT ${pageSize} ROWS ONLY")
	List<Group> getPaggedGroups(@Param("startIndex")int startIndex, @Param("pageSize")int pageSize);
	
	@Select("SELECT * FROM users WHERE username IN(SELECT username FROM users_groups WHERE groupname='${groupName}')")
	List<User> getUsers(@Param("groupName")String groupName);
	
	@Select("SELECT COUNT(*) FROM groups")	
	int getNumberOfGroups();
	
	@Select("SELECT * FROM groups WHERE groupname = '${groupName}'")
	Group getGroupByName(@Param("groupName")String groupName);
	
	@Select("SELECT * FROM groups WHERE isproject = TRUE")
	List<Group> getGroupProjects();
	
	@Select("SELECT * FROM roles WHERE rolename IN (SELECT rolename FROM groups_roles WHERE groupname='${groupName}')")
	List<Role> getGroupRoles(@Param("groupName")String groupName);
	
	@Select("SELECT * FROM roles WHERE rolename IN (SELECT rolename FROM groups_roles WHERE groupname='${groupName}') and isDataset = TRUE")
	List<Role> getGroupDatasets(@Param("groupName")String groupName);
	
	@Select("SELECT * FROM groups WHERE coordinator= '${coordinator}'")
	List<Group> getGroupsByCoordinator(@Param("coordinator")String coordinator);
	
	@Select("select * from GROUPS WHERE groupname IN (select groupname from GROUPS_ROLES where rolename IN (Select rolename from ROLES where ROLES.ISRESTRICTED=true))")
	List<Group> getRestrictedGroups();
	
	@Select("select count(*) from GROUPS WHERE groupname IN (select groupname from GROUPS_ROLES where rolename IN (Select rolename from ROLES where ROLES.ISRESTRICTED=true) and groupname='${groupName}')")
	int isRestrictedGroup(@Param("groupName")String groupName);
	
	@Insert("INSERT INTO groups (groupname,description,isproject,coordinator) values (#{groupName},#{description,jdbcType=VARCHAR},#{isProject},#{coordinator,jdbcType=VARCHAR})")
	void insertGroup(Group group);
	
	@Update("UPDATE groups SET groupname = #{groupName}, description = #{description,jdbcType=VARCHAR}, isproject = #{isProject}, coordinator = #{coordinator,jdbcType=VARCHAR} WHERE groupname = #{groupName}")
	void updateGroup(Group group);
	
	@Delete("DELETE FROM groups where groupname = '${groupName}'")
	void deleteGroup(@Param("groupName")String groupName);
	
	@Insert("INSERT INTO groups_roles (groupname,rolename) values ('${groupName}','${roleName}')")
	void insertRole(@Param("groupName")String groupName, @Param("roleName")String roleName);
	
	@Delete("DELETE FROM groups_roles WHERE groupname = '${groupName}' AND rolename = '${roleName}'")
	void deleteRole(@Param("groupName")String groupName, @Param("roleName")String roleName);
}