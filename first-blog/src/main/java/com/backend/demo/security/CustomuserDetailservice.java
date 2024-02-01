package com.backend.demo.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.demo.entities.User;
import com.backend.demo.exceptions.ResourceNotFoundException;
import com.backend.demo.repo.UserRepo;


@Service
public class CustomuserDetailservice implements UserDetailsService{

	@Autowired
	private UserRepo userrepos;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		//load user from database by username
		User user=this.userrepos.findByEmail(username).orElseThrow(()-> new ResourceNotFoundException("User","email:"+username,0));
		
		return user;
	}

}
