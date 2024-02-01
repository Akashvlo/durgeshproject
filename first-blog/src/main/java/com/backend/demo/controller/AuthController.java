package com.backend.demo.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.demo.entities.User;
import com.backend.demo.exceptions.ApiException;
import com.backend.demo.payloads.JwtAuthRequest;
import com.backend.demo.payloads.JwtAuthResponse;
import com.backend.demo.payloads.UserDto;
import com.backend.demo.security.JwtTokenHelper;
import com.backend.demo.services.im.UserServiceim;
//import com.blog.application.exceptions.ApiException;
//import com.blog.application.payloads.UserDTO;
//import com.blog.application.services.UserServiceImpl;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
	private JwtTokenHelper jwtTokenHelper;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserServiceim userServiceIm;
	
	@Autowired
	public AuthController(JwtTokenHelper jwtTokenHelper,UserDetailsService userDetailsService,AuthenticationManager authenticationManager, UserServiceim userServiceIm) {
	
		this.jwtTokenHelper = jwtTokenHelper;
		
		this.userDetailsService = userDetailsService;
		
		this.authenticationManager = authenticationManager;
		
		this.userServiceIm = userServiceIm;
	}
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(
			@RequestBody JwtAuthRequest request
			) throws Exception{
		
		this.authenticate(request.getUsername(), request.getPassword());
		
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
		
		String token = this.jwtTokenHelper.generateToken(userDetails);
		
		
		
		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);
		response.setUser(this.modelMapper.map((User)userDetails,UserDto.class));
		
		return new ResponseEntity<JwtAuthResponse>(response,HttpStatus.OK);
	}
	
	private void authenticate(String username, String password) throws Exception {
		
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(username, password);
		     //  this.authenticationManager.authenticate(authenticationToken);
	

		try {
			this.authenticationManager.authenticate(authenticationToken);
		}catch(BadCredentialsException e) {
			System.out.println("Invalid Details");
			throw new ApiException("Invalid Username or password!");
		}
	}

//register new user api
	
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerNewUser(
			@RequestBody UserDto userDto){
		//UserDto registeredUser=this.userServiceIm.registerNewUser(userDto);
				return new ResponseEntity<>(this.userServiceIm.registerNewUser(userDto),
						HttpStatus.CREATED);
	}
}

