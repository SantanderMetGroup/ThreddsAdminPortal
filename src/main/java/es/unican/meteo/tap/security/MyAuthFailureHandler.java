package es.unican.meteo.tap.security;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.StringUtils;

import es.unican.meteo.tap.constants.ServiceType;
import es.unican.meteo.tap.model.Token;
import es.unican.meteo.tap.model.User;
import es.unican.meteo.tap.service.TokenService;
import es.unican.meteo.tap.service.UserService;

public class MyAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler{

	@Autowired
	UserService userService;
	
	@Autowired
	TokenService tokenService;
	
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException{
		DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
		if(exception.getClass().isAssignableFrom(UsernameNotFoundException.class)) {
			OpenIDAuthenticationToken openIdAuth = (OpenIDAuthenticationToken)exception.getAuthentication();
			request.getSession(true).setAttribute("openid", openIdAuth.getPrincipal().toString());
			Iterator<OpenIDAttribute> attributesIterator = openIdAuth.getAttributes().iterator();
			String parameters = "";
			while(attributesIterator.hasNext()){
				OpenIDAttribute attribute = attributesIterator.next();
				if(!StringUtils.isEmpty(attribute.getValues().get(0)))
					//request.getSession(true).setAttribute(attribute.getName(), attribute.getValues().get(0));
					parameters += "&"+attribute.getName()+"="+attribute.getValues().get(0);
			}
			// redirect to create account page
			Token token = tokenService.generateToken(openIdAuth.getPrincipal().toString(), ServiceType.OPENID);
			redirectStrategy.sendRedirect(request, response, "/register?hash="+token.getHash()+parameters);
		}else if(exception.getClass().isAssignableFrom(BadCredentialsException.class)){
			if(exception.getAuthentication().getClass().isAssignableFrom(OpenIDAuthenticationToken.class)){
				User user = userService.getUserByOpenID(((OpenIDAuthenticationToken)exception.getAuthentication()).getName());
				if(user!= null && !user.isActive())
					redirectStrategy.sendRedirect(request, response, "/signin?error=Activate your account");
			}else{
				User user = userService.getUserByUsername(((UsernamePasswordAuthenticationToken)exception.getAuthentication()).getName());
				if(!StringUtils.isEmpty(user)){
					if(!user.isActive())
						redirectStrategy.sendRedirect(request, response, "/signin?error=Activate your account");
					else
						redirectStrategy.sendRedirect(request, response, "/signin?error=Invalid password");
				}else{
					redirectStrategy.sendRedirect(request, response, "/signin?error=Invalid username");
				}
			}
		}else if(exception.getCause().getClass().isAssignableFrom(CannotGetJdbcConnectionException.class)){
			redirectStrategy.sendRedirect(request, response, "/signin?error=Database connection unavailable");
		}else{
			redirectStrategy.sendRedirect(request, response, "/signin?error="+exception.getMessage());
		}
	}
}
