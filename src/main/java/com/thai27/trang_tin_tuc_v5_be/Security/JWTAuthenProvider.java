package com.thai27.trang_tin_tuc_v5_be.Security;

import com.thai27.trang_tin_tuc_v5_be.Service.UserDetailServiceImplement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JWTAuthenProvider implements AuthenticationProvider {

	private final UserDetailServiceImplement userDetailSrvImp;

	private final PasswordEncoder encoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = String.valueOf(authentication.getPrincipal());
		String password = String.valueOf(authentication.getCredentials());
		UserDetails userDetail = userDetailSrvImp.loadUserByUsername(username);
			if (encoder.matches(password, userDetail.getPassword())) {
				return new UsernamePasswordAuthenticationToken(userDetail.getUsername(), userDetail.getPassword(),
						userDetail.getAuthorities());
			} else throw new BadCredentialsException("Mật khẩu không chính xác");
		}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

}
