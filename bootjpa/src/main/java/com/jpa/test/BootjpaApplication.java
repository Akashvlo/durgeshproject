package com.jpa.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.context.ApplicationContext;

import com.jpa.test.dao.UserRepo;
import com.jpa.test.entities.User;

@SpringBootApplication
public class BootjpaApplication {

	public static void main(String[] args) {
		
		
	ApplicationContext context=	SpringApplication.run(BootjpaApplication.class, args);
	
	UserRepo userRepo = context.getBean(UserRepo.class);
	
	User user=new User();
	user.setName("Akash srivastava");//("Akash Srivastava");
	/*user.setCity("Varanasi");*/
	user.setCity("Varanasi");
	user.setStatus("I am java programmer");
	User user1 = userRepo.save(user);
	System.out.println(user1);
	}

}
