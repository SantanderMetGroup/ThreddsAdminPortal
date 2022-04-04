package es.unican.meteo.tap.service;

import java.util.List;
import java.util.Map;

import es.unican.meteo.tap.constants.RoleName;
import es.unican.meteo.tap.model.User;

public interface CustomUserDetails {
	public String getMessage(String key);
	public String getMessage(String key, Object... params);
	public String getGlobalProperty(String key);
	public List<String> getGlobalPropertyList(String key);
	public User getLoggedInUser();
	public RoleName getUserCredential();
	public boolean isUserAdmin(String username);
	public Map<String, String> getCountryCodesMap();
	public Boolean isRecaptchaValid(String remoteIp, String challenge, String response);
}
