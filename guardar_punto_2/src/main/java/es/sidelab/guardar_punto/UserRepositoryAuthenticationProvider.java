package es.sidelab.guardar_punto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UserRepositoryAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private UsuariosRepository userRepository;
	
	@Autowired
	private UserComponent userComponent;
	
	@Override
	public Authentication authenticate (Authentication auth) throws AuthenticationException {
		
		Usuarios user = userRepository.findByEmailIgnoreCaseLike(auth.getName()); //name = mail
		
				
		if (user == null) {
			throw new BadCredentialsException("User not found");
		}
		
		String password = (String) auth.getCredentials();		;
		if (!new BCryptPasswordEncoder().matches(password, user.getContrasenna())) {
			throw new BadCredentialsException("Wrong password");
		} else {
			userComponent.setLoggedUser(user);
			
			List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
			for (String role : user.getRoles()) {
				roles.add(new SimpleGrantedAuthority(role));
			}
			
			return new UsernamePasswordAuthenticationToken(user.getEmail(), password, roles);
		}		
	}
	
	@Override
	public boolean supports(Class<?> authenticationObject) {
		return true;
	}
}
