package es.unican.meteo.tap.service.impl;

import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import es.unican.meteo.tap.service.ContextHelper;
import es.unican.meteo.tap.service.MailServiceAsync;

@Service
public class MailServiceAsyncImpl implements MailServiceAsync{
	
	private static final Logger logger = Logger.getLogger(MailServiceAsyncImpl.class);
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	private ContextHelper contextHelper;

	public void sendMail(String from, String to, String subject, String body){
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		try{
			mailSender.send(message);
       }catch(MailException e){
    	   logger.error("Mail exception sending the email to "+ to + "\n Exception: " + e.getMessage());
       }
	}

	@Async
	public void sendMimeMail(final String from, final String to, final String subject, final String templateURI, final Map<String, Object> model){
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                message.setTo(to);
                message.setFrom(from);
                message.setReplyTo(from);
                message.setSubject(subject);
                String body = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateURI, "UTF-8", model);
                message.setText(body, true);
            }
        };
       try{
    	   this.mailSender.send(preparator);
       }catch(MailException e){
    	   logger.error("Mail exception sending the email to "+ to + "\n Exception: " + e.getMessage());
       }
	}
}
