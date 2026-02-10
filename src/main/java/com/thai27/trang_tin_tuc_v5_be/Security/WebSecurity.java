package com.thai27.trang_tin_tuc_v5_be.Security;

import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class WebSecurity {

	@Autowired
	JWTTokenFilter tokenFilter;

	@Autowired
	JWTAuthenProvider authentication;

	@Bean
	public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {

		http
				.cors(Customizer.withDefaults())
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests((auth) -> auth
						.requestMatchers("/api/comments/permit/**").permitAll()
						.requestMatchers("/api/news-like/permit/**").permitAll()
						.requestMatchers("/api/user/permit/**").permitAll()
						.requestMatchers("/api/news/permit/**").permitAll()
						.requestMatchers("/api/sub-categories/permit/**").permitAll()
						.requestMatchers("/api/categories/permit/**").permitAll()
						.requestMatchers("/api/managements/**").hasAuthority(Constant.ROLE_ADMIN)
						.requestMatchers("/sse/**").permitAll()
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.anyRequest().authenticated())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(List.of(
				"http://localhost:3000",
				"https://trang-tin-tuc-v5-fe.onrender.com"
		));

		configuration.setAllowedMethods(List.of(
				"GET", "POST", "PUT", "DELETE", "OPTIONS"
		));

		configuration.setAllowedHeaders(List.of(
				"Authorization",
				"Content-Type",
				"Cache-Control",
				"ngrok-skip-browser-warning"
		));

		configuration.setExposedHeaders(List.of(
				"Authorization"
		));

		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

}
