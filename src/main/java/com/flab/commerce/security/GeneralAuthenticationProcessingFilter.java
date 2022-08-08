package com.flab.commerce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.commerce.domain.user.LoginDto;
import com.flab.commerce.util.Constants;
import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class GeneralAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

  private final ObjectMapper objectMapper;

  private final Validator validator;

  public GeneralAuthenticationProcessingFilter(ObjectMapper objectMapper, Validator validator,
      AuthenticationManager authenticationManager,
      AuthenticationSuccessHandler authenticationSuccessHandler,
      AuthenticationFailureHandler authenticationFailureHandler) {
    super(new OrRequestMatcher(
            new AntPathRequestMatcher("/users/login", HttpMethod.POST.toString()),
            new AntPathRequestMatcher("/owners/login", HttpMethod.POST.toString())),
        authenticationManager);
    this.objectMapper = objectMapper;
    this.validator = validator;
    setAuthenticationSuccessHandler(authenticationSuccessHandler);
    setAuthenticationFailureHandler(authenticationFailureHandler);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

    if (!isJson(request)) {
      throw new IllegalArgumentException("Authentication method not supported");
    }

    LoginDto loginDto = getLoginDto(request);
    validateLoginDto(loginDto);

    return getAuthenticationManager().authenticate(
        new UsernamePasswordAuthenticationToken(loginDto, loginDto.getPassword()));
  }

  private LoginDto getLoginDto(HttpServletRequest request) throws IOException {
    LoginDto loginDto = objectMapper.readValue(request.getReader(), LoginDto.class);
    loginDto.setUri(request.getRequestURI());
    return loginDto;
  }

  private boolean isJson(HttpServletRequest request) {
    return request.getHeader(Constants.CONTENT_TYPE).contains(MediaType.APPLICATION_JSON_VALUE);
  }

  private void validateLoginDto(LoginDto loginDto) {
    Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
