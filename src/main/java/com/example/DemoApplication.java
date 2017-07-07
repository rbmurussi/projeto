package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.mongodb.Mongo;

@Configuration
@SpringBootApplication
@EnableWebSecurity
@ComponentScan
public class DemoApplication extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	public @Bean Mongo mongo() throws Exception {
		return new com.mongodb.MongoClient("locahost");
	}

	public @Bean MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongo(), "database");
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		System.out.println("configureGlobal");
		auth
        .inMemoryAuthentication()
            .withUser("user").password("password").roles("USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

//		http
//        .authorizeRequests()
//            .antMatchers("/home", "/").permitAll()
//            .anyRequest().authenticated()
//            .and()
//        .formLogin()
//            .loginPage("/login.html")
//            .permitAll()
//            .and()
//        .logout().logoutUrl("/hello.html")
//            .permitAll();

	}
}
