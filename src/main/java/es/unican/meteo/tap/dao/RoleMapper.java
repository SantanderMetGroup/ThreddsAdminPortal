package es.unican.meteo.tap.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import es.unican.meteo.tap.model.Policy;
import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.model.User;

public interface RoleMapper {
	
	@Select("SELECT * FROM roles WHERE rolename = '${roleName}'")
	Role getRoleByName(@Param("roleName")String roleName);

	@Select("SELECT * FROM roles")
	List<Role> getRoles();
	
	@Select("SELECT * FROM roles WHERE isDataset = TRUE")
	List<Role> getDatasets();

	@Select("SELECT * FROM roles OFFSET ${startIndex} ROWS FETCH NEXT ${pageSize} ROWS ONLY")
	List<Role> getPaggedRoles(@Param("startIndex")int startIndex, @Param("pageSize")int pageSize);

	@Select("SELECT * FROM users_roles where authorized=false")
	List<User> getPendingOfAuthUsers();
	
	@Select("SELECT COUNT(*) FROM roles")	
	int getNumberOfRoles();
	
	@Insert("INSERT INTO roles (rolename, description, isrestricted, isdataset, metadata) VALUES (#{roleName},#{description,jdbcType=VARCHAR},#{isRestricted},#{isDataset}, #{metadata})")
	void insertRole(Role role);
	
	@Update("UPDATE roles SET isrestricted = #{isRestricted}, isDataset = #{isDataset}, description = #{description}, metadata = #{metadata} WHERE rolename = #{roleName}")
	void updateRole(Role role);
	
	@Delete("DELETE FROM roles WHERE rolename = '${roleName}'")
	void deleteRole(@Param("roleName")String roleName);

	@Delete("DELETE FROM users_roles WHERE rolename = '${roleName}'")
	void deleteRoleUserDependencies(@Param("roleName")String roleName);
	
	@Delete("DELETE FROM roles_policy WHERE roleName = '${roleName}'")
	void deleteRolePolicyDependencies(@Param("roleName")String roleName);

	@Insert("INSERT INTO roles_policy (rolename, policyname) values ('${roleName}','${policyName}')")
	void insertPolicy(@Param("roleName")String roleName, @Param("policyName")String policyName);
	
	@Delete("DELETE FROM roles_policy WHERE roleName = '${roleName}' AND policyName = '${policyName}'")
	void deletePolicy(@Param("roleName")String roleName, @Param("policyName")String policyName);
	
	@Select("SELECT * FROM policy WHERE policyname IN (SELECT policyname FROM roles_policy WHERE rolename='${roleName}')")
	List<Policy> getPolicies(@Param("roleName")String roleName);
	
	@Select("SELECT * FROM roles where rolename IN (SELECT DISTINCT rolename FROM roles_policy)")
	List<Role> getRolesWithPolicies();
	
	@Select("SELECT * FROM users where username IN (select DISTINCT username from USERS_ROLES where rolename='${roleName}')")
	List<User> getRoleUsers(@Param("roleName")String roleName);

}
