package es.unican.meteo.tap.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.inject.internal.Lists;

import es.unican.meteo.tap.model.Group;
import es.unican.meteo.tap.model.Policy;
import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.model.User;
import es.unican.meteo.tap.service.ContextHelper;
import es.unican.meteo.tap.service.GroupService;
import es.unican.meteo.tap.service.MailService;
import es.unican.meteo.tap.service.PolicyService;
import es.unican.meteo.tap.service.RoleService;
import es.unican.meteo.tap.service.UserService;
import es.unican.meteo.tap.util.ResponseMessage;
import es.unican.meteo.tap.validator.UserValidator;

@Controller
@RequestMapping(value="rest/*")
public class RESTController {
	
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
	
	//TODO: restringir via properties los roles de admin para que el usuario no los vea ni pueda consultarlos
	@RequestMapping(method=RequestMethod.GET, value="/roles/{name}")
	 public @ResponseBody Role getRoleInfoHandler(HttpServletRequest arg0, HttpServletResponse arg1, Model model, @PathVariable String name) throws Exception {
		if(contextHelper.isUserAdmin(contextHelper.getLoggedInUser().getUsername()))
			return roleService.getRoleByName(name);
		return null;
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="/roles/{name}/policies")
	 public @ResponseBody List<Policy> getRolePoliciesHandler(HttpServletRequest arg0, HttpServletResponse arg1, Model model, @PathVariable String name) throws Exception {
		return roleService.getPolicies(name);
	 }

	@RequestMapping(method=RequestMethod.GET, value="/groups/{name}/policies")
	 public @ResponseBody List<Policy> getGroupPoliciesHandler(HttpServletRequest arg0, HttpServletResponse arg1, Model model, @PathVariable String name) throws Exception {
		List<Policy> groupPolicies = new ArrayList<Policy>();
		for(Role role : groupService.getGroupRoles(name)){
			for(Policy policy: roleService.getPolicies(role.getRoleName())){
				if(!groupPolicies.contains(policy))
					groupPolicies.add(policy);
			}
		}
		return groupPolicies;	
	 }

	@RequestMapping(method=RequestMethod.GET, value="/groups")
	 public @ResponseBody List<Group> getGroupsHandler(HttpServletRequest arg0, HttpServletResponse arg1, Model model, @RequestParam(required=false) String[] roleNames) throws Exception {
		if(!StringUtils.isEmpty(roleNames))
			return groupService.getGroupsByRoles(Lists.newArrayList(roleNames));
		return groupService.getGroupProjects();
	 }
	
	
	@RequestMapping(method=RequestMethod.GET, value="/groups/{name}")
	 public @ResponseBody Group getGroupHandler(HttpServletRequest arg0, HttpServletResponse arg1, Model model, @PathVariable String name) throws Exception {
		Group group = groupService.getGroup(name);
		String coordinatorUsername = group.getCoordinator();
		if(!StringUtils.isEmpty(coordinatorUsername)){
			User moderator = userService.getUserByUsername(coordinatorUsername);
			group.setCoordinator(moderator.getFirstName() + " " + moderator.getLastName());
		}
		return group;
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="/groups/{name}/roles")
	 public @ResponseBody List<Role> getGroupRolesHandler(HttpServletRequest arg0, HttpServletResponse arg1, Model model, @PathVariable String name) throws Exception {
		return groupService.getGroupRoles(name);
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="/user/{username}/group/{groupName}")
	 public ModelAndView authorizeGroup(HttpServletRequest arg0, HttpServletResponse arg1, Model model, @PathVariable String username, @PathVariable String groupName, @RequestParam String token, @RequestParam boolean authorized) throws Exception {
		User coordinator = userService.getUserByToken(token);
		Group group = groupService.getGroup(groupName);
		if(coordinator != null && coordinator.getUsername().equals(group.getCoordinator())){
			if(authorized){
				userService.authorizeGroup(username, groupName, true);
				model.addAttribute("message", contextHelper.getMessage("app.success.authorized",groupName));
			}else{
				userService.deleteGroup(username, groupName);
				model.addAttribute("message", contextHelper.getMessage("app.success.rejected",groupName));
			}
			mailService.sendGroupAuthorizationResponseEmail(userService.getUserByUsername(username), groupService.getGroupDatasets(groupName), groupName, authorized);
			model.addAttribute("redirect", true);
			model.addAttribute("location", "home");
			model.addAttribute("timeout", "3000");
			return new ModelAndView("public/Success");
		}	
		model.addAttribute("message", contextHelper.getMessage(contextHelper.getMessage("app.error")+" ",groupName));
		return new ModelAndView("error");
	 }
	
	
	@RequestMapping(method=RequestMethod.GET, value="/v1/signin/verify")
	 public @ResponseBody ResponseMessage verifyCredentials(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam String username, @RequestParam String password) throws Exception {
		User user = userService.getUserByUsername(username);
		if(user == null)
			return new ResponseMessage("Username does not exist","ERROR");
		PasswordEncoder encoder = new Md5PasswordEncoder();
		String passwordEncoded = encoder.encodePassword(password, null);
		if(!user.getPassword().equals(passwordEncoded))
			return new ResponseMessage("Invalid password","ERROR");
		return new ResponseMessage("Valid credentials","SUCCESS");

	 }
		
}
