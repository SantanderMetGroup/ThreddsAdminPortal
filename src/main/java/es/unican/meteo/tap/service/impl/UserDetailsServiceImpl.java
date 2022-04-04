package es.unican.meteo.tap.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;

import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.service.UserService;

public class UserDetailsServiceImpl implements UserDetailsService, AuthenticationUserDetailsService<OpenIDAuthenticationToken>{
	
	@Autowired
	UserService userService;
	
    private Map<String, String> usersToCookies = new HashMap<String, String>();
    private Map<String, String> cookiesToUsers = new HashMap<String, String>();
    private Map<String, Date> sessionValidity = new HashMap<String, Date>();

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		es.unican.meteo.tap.model.User user = userService.getUserByUsername(username);
		if(user == null)
			throw new UsernameNotFoundException("User does not exist");
		return new User(user.getUsername(), user.getPassword(), user.isActive(), true, true, true, getGrantedAuthorities(username));
	}
	
	public List<GrantedAuthority> getGrantedAuthorities(String username) {
	    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	    for (Role role : userService.getAllRoles(username)) {
	        authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
	    }
	    return authorities;
	}

	public UserDetails loadUserDetails(OpenIDAuthenticationToken arg0) throws UsernameNotFoundException, InternalAuthenticationServiceException {
		try{
			es.unican.meteo.tap.model.User user = userService.getUserByOpenID(arg0.getPrincipal().toString());
			if(user == null)
				throw new UsernameNotFoundException("User does not exist");
			if(!user.isActive())
				throw new BadCredentialsException("User does not exist");
			return new User(user.getUsername(), user.getPassword(), user.isActive(), true, true, true, getGrantedAuthorities(user.getUsername()));
		}catch(MyBatisSystemException e){
			throw new InternalAuthenticationServiceException(e.getMessage(),e.getCause().getCause());
		}
	}
	
	public String createSSOSesion(String username){
	        String newCookieValue = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + username;
	        String oldCookie = usersToCookies.get(username);
	        removeSSOSession(oldCookie);
	        cookiesToUsers.put(newCookieValue, username);
	        Date now = new Date();
	        Date sessionValidityTime = new Date(now.getTime() + 30 * 1000);
	        sessionValidity.put(newCookieValue, sessionValidityTime);
	        return newCookieValue;
	}
	
    public void removeSSOSession(String cookie)
    {
        if (cookie != null)
        {
            logger.debug("Removing SSO session: " + cookie);
            String username = cookiesToUsers.get(cookie);
            if (username != null)
                usersToCookies.remove(username);
            cookiesToUsers.remove(cookie);
            sessionValidity.remove(cookie);
        }
    }
    
    public UserDetails loadUserByCookie(String cookie) throws UsernameNotFoundException, DataAccessException
    {
        logger.debug("Loading user by cookie: " + cookie);
        Date sessionValidUntil = sessionValidity.get(cookie);
        Date now = new Date();
        logger.debug("Session valid until: " + sessionValidUntil + " now: " + now);
        if (sessionValidUntil == null || now.after(sessionValidUntil))
        {
            logger.debug("Session no longer valid.");
            cookie = null;
        }
        return loadUserByUsername(cookiesToUsers.get(cookie));
    }
    
	private static final Logger logger = Logger.getLogger(UserDetailsServiceImpl.class);
	
}
