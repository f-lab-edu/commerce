package com.flab.commerce.security.owner;

import com.flab.commerce.domain.owner.Owner;
import com.flab.commerce.domain.owner.OwnerMapper;
import com.flab.commerce.util.Constants;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerDetailsService implements UserDetailsService {

  private final OwnerMapper ownerMapper;

  @Override
  public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
    Owner owner = ownerMapper.findByEmail(email);
    if (owner == null) {
      throw new UsernameNotFoundException(email);
    }

    List<GrantedAuthority> roles = Collections.singletonList(
        new SimpleGrantedAuthority(Constants.ROLE_OWNER));

    return new OwnerDetails(owner, roles);
  }
}
