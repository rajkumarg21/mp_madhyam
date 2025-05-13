package com.mp.madhyam.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.mp.madhyam.service.impl.CustomUserDetailsService;
import com.mp.madhyam.util.JwtRequestFilter;

@Configuration
public class SecurityConfig {
	 @Autowired
	 private JwtRequestFilter jwtFilter;
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		/*
		 * http.cors(Customizer.withDefaults()) // Enable CORS .csrf(csrf ->
		 * csrf.disable()) // Disable CSRF .authorizeHttpRequests(auth ->
		 * auth.anyRequest().permitAll()) .httpBasic(httpBasic -> httpBasic.disable())
		 * // Disable HTTP Basic .formLogin(form -> form.disable()); // Disable form
		 * login
		 */
		
		http
		.cors(Customizer.withDefaults())
		.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/users/login", "/api/users/register").permitAll()
            .anyRequest().authenticated()
        );

    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Specific origin
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true); // Needed with withCredentials=true
		configuration.setExposedHeaders(List.of("Authorization"));
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
