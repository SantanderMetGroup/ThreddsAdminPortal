package es.unican.meteo.tap.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.web.bind.MissingServletRequestParameterException;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

	String defaultLoginUrl;
	
	
	
    public String getDefaultLoginUrl() {
		return defaultLoginUrl;
	}



	public void setDefaultLoginUrl(String defaultLoginUrl) {
		this.defaultLoginUrl = defaultLoginUrl;
	}



	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
		PrintWriter writer = response.getWriter();
		DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    	if(BadCredentialsException.class.isInstance(authenticationException)){ //unauthorized
    		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    		response.setHeader("message", authenticationException.getMessage());
    		writer.println("HTTP Status " + HttpServletResponse.SC_UNAUTHORIZED + " - " + authenticationException.getMessage());
    	}
    	if(MissingServletRequestParameterException.class.isInstance(authenticationException)){ //400 error
    		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    		writer.println("HTTP Status " + HttpServletResponse.SC_BAD_REQUEST + " - " + authenticationException.getMessage());
    	}
    	if(InsufficientAuthenticationException.class.isInstance(authenticationException)){ //UNAUTHORIZED
    		throw authenticationException;
//    		String loginURL = defaultLoginUrl;
//    		if(!StringUtils.isEmpty(request.getRequestURL())){
//    			loginURL += "?referrer=" + request.getRequestURL();
//    			if(!StringUtils.isEmpty(request.getQueryString()))
//    				loginURL += request.getQueryString();
//    		}
//    		redirectStrategy.sendRedirect(request, response, loginURL); 
    	}
    }
}