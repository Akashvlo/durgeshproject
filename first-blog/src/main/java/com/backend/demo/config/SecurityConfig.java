package com.backend.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.backend.demo.security.CustomuserDetailservice;
import com.backend.demo.security.JwtAuthenticationEntryPoint;
import com.backend.demo.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
	private CustomuserDetailservice customUserDetailService;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http
		.csrf()
		.disable()
		.authorizeHttpRequests()
		.antMatchers("/api/v1/auth/**").permitAll()
		//.antMatchers("/api/v1/user/**").permitAll()
		
		.antMatchers(HttpMethod.GET).permitAll()
		.antMatchers(HttpMethod.POST).permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.exceptionHandling().authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		//super.configure(http);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		
		auth.userDetailsService(this.customUserDetailService).passwordEncoder(passwordEncoder());
		//super.configure(auth);
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
    @Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}
    
    @Bean
    public FilterRegistrationBean corsFilter(){
    UrlBasedCorsConfigurationSource source =new UrlBasedCorsConfigurationSource();
            CorsConfiguration corsConfiguration = new CorsConfiguration();
     corsConfiguration.setAllowCredentials(true);
    corsConfiguration.addAllowedOriginPattern("*");
     corsConfiguration.addAllowedHeader("Authorization");
     corsConfiguration.addAllowedHeader("Content-Type");
     corsConfiguration.addAllowedHeader("Accept");
     corsConfiguration.addAllowedMethod("POST");
    corsConfiguration.addAllowedMethod("PUT");
     corsConfiguration.addAllowedMethod("GET");
     corsConfiguration.addAllowedMethod("DELETE");
     corsConfiguration.addAllowedMethod("OPTIONS");
     source.registerCorsConfiguration("/**", corsConfiguration);
     FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
     
     bean.setOrder(-110);
     return bean;
    }

	
	
	
	
	
	

}
