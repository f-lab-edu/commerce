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

public class GeneralAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    GeneralUserDetails userDetails = (GeneralUserDetails) userDetailsService.loadUserByUsername(username);

    String enteredPassword = (String) authentication.getCredentials();
    String encodingPassword = userDetails.getPassword();
    if (!Utils.PASSWORD_ENCODER.matches(enteredPassword, encodingPassword)) {
      throw new BadCredentialsException("Bad credentials");
    }

    PrincipalDto principalDto = UserObjectMapper.INSTANCE.userToPrincipalDto(userDetails.getUser());

    return new GeneralAuthenticationToken(principalDto, null, userDetails.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(GeneralAuthenticationToken.class);
  }
}
