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
    super(new AntPathRequestMatcher("/user/login", HttpMethod.POST.toString()));
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

    if (!isPost(request) || !isJson(request)) {
      throw new IllegalArgumentException("Authentication method not supported");
    }

    LoginDto loginDto = objectMapper.readValue(request.getReader(), LoginDto.class);
    if (!StringUtils.hasLength(loginDto.getEmail()) && !StringUtils.hasLength(
        loginDto.getPassword())) {
      throw new IllegalArgumentException("Email or password not empty");
    }

    return getAuthenticationManager().authenticate(
        new RestAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
  }

  private boolean isPost(HttpServletRequest request) {
    return HttpMethod.POST.toString().equals(request.getMethod());
  }

  private boolean isJson(HttpServletRequest request) {
    return request.getHeader(Constants.CONTENT_TYPE).contains(MediaType.APPLICATION_JSON_VALUE);
  }
}
