package es.unican.meteo.tap.service.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.unican.meteo.tap.constants.ServiceType;
import es.unican.meteo.tap.dao.TokenMapper;
import es.unican.meteo.tap.model.Token;
import es.unican.meteo.tap.service.TokenService;
import es.unican.meteo.tap.service.UserService;

@Service
public class TokenServiceImpl implements TokenService{

	@Autowired
	TokenMapper tokenMapper;
	
	@Autowired
	UserService userService;
	
	private final int defaultExpiration = 2;
	
	@Override
	public List<Token> getTokens() {
		return tokenMapper.getTokens();
	}

	@Override
	public Token getTokenByHash(String hash) {
		return tokenMapper.getTokenByHash(hash);
	}

	@Override
	public Token generateToken(String credential, ServiceType service) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, defaultExpiration);
		Token token = new Token(DigestUtils.sha256Hex(credential + calendar.getTime()), credential,new Timestamp( calendar.getTime().getTime()), service);
		List<Token> storedTokens = tokenMapper.getTokensByCredentialAndService(credential, service);
		for(Token storedToken : storedTokens)
			tokenMapper.deleteToken(storedToken.getCredential(),storedToken.getService());
		tokenMapper.insertToken(token);
		return tokenMapper.getTokenByHash(token.getHash());
	}

	@Override
	public void deleteToken(String credential, ServiceType service) {
		tokenMapper.deleteToken(credential, service);
	}

	@Override
	public boolean checkValidity(String hash, String credential) {
		Token token = tokenMapper.getTokenByHash(hash);
		if(token == null)
			return false;
		Date currentTime = new Date(System.currentTimeMillis());
		Date expDate = new Date(token.getExpiration().getTime());
		if(token.getCredential().equals(credential) && currentTime.compareTo(expDate) <= 0)
			return true;
		return false;
	}

}