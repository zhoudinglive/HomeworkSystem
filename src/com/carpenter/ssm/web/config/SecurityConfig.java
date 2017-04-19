package com.carpenter.ssm.web.config;



import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**   
* @Title: SecurityConfig.java 
* @Package com.carpenter.ssm.web.config 
* @Description: TODO 
* @author carpenter   
* @date 2016年8月18日 下午9:49:49 
* @version V1.0   
*/

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	DataSource dataSource;
	
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception{
		auth.jdbcAuthentication().dataSource(dataSource)
			.passwordEncoder(passwordEncoder())
			.usersByUsernameQuery(
					"select username, password, enabled from users where username = ?")
			.authoritiesByUsernameQuery(
					"select username, role from user_roles where username = ?");
			
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests()
				.antMatchers("/user/**").access("hasRole('ROLE_USER')")
				.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.and()
				.formLogin().loginPage("/login").failureUrl("/login_error").successForwardUrl("/login")
				.usernameParameter("username").passwordParameter("password")
//			.and()
//				.rememberMe()
//					.tokenValiditySeconds(604800)
//					.key("rememberMe")
			.and()
				.logout()//.logoutSuccessUrl("/logout")
			.and()
				.exceptionHandling().accessDeniedPage("/403")
			.and()
				.csrf();
				
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
}
