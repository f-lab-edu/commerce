package com.flab.commerce.security.user;

import com.flab.commerce.domain.user.User;
import com.flab.commerce.domain.user.UserMapper;
import com.flab.commerce.util.Constants;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneralUserDetailsService implements UserDetailsService {

  private final UserMapper userMapper;

  @Override
  public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
    User user = userMapper.findByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException(email);
    }

    List<GrantedAuthority> roles = Collections.singletonList(
        new SimpleGrantedAuthority(Constants.ROLE_USER));

    return new GeneralUserDetails(user, roles);
  }
}
