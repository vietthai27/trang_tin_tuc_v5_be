package com.thai27.trang_tin_tuc_v5_be.Security;

import com.thai27.trang_tin_tuc_v5_be.Service.UserDetailServiceImplement;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.lang.NonNull;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JWTTokenFilter extends OncePerRequestFilter {

	@Autowired
	JWTUtil jwt;

	@Autowired
	UserDetailServiceImplement userSrv;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
			throws ServletException, IOException {
		String authorHeader = request.getHeader("Authorization");
		if (authorHeader == null || authorHeader.isEmpty() || !authorHeader.startsWith("Bearer")) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = authorHeader.split(" ")[1].trim();
		try {
			if (!jwt.isTokenValid(token)) {
				filterChain.doFilter(request, response);
				return;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		String username = null;
		try {
			username = jwt.getUsername(token);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		UserDetails userInfo = userSrv.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null,
				userInfo.getAuthorities());
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authToken);
		filterChain.doFilter(request, response);
	}


}
