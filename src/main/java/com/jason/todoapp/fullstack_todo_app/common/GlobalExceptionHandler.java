package com.jason.todoapp.fullstack_todo_app.common;

import com.jason.todoapp.fullstack_todo_app.common.dtos.ApiErrorResponse;
import com.jason.todoapp.fullstack_todo_app.common.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatchException(
    MethodArgumentTypeMismatchException ex,
    HttpServletRequest req
  ) {
    ApiErrorResponse response = ApiErrorResponse.of(
      HttpStatus.BAD_REQUEST,
      ex.getMessage(),
      req.getRequestURI()
    );
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
    MethodArgumentNotValidException ex,
    HttpServletRequest req
  ) {
    Map<String, ArrayList<String>> errors = new HashMap<>();

    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      String field = fieldError.getField();
      String message = fieldError.getDefaultMessage();

      errors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
    }
    ApiErrorResponse response = ApiErrorResponse.of(
      HttpStatus.BAD_REQUEST,
      ex.getMessage(),
      req.getRequestURI(),
      errors
    );
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleNotFoundException(
    NotFoundException ex,
    HttpServletRequest req
  ) {
    ApiErrorResponse response = ApiErrorResponse.of(
      HttpStatus.NOT_FOUND,
      ex.getMessage(),
      req.getRequestURI()
    );
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }
}
