package com.mp.madhyam.service;

import org.springframework.stereotype.Service;

import com.mp.madhyam.dto.UserRegistrationDTO;
import com.mp.madhyam.entity.UserDetailsImpl;

@Service
public interface UserService {
	public UserDetailsImpl registerUser(UserRegistrationDTO dto);
}
