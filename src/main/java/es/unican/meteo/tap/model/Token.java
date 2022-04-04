package es.unican.meteo.tap.model;

import java.sql.Timestamp;

import es.unican.meteo.tap.constants.ServiceType;

public class Token {
	
	private String hash;
	private String credential;
	private Timestamp expiration;
	private ServiceType service;
		
	public Token() {}
	
	public Token(String hash, String credential, Timestamp expiration, ServiceType service) {
		this.hash = hash;
		this.credential = credential;
		this.expiration = expiration;
		this.service = service;
	}

	public String getHash() {
		return hash;
	}
	
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public String getCredential() {
		return credential;
	}
	
	public void setCredential(String credential) {
		this.credential = credential;
	}

	public ServiceType getService() {
		return service;
	}

	public void setService(ServiceType service) {
		this.service = service;
	}

	public Timestamp getExpiration() {
		return expiration;
	}

	public void setExpiration(Timestamp expiration) {
		this.expiration = expiration;
	}

}