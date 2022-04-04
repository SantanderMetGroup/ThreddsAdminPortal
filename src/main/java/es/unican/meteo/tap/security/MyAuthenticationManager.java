package es.unican.meteo.tap.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import es.unican.meteo.tap.model.Role;
import es.unican.meteo.tap.model.User;
import es.unican.meteo.tap.service.UserService;

public class MyAuthenticationManager implements AuthenticationManager{

	@Autowired
	UserService userService;
	
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Object hash = authentication.getPrincipal();
		User user = userService.getUserByUsername(""); 
		//org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getUserGrantedAuthorities(userService.getAllRoles(user.getUsername())));
       	return new UsernamePasswordAuthenticationToken(null, user.getPassword(), getUserGrantedAuthorities(userService.getAllRoles(user.getUsername())));
	}
	
    private Collection<GrantedAuthority> getUserGrantedAuthorities(Collection<Role> roles){
        Iterable<GrantedAuthority> iterableGrantedAuthorities = Iterables.transform(roles, new Function<Role,GrantedAuthority>(){
			public GrantedAuthority apply(final Role role){
				return new GrantedAuthority() {
					private static final long serialVersionUID = 1L;
					public String getAuthority() {
						return role.getRoleName();
					}
				};
			}
		});
        return Lists.newArrayList(iterableGrantedAuthorities);
    }

}
