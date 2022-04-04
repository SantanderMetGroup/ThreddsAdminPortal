package es.unican.meteo.tap.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class MyAccessDeniedHandler implements AccessDeniedHandler {

	
	private String accessDeniedUrl;
 
	public MyAccessDeniedHandler() {
	}
 
	public MyAccessDeniedHandler(String accessDeniedUrl) {
		this.accessDeniedUrl = accessDeniedUrl;
	}

	@ResponseStatus(value= HttpStatus.UNAUTHORIZED)
	@ExceptionHandler({ AuthenticationException.class})
	public void handle(HttpServletRequest request,HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
	   response.sendRedirect(accessDeniedUrl);
	   request.getSession().setAttribute("message", "You do not have permission to access this page!");
	}
 
	public String getAccessDeniedUrl() {
		return accessDeniedUrl;
	}
 
	public void setAccessDeniedUrl(String accessDeniedUrl) {
		this.accessDeniedUrl = accessDeniedUrl;
	}
}