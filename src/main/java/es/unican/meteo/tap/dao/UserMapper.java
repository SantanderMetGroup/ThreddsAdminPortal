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
import es.unican.meteo.tap.model.UserRole;

public interface UserMapper {
	
	@Select("SELECT * FROM users WHERE username = '${username}'")
	User getUserByUsername(@Param("username")String username);
	
	@Select("SELECT * FROM users WHERE email = '${email}'")
	User getUserByEmail(@Param("email")String email);
	
	@Select("SELECT * FROM users WHERE token = '${token}'")
	User getUserByToken(@Param("token")String token);
	
	@Select("SELECT * FROM users WHERE openid = '${openid}'")
	User getUserByOpenID(@Param("openid")String openID);
	
	@Select("select username, rolename FROM users_roles WHERE rolename IN (SELECT roleName FROM roles WHERE rolename IN(select DISTINCT rolename from ROLES_POLICY where policyname='${policyName}'))")
	List<UserRole> getUsersByPolicy(@Param("policyName") String policyName);
	
	@Select("SELECT * FROM users")
	List<User> getUsers();

	@Select("SELECT * FROM users WHERE username IN (SELECT username FROM users_roles WHERE rolename = 'ROLE_ADMIN')")
	List<User> getAdminUsers();
	
	@Select("SELECT * FROM users OFFSET ${startIndex} ROWS FETCH NEXT ${pageSize} ROWS ONLY")
	List<User> getPaggedUsers(@Param("startIndex")int startIndex, @Param("pageSize")int pageSize);
	
	@Select("SELECT * FROM users WHERE UPPER(${column}) LIKE UPPER('%${text}%') OFFSET ${startIndex} ROWS FETCH NEXT ${pageSize} ROWS ONLY")
	List<User> getPaggedUsersFiltered(@Param("startIndex")int startIndex, @Param("pageSize")int pageSize, @Param("text")String text, @Param("column")String column);
	
	@Select("SELECT * FROM users ORDER BY UPPER(${column}) ${orderBy} OFFSET ${startIndex} ROWS FETCH NEXT ${pageSize} ROWS ONLY")
	List<User> getPaggedUsersOrdered(@Param("startIndex")int startIndex, @Param("pageSize")int pageSize, @Param("column")String column, @Param("orderBy")String orderBy);
	
	@Select("SELECT * FROM users WHERE UPPER(${columnFilter}) LIKE UPPER('%${text}%')  ORDER BY UPPER(${column}) ${orderBy} OFFSET ${startIndex} ROWS FETCH NEXT ${pageSize} ROWS ONLY")
	List<User> getPaggedUsersOrderedAndFiltered(@Param("startIndex")int startIndex, @Param("pageSize")int pageSize, @Param("column")String column, @Param("orderBy")String orderBy, @Param("text")String text, @Param("columnFilter")String columnFilter);
	
	
	@Select("SELECT COUNT(*) FROM users")	
	int getNumberOfUsers();
	
	@Insert("INSERT INTO users (username,password,firstname,lastname,email,institution,isocode,token,motivation,metadata,active,newsletter,openid,regdate) values (#{username},#{password},#{firstName},#{lastName},#{email},#{institution},#{isoCode},#{token},#{motivation}, #{metadata}, #{active},#{newsletter},#{openid,jdbcType=VARCHAR},CURRENT_DATE)")
	void insertUser(User user);
	
	@Update("UPDATE users SET username = #{username}, password=#{password}, firstName=#{firstName}, lastName=#{lastName}, email=#{email}, institution=#{institution}, isocode=#{isoCode}, motivation=#{motivation}, metadata=#{metadata}, openid=#{openid,jdbcType=VARCHAR} WHERE username=#{username}")
	void updateUser(User user);
	
	@Delete("DELETE from users WHERE username = '${username}'")
	void deleteUser(@Param("username")String username);
	
	@Update("UPDATE users SET token = '${token}' WHERE username='${username}'")
	void resetUserToken(@Param("username")String username, @Param("token")String token);
	
	@Update("UPDATE users SET active = '${active}' WHERE username='${username}'")
	void activateUser(@Param("username")String username, @Param("active")boolean activate);

	@Select("SELECT * from roles WHERE rolename IN (SELECT rolename FROM users_roles WHERE username='${username}' AND accepted='true' AND authorized='true')")
	List<Role> getRoles(@Param("username")String username);
	
	@Select("SELECT * from roles WHERE rolename IN (SELECT rolename FROM users_roles WHERE username='${username}')")
	List<Role> getAllRoles(@Param("username")String username);
	
	@Select("SELECT * from roles WHERE rolename IN (SELECT rolename FROM users_roles WHERE username='${username}' AND issingle='true')")
	List<Role> getSingleRoles(@Param("username")String username);
	
	@Select("SELECT * from roles WHERE rolename IN (SELECT rolename FROM users_roles WHERE username='${username}' AND accepted='true' AND authorized='true') and isdataset=TRUE")
	List<Role> getDatasets(@Param("username")String username);
	
	@Select("SELECT * from roles WHERE rolename IN (SELECT rolename FROM users_roles WHERE username='${username}' AND accepted='false')")
	List<Role> getPendingOfAcceptanceRoles(@Param("username")String username);

	@Select("SELECT * from roles WHERE rolename IN (SELECT rolename FROM users_roles WHERE username='${username}' AND authorized='false')")
	List<Role> getPendingOfAuthRoles(@Param("username")String username);

	/* User roles dependencies */
	
	@Insert("INSERT INTO users_roles (username,rolename,authorized,accepted,isSingle) VALUES ('${username}','${rolename}','${authorized}','${accepted}', '${isSingle}')")
	void insertRole(@Param("username")String username, @Param("rolename")String rolename, @Param("authorized")boolean authorized, @Param("accepted")boolean accepted, @Param("isSingle")boolean isSingle);

	@Update("UPDATE users_roles SET authorized='${authorized}', accepted='${accepted}', issingle = '${isSingle}' WHERE username='${username}' AND rolename='rolename'")
	void updateRole(@Param("username")String username, @Param("rolename")String rolename, @Param("authorized")boolean authorized, @Param("accepted")boolean accepted, @Param("isSingle")boolean isSingle);

	@Update("UPDATE users_roles set authorized='${authorized}' WHERE username = '${username}' AND rolename = '${rolename}'")
	void authorizeRole(@Param("username")String username, @Param("rolename")String rolename, @Param("authorized") boolean authorized);

	@Update("UPDATE users_roles set accepted='${accepted}' WHERE username = '${username}' AND rolename = '${rolename}'")
	void acceptRole(@Param("username")String username, @Param("rolename")String rolename, @Param("accepted")boolean accepted);

	@Delete("DELETE FROM users_roles WHERE username = '${username}' AND rolename = '${rolename}'")
	void deleteRole(@Param("username")String username, @Param("rolename")String rolename);

	@Delete("DELETE FROM users_roles WHERE username = '${username}'")
	void deleteRoles(@Param("username")String username);
	
	/* User groups dependencies */
	
	@Insert("INSERT INTO users_groups(username,groupname,authorized) VALUES('${username}','${groupName}','${authorized}')")
	void insertGroup(@Param("username")String username, @Param("groupName")String groupName, @Param("authorized")boolean authorized);
	
	@Update("UPDATE users_groups set authorized='${authorized}'WHERE username = '${username}' AND groupname = '${groupName}'")
	void authorizeGroup(@Param("username")String username, @Param("groupName")String groupName, @Param("authorized")boolean authorized);
	
	@Delete("DELETE FROM users_groups WHERE username = '${username}' AND groupname = '${groupName}'")
	void deleteGroup(@Param("username")String username,@Param("groupName")String groupName);
	
	@Delete("DELETE FROM users_groups WHERE username = '${username}'")
	void deleteGroups(@Param("username")String username);
	
	@Select("SELECT * from GROUPS where groupname IN (SELECT groupname FROM users_groups WHERE username = '${username}' AND authorized=TRUE)")
	List<Group> getGroups(@Param("username")String username);
	
	@Select("SELECT * from GROUPS where groupname IN (SELECT groupname FROM users_groups WHERE username = '${username}')")
	List<Group> getAllGroups(@Param("username")String username);
	
	@Select("SELECT * from GROUPS where groupname IN (SELECT groupname FROM users_groups WHERE username = '${username}' AND authorized=FALSE)")
	List<Group> getPendingOfAuthGroups(@Param("username")String username);
	
	@Select("SELECT * from users WHERE informed=TRUE")
	List<User> getInformedUsers();
	
}