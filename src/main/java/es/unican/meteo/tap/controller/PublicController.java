package es.unican.meteo.tap.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import es.predictia.util.Metadata;
import es.unican.meteo.tap.constants.GroupName;
import es.unican.meteo.tap.constants.ServiceType;
import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.model.User;
import es.unican.meteo.tap.service.ContextHelper;
import es.unican.meteo.tap.service.MailService;
import es.unican.meteo.tap.service.RoleService;
import es.unican.meteo.tap.service.TokenService;
import es.unican.meteo.tap.service.UserService;
import es.unican.meteo.tap.validator.RequestNewPasswordValidation;
import es.unican.meteo.tap.validator.UserValidator;

/**
* Clase encargada de ejercer de controlador para Spring, basada en el modelo MVC
*
*/

@Controller
public class PublicController {

	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	MailService mailService;
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	ContextHelper contextHelper;
	
	@Autowired
	UserValidator userValidator;
	
	@Autowired
	RequestNewPasswordValidation requestNewPasswordValidation;

	@RequestMapping(method=RequestMethod.GET, value="/home")
	 public String homeHandler(HttpServletRequest arg0, HttpServletResponse arg1, Model model) throws Exception {
		return "public/Home";
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="")
	 public String userHandler(HttpServletRequest arg0, HttpServletResponse arg1, Model model) throws Exception {
		return "redirect:home";
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="/terms")
	 public String termsHandler(HttpServletRequest arg0, HttpServletResponse arg1, Model model) throws Exception {
		return "public/Terms";
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="/success")
	 public String successHandler(HttpServletRequest arg0, HttpServletResponse arg1, Model model) throws Exception {
		model.addAttribute("location", "AAAAAAAAAAAA");
		model.addAttribute("message", "WELL DONE!");
		return "public/Success";
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="/signup")
	 public String signupRequestHandler(HttpServletRequest request, 
			 @RequestParam(value = "hash", required = false) String hash, @RequestParam(value = "email", required = false) String email, @RequestParam(value = "firstname", required = false) String firstname, 
			 @RequestParam(value = "lastname", required = false) String lastname, Model model) throws Exception {
		User user = new User();
		List<String> sessionAttributes = Collections.list(request.getSession().getAttributeNames());
		if(sessionAttributes.contains("openid")){
			String openid = request.getSession().getAttribute("openid").toString();
			user.setOpenid(openid);
			if(!StringUtils.isEmpty("token")){
				if(tokenService.checkValidity(hash, openid))
					model.addAttribute("allowLinkAccount",true);
			}
			if(!StringUtils.isEmpty(email))
				user.setEmail(email);
			if(!StringUtils.isEmpty(firstname))
				user.setFirstName(firstname);
			if(!StringUtils.isEmpty(lastname))
				user.setLastName(lastname);
			request.getSession().removeAttribute("openid");
		}	
		model.addAttribute("user", user);
		model.addAttribute("countryList", contextHelper.getCountryCodesMap());
		model.addAttribute("recaptchaKey", contextHelper.getGlobalProperty("recaptcha.publickey"));
		return "public/Signup";
	 }

	@RequestMapping(method=RequestMethod.POST, value="/signup")
	 public String registerUserRequestHandler(@ModelAttribute("user") User user,  @RequestParam("g-recaptcha-response") String recaptchaResponse, 
			 	@RequestParam(value = "hash", required = false) String hash, Model model, HttpServletRequest httpServletRequest, ServletRequest servletRequest, BindingResult result) throws Exception{
		Map<String, Object> errorsMap = new HashMap<String, Object>();
		userValidator.validateReCaptcha(recaptchaResponse, servletRequest.getRemoteAddr(), model, errorsMap);
		userValidator.validate(user, result);
		userValidator.validateRegistration(user, hash, servletRequest.getParameter("termsofuse"), model, result, errorsMap);
		model.addAttribute("countryList", contextHelper.getCountryCodesMap());
		model.mergeAttributes(errorsMap);
		if(result.hasErrors() || errorsMap.size()>0){
			signupRequestHandler(httpServletRequest, hash, user.getEmail(), user.getFirstName(), user.getLastName(), model);
		}
		Metadata metadata = new Metadata();		
		user.setMetadata(metadata.toString());
		userService.insertUser(user);
		userService.insertGroup(user.getUsername(), GroupName.GROUP_USER.toString(), true, true);
		User savedUser = userService.getUserByUsername(user.getUsername());
		mailService.sendConfirmationEmail(savedUser);
		model.addAttribute("redirect",true);
		model.addAttribute("timeout", 6000);
		model.addAttribute("location", "signin");
		model.addAttribute("message", contextHelper.getMessage("app.register.success"));
		return "public/Success";
	 }

	//TODO: 
//	@RequestMapping(method=RequestMethod.POST, value="/link")
//	 public String linkAccountRequestHandler(@ModelAttribute("user") User user, @RequestParam("g-recaptcha-response") String recaptchaResponse, Model model, HttpServletRequest httpServletRequest, ServletRequest servletRequest, BindingResult result){
//		userValidator.validateUsername(user, result);
//		userValidator.validatePassword(user, result);
//		userValidator.validateReCaptcha(recaptchaResponse, servletRequest.getRemoteAddr(), model, errorsMap);
//		userValidator.validateTermsOfUse(servletRequest.getParameter("termsofuse"), model, errorsMap);
//		model.addAttribute("contextType","link");
//		if(result.hasErrors()){
//			userValidator.validatePassword(user, result);
//			userValidator.validateReCaptcha(recaptchaResponse, servletRequest.getRemoteAddr(), model, errorsMap);
//			userValidator.validateTermsOfUse(servletRequest.getParameter("termsofuse"), model, errorsMap);
//			return "public/UserRegistration";
//		}			
//		storedUser.setOpenid(user.getOpenid());
//		userService.updateUser(storedUser);
//		model.addAttribute("redirect",true);
//		model.addAttribute("timeout", 5000);
//		model.addAttribute("location", "signin");
//		model.addAttribute("message", contextHelper.getMessage("app.link.success"));
//		return "public/Success";
//	}
	
	@RequestMapping(method=RequestMethod.GET, value="/confirm")
	 public String confirmEmailRequestHandler(@RequestParam("username") String username, @RequestParam("token") String token, Model model){
		model.addAttribute("redirect", true);
		model.addAttribute("location", "signin");
		model.addAttribute("timeout", "5000");
		if(token.equals( userService.getUserByUsername(username).getToken())){
			userService.activateUser(username, true);
			userService.resetUserToken(username);
			model.addAttribute("message", contextHelper.getMessage("app.confirmemail.success","5"));
			return "public/Success";
		}
		model.addAttribute("message", contextHelper.getMessage("app.confirmemail.error"));
		return "public/Error";
	}

	@RequestMapping(method=RequestMethod.GET, value="/recovery")
	 public String requestNewPasswordFormHandler(ModelMap model){
		model.addAttribute("user", new User());
		model.addAttribute("recaptchaKey", contextHelper.getGlobalProperty("recaptcha.publickey"));
		return "public/AccountRecovery";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/recovery")
	 public String restorePasswordRequestHandler(@ModelAttribute("user") User user, @RequestParam("g-recaptcha-response") String recaptchaResponse, Model model, HttpServletRequest httpServletRequest, ServletRequest servletRequest, BindingResult result){
		String remoteAddr = servletRequest.getRemoteAddr();
		requestNewPasswordValidation.validate(user, result);
		if(result.hasErrors()){
			return "public/PasswordRequest";
		}else{
			if (contextHelper.isRecaptchaValid(remoteAddr, recaptchaResponse)){
				User savedUser = userService.getUserByEmail(user.getEmail());
				userService.resetUserToken(savedUser.getUsername());
				savedUser = userService.getUserByUsername(savedUser.getUsername());
				mailService.sendRestorePasswordEmail(savedUser);
			} else {
				model.addAttribute("invalidCaptcha", contextHelper.getMessage("app.error.captcha"));  
				return "public/PasswordRequest";
			}
		}
		model.addAttribute("message", contextHelper.getMessage("app.success.passwordrequested"));
		return "public/Success";
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="/reset")
	 public String resetPasswordFormHandler(@RequestParam("username")String username, @RequestParam("token")String token, ModelMap model){
		User savedUser = userService.getUserByUsername(username);
		if(savedUser.getToken().equals(token)){
			userService.resetUserToken(savedUser.getUsername());
			User user = new User();
			user.setUsername(username);
			model.addAttribute("user", user);
			return "public/PasswordReset";
		}
		return "public/Error";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/reset")
	public String resetPasswordHanlder(@ModelAttribute("user") User user, ModelMap model){
		User savedUser = userService.getUserByUsername(user.getUsername());
		savedUser.setPassword(user.getPassword());
		savedUser.setConfirmPassword(user.getConfirmPassword());
		userService.updateUser(savedUser);
		model.addAttribute("redirect", true);
		model.addAttribute("location", "signin");
		model.addAttribute("timeout", "4000");
		model.addAttribute("message", contextHelper.getMessage("app.success.passwordchanged", "4"));
		return "public/Success";
	}

	@RequestMapping(method=RequestMethod.GET, value="/getRoleInfo")
	 public @ResponseBody Role getRolesInJSON(@RequestParam String rolename){
		return roleService.getRoleByName(rolename);
	 }

}