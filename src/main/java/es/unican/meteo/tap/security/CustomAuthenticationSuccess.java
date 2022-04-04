package es.unican.meteo.tap.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import es.unican.meteo.tap.service.ContextHelper;

public class CustomAuthenticationSuccess implements AuthenticationSuccessHandler{

	@Autowired
	ContextHelper contextHelper;
	
	@Autowired
	CustomRememberMeServices rememberMe;
	
	private String defaultTargetUrl;
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private final String OPENID_SECURITY_URL = "/j_spring_openid_security_check";
	public CustomAuthenticationSuccess(){}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		if(request.getServletPath().equals(OPENID_SECURITY_URL)){
			rememberMe.generateRememberMeCookieManually(request, response, authentication);
		}
		String referer = request.getParameter("referrer");
		response.addHeader("username", contextHelper.getLoggedInUser().getUsername());
		if(!StringUtils.isEmpty(referer))
			redirectStrategy.sendRedirect(request, response, referer);
		else
			redirectStrategy.sendRedirect(request, response, defaultTargetUrl);
	}

	public RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	public String getDefaultTargetUrl() {
		return defaultTargetUrl;
	}

	public void setDefaultTargetUrl(String defaultTargetUrl) {
		this.defaultTargetUrl = defaultTargetUrl;
	}
	
	
}
