package es.unican.meteo.tap.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import com.google.common.collect.Lists;

public class MyAuthenticationProcessingFilter extends GenericFilterBean {

    private final Collection<String> nonTokenAuthUrls = Lists.newArrayList("/dp/rest");
    private final String authenticationURL = "/dp/rest/authenticate";
    
    AuthenticationManager authenticationManager;
    AuthenticationEntryPoint authenticationEntryPoint;


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        try{
//        	if(authenticationURL.equals(httpRequest.getRequestURI())){
//        			if(httpRequest.getParameter("username") == null)
//        				throw new BadCredentialsException("Missing username");
//        			else if(httpRequest.getParameter("password") == null)
//        				throw new BadCredentialsException("Missing password");
//        	}else if(!nonTokenAuthUrls.contains(httpRequest.getRequestURI())){ //Auth by token
//	        	 String hash = httpRequest.getHeader("token");
//	             UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(hash, null);
//	             authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
//	             SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));
//	        }
        	//Check cookies and add
        	Cookie[] cookies = httpRequest.getCookies();
        	if(cookies.length ==0){
        		final Cookie sessionCookie = new Cookie("JSESSIONID", httpRequest.getSession().getId());
    			sessionCookie.setMaxAge(-1);
    			sessionCookie.setSecure(false);
    			sessionCookie.setPath("/");
    			httpResponse.addCookie(sessionCookie);
        	}
        }catch(AuthenticationException authenticationException){
        	SecurityContextHolder.clearContext();
        	authenticationEntryPoint.commence(httpRequest, httpResponse, authenticationException);
        }
         //response.reset();
         chain.doFilter(request, response); 
    }


	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}


	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}


	public AuthenticationEntryPoint getAuthenticationEntryPoint() {
		return authenticationEntryPoint;
	}


	public void setAuthenticationEntryPoint(
			AuthenticationEntryPoint authenticationEntryPoint) {
		this.authenticationEntryPoint = authenticationEntryPoint;
	}


    

}