package com.flab.commerce.security;

import com.flab.commerce.security.owner.OwnerDetailsService;
import com.flab.commerce.security.user.GeneralUserDetailsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Getter
@Service
@RequiredArgsConstructor
public class UserDetailsServiceFactory {

  private final GeneralUserDetailsService generalUserDetailsService;

  private final OwnerDetailsService ownerDetailsService;

  public UserDetailsService getUserDetailsService(String uri) {
    if (uri.equals("/owners/login")) {
      return ownerDetailsService;
    }
    return generalUserDetailsService;
  }
}
