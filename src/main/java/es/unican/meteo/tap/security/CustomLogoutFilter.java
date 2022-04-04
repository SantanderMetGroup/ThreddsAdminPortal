package es.unican.meteo.tap.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class CustomLogoutFilter extends LogoutFilter {

	public CustomLogoutFilter(LogoutSuccessHandler logoutSuccessHandler,
			LogoutHandler[] handlers) {
		super(logoutSuccessHandler, handlers);
		// TODO Auto-generated constructor stub
	}
	
	  @Override
	    protected boolean requiresLogout(HttpServletRequest request, HttpServletResponse response)
	    {
	        logger.debug("UmsLogoutFilter.requiresLogout");
	        // Normal logout processing (i.e. detect logout URL)
	        if (super.requiresLogout(request, response))
	            return true;
	        // If SSO cookie is stale, clear session contents
	        String cookieName = "JSESSIONID";
	        HttpSession session = request.getSession();
	        String sessionSso = (String) request.getSession().getAttribute(cookieName);
	        if (sessionSso != null)
	        {
	            String browserSso = getCookieValue(request, cookieName);
	            if (!sessionSso.equals(browserSso))
	            {
	                logger.debug("Invalidating stale session: " + sessionSso);
	                session.invalidate();
	            }
	        }
	        return false;
	    }

	    protected String getCookieValue(HttpServletRequest request, String cookieName)
	    {
	        String cookieValue = null;
	        Cookie[] cookies = request.getCookies();
	        if (cookies != null)
	            for (Cookie cookie : cookies)
	                if (cookie.getName().equals(cookieName))
	                {
	                    cookieValue = cookie.getValue();
	                    break;
	                }
	        return cookieValue;
	    }

}
