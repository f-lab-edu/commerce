package com.flab.commerce.config;

import com.flab.commerce.security.RestAuthenticationFailureHandler;
import com.flab.commerce.security.RestAuthenticationProcessingFilter;
import com.flab.commerce.security.RestAuthenticationProvider;
import com.flab.commerce.security.RestAuthenticationSuccessHandler;
import com.flab.commerce.security.RestLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
        .antMatchers("/user/login", "/user/register", "/user/logout").permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilterBefore(restAuthenticationProcessingFilter(),
            UsernamePasswordAuthenticationFilter.class);

    http.logout()
        .logoutUrl("/user/logout")
        .logoutSuccessHandler(restLogoutSuccessHandler());

    http.csrf().disable();
  }

  @Bean
  public RestAuthenticationProvider restAuthenticationProvider() {
    return new RestAuthenticationProvider();
  }

  @Bean
  public RestAuthenticationProcessingFilter restAuthenticationProcessingFilter() throws Exception {
    RestAuthenticationProcessingFilter filter = new RestAuthenticationProcessingFilter();
    filter.setAuthenticationManager(authenticationManagerBean());
    filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
    filter.setAuthenticationFailureHandler(authenticationFailureHandler());
    return filter;
  }

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new RestAuthenticationSuccessHandler();
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new RestAuthenticationFailureHandler();
  }

  @Bean
  public RestLogoutSuccessHandler restLogoutSuccessHandler() {
    return new RestLogoutSuccessHandler();
  }
}