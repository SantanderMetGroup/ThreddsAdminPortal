package es.unican.meteo.tap.model;

public class Policy {
	String policyName;
	String description;
	String agreement;
	
	public Policy(){}

	public Policy(String policyName, String description, String agreement) {
		super();
		this.policyName = policyName;
		this.description = description;
		this.agreement = agreement;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAgreement() {
		return agreement;
	}

	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((agreement == null) ? 0 : agreement.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((policyName == null) ? 0 : policyName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Policy policy = (Policy)obj;
		if(this.getPolicyName().equals(policy.getPolicyName()))
			return true;
		return false;
	}
}
