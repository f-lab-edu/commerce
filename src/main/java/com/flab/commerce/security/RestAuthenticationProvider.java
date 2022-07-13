package com.flab.commerce.security;

import com.flab.commerce.user.UserObjectMapper;
import com.flab.commerce.user.dto.PrincipalDto;
import com.flab.commerce.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

public class RestAuthenticationProvider implements AuthenticationProvider {


  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    RestUserDetails restUserDetails = (RestUserDetails) userDetailsService.loadUserByUsername(username);

    String enteredPassword = (String) authentication.getCredentials();
    String encodingPassword = restUserDetails.getPassword();
    if (!Utils.PASSWORD_ENCODER.matches(enteredPassword, encodingPassword)) {
      throw new BadCredentialsException("Bad credentials");
    }

    PrincipalDto principalDto = UserObjectMapper.INSTANCE.userToPrincipalDto(restUserDetails.getUser());

    return new RestAuthenticationToken(principalDto, null,
        restUserDetails.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(RestAuthenticationToken.class);
  }
}
