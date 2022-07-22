package com.flab.commerce.security;

import com.flab.commerce.user.User;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class GeneralUserDetails extends org.springframework.security.core.userdetails.User {

  private User user;

  public GeneralUserDetails(User user,
      Collection<? extends GrantedAuthority> authorities) {
    super(user.getEmail(), user.getPassword(), authorities);
    this.user = user;
  }
}