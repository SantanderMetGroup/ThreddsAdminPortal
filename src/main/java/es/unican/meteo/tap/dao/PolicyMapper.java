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

public interface PolicyMapper {
	
	@Select("SELECT * FROM policy WHERE policyname = '${policyName}'")
	Policy getPolicyByName(@Param("policyName")String policyName);
	
	@Select("SELECT * FROM policy")
	List<Policy> getPolicies();
	
	@Select("SELECT * FROM policy OFFSET ${startIndex} ROWS FETCH NEXT ${pageSize} ROWS ONLY")
	List<Policy> getPaggedPolicies(@Param("startIndex")int startIndex, @Param("pageSize")int pageSize);
	
	@Select("SELECT COUNT(*) FROM policy")	
	int getNumberOfPolicies();
	
	@Select("SELECT * FROM roles WHERE rolename IN(select DISTINCT rolename from ROLES_POLICY where policyname='${policyName}')")
	List<Role> getPolicyRoles(@Param("policyName")String policyName);
	
	@Insert("INSERT INTO policy (policyname, description, agreement) VALUES (#{policyName},#{description},#{agreement})")
	void insertPolicy(Policy policy);
	
	@Update("UPDATE policy SET description = #{description}, agreement = #{agreement} WHERE policyname = #{policyName}")
	void updatePolicy(Policy policy);
	
	@Delete("DELETE FROM policy WHERE policyname = '${policyName}'")
	void deletePolicy(@Param("policyName")String policyName);
	
	@Delete("DELETE FROM roles_policy WHERE policyname = '${policyName}'")
	void deletePolicyDependencies(@Param("policyName")String policyName);

}
