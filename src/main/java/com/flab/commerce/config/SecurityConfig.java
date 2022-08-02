package com.flab.commerce.config;

import com.flab.commerce.security.user.GeneralAuthenticationFailureHandler;
import com.flab.commerce.security.user.GeneralAuthenticationProcessingFilter;
import com.flab.commerce.security.user.GeneralAuthenticationProvider;
import com.flab.commerce.security.user.GeneralAuthenticationSuccessHandler;
import com.flab.commerce.security.user.GeneralLogoutSuccessHandler;
import com.flab.commerce.security.user.GeneralUserDetailsService;
import com.flab.commerce.security.owner.OwnerAuthenticationFailureHandler;
import com.flab.commerce.security.owner.OwnerAuthenticationProcessingFilter;
import com.flab.commerce.security.owner.OwnerAuthenticationProvider;
import com.flab.commerce.security.owner.OwnerAuthenticationSuccessHandler;
import com.flab.commerce.security.owner.OwnerDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final GeneralUserDetailsService generalUserDetailsService;
  private final OwnerDetailsService ownerDetailsService;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(generalAuthenticationProvider())
        .authenticationProvider(ownerAuthenticationProvider());

  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/users", "/users/login").permitAll()
        .antMatchers(HttpMethod.GET, "/users/logout").permitAll()
        .antMatchers(HttpMethod.POST, "/owners", "/owners/login").permitAll()
        .antMatchers(HttpMethod.GET, "/owners").permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilterBefore(generalAuthenticationProcessingFilter(),
            UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(ownerAuthenticationProcessingFilter(),
            UsernamePasswordAuthenticationFilter.class);

    http.logout()
        .logoutRequestMatcher(new OrRequestMatcher(new AntPathRequestMatcher("/users/logout"),
            new AntPathRequestMatcher("/owners/logout")))
        .logoutSuccessHandler(generalLogoutSuccessHandler());

    http.csrf().disable();
  }

  @Bean
  public AuthenticationProvider generalAuthenticationProvider() {
    return new GeneralAuthenticationProvider(generalUserDetailsService);
  }

  @Bean
  public GeneralAuthenticationProcessingFilter generalAuthenticationProcessingFilter()
      throws Exception {
    GeneralAuthenticationProcessingFilter filter = new GeneralAuthenticationProcessingFilter();
    filter.setAuthenticationManager(authenticationManagerBean());
    filter.setAuthenticationSuccessHandler(generalAuthenticationSuccessHandler());
    filter.setAuthenticationFailureHandler(generalAuthenticationFailureHandler());
    return filter;
  }

  @Bean
  public AuthenticationSuccessHandler generalAuthenticationSuccessHandler() {
    return new GeneralAuthenticationSuccessHandler();
  }

  @Bean
  public AuthenticationFailureHandler generalAuthenticationFailureHandler() {
    return new GeneralAuthenticationFailureHandler();
  }

  @Bean
  public LogoutSuccessHandler generalLogoutSuccessHandler() {
    return new GeneralLogoutSuccessHandler();
  }

  @Bean
  public AuthenticationProvider ownerAuthenticationProvider() {
    return new OwnerAuthenticationProvider(ownerDetailsService);
  }

  @Bean
  public OwnerAuthenticationProcessingFilter ownerAuthenticationProcessingFilter()
      throws Exception {
    OwnerAuthenticationProcessingFilter filter = new OwnerAuthenticationProcessingFilter();
    filter.setAuthenticationManager(authenticationManagerBean());
    filter.setAuthenticationSuccessHandler(ownerAuthenticationSuccessHandler());
    filter.setAuthenticationFailureHandler(ownerAuthenticationFailureHandler());
    return filter;
  }

  @Bean
  public AuthenticationSuccessHandler ownerAuthenticationSuccessHandler() {
    return new OwnerAuthenticationSuccessHandler();
  }

  @Bean
  public AuthenticationFailureHandler ownerAuthenticationFailureHandler() {
    return new OwnerAuthenticationFailureHandler();
  }
}