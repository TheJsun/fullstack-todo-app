package com.jason.todoapp.fullstack_todo_app.common;

import com.jason.todoapp.fullstack_todo_app.common.dtos.ApiErrorResponse;
import com.jason.todoapp.fullstack_todo_app.common.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

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
