package com.flab.commerce.security.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.user.dto.LoginDto;
import com.flab.commerce.util.Constants;
import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


public class GeneralAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private Validator validator;

  public GeneralAuthenticationProcessingFilter() {
    super(new AntPathRequestMatcher("/users/login", HttpMethod.POST.toString()));
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

    if (!isJson(request)) {
      throw new IllegalArgumentException("Authentication method not supported");
    }
    
    LoginDto loginDto = objectMapper.readValue(request.getReader(), LoginDto.class);
    validationLoginDto(loginDto);

    return getAuthenticationManager().authenticate(
        new GeneralAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
  }

  private boolean isJson(HttpServletRequest request) {
    return request.getHeader(Constants.CONTENT_TYPE).contains(MediaType.APPLICATION_JSON_VALUE);
  }

  private void validationLoginDto(LoginDto loginDto) {
    Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
