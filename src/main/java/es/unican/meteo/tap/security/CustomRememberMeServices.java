package es.unican.meteo.tap.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.util.ReflectionUtils;

import es.unican.meteo.tap.service.impl.UserDetailsServiceImpl;

public class CustomRememberMeServices extends PersistentTokenBasedRememberMeServices{

	@Autowired
	UserDetailsServiceImpl userDetailsService;


    public CustomRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
    	super(key, userDetailsService, tokenRepository);
    }
    
    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
//        request.getSession().setAttribute("username", "vegasm");
//    	String cookieValue = encodeCookie(tokens);
//        Cookie cookie = new Cookie(getCookieName(), cookieValue);
//        cookie.setMaxAge(maxAge);
//        cookie.setDomain("." + request.getServerName());
//        cookie.setPath("/");
//        cookie.setSecure(false);
//        cookie.setHttpOnly(false);
//        response.addCookie(cookie);
    }
    
    @Override
    protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
//        Cookie cookie = new Cookie(getCookieName(), null);
//        cookie.setMaxAge(0);
//        cookie.setPath("/");
//        response.addCookie(cookie);
    }

    public void generateRememberMeCookieManually(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication){
    	//onLoginSuccess(request, response, successfulAuthentication);
    }
}
