package es.unican.meteo.tap.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import es.unican.meteo.tap.model.User;
import es.unican.meteo.tap.service.UserService;

@Component
public class RequestNewPasswordValidation  implements Validator{

	@Autowired
	private UserService userService;
	
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {
		User user = (User)target; 
		if(userService.getUserByEmail(user.getEmail())==null){
			errors.rejectValue("email", "app.error.nonexistentemail");
		}
		//TODO: generar caso de usuario no activo que intenta cambiar la contra. NO PUEDE HACERLO
	}
}