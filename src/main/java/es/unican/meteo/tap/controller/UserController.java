package es.unican.meteo.tap.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.SendFailedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import es.predictia.util.Metadata;
import es.unican.meteo.tap.constants.Variables;
import es.unican.meteo.tap.model.Group;
import es.unican.meteo.tap.model.Policy;
import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.model.RoleDataset;
import es.unican.meteo.tap.model.User;
import es.unican.meteo.tap.service.ContextHelper;
import es.unican.meteo.tap.service.GroupService;
import es.unican.meteo.tap.service.MailService;
import es.unican.meteo.tap.service.PolicyService;
import es.unican.meteo.tap.service.RoleService;
import es.unican.meteo.tap.service.UserService;
import es.unican.meteo.tap.util.JtableJsonOptionsResponse;
import es.unican.meteo.tap.validator.UserValidator;

@Controller
@RequestMapping(value="user/*")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;	
	
	@Autowired
	GroupService groupService;
	
	@Autowired
	PolicyService policyService;
	
	@Autowired
	ContextHelper contextHelper;
	
	@Autowired
	MailService mailService;
	
	@Autowired
	UserValidator userValidator;

	@RequestMapping(method=RequestMethod.GET, value="")
	 public String userHandler(HttpServletRequest arg0, HttpServletResponse arg1, Model model) throws Exception {
		return "redirect:home";
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="/home")
	 public String userPanelHandler(HttpServletRequest arg0, HttpServletResponse arg1, Model model) throws Exception {
		User loggedInUser = contextHelper.getLoggedInUser();
		model.addAttribute("role",contextHelper.getUserCredential());
		model.addAttribute("user", loggedInUser);
		model.addAttribute("pendingOfAcceptRoles", userService.getPendingOfAcceptanceRoles(loggedInUser.getUsername()));
		model.addAttribute("pendingOfAuthUsers", roleService.getPendingOfAuthUsers());
		return "/user/Home";
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="/account")
	 public String editUserHandler(Model model) throws Exception {
		User loggedInUser = contextHelper.getLoggedInUser();
		loggedInUser.setConfirmPassword(loggedInUser.getPassword());
		model.addAttribute("role",contextHelper.getUserCredential());
		model.addAttribute("user", loggedInUser);
		model.addAttribute("countryList", contextHelper.getCountryCodesMap());
		return "/user/Account";
	 }
	
	/*
	 * Account POST is split in 3 parts. Email - Password and others
	 */
	@RequestMapping(method=RequestMethod.POST, value="/account")
	 public String editUserHandler(@ModelAttribute("user") User user, String action, HttpServletRequest httpServletRequest, Model model, BindingResult result){
		User loggedInUser = contextHelper.getLoggedInUser();
		model.addAttribute("countryList", contextHelper.getCountryCodesMap());
		model.addAttribute("user", loggedInUser);
		model.addAttribute("role",contextHelper.getUserCredential());
		if(action.equals("change_email")){
			userValidator.validateEmail(user, result);
			loggedInUser.setActive(false);
			loggedInUser.setEmail(user.getEmail());
			if(!result.hasErrors()){
				mailService.sendEmailChangedEmail(loggedInUser);
				userService.updateUser(loggedInUser);
			}
		}else if(action.equals("change_password")){
			userValidator.validatePassword(user, result);
			if(!result.hasErrors()) userService.changeUserPassword(loggedInUser.getUsername(), user.getPassword());
		}else{
			userValidator.validatePersonalDetails(user, result);
			userValidator.validateResearchDetails(user, result);
			if(!result.hasErrors()){
				loggedInUser.setFirstName(user.getFirstName());
				loggedInUser.setLastName(user.getLastName());
				loggedInUser.setIsoCode(user.getIsoCode());
				loggedInUser.setInstitution(user.getInstitution());
				loggedInUser.setMotivation(user.getMotivation());
				loggedInUser.setNewsletter(user.isNewsletter());
				userService.updateUser(loggedInUser);
				model.addAttribute("success",true);
				return "/user/Account";
			}
		}
		if(userValidator.generateErrorsMap(model, result))
			return "/user/Account";
		return "redirect:../j_spring_security_logout";
	 }
	
	
	@RequestMapping(method=RequestMethod.GET, value="/datasets")
	 public String roleAssingmentFormHandler(Model model) throws Exception {
		User loggedInUser = contextHelper.getLoggedInUser();
		Function<Role, JtableJsonOptionsResponse> roleToListOption = new Function<Role, JtableJsonOptionsResponse>(){
			public JtableJsonOptionsResponse apply(Role role) {
			    return new JtableJsonOptionsResponse(role.getRoleName(), role.getRoleName());
			}
		};
		List<Role> availableRoles = roleService.getDatasets();
		List<Role> rolesWithPoliciesList = roleService.getRolesWithPolicies();
		rolesWithPoliciesList.retainAll(Lists.newArrayList(availableRoles));
		model.addAttribute("role",contextHelper.getUserCredential());
		model.addAttribute("user", userService.getUserByUsername(loggedInUser.getUsername()));
		model.addAttribute("availableRolesList", availableRoles);
		model.addAttribute("rolesWithPoliciesList", rolesWithPoliciesList);
		model.addAttribute("pendingOfAuthRolesList", userService.getPendingOfAuthRoles(loggedInUser.getUsername()));
		model.addAttribute("pendingOfAcceptRolesList", Lists.newArrayList(Iterables.transform(userService.getPendingOfAcceptanceRoles(loggedInUser.getUsername()), roleToRoleDatasetFunction)));
		model.addAttribute("userRolesList", Lists.newArrayList(Iterables.transform(userService.getDatasets(loggedInUser.getUsername()), roleToRoleDatasetFunction)));
		return "/user/Datasets";
	 }

	@Deprecated
	@RequestMapping(method=RequestMethod.POST, value="/datasets")
	 public @ResponseBody String saveRolesHandler(@RequestParam String username, @RequestBody Map<String, Object> json, HttpServletRequest httpServletRequest) throws Exception {
		List<String> acceptedDatasets = Lists.newArrayList(Iterables.transform((ArrayList<Object>)json.get("acceptedDatasets"), objectToListOfStringsFunction));
		List<String> declinedDatasets = Lists.newArrayList(Iterables.transform((ArrayList<Object>)json.get("declinedDatasets"), objectToListOfStringsFunction));	
		//Deleted roles
		for(String roleName : declinedDatasets)
				userService.deleteRole(username, roleName);
		//Accepted roles
		for(String roleName : acceptedDatasets){
			if(userService.getPendingOfAcceptanceRoles(username).contains(new Role(roleName)))
				userService.acceptRole(username, roleName, true);
		}		
		return "{\"message\" : \"success\"}";
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="/groups")
	 public String getUserGroups(@RequestParam(required=false) String dataset, @RequestParam(required=false) List<Group> group, Model model) throws Exception {
		User loggedInUser = contextHelper.getLoggedInUser();
		List<Group> availableGroupList = groupService.getGroupProjects();
		if(group != null && !group.isEmpty())
			availableGroupList.retainAll(group);
		List<RoleDataset> availableRoleDatasets = Lists.newArrayList(Iterables.transform(roleService.getDatasets(), roleToRoleDatasetFunction));
		if(dataset != null){
			if(!StringUtils.isEmpty(dataset) && !dataset.equals("ALL"))
				availableRoleDatasets.retainAll(Lists.newArrayList(new Role(dataset)));
			model.addAttribute("availableDatasetsList", availableRoleDatasets);
		}
		model.addAttribute("role",contextHelper.getUserCredential());
		model.addAttribute("option", new Group());
		model.addAttribute("user", loggedInUser);
		model.addAttribute("availableGroupsList", availableGroupList);
		model.addAttribute("userGroupsList", userService.getGroups(loggedInUser.getUsername()));
		model.addAttribute("pendingOfAuthGroupsList", userService.getPendingOfAuthGroups(loggedInUser.getUsername()));
		model.addAttribute("pendingOfAcceptanceRolesList", userService.getPendingOfAcceptanceRoles(loggedInUser.getUsername()));
		return "/user/Groups";
	 }
	
	@RequestMapping(method=RequestMethod.POST, value="/groups")
	 public @ResponseBody String saveUserGroups(Model model, @RequestBody Map<String, Object> json) throws Exception {
		List<String> selectedGroups = Lists.newArrayList(Iterables.transform((ArrayList<Object>)json.get("selectedGroups"), objectToListOfStringsFunction));
		User loggedInUser = contextHelper.getLoggedInUser();		
		for(String groupName : selectedGroups){
			if(groupService.hasRestrictedRoles(groupName)){
				userService.insertGroup(loggedInUser.getUsername(), groupName, false, true);
				User coordinator = userService.getUserByUsername(groupService.getGroup(groupName).getCoordinator());
				if(coordinator!=null)
					mailService.sendGroupRequestEmail(coordinator, loggedInUser, groupName);
				mailService.sendAuthorizationRequestEmail(loggedInUser, contextHelper.getMessage("app.group.title"), groupName);	
			}else{
				userService.insertGroup(loggedInUser.getUsername(), groupName, true, true);
				mailService.sendNewMemberEmail(loggedInUser, contextHelper.getMessage("app.group.title"), groupName);
			}
		}
		return "{\"message\" : \"success\"}";
	 }	
	
	@RequestMapping(method=RequestMethod.DELETE, value="/groups")
	 public @ResponseBody String deleteUserGroup(Model model, String groupName) throws Exception {
		User loggedInUser = contextHelper.getLoggedInUser();		
		userService.deleteGroup(loggedInUser.getUsername(), groupName);
		return "{\"message\" : \"success\"}";
	 }	
	
	@Deprecated
	@RequestMapping(method=RequestMethod.POST, value="/groups1")
	 public @ResponseBody String setUserGroups(Model model, @RequestBody Map<String, Object> json) throws Exception {
		List<String> selectedGroups = Lists.newArrayList(Iterables.transform((ArrayList<Object>)json.get("selectedGroups"), objectToListOfStringsFunction));
		List<String> acceptedRoles = Lists.newArrayList();
		List<String> deletedGroups = Lists.newArrayList(Iterables.transform((ArrayList<Object>)json.get("deletedGroups"), objectToListOfStringsFunction));
		User loggedInUser = contextHelper.getLoggedInUser();		
		for(String groupName : deletedGroups)
			userService.deleteGroup(loggedInUser.getUsername(), groupName);
		for(String roleName : acceptedRoles)
			userService.acceptRole(loggedInUser.getUsername(), roleName, true);	
		for(String groupName : selectedGroups){
			if(groupService.hasRestrictedRoles(groupName)){
				userService.insertGroup(loggedInUser.getUsername(), groupName, false, true);
				User coordinator = userService.getUserByUsername(groupService.getGroup(groupName).getCoordinator());
				if(coordinator!=null)
					mailService.sendGroupRequestEmail(coordinator, loggedInUser, groupName);
				mailService.sendAuthorizationRequestEmail(loggedInUser, contextHelper.getMessage("app.group.title"), groupName);	
			}else{
				userService.insertGroup(loggedInUser.getUsername(), groupName, true, true);
				mailService.sendNewMemberEmail(loggedInUser, contextHelper.getMessage("app.group.title"), groupName);
			}
		}
		return "{\"message\" : \"success\"}";
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="/groupRolePoliciesForm")
	 public String groupRolePoliciesFormHandler(@RequestParam String groupName, Model model) throws Exception {
		List<Role> groupRoles = groupService.getGroupRoles(groupName);
		Map<String, List<Policy>> rolePoliciesMap = new HashMap<String, List<Policy>>();
		for(Role role : groupRoles){
			rolePoliciesMap.put(role.getRoleName(), roleService.getPolicies(role.getRoleName()));
		}
		model.addAttribute("groupRoles", Lists.newArrayList(Iterables.transform(groupRoles, roleToRoleDatasetFunction)));
		model.addAttribute("rolePoliciesMap", rolePoliciesMap);
		return "/global/GroupRolePolicies";
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="/groupRolesForm")
	 public String groupRolesFormHandler(@RequestParam String groupName, Model model) throws Exception {
		model.addAttribute("groupRoles", Lists.newArrayList(Iterables.transform(groupService.getGroupRoles(groupName), roleToRoleDatasetFunction)));
		return "/global/GroupRoles";
	 }
	
	
	@RequestMapping(method=RequestMethod.GET, value="/rolePoliciesForm")
	 public String rolePoliciesFormHandler(@RequestParam String roleName, Model model) throws Exception {
		model.addAttribute("rolePolicies", roleService.getPolicies(roleName));
		return "/global/RolePolicies";
	 }

    
	Function<Object, String> objectToListOfStringsFunction = new Function<Object, String>() { 
        public String apply(Object o) { 
        	return (String)o;			        
        }
    };
    
	Function<Role, RoleDataset> roleToRoleDatasetFunction = new Function<Role, RoleDataset>() { 
        public RoleDataset apply(Role role) { 
        	RoleDataset roleDataset = new RoleDataset();
        	roleDataset.setRoleName(role.getRoleName());
        	roleDataset.setDescription(role.getDescription());
        	roleDataset.setRestricted(role.getIsRestricted());
        	roleDataset.setIsDataset(role.getIsDataset());
        	Metadata meta = new Metadata(role.getMetadata());
        	roleDataset.setDatasetURL(meta.getVariable(Variables.URL.toString()));
        	roleDataset.setPrivacy(Boolean.parseBoolean(meta.getVariable(Variables.IS_PRIVATE.toString())));
        	roleDataset.setLabel(meta.getVariable(Variables.LABEL.toString()));
        	return roleDataset;
        }
    };
    
    public List<Group> filterGroups(List<Group> groups){
    	List<Group> filteredGroups = Lists.newArrayList();
    	List<String> groupsToFilter = contextHelper.getGlobalPropertyList("tap.filter.groups");
    	for(Group group: groups){
    		if(!groupsToFilter.contains(group.getGroupName())){
    			filteredGroups.add(group);
    		}
    	}
    	return filteredGroups;
    }
    
    
}
