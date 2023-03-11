package com.radovan.spring.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.radovan.spring.security.handler.LoginSuccessHandler;
import com.radovan.spring.service.impl.UserDetailsImpl;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());// .withUser("ADMIN").password("ADMIN").roles("ADMIN");

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.formLogin()
				
				.loginPage("/login")
				.successHandler(new LoginSuccessHandler())
				.loginProcessingUrl("/login")
				.usernameParameter("email").passwordParameter("password")
				.permitAll();

		http.logout()
				.permitAll()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout")
				.and().csrf().disable();

		http
        .authorizeRequests()
        .antMatchers("/login").anonymous().
        antMatchers("/loginErrorPage","/books/**","/aboutUs","/suspensionPage").permitAll()
        .antMatchers("/","/home","/registerComplete","/registerFail","/loginPassConfirm").permitAll()
		.antMatchers("/admin/**").hasAuthority("ADMIN")
		.antMatchers("/cart/**","/order/**","/reviews/**").hasAuthority("ROLE_USER")
		.antMatchers("/userRegistration").anonymous()
		.antMatchers("/register").anonymous()
        .anyRequest().authenticated();
        

	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/images/**" , "/css/**",
				"/js/**");
	}
}
