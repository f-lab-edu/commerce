package com.flab.commerce.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Spring MVC에서 발생하는 모든 예외 처리 클래스
 * @author byeonggil park
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    final BindingResult bindingResult = ex.getBindingResult();
    final FieldError fieldError = bindingResult.getFieldError();
    final String defaultMessage = fieldError.getDefaultMessage();
    return super.handleExceptionInternal(ex, defaultMessage, headers, status, request);
  }
}
