package com.flab.commerce.security.owner;

import com.flab.commerce.domain.owner.OwnerObjectMapper;
import com.flab.commerce.domain.owner.PrincipalOwnerDto;
import com.flab.commerce.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

@RequiredArgsConstructor
public class OwnerAuthenticationProvider implements AuthenticationProvider {

  private final UserDetailsService userDetailsService;


  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    OwnerDetails ownerDetails = (OwnerDetails) userDetailsService.loadUserByUsername(username);

    String enteredPassword = (String) authentication.getCredentials();
    String encodingPassword = ownerDetails.getPassword();
    if (!Utils.PASSWORD_ENCODER.matches(enteredPassword, encodingPassword)) {
      throw new BadCredentialsException("Bad credentials");
    }

    PrincipalOwnerDto principalDto = OwnerObjectMapper.INSTANCE.toDto(ownerDetails.getOwner());

    return new OwnerAuthenticationToken(principalDto, null, ownerDetails.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(OwnerAuthenticationToken.class);
  }
}
