package com.flab.commerce.security;

import com.flab.commerce.mapper.UserMapper;
import com.flab.commerce.user.User;
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
public class RestUserDetailService implements UserDetailsService {

  private final UserMapper userMapper;

  @Override
  public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
    User user = userMapper.findByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException(email);
    }

    List<GrantedAuthority> roles = Collections.singletonList(
        new SimpleGrantedAuthority(Constants.ROLE_USER));

    return new UserContext(user, roles);
  }
}
