package com.backend.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.demo.entities.Role;

public interface RoleRepo extends JpaRepository<Role,Integer>{
	
	

}
