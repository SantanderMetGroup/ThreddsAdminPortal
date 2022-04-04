package es.unican.meteo.tap.service;

import java.util.List;

import es.unican.meteo.tap.constants.ServiceType;
import es.unican.meteo.tap.model.Token;

public interface TokenService {
	
	List<Token> getTokens();
	
	Token getTokenByHash(String hash);
	
	Token generateToken(String credential, ServiceType service);
	
	void deleteToken(String credential, ServiceType service);
	
	boolean checkValidity(String hash, String credential);

}
