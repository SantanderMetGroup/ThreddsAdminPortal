package es.unican.meteo.tap.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
	
	@RequestMapping(value="/welcome", method = RequestMethod.GET)
	public String printWelcome(ModelMap model, Principal principal){
		String name = principal.getName();
		model.addAttribute("username", name);
		return "user/Welcome";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(HttpServletRequest req, ModelMap model) {
		return "redirect:signin";
	}
	
	@RequestMapping(value="/signin", method = RequestMethod.GET)
	public String signin(HttpServletRequest req, ModelMap model) {
		req.getCookies();req.getSession();
		return "public/Signin";
	}
	
	@RequestMapping(value="/openidLogin", method = RequestMethod.GET)
	public String openidLogin(ModelMap model) {
		return "public/OpenIDLogin";
	}

 
	@RequestMapping(value="/signinfailed", method = RequestMethod.GET)
	public String loginerror(@RequestParam("error") String error, ModelMap model) { 
		model.addAttribute("error", error);
		return "public/Signin";
	}
 
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(ModelMap model) {
		return "public/Login";
	}
}
