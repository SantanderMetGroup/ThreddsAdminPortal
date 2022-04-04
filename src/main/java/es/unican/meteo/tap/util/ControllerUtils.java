package es.unican.meteo.tap.util;

import javax.servlet.http.HttpServletRequest;

public class ControllerUtils {

	public static String getBaseURL(HttpServletRequest httpServletRequest){
		if((httpServletRequest.getScheme().equals("http") && httpServletRequest.getServerPort() == 80) || (httpServletRequest.getScheme().equals("https") && httpServletRequest.getServerPort() == 443))
			return String.format("%s://%s%s/",httpServletRequest.getScheme(),  httpServletRequest.getServerName(), httpServletRequest.getContextPath());
		return String.format("%s://%s:%d%s/",httpServletRequest.getScheme(),  httpServletRequest.getServerName(), httpServletRequest.getServerPort(), httpServletRequest.getContextPath());
	}
}
