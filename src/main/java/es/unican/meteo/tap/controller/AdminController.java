package es.unican.meteo.tap.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.neovisionaries.i18n.CountryCode;

import es.unican.meteo.tap.constants.GroupName;
import es.unican.meteo.tap.constants.ViewType;
import es.unican.meteo.tap.model.Group;
import es.unican.meteo.tap.model.Policy;
import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.model.User;
import es.unican.meteo.tap.model.UserRole;
import es.unican.meteo.tap.service.ContextHelper;
import es.unican.meteo.tap.service.GroupService;
import es.unican.meteo.tap.service.MailService;
import es.unican.meteo.tap.service.PolicyService;
import es.unican.meteo.tap.service.RoleService;
import es.unican.meteo.tap.service.UserService;
import es.unican.meteo.tap.util.ElementsInOptions;
import es.unican.meteo.tap.util.JtableJson;
import es.unican.meteo.tap.util.JtableJsonListResponse;
import es.unican.meteo.tap.util.JtableJsonOptionsResponse;
import es.unican.meteo.tap.util.JtableJsonResponse;
import es.unican.meteo.tap.validator.UserValidator;

/**
* Clase encargada de ejercer de controlador para Spring, basada en el modelo MVC
* */

@Controller
@RequestMapping(value="admin/*")
public class AdminController {

	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	PolicyService policyService;
	
	@Autowired
	GroupService groupService;
	
	@Autowired
	MailService mailService;
	
	@Autowired
	ContextHelper contextHelper;
	
	@Autowired
	UserValidator userValidator;
	
	/* HTML VIEW PAGES */
	
	@RequestMapping(method=RequestMethod.GET, value="/users")
	 public String userAdminHandler(Model model) throws Exception {
		model.addAttribute("role",contextHelper.getUserCredential());
		model.addAttribute("user", contextHelper.getLoggedInUser());
		return "/admin/PanelUsers";
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="/roles")
	 public String roleAdministrationHandler(Model model) throws Exception {
		model.addAttribute("role",contextHelper.getUserCredential());
		model.addAttribute("user", contextHelper.getLoggedInUser());
		return "/admin/PanelRoles";
	 }
	
	
	@RequestMapping(method=RequestMethod.GET, value="/groups")
	 public String groupAdministrationHandler(Model model) throws Exception {
		model.addAttribute("role",contextHelper.getUserCredential());
		model.addAttribute("user", contextHelper.getLoggedInUser());
		return "/admin/PanelGroups";
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="/policies")
	 public String policyAdministrationHandler(Model model) throws Exception {
		model.addAttribute("role",contextHelper.getUserCredential());
		model.addAttribute("user", contextHelper.getLoggedInUser());
		return "/admin/PanelPolicies";
	 }
	
	/*
	 * 
	 * GROUP ROLES CRUD 
	 * 
	 */
	
	@RequestMapping(method=RequestMethod.GET, value="/groupRoles")
	 public String groupAssingmentFormHandler(@RequestParam String groupName, Model model) throws Exception {
		model.addAttribute("option", new Role());
		model.addAttribute("groupName", groupName);
		model.addAttribute("availableRolesList", roleService.getRoles()); 
		model.addAttribute("groupRoles", groupService.getGroupRoles(groupName));
		return "/admin/GroupRoles";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/groupRoles")
	 public @ResponseBody String groupAssignmentFormHandler(@RequestParam String groupName, Model model, @RequestBody Map<String, Object> json) throws Exception {
		List<String> selectedRoles = Lists.newArrayList(Iterables.transform((ArrayList<Object>)json.get("selectedRoles"), objectToListOfStringsFunction));
		for(String roleName : selectedRoles){
			groupService.insertRole(groupName, roleName);
			for(User user : groupService.getUsers(groupName)){
				String subject = contextHelper.getMessage("app.mail.subject.dataset.new");
				if(roleService.hasPolicies(roleName)){
					userService.insertRole(user.getUsername(), roleName, true, false, false);
					String url = contextHelper.getGlobalProperty("tap.baseurl")+"/user/datasets";
					String message ="";
					if(!userService.getRoles(user.getUsername()).contains(new Role(roleName)))
						message = contextHelper.getMessage("app.mail.message.dataset.added",roleName, groupName, url);
					else
						message = contextHelper.getMessage("app.mail.message.dataset.accepted",roleName, groupName, url);
					mailService.sendSystemMessageEmail(Lists.newArrayList(user), subject , message);
				}else{
					userService.insertRole(user.getUsername(), roleName, true, true, false);
					String message = contextHelper.getMessage("app.mail.message.dataset.open",roleName, groupName);
					mailService.sendSystemMessageEmail(Lists.newArrayList(user), subject , message);
					mailService.sendSystemMessageEmail(Lists.newArrayList(user), contextHelper.getMessage("app.mail.subject.dataset.new"), message);
				}
			}
		}
		return "{\"message\" : \"success\"}";
	 }
	
	@RequestMapping(method=RequestMethod.DELETE, value="/group/{groupName}/role/{roleName}")
	 public @ResponseBody String deleteGroupRole(Model model, @PathVariable String roleName, @PathVariable String groupName) throws Exception {
		groupService.deleteRole(groupName, roleName);
		return "{\"message\" : \"success\"}";
	 }	
	
	/*
	 * USER GROUPS CRUD
	 */
	
	@RequestMapping(method=RequestMethod.GET, value="/userGroups")
	 public String projectAssingmentFormHandler(Model model, @RequestParam String username) throws Exception {
		User requestedUser = userService.getUserByUsername(username);
		List<Group> availableGroups = groupService.getGroups();
		model.addAttribute("option", new Group());
		model.addAttribute("user", requestedUser);
		model.addAttribute("viewType", ViewType.VIEW_ADMIN);
		model.addAttribute("availableGroupsList", Lists.newArrayList(availableGroups)); 
		model.addAttribute("userGroupsList", userService.getGroups(requestedUser.getUsername()));
		model.addAttribute("pendingOfAuthGroupsList", userService.getPendingOfAuthGroups(requestedUser.getUsername()));
		return "/admin/UserGroups";
	 }
	
	@RequestMapping(method=RequestMethod.POST, value="/userGroups")
	 public @ResponseBody String projectAssingmentFormHandler(String username, Model model, @RequestBody Map<String, Object> json) throws Exception {
		List<String> selectedGroups = Lists.newArrayList(Iterables.transform((ArrayList<Object>)json.get("selectedGroups"), objectToListOfStringsFunction));
		List<String> authorizedGroups = Lists.newArrayList(Iterables.transform((ArrayList<Object>)json.get("authorizedGroups"), objectToListOfStringsFunction));
		List<String> rejectedGroups = Lists.newArrayList(Iterables.transform((ArrayList<Object>)json.get("rejectedGroups"), objectToListOfStringsFunction));	
		List<Group> authorizationPendingGroups = userService.getPendingOfAuthGroups(username);
		for(String groupName : rejectedGroups){
			if(authorizationPendingGroups.contains(new Group(groupName)))
				mailService.sendGroupAuthorizationResponseEmail(userService.getUserByUsername(username), Lists.newArrayList(new Role()), groupName, false);
			userService.deleteGroup(username, groupName);
		}
		for(String groupName : authorizedGroups){
			userService.authorizeGroup(username, groupName, true);
			mailService.sendGroupAuthorizationResponseEmail(userService.getUserByUsername(username), groupService.getGroupDatasets(groupName), groupName, true);
		}
		for(String groupName : selectedGroups)
			userService.insertGroup(username, groupName, true, false);
		return "{\"message\" : \"success\"}";
	 }
	
	@RequestMapping(method=RequestMethod.DELETE, value="/user/{username}/group/{groupName}")
	 public @ResponseBody String deleteUserGroup(Model model, @PathVariable String username, @PathVariable String groupName) throws Exception {
		userService.deleteGroup(username, groupName);
		return "{\"message\" : \"success\"}";
	 }	
	
	/*
	 * USER ROLES CRUD
	 * 
	 */
	@RequestMapping(method=RequestMethod.GET, value="/userRoles")
	 public String roleAssingmentFormHandler(@RequestParam(required=false) String username, Model model) throws Exception {
		if(StringUtils.isNotBlank(username)){
			model.addAttribute("availableRolesList", Lists.newArrayList(roleService.getRoles()));
			model.addAttribute("pendingOfAuthRolesList", userService.getPendingOfAuthRoles(username));
			model.addAttribute("pendingOfAcceptRolesList", userService.getPendingOfAcceptanceRoles(username));
			model.addAttribute("userRolesList", userService.getRoles(username));
			model.addAttribute("user", userService.getUserByUsername(username));
		}
		return "/admin/UserRoles";
	 }
	

	
	@RequestMapping(method=RequestMethod.POST, value="/user/{username}/role")
	 public @ResponseBody String saveUserRoles(Model model, @PathVariable String username, @RequestBody Map<String, Object> json) throws Exception {
		Function<Object, Role> objectToListOfRolesFunction = new Function<Object, Role>() { 
	        public Role apply(Object o) { 
	        	return roleService.getRoleByName((String)o);			        
	        }
	    };
	    List<Role> selectedRoles = Lists.newArrayList(Iterables.transform((ArrayList<Object>)json.get("selectedRoles"), objectToListOfRolesFunction));
		List<Role> authorizedRoles = Lists.newArrayList(Iterables.transform((ArrayList<Object>)json.get("authorizedRoles"), objectToListOfRolesFunction));
		List<Role> rejectedRoles = Lists.newArrayList(Iterables.transform((ArrayList<Object>)json.get("rejectedRoles"), objectToListOfRolesFunction));
	    
		for(Role role : authorizedRoles)
			userService.authorizeRole(username, role.getRoleName(), true);
		for(Role role : rejectedRoles)
			userService.deleteRole(username, role.getRoleName());		
		List<Role> storedRoles = userService.getAllRoles(username);
		for(Role role : selectedRoles){
			if(!storedRoles.contains(role)){
				if(roleService.getPolicies(role.getRoleName()).size()>0)
					userService.insertRole(username, role.getRoleName(), true, false, true);
				else
					userService.insertRole(username, role.getRoleName(), true, true, true);
			}
		}
		return "{\"message\" : \"success\"}";
	 }	
	
	@RequestMapping(method=RequestMethod.DELETE, value="/user/{username}/role/{roleName}")
	 public @ResponseBody String deleteUserRole(Model model, @PathVariable String username, @PathVariable String roleName) throws Exception {
		userService.deleteRole(username, roleName);
		return "{\"message\" : \"success\"}";
	 }	
	
	/*
	 * 
	 * ROLE POLICIES CRUD
	 *  
	 */
	
	@RequestMapping(method=RequestMethod.GET, value="/rolePolicies")
	 public String policyAssingmentFormHandler(@RequestParam String roleName, Model model) throws Exception {
		if(StringUtils.isNotBlank(roleName)){
			model.addAttribute("availablePoliciesList", policyService.getPolicies());
			model.addAttribute("rolePoliciesList", roleService.getPolicies(roleName));
			model.addAttribute("roleName", roleService.getRoleByName(roleName).getRoleName());
		}
		return "/admin/RolePolicies";
	 }
	
	@RequestMapping(method=RequestMethod.POST, value="/rolePolicies")
	 public @ResponseBody String savePolicyAssignmentHandler(@RequestParam String roleName, @RequestBody Map<String, Object> json) throws Exception {
		Function<Object, Policy> objectToListOfPoliciesFunction = new Function<Object, Policy>() { 
	        public Policy apply(Object o) { 
	        	return policyService.getPolicyByName((String)o);			        
	        }
	    };
	    List<Policy> selectedPolicies = Lists.newArrayList(Iterables.transform((ArrayList<Object>)json.get("selectedPolicies"), objectToListOfPoliciesFunction));

	    for(Policy selectedPolicy : selectedPolicies){
	    	roleService.insertPolicy(roleName, selectedPolicy.getPolicyName());
	    	List<User> usersToModify = roleService.getRoleUsers(roleName);
			for(User user : usersToModify){
				userService.acceptRole(user.getUsername(), roleName, false);
			}
			String url = contextHelper.getGlobalProperty("tap.baseurl")+"/user/datasets";
			mailService.sendSystemMessageEmail(usersToModify, contextHelper.getMessage("app.mail.role.acceptance.subject"), contextHelper.getMessage("app.mail.message.dataset.modified", roleName, url));
	    }
	    return "{\"success\":true,\"message\" : \"success\"}";
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/role/{roleName}/policy/{policyName}")
	 public @ResponseBody String deleteRolePolicy(Model model, @PathVariable String policyName, @PathVariable String roleName) throws Exception {
		roleService.removePolicy(roleName, policyName);
		return "{\"message\" : \"success\"}";
	 }	
	
	
	/*
	 * ROLE REST SERVICES
	 * 
	 */
	 @RequestMapping(method=RequestMethod.POST, value="/getRoles")
	 public @ResponseBody JtableJsonListResponse<Role> getRolesInJsonHandler(@RequestParam(required=false) int jtStartIndex, @RequestParam(required=false) int jtPageSize){
		 JtableJsonListResponse<Role> jsonResponse = new JtableJsonListResponse<Role>();
		 jsonResponse.setRecords(roleService.getPaggedRoles(jtStartIndex, jtPageSize));
		 jsonResponse.setTotalRecordCount(roleService.getNumberOfRoles());
		 return jsonResponse;
	 }
	
	@RequestMapping(method=RequestMethod.POST, value="/createRole")
	 public @ResponseBody JtableJsonResponse<Role> createRoleHandler(@ModelAttribute("role") @Valid Role role, Model model, HttpServletRequest httpServletRequest, ServletRequest servletRequest, BindingResult result){
		JtableJsonResponse<Role> jsonResponse = new JtableJsonResponse<Role>();
		if(!result.hasErrors()){
			roleService.insertRole(role);
			jsonResponse.setRecord(role);
		}else{
			jsonResponse.setResult(contextHelper.getMessage("app.error"));
			jsonResponse.setMessage(contextHelper.getMessage("app.error.invaliduser"));
		}
		return jsonResponse;
	}

	@RequestMapping(method=RequestMethod.POST, value="/updateRole")
	 public @ResponseBody JtableJsonResponse<Role> recordRoleRequestHandler(@ModelAttribute("role") @Valid Role role, Model model, HttpServletRequest httpServletRequest, ServletRequest servletRequest, BindingResult result){
		JtableJsonResponse<Role> jsonResponse = new JtableJsonResponse<Role>();
		if(!result.hasErrors()){
			roleService.updateRole(role);
			jsonResponse.setRecord(role);
		}else{
			jsonResponse.setResult(contextHelper.getMessage("app.error"));
			jsonResponse.setMessage(contextHelper.getMessage("app.error.invalidrole"));
		}
		return jsonResponse;
	 }
	
	 @RequestMapping(method=RequestMethod.POST, value="/deleteRole")
	 public @ResponseBody JtableJson<Role> deleteRoleHandler(@RequestParam String roleName, Model model){
		 roleService.deleteRole(roleName); 
		 return new JtableJson<Role>();
	 }
	 
	 /*
	  * USERS REST SERVICES
	  * 
	  */

	 @RequestMapping(method=RequestMethod.POST, value="/getUsers")
	 public @ResponseBody JtableJsonListResponse<User> getUsersInJsonHandler(@RequestParam(required=false) String text, @RequestParam(required=false) String column, @RequestParam(required=false) int jtStartIndex, @RequestParam(required=false) int jtPageSize,@RequestParam(required=false) String jtSorting){
		 JtableJsonListResponse<User> users = new JtableJsonListResponse<User>();
	     users.setRecords(userService.getPagedUsers(jtStartIndex, jtPageSize, jtSorting, text, column));
		 users.setTotalRecordCount(userService.getNumberOfUsers());
		 return users;
	 }

	 @RequestMapping(method=RequestMethod.POST, value="/createUser")
	 public @ResponseBody JtableJsonResponse<User> createUserHandler(@ModelAttribute("user") User user, HttpServletRequest httpServletRequest, Model model, BindingResult result){
		userValidator.validate(user, result);
		JtableJsonResponse<User> jsonResponse = new JtableJsonResponse<User>();
		if(result.hasErrors()){
			jsonResponse.setResult(contextHelper.getMessage("app.error"));
			jsonResponse.setMessage(contextHelper.getMessage("app.error.invalid") + result.getAllErrors());
		}else{
			userService.insertUser(user);
			User savedUser = userService.getUserByUsername(user.getUsername());
			mailService.sendConfirmationEmail(savedUser);
			jsonResponse.setRecord(savedUser);
		}
		return jsonResponse;
	 }
	 
	 @RequestMapping(method=RequestMethod.POST, value="/updateUser")
	 public @ResponseBody JtableJsonResponse<User> updateUserHandler(@ModelAttribute("user") User user, HttpServletRequest httpServletRequest, Model model, BindingResult result){
		userValidator.validate(user, result);
		JtableJsonResponse<User> jsonResponse = new JtableJsonResponse<User>();
		if(result.hasErrors()){
			jsonResponse.setResult(contextHelper.getMessage("app.error"));
			jsonResponse.setMessage(contextHelper.getMessage("app.error.invalid") + result.getAllErrors());
		}else{
			userService.updateUser(user);
			jsonResponse.setRecord(user);
		}
		return jsonResponse;
	 }
	 
	 @RequestMapping(method=RequestMethod.POST, value="/deleteUser")
	 public @ResponseBody JtableJson<User> deleteUserHandler(@RequestParam String username, Model model){
		 userService.deleteUser(username);
		 return new JtableJson<User>();
	 }
	 

	 /*
	  * POLICIES REST SERVICES
	  * 
	  */
	 
	 @RequestMapping(method=RequestMethod.POST, value="/getPolicies")
	 public @ResponseBody JtableJsonListResponse<Policy> getPoliciesInJsonHandler(@RequestParam(required=false) int jtStartIndex, @RequestParam(required=false) int jtPageSize){
		 JtableJsonListResponse<Policy> jsonResponse = new JtableJsonListResponse<Policy>();
		 jsonResponse.setRecords(policyService.getPagedPolicies(jtStartIndex, jtPageSize));
		 jsonResponse.setTotalRecordCount(policyService.getNumberOfPolicies());
		 return jsonResponse;
	 }
	 
	 @RequestMapping(method=RequestMethod.POST, value="/createPolicy")
	 public @ResponseBody JtableJsonResponse<Policy> createPolicyHandler(@ModelAttribute("policy") Policy policy, Model model, BindingResult result){
		 policyService.insertPolicy(policy);
		 JtableJsonResponse<Policy> jsonResponse = new JtableJsonResponse<Policy>();
		 jsonResponse.setRecord(policy);
		 return jsonResponse;
	 }
	 
	 @RequestMapping(method=RequestMethod.POST, value="/updatePolicy")
	 public @ResponseBody JtableJsonResponse<Policy> updatePolicyHandler(@ModelAttribute("policy") Policy policy, @RequestParam(required=false)boolean disableRoles, Model model, BindingResult result){
		policyService.updatePolicy(policy);
		if(disableRoles){
			List<UserRole> userRolesToModify = userService.getUsersRolesByPolicy(policy.getPolicyName());
			List<User> usersToNotify = new ArrayList<User>();
			for(UserRole userRole : userRolesToModify){
				userService.acceptRole(userRole.getUsername(), userRole.getRoleName(), false);
				User user = userService.getUserByUsername(userRole.getUsername());
				if(!usersToNotify.contains(user))
					usersToNotify.add(user);
			}
			String url = contextHelper.getGlobalProperty("tap.baseurl")+"/user/datasets";
			mailService.sendSystemMessageEmail(usersToNotify, contextHelper.getMessage("app.mail.role.acceptance.subject"), contextHelper.getMessage("app.mail.message.policy.modified", policy.getPolicyName(), url));
		}
		JtableJsonResponse<Policy> jsonResponse = new JtableJsonResponse<Policy>();
		jsonResponse.setRecord(policy);
		return jsonResponse;
	 }
	 
	 
	 @RequestMapping(method=RequestMethod.POST, value="/deletePolicy")
	 public @ResponseBody JtableJson<Policy> deletePolicyHandler(@ModelAttribute("policy") Policy policy, Model model, BindingResult result){
		 policyService.deletePolicy(policy.getPolicyName());
		 return new JtableJson<Policy>();
	 }
	 
	 /*
	  * GROUPS REST SERVICES
	  * 
	  */
	 
	 @RequestMapping(method=RequestMethod.POST, value="/getGroups")
	 public @ResponseBody JtableJsonListResponse<Group> getGroupsInJsonHandler(@RequestParam(required=false) int jtStartIndex, @RequestParam(required=false) int jtPageSize){
		 JtableJsonListResponse<Group> jsonResponse = new JtableJsonListResponse<Group>();
		 jsonResponse.setRecords(groupService.getPagedGroups(jtStartIndex, jtPageSize));
		 jsonResponse.setTotalRecordCount(policyService.getNumberOfPolicies());
		 return jsonResponse;
	 }
	 
	 @RequestMapping(method=RequestMethod.POST, value="/createGroup")
	 public @ResponseBody JtableJsonResponse<Group> createGroupHandler(@ModelAttribute("group") Group group, @RequestParam(required=false) boolean isProject, Model model, BindingResult result){
		 groupService.insertGroup(group);
		 JtableJsonResponse<Group> jsonResponse = new JtableJsonResponse<Group>();
		 jsonResponse.setRecord(group);
		 return jsonResponse;
	 }
	 
	 @RequestMapping(method=RequestMethod.POST, value="/updateGroup")
	 public @ResponseBody JtableJsonResponse<Group> updateGroupHandler(@ModelAttribute("group") Group group, Model model, BindingResult result){
		 groupService.updateGroup(group);
		 JtableJsonResponse<Group> jsonResponse = new JtableJsonResponse<Group>();
		 jsonResponse.setRecord(group);
		 return jsonResponse;
	 }
	 
	 
	 @RequestMapping(method=RequestMethod.POST, value="/deleteGroup")
	 public @ResponseBody JtableJson<Group> deleteGroupHandler(@ModelAttribute("group") Group group, Model model, BindingResult result){
		 groupService.deleteGroup(group.getGroupName());
		 return new JtableJson<Group>();
	 }
	 /*
	  * ADITIONAL SERVICES
	  */
		 
	 	 
	 @RequestMapping(method=RequestMethod.GET, value="/getUserRoles")
	 public @ResponseBody JtableJsonListResponse<Role> getUserRoles(@RequestParam String username){
		 JtableJsonListResponse<Role> roles = new JtableJsonListResponse<Role>();
		 roles.setRecords(userService.getRoles(username));
		 return roles;
	 }
	 
	 @RequestMapping(method=RequestMethod.GET, value="/getPendingOfAuthUsers")
	 public @ResponseBody List<User> getPendingOfAuthUsers(){
		 return roleService.getPendingOfAuthUsers();
	 }
	 
	 @RequestMapping(method=RequestMethod.GET, value="/successMsg")
	 public @ResponseBody String sucessMsgHandler(Model model){
		 return "{\"message\" : \"success\"}";
	 }


	 
	 @RequestMapping(method=RequestMethod.GET, value="/getPolicyInfo")
	 public @ResponseBody Policy policyInfoHandler(@ModelAttribute("policyName") String policyName, Model model, BindingResult result){
		 return policyService.getPolicyByName(policyName);
	 }
	 
	
	@RequestMapping(method=RequestMethod.GET, value="/message")
	 public String policyAssingmentFormHandler(@RequestParam(required=false) String[] groupNames, Model model) throws Exception {
		List<Group> systemGroups = groupService.getGroups();
		systemGroups.add(new Group(GroupName.GROUP_NEWSLETTER.toString()));
		model.addAttribute("user", contextHelper.getLoggedInUser());
		model.addAttribute("role",contextHelper.getUserCredential());
		model.addAttribute("availableGroupsList", systemGroups);
		return "/admin/PanelMessage";
	 }
	
	@RequestMapping(method=RequestMethod.POST, value="/message")
	 public String sendSystemMessageHandler(@RequestParam String subject, @RequestParam String message, ServletRequest servletRequest, Model model) throws Exception {
		List<String> groupsToSend = Lists.newArrayList(servletRequest.getParameterValues("to[]"));
		if(groupsToSend.contains(GroupName.GROUP_USER.toString())){
			mailService.sendSystemMessageEmail(groupService.getUsers(GroupName.GROUP_USER.toString()),subject, message);
		}else if(groupsToSend.contains(GroupName.GROUP_NEWSLETTER.toString())){
			mailService.sendSystemMessageEmail(groupService.getUsers(GroupName.GROUP_USER.toString()),subject, message);
		}else{
			for(String groupName : groupsToSend)
				mailService.sendSystemMessageEmail(groupService.getUsers(groupName),subject, message);
		}
		return "redirect:/admin/message";
	 }
	
	
	 
	 @RequestMapping(method=RequestMethod.GET, value="/j_spring_security_logout")
	 public String adminLogout(){
		 return "redirect:/j_spring_security_logout";
	 }
	 
	@RequestMapping(method=RequestMethod.POST, value="/getCountryNames")
	 public @ResponseBody ElementsInOptions<JtableJsonOptionsResponse> getCountryNames(){
		List<JtableJsonOptionsResponse> countryRecords = new ArrayList<JtableJsonOptionsResponse>();
		CountryCode[] ccList = CountryCode.values();
		for(int i=0; i<ccList.length; i++){
			countryRecords.add(new JtableJsonOptionsResponse(ccList[i].getAlpha2(), ccList[i].getName()));
		}
		ElementsInOptions<JtableJsonOptionsResponse> countryNamesJson = new ElementsInOptions<JtableJsonOptionsResponse>();		
		countryNamesJson.setOptions(countryRecords);
		return countryNamesJson;
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="/resendConfirmationEmail")
	public String resendConfirmationEmailHandler(HttpServletRequest httpServletRequest, @RequestParam("username")String username){
		User user = userService.getUserByUsername(username);
		if(!user.isActive())
			mailService.sendConfirmationEmail(user);
		return "success";
	}
	
	Function<Object, String> objectToListOfStringsFunction = new Function<Object, String>() { 
        public String apply(Object o) { 
        	return (String)o;			        
        }
    };
}