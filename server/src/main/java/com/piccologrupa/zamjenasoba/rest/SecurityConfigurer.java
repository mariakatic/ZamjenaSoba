package com.piccologrupa.zamjenasoba.rest;

import com.piccologrupa.zamjenasoba.jwt.AuthEntryPointJwt;
import com.piccologrupa.zamjenasoba.jwt.AuthTokenFilter;
import com.piccologrupa.zamjenasoba.service.StudentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SessionRegistry sessionRegistry() {
	    return new SessionRegistryImpl();
	}

	@Autowired
	private StudentDetailService studentDetailService;
	
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(studentDetailService).passwordEncoder(bCryptPasswordEncoder());
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.cors().and().csrf().disable();

		http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests().antMatchers("/api/v1/**").permitAll()
			.antMatchers("/api/test/**").permitAll()
			.antMatchers("/").permitAll()
			.anyRequest().permitAll();
		
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		
				/*.authorizeRequests()
					.antMatchers("/admin").hasAuthority("ADMIN")
                	.antMatchers("/student").hasAnyAuthority("ADMIN", "STUDENT")
                	.antMatchers("/api/v1/register").permitAll()
					.anyRequest().permitAll()
				.and()
				.formLogin()
					.loginPage("/api/v1/login")
					.permitAll();*/

	}

}
