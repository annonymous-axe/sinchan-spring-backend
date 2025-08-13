//package com.sinchan.configs;
//
//import javax.sql.DataSource;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.security.provisioning.UserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//
//@Configuration
//@EnableWebSecurity
//public class SecutiryConfig {
//
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Bean
//	public UserDetailsManager userDetailsManager(DataSource dataSource) {
//
//		JdbcUserDetailsManager jdbcManager = new JdbcUserDetailsManager(dataSource);
//
//		//get user details
//		jdbcManager.setUsersByUsernameQuery("select email, password, active from users where email = ?");
//
//		//get user roles
//		jdbcManager.setAuthoritiesByUsernameQuery("select email, role from roles where email = ?");
//
//		return jdbcManager;
//
//	}
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//		return http
//				.authorizeHttpRequests(
//						request -> {
//							request
//							.requestMatchers("/").hasRole("ADMIN")
//							.requestMatchers("/entity/**").hasRole("SUPERADMIN")
//							.anyRequest().authenticated();
//						}
//				)
//				.formLogin(
//						login -> {
//							login
//								.loginPage("/login")
//								.loginProcessingUrl("/login")
//								.defaultSuccessUrl("/", true)
//								.usernameParameter("email")
//								.passwordParameter("password")
//								.failureUrl("/login?error='true'")
//								.permitAll();
//
//						}
//				)
//				.logout(
//						logout -> {
//							logout
//								.logoutUrl("/logout")
//								.logoutSuccessUrl("/login?logout=true")
//								.invalidateHttpSession(true) // <-- Clear Session Data
//								.clearAuthentication(true)   // <-- Clear authentication
//								.deleteCookies("JSESSIONID") // <-- Delete session cookie
//								.permitAll();
//						}
//				)
//				.build();
//
//	}
//
//}