package com.rabo.paymentinitiation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.rabo.paymentinitiation.model.ErrorReasonCode;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/**
     * subjectPrincipalRegex("CN=(.*?),") :- The regular expression used to extract a username from the client certificate’s subject name.
     * (CN value of the client certificate)
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .and().x509().subjectPrincipalRegex("CN=(.*?),")
                .userDetailsService(userDetailsService());
    }


    @Bean
    @Override
    public UserDetailsService userDetailsService() {
    	
    	return (UserDetailsService) username -> {
            if (username.startsWith("Sandbox-TPP")) {
            	return new User(username, "",
                        AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
            } else {
                throw new UsernameNotFoundException(ErrorReasonCode.UNKNOWN_CERTIFICATE.name());
            }
        };
    }
}