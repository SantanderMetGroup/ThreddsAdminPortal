package es.unican.meteo.tap.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import es.unican.meteo.tap.constants.ServiceType;
import es.unican.meteo.tap.model.Token;

public interface TokenMapper {
	
	@Select("SELECT * FROM tokens")
	List<Token> getTokens();
	
	@Select("SELECT * FROM tokens where hash = '${hash}'")
	Token getTokenByHash(@Param("hash")String hash);
	
	@Select("SELECT * FROM tokens where credential = '${credential}'")
	List<Token> getTokensByCredential(@Param("credential")String credential);
	
	@Select("SELECT * FROM tokens where credential = '${credential}' and service = '${service}'")
	List<Token> getTokensByCredentialAndService(@Param("credential")String credential,@Param("service")ServiceType service);
	
	@Insert("INSERT INTO tokens (hash, credential, expiration, service) values (#{hash},#{credential},#{expiration},#{service})")
	void insertToken(Token token);
	
	@Delete("DELETE FROM tokens WHERE credential = '${credential}' AND service = '${service}'")
	void deleteToken(@Param("credential")String credential, @Param("service")ServiceType service);
	
}