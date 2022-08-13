package com.flab.commerce.config;

import com.flab.commerce.security.GeneralAuthenticationProcessingFilter;
import com.flab.commerce.security.GeneralLogoutSuccessHandler;
import com.flab.commerce.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final GeneralAuthenticationProcessingFilter generalAuthenticationProcessingFilter;
  private final GeneralLogoutSuccessHandler generalLogoutSuccessHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/users", "/users/login").permitAll()
        .antMatchers(HttpMethod.GET, "/users/logout").permitAll()
        .antMatchers(HttpMethod.POST, "/owners", "/owners/login").permitAll()
        .antMatchers(HttpMethod.GET, "/owners/logout").permitAll()
        .antMatchers(HttpMethod.GET, "/stores/*/menus").permitAll()
        .antMatchers(HttpMethod.POST, "/stores/*/menus").hasAuthority(Constants.ROLE_OWNER)
        .antMatchers(HttpMethod.DELETE, "/stores/*/menus/*").hasAuthority(Constants.ROLE_OWNER)
        .antMatchers(HttpMethod.PATCH, "/stores/*/menus/*").hasAuthority(Constants.ROLE_OWNER)
        .anyRequest().authenticated()
        .and()
        .addFilterBefore(generalAuthenticationProcessingFilter,
            UsernamePasswordAuthenticationFilter.class);

    http.logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/**/logout"))
        .logoutSuccessHandler(generalLogoutSuccessHandler);

    http.csrf().disable();
  }
}