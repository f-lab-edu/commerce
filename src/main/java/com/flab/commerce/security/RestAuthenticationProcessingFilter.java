package com.flab.commerce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.user.dto.LoginDto;
import com.flab.commerce.util.Constants;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

public class RestAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

  @Autowired
  private ObjectMapper objectMapper;

  public RestAuthenticationProcessingFilter() {
    super(new AntPathRequestMatcher("/users/login", HttpMethod.POST.toString()));
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

    if (!isJson(request)) {
      throw new IllegalArgumentException("Authentication method not supported");
    }

    LoginDto loginDto = objectMapper.readValue(request.getReader(), LoginDto.class);

    String email = loginDto.getEmail();
    String password = loginDto.getPassword();

    if (!StringUtils.hasLength(email) && !StringUtils.hasLength(password)) {
      throw new IllegalArgumentException("Email or password not empty");
    }

    return getAuthenticationManager().authenticate(new RestAuthenticationToken(email, password));
  }

  private boolean isJson(HttpServletRequest request) {
    return request.getHeader(Constants.CONTENT_TYPE).contains(MediaType.APPLICATION_JSON_VALUE);
  }
}
