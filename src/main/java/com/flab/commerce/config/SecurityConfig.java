package com.flab.commerce.config;

import com.flab.commerce.security.GeneralAuthenticationFailureHandler;
import com.flab.commerce.security.GeneralAuthenticationProcessingFilter;
import com.flab.commerce.security.GeneralAuthenticationProvider;
import com.flab.commerce.security.GeneralAuthenticationSuccessHandler;
import com.flab.commerce.security.GeneralLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(restAuthenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/users", "/users/login").permitAll()
        .antMatchers(HttpMethod.GET, "/users/logout").permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilterBefore(restAuthenticationProcessingFilter(),
            UsernamePasswordAuthenticationFilter.class);


    http.logout()
        .logoutUrl("/users/logout")
        .logoutSuccessHandler(restLogoutSuccessHandler());

    http.csrf().disable();
  }

  @Bean
  public GeneralAuthenticationProvider restAuthenticationProvider() {
    return new GeneralAuthenticationProvider();
  }

  @Bean
  public GeneralAuthenticationProcessingFilter restAuthenticationProcessingFilter() throws Exception {
    GeneralAuthenticationProcessingFilter filter = new GeneralAuthenticationProcessingFilter();
    filter.setAuthenticationManager(authenticationManagerBean());
    filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
    filter.setAuthenticationFailureHandler(authenticationFailureHandler());
    return filter;
  }

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new GeneralAuthenticationSuccessHandler();
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new GeneralAuthenticationFailureHandler();
  }

  @Bean
  public LogoutSuccessHandler restLogoutSuccessHandler() {
    return new GeneralLogoutSuccessHandler();
  }
}