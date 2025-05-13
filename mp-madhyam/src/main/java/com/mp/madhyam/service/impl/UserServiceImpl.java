package com.mp.madhyam.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mp.madhyam.dto.UserRegistrationDTO;
import com.mp.madhyam.entity.Role;
import com.mp.madhyam.entity.UserDetailsImpl;
import com.mp.madhyam.repository.RoleRepository;
import com.mp.madhyam.repository.UserRepository;
import com.mp.madhyam.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserDetailsImpl registerUser(UserRegistrationDTO dto) {
		if (userRepo.findByEmail(dto.getEmail()).isPresent()) {
	        throw new IllegalArgumentException("User with this email already exists");
	    }
		
		UserDetailsImpl user = new UserDetailsImpl();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		Role userRole = roleRepo.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found"));

		user.setRoles(Set.of(userRole));

		return userRepo.save(user);
	}

}
