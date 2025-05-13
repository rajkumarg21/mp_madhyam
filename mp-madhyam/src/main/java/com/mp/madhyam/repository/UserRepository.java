package com.mp.madhyam.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mp.madhyam.entity.UserDetailsImpl;

@Repository
public interface UserRepository extends JpaRepository<UserDetailsImpl, Long> {
	Optional<UserDetailsImpl> findByEmail(String email);
}


