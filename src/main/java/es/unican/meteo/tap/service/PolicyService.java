package es.unican.meteo.tap.service;

import java.util.List;

import es.unican.meteo.tap.model.Policy;
import es.unican.meteo.tap.model.Role;

public interface PolicyService {
	Policy getPolicyByName(String policyName);
	List<Policy> getPolicies();
	List<Role> getPolicyRoles(String policyName);//gets all roles associated with this policy
	List<Policy> getPagedPolicies(int startIndex, int pageSize);
	int getNumberOfPolicies();
	void insertPolicy(Policy policy);
	void updatePolicy(Policy policy);
	void deletePolicy(String policyName);
}
