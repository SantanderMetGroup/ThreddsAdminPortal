package es.unican.meteo.tap.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neovisionaries.i18n.CountryCode;

import es.unican.meteo.tap.constants.RoleName;
import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.model.User;
import es.unican.meteo.tap.service.ContextHelper;
import es.unican.meteo.tap.service.RoleService;
import es.unican.meteo.tap.service.UserService;

@Service
public class ContextHelperImpl implements ContextHelper{

	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;
	
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("locale/messages", LocaleContextHolder.getLocale());

	public String getMessage(String key) {
		return resourceBundle.getString(key);
	}
	
	public String getMessage(String key, Object... params){
		return MessageFormat.format(getMessage(key), params);
	}
	
	public User getLoggedInUser(){
		return userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
	}
	
	public boolean isLoggedInUserAdmin(){
		return userService.getRoles(getLoggedInUser().getUsername()).contains(roleService.getRoleByName(RoleName.ROLE_ADMIN.name()));
	}
	
	public boolean isUserAdmin(String username){
		return userService.getRoles(username).contains(roleService.getRoleByName(RoleName.ROLE_ADMIN.name()));
	}
	
	public String getGlobalProperty(String key){
		Properties prop = new Properties();
		InputStream in = getClass().getResourceAsStream("/global_variables.properties");
		try {
			prop.load(in);
			return prop.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> getGlobalPropertyList(String key){
		Properties prop = new Properties();
		InputStream in = getClass().getResourceAsStream("/global_variables.properties");
		try {
			prop.load(in);
			return Lists.newArrayList(prop.getProperty(key).split(","));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String, String> getCountryCodesMap(){
		Map<String, String> countries = new LinkedHashMap<String, String>();
		CountryCode[] ccList = CountryCode.values();
		for(CountryCode cc : ccList){
			if(cc.getAlpha2()!="AX" && cc.getAlpha2()!="CW")
					countries.put(cc.getAlpha2(), cc.getName());
		}
		return countries;
	}

	public Boolean isRecaptchaValid(String remoteIp, String response) {
		//Build parameter string
		String secret = getGlobalProperty("recaptcha.privatekey");
		String url = getGlobalProperty("recaptcha.verificationurl");
        //Send the request
    	CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    	HttpPost httpPost = new HttpPost(url);
    	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    	postParameters.add(new BasicNameValuePair("secret", secret));
    	postParameters.add(new BasicNameValuePair("remoteip", remoteIp));
    	postParameters.add(new BasicNameValuePair("response", response));
    	try {
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			String json = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			JsonObject jsonObj = new JsonParser().parse(json).getAsJsonObject();
			return jsonObj.get("success").getAsBoolean();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
	}
	

	public RoleName getUserCredential() {
		User loggedInUser = getLoggedInUser();
		List<Role> userRoles = userService.getRoles(loggedInUser.getUsername());
		if(userRoles.contains(new Role(RoleName.ROLE_ADMIN.name()))){
			return RoleName.ROLE_ADMIN;
		}else if(userRoles.contains(new Role(RoleName.ROLE_MANAGER.name()))){
			return RoleName.ROLE_MANAGER;
		}
		return RoleName.ROLE_USER;
	}
}
