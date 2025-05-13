package com.mp.madhyam.controller;
import static com.mp.madhyam.util.MadhyamConstants.JWT_EXPIRATION_MS;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mp.madhyam.dto.LoginRequest;
import com.mp.madhyam.dto.UserRegistrationDTO;
import com.mp.madhyam.dto.UserResponseDTO;
import com.mp.madhyam.entity.Role;
import com.mp.madhyam.entity.UserDetailsImpl;
import com.mp.madhyam.service.UserService;
import com.mp.madhyam.util.JwtUtil;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO dto) {
		 try {
		UserDetailsImpl savedUser = userService.registerUser(dto);
		UserResponseDTO response = new UserResponseDTO();
		response.setId(savedUser.getId());
		response.setName(savedUser.getName());
		response.setEmail(savedUser.getUsername());
		response.setRoles(savedUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
		return ResponseEntity.ok(savedUser);
		 }catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409 Conflict
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
	    }
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

			//SecurityContextHolder.getContext().setAuthentication(authentication);
			//session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			String jwt = jwtUtil.generateToken(userDetails);

			/*
			 * Set<String> roles =
			 * userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
			 * .collect(Collectors.toSet());
			 * 
			 * return ResponseEntity.ok(Map.of("roles", roles));
			 */
			 return ResponseEntity.ok(Map.of(
		                "token", jwt,
		                "roles", userDetails.getAuthorities().stream()
		                        .map(GrantedAuthority::getAuthority).collect(Collectors.toSet()),
		            "expiresAt", System.currentTimeMillis() + JWT_EXPIRATION_MS
		        ));
		} catch (Exception e) {
			e.printStackTrace(); // <--- Log the actual cause
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
		}
	}
}
