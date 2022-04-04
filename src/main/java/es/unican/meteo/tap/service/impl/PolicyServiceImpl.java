package es.unican.meteo.tap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.unican.meteo.tap.dao.PolicyMapper;
import es.unican.meteo.tap.model.Policy;
import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.service.PolicyService;

@Service(value="policyService")
public class PolicyServiceImpl implements PolicyService{

	@Autowired
	PolicyMapper policyMapper;
	
	public Policy getPolicyByName(String policyName) {
		return policyMapper.getPolicyByName(policyName);
	}

	public List<Policy> getPolicies() {
		return policyMapper.getPolicies();
	}
	
	public List<Policy> getPagedPolicies(int startIndex, int pageSize) {
		return policyMapper.getPaggedPolicies(startIndex, pageSize);
	}

	public int getNumberOfPolicies() {
		return policyMapper.getNumberOfPolicies();
	}


	public void insertPolicy(Policy policy) {
		policyMapper.insertPolicy(policy);
	}

	public void updatePolicy(Policy policy) {
		policyMapper.updatePolicy(policy);
	}

	public void deletePolicy(String policyName) {
		policyMapper.deletePolicyDependencies(policyName);
		policyMapper.deletePolicy(policyName);
	}

	public List<Role> getPolicyRoles(String policyName) {
		return policyMapper.getPolicyRoles(policyName);
	}
}
