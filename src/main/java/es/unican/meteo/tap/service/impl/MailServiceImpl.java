package es.unican.meteo.tap.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.model.User;
import es.unican.meteo.tap.service.ContextHelper;
import es.unican.meteo.tap.service.MailService;
import es.unican.meteo.tap.service.MailServiceAsync;

@Service
public class MailServiceImpl implements MailService{
	
	private static final Logger logger = Logger.getLogger(MailServiceImpl.class);
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	private ContextHelper contextHelper;
	
	@Autowired
	private MailServiceAsync mailService;
	
	/*
	 * email verification. store the MD5 code in USER.verification and sends an email with the url to activate himself
	 */
	public void sendConfirmationEmail(User user){
		String subject = contextHelper.getMessage("app.mail.emailconfirm.subject");
		String url = contextHelper.getGlobalProperty("tap.baseurl") + "/confirm?username="+user.getUsername()+"&token="+user.getToken();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("user", user);
		model.put("link",url);
		mailService.sendMimeMail(contextHelper.getGlobalProperty("tap.email.noreply"), user.getEmail(), subject, "templates/RegisterConfirmation.vm", model);
	}
	
	public void sendEmailChangedEmail(User user){
		String subject = contextHelper.getMessage("app.mail.emailconfirm.subject");
		String url = contextHelper.getGlobalProperty("tap.baseurl") + "/confirm?username="+user.getUsername()+"&token="+user.getToken();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("user", user);
		model.put("link",url);
		mailService.sendMimeMail(contextHelper.getGlobalProperty("tap.email.noreply"), user.getEmail(), subject, "templates/RegisterConfirmation.vm", model);
	}

	public void sendRestorePasswordEmail(User user) {
		String subject = contextHelper.getMessage("app.mail.emailrestore.subject");
		String url = contextHelper.getGlobalProperty("tap.baseurl") + "/reset?username="+user.getUsername()+"&token="+user.getToken();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("user", user);
		model.put("link",url);
		mailService.sendMimeMail(contextHelper.getGlobalProperty("tap.email.noreply"), user.getEmail(), subject, "templates/PasswordRequest.vm", model);
	}
	
	public void sendAuthorizationRequestEmail(User user, String type, String groupName) {
		String from = contextHelper.getGlobalProperty("tap.email.noreply");
		String subject = contextHelper.getMessage("app.mail.request.subject", type);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("user", user);
		model.put("groupName", groupName);
		model.put("type", type);
		for(String email : contextHelper.getGlobalPropertyList("tap.managers.email"))
			mailService.sendMimeMail(from, email, subject, "templates/UserRequest.vm", model);
	}
	
	public void sendGroupRequestEmail(User coordinator, User user, String groupName) {
		String subject = contextHelper.getMessage("app.mail.request.subject","Group");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("coordinatorName", coordinator.getFirstName());
		model.put("user", user);
		model.put("groupName", groupName);
		model.put("authorizationLink", contextHelper.getGlobalProperty("tap.baseurl") + "/rest/user/" + user.getUsername() + "/group/" + groupName + "?token=" + coordinator.getToken() + "&authorized=true");
		model.put("rejectionLink", contextHelper.getGlobalProperty("tap.baseurl") + "/rest/user/" + user.getUsername() + "/group/" + groupName + "?token=" + coordinator.getToken() + "&authorized=false");
		mailService.sendMimeMail(contextHelper.getGlobalProperty("tap.email.noreply"), coordinator.getEmail(), subject, "templates/GroupAuthorization.vm", model);
	}
	
	public void sendGroupAuthorizationResponseEmail(User user, List<Role> groupDatasets, String groupName, boolean authorized){
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("user", user);
		model.put("datasetList", groupDatasets);
		model.put("groupName", groupName);
		model.put("link", contextHelper.getGlobalProperty("tap.baseurl") + "/user/home");
		if(authorized){
			mailService.sendMimeMail(contextHelper.getGlobalProperty("tap.email.noreply"), user.getEmail(), contextHelper.getMessage("app.mail.authorized.subject", groupName), "templates/GroupAuthorized.vm", model);
		}else{
			model.put("type", contextHelper.getMessage("app.group.title"));
			mailService.sendMimeMail(contextHelper.getGlobalProperty("tap.email.noreply"), user.getEmail(), contextHelper.getMessage("app.mail.rejected.subject", groupName), "templates/UserRequestRejected.vm", model);
		}
	}

	public void sendDatasetAcceptanceEmail(List<User> users, String policyName) {
		String subject = contextHelper.getMessage("app.mail.acceptance.subject", contextHelper.getMessage("app.dataset.title"));
		for(User user : users){
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("user", user);
			model.put("policyName", policyName);
			model.put("link", contextHelper.getGlobalProperty("tap.baseurl") + "/user/home");
			mailService.sendMimeMail(contextHelper.getGlobalProperty("tap.email.noreply"), user.getEmail(), subject, "templates/DatasetAcceptance.vm", model);
		}
	}

	public void sendNewMemberEmail(User user,  String type, String name) {
		String from = contextHelper.getGlobalProperty("tap.email.noreply");
		String subject = contextHelper.getMessage("app.mail.newmember.subject", type);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("user", user);
		model.put("type", type);
		model.put("name", name);
		for(String email : contextHelper.getGlobalPropertyList("tap.managers.email"))
			mailService.sendMimeMail(from, email, subject, "templates/NewMember.vm", model);
	}

	public void sendSystemMessageEmail(List<User> users, String subject, String message) {
		for(User user : users){
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("user", user);
			model.put("message", message);
			mailService.sendMimeMail(contextHelper.getGlobalProperty("tap.email.noreply"), user.getEmail(), contextHelper.getMessage("app.mail.basic.subject", subject), "templates/SystemMessage.vm", model);
		}
	}
	
}
