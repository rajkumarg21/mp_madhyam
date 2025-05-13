package com.mp.madhyam.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mp.madhyam.entity.UserDetailsImpl;
import com.mp.madhyam.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserDetailsImpl user = userRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		System.out.println("Loaded user: " + user.getUsername());
		return user;
	}
}
