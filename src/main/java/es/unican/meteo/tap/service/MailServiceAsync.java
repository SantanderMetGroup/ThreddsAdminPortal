package es.unican.meteo.tap.service;

import java.util.List;
import java.util.Map;

public interface MailServiceAsync {
	
	public void sendMail(String from, String to, String subject, String body);
	public void sendMimeMail(final String from, final String to, final String subject, final String templateURI, final Map<String, Object> model);
}
