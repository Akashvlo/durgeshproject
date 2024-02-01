package com.backend.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.demo.entities.User;

public interface UserRepo extends JpaRepository<User,Integer>{
	
	Optional<User>findByEmail(String email);

}
