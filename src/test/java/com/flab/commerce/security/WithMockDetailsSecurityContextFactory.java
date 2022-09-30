package com.flab.commerce.security;

import com.flab.commerce.domain.owner.Owner;
import com.flab.commerce.domain.user.User;
import com.flab.commerce.security.owner.OwnerDetails;
import com.flab.commerce.security.user.GeneralUserDetails;
import com.flab.commerce.util.Constants;
import com.flab.commerce.util.Utils;
import java.time.ZonedDateTime;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockDetailsSecurityContextFactory implements
    WithSecurityContextFactory<WithMockDetails> {

  @Override
  public SecurityContext createSecurityContext(WithMockDetails withMockDetails) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();

    UserDetails userDetails = getUserDetails(withMockDetails.role());

    Authentication auth =
        UsernamePasswordAuthenticationToken.authenticated(userDetails, userDetails.getPassword(),
            userDetails.getAuthorities());

    context.setAuthentication(auth);

    return context;
  }

  private UserDetails getUserDetails(String rule) {
    if (Constants.ROLE_OWNER.equals(rule)) {
      return getOwnerDetails();
    }
    return getGeneralUserDetails();
  }

  private GeneralUserDetails getGeneralUserDetails() {
    User user = User.builder()
        .id(1L)
        .email("user@email.com")
        .password(Utils.encodePassword("password"))
        .name("이름")
        .phone("010-1234-5678")
        .address("주소지")
        .zipcode("123")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();
    return new GeneralUserDetails(user,
        Collections.singletonList((GrantedAuthority) () -> Constants.ROLE_USER));
  }

  private OwnerDetails getOwnerDetails() {
    Owner owner = Owner.builder()
        .id(1L)
        .email("owner@email.com")
        .password(Utils.encodePassword("password"))
        .name("이름")
        .phone("010-1234-5678")
        .createDateTime(ZonedDateTime.now())
        .modifyDateTime(ZonedDateTime.now())
        .build();

    return new OwnerDetails(owner,
        Collections.singletonList((GrantedAuthority) () -> Constants.ROLE_OWNER));
  }
}
