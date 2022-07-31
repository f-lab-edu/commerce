package com.flab.commerce.security.owner;

import com.flab.commerce.domain.owner.Owner;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class OwnerDetails extends org.springframework.security.core.userdetails.User {

  private Owner owner;

  public OwnerDetails(Owner owner,
      Collection<? extends GrantedAuthority> authorities) {
    super(owner.getEmail(), owner.getPassword(), authorities);
    this.owner = owner;
  }
}