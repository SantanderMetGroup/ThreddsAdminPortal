package es.unican.meteo.tap.service;

import java.util.List;

import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.model.User;

public interface MailService {
	
	public void sendConfirmationEmail(User user);
	public void sendEmailChangedEmail(User user);
	public void sendRestorePasswordEmail(User user);

	public void sendAuthorizationRequestEmail(User user, String type, String groupName);
	public void sendGroupRequestEmail(User coordinator, User user, String groupName);
	public void sendGroupAuthorizationResponseEmail(User user, List<Role> groupDatasets, String groupName, boolean authorized);
	public void sendDatasetAcceptanceEmail(List<User> users, String policyName);
	public void sendNewMemberEmail(User user, String type, String name);
	public void sendSystemMessageEmail(List<User> users, String subject, String message);
}
