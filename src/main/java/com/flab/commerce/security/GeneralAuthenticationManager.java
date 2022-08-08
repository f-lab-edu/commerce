package com.flab.commerce.security;

import com.flab.commerce.domain.user.LoginDto;
import com.flab.commerce.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeneralAuthenticationManager implements AuthenticationManager {

  private final UserDetailsServiceFactory userDetailsServiceFactory;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    LoginDto loginDto = (LoginDto) authentication.getPrincipal();
    UserDetailsService userDetailsService = userDetailsServiceFactory.getUserDetailsService(
        loginDto.getUri());
    UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getEmail());

    String enteredPassword = (String) authentication.getCredentials();
    String encodingPassword = userDetails.getPassword();
    if (!Utils.PASSWORD_ENCODER.matches(enteredPassword, encodingPassword)) {
      throw new BadCredentialsException("Bad credentials");
    }

    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }
}
