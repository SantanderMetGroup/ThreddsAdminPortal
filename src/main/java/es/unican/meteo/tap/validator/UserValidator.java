package es.unican.meteo.tap.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;

import es.unican.meteo.tap.constants.ServiceType;
import es.unican.meteo.tap.model.User;
import es.unican.meteo.tap.service.ContextHelper;
import es.unican.meteo.tap.service.TokenService;
import es.unican.meteo.tap.service.UserService;

@Component
public class UserValidator implements Validator{

	@Autowired
	private UserService userService;
	
	@Autowired
	private ContextHelper contextHelper;
	
	@Autowired
	private TokenService tokenService;
	
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "app.field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "app.field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "app.field.required");
		User user = (User)target;
		validateUsername(user, errors);
		validatePassword(user, errors);
		validateEmail(user, errors);
		validatePersonalDetails(target, errors);
		validateResearchDetails(target, errors);
	}
	
	public void validateUsername(User user, Errors errors){
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "app.field.required");
		rejectIfHasWhitespaces(errors, "username", "app.error.whitespaces");
		if(StringUtils.isEmpty(user.getUsername()) && userService.getUserByUsername(user.getUsername()) != null)
			errors.rejectValue("username", "app.error.username.exist");
		if(containsIllegals(user.getUsername()))
			errors.rejectValue("username", "app.error.invalidtext");
	}
	
	public void validatePassword(User user, Errors errors){
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "app.field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "app.field.required");
		rejectIfHasWhitespaces(errors, "password", "app.error.whitespaces");
		rejectIfHasWhitespaces(errors, "confirmPassword", "app.error.whitespaces");
		if(!user.getPassword().equals(user.getConfirmPassword()))
			errors.rejectValue("password", "app.error.passnotmatch");
	}
	
	public void validateEmail(User user, Errors errors){
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "app.field.required");
		rejectIfHasWhitespaces(errors, "email", "app.error.whitespaces");
		User storedUser = userService.getUserByEmail(user.getEmail());
		if(storedUser != null)
			errors.rejectValue("email","app.error.email.exist");
	}
	
	public void validatePersonalDetails(Object target, Errors errors){
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "app.field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "app.field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "isoCode", "app.field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newsletter", "app.field.required");
	}
	
	public void validateResearchDetails(Object target, Errors errors){
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "institution", "app.field.required");
		ValidationUtils.rejectIfEmpty(errors, "motivation", "app.field.required");
	}

	
	public void validateReCaptcha(String response, String remoteAddress, Model model, Map<String,Object> errorsMap){
		if (!contextHelper.isRecaptchaValid(remoteAddress, response)){
			errorsMap.put("invalidCaptcha", contextHelper.getMessage("app.error.captcha"));
		}
	}
	
	public void validateRegistration(User user, String hash, String termsOfUse, Model model, Errors errors, Map<String,Object> errorsMap){
		validateTermsOfUse(termsOfUse, model, errorsMap);
		if(!StringUtils.isEmpty(user.getOpenid()) && !StringUtils.isEmpty(hash)){
			if(!StringUtils.isEmpty(userService.getUserByOpenID(user.getOpenid())))
				errorsMap.put("errors",Lists.newArrayList("OpenID already in use"));
			if(!tokenService.checkValidity(hash, user.getOpenid())){
				errorsMap.put("errors",Lists.newArrayList("Invalid or expired hash token. Please, repeat the process."));
			}else if(!errors.hasErrors()){
				tokenService.deleteToken(user.getOpenid(), ServiceType.OPENID);
			}
		}
	}
	
	public void validateTermsOfUse(String termsOfUse, Model model, Map<String,Object> errorsMap){
		if(StringUtils.isEmpty(termsOfUse)){
			errorsMap.put("termsOfUseRequired", contextHelper.getMessage("app.field.required.terms"));
		}
	}
	
	
	public void validatePassword(String password, String confirmPassword, Map<String,Object> errorsMap){
		if(StringUtils.isEmpty(password) || StringUtils.isEmpty(confirmPassword)){
			errorsMap.put("invalidPassword", contextHelper.getMessage("app.error.password"));
		}else if(!password.equals(confirmPassword)){
			errorsMap.put("invalidPassword", contextHelper.getMessage("app.error.passnotmatch"));
		}
	}
	
	public void validateOpenID(String username, String openId, String hash, Model model, boolean hasErrors){
		if(!StringUtils.isEmpty(openId) && !StringUtils.isEmpty(hash)){
			if(!StringUtils.isEmpty(userService.getUserByOpenID(openId)))
				model.addAttribute("errors",Lists.newArrayList("OpenID already in use"));
			if(!tokenService.checkValidity(hash, openId)){
				model.addAttribute("errors",Lists.newArrayList("Invalid or expired hash token. Please, repeat the process."));
				hasErrors=true;
			}else if(!hasErrors){
				tokenService.deleteToken(openId, ServiceType.OPENID);
			}
		}
	}
	
	/*
	 * 
	 */
	private boolean containsIllegals(String toExamine) {
		Pattern whitespace = Pattern.compile("[ ~#@\"$*+%{}<>\\||//^_]");
		Matcher matcher = whitespace.matcher(toExamine);
		return matcher.find();
	}
	
	private void rejectIfHasWhitespaces(Errors errors, String field, String errorCode){
		if(CharMatcher.WHITESPACE.matchesAnyOf(errors.getFieldValue(field).toString()))
			errors.rejectValue(field, errorCode);
	}
	
	/*
	 * Generates Errors Map and inserts them into the model as attribute
	 */
	public boolean generateErrorsMap(Model model, Errors errors){
		Map<String, Object> errorsMap = new HashMap<String, Object>();
		if(errors.hasErrors()){
			for(FieldError fieldError : errors.getFieldErrors()){
				errorsMap.put(fieldError.getField(), contextHelper.getMessage(fieldError.getCode()));
			}
			model.addAttribute("errorsMap", errorsMap);
			return true;
		}
		return false;
	}
	


}
