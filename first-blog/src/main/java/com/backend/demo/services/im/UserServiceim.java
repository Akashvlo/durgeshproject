package com.backend.demo.services.im;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.demo.config.AppConstants;
import com.backend.demo.entities.Role;
import com.backend.demo.entities.User;
import com.backend.demo.exceptions.ResourceNotFoundException;
import com.backend.demo.payloads.UserDto;
import com.backend.demo.repo.RoleRepo;
import com.backend.demo.repo.UserRepo;
import com.backend.demo.services.UserService;
@Service
public class UserServiceim implements UserService {
	
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;

	@Override
	public UserDto createUser(UserDto userDto) {
		// TODO Auto-generated method stub
		User user=this.dtoToUser(userDto);
		 User savedUser =this.userRepo.save(user);
		return this.userToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		// TODO Auto-generated method stub
		User user=this.userRepo.findById(userId)
			.orElseThrow(()-> new ResourceNotFoundException("User"," Id ",userId));
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());
		
		User updatedUser=this.userRepo.save(user);
		UserDto userDto1=this.userToDto(updatedUser);
		
		
		
		return userDto1;
	}

	@Override
	public UserDto getUserById(Integer userId) {
		// TODO Auto-generated method stub
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","Id",userId));
		return this.userToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {
		
		List<User> users=this.userRepo.findAll();
		List<UserDto> userDtos=users.stream().map(user->this.userToDto(user)).collect(Collectors.toList());
		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		// TODO Auto-generated method stub
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","Id", userId));
           
		this.userRepo.delete(user);
	}



     private User dtoToUser(UserDto userDto) {
    	 
    	 User user=this.modelMapper.map(userDto, User.class);
    	 
    //	 User user=new User();
    //	 user.setId(userDto.getId());
    	 //user.setName(userDto.getName());
    	// user.setEmail(userDto.getEmail());
    //	 user.setAbout(userDto.getAbout());
   // 	 user.setPassword(userDto.getPassword());
         return user;    	 
     }
     
     public UserDto userToDto(User user) {
    	 
    	 
    	 UserDto userDto=this.modelMapper.map(user, UserDto.class);
    	 
    /*	 UserDto userDto=new UserDto();
    	 userDto.setId(user.getId());
    	 userDto.setName(user.getName());
    	 userDto.setEmail(user.getEmail());
    	 userDto.setPassword(user.getPassword());
    	 userDto.setAbout(user.getAbout());
    	*/ 
    	 
    	 return userDto;
    	 
   
    	 
    	 
    	 
    	 
     }
       @Override
      public UserDto registerNewUser(UserDto userDto) {
   User user = this.modelMapper.map(userDto, User.class);
   
     
              user.setPassword(this.passwordEncoder.encode(user.getPassword()));
     
     Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).get();
     user.getRoles().add(role);
     User newUser = this.userRepo.save(user);
     return this.modelMapper.map(newUser,UserDto.class);
      }
     

}


