package com.flab.commerce.exception;


import static com.flab.commerce.util.Constants.JSON_PROCESSING_EXCEPTION_MESSAGE;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Spring MVC에서 발생하는 모든 예외 처리 클래스
 *
 * @author byeonggil park
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(JsonProcessingException.class)
  public ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException ex) {
    return ResponseEntity.badRequest().body(JSON_PROCESSING_EXCEPTION_MESSAGE);
  }
  
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    final BindingResult bindingResult = ex.getBindingResult();
    final FieldError fieldError = bindingResult.getFieldError();
    final String defaultMessage = fieldError.getDefaultMessage();
    return super.handleExceptionInternal(ex, defaultMessage, headers, status, request);
  }


  @ExceptionHandler
  public ResponseEntity<Object> handleException(Exception ex) throws Exception {
    if (ex instanceof BadInputException) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    } else if (ex instanceof AccessDeniedException) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    } else {
      throw ex;
    }
  }

}
