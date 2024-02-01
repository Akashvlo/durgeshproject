package com.backend.demo.payloads;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
	
	private int id;
	@NotEmpty
	@Size(min=4,message="username must be min of 4 char")
	private String name;
	@Email(message="email address is not valid")
	@NotEmpty(message="message is required")
	private String email;
	@NotEmpty
	//@Size(min=4,max=10,message="password must be min of 4 and max of 10")
	private String password;
	@NotEmpty
	private String about;
	
	 private Set<RoleDto> roles=new HashSet<>();
	 @JsonIgnore
	 public String getPassword() {
		 return this.password;
	 }
	 @JsonProperty
	 public void setPassword(String password) {
		 this.password=password;
	 }

}
