package com.jason.todoapp.fullstack_todo_app.common.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
  Instant timestamp,
  int status,
  String error,
  String message,
  String path,
  Map<String, ArrayList<String>> details
) {
  public static ApiErrorResponse of(
    HttpStatus httpStatus,
    String message,
    String path
  ) {
    return new ApiErrorResponse(
      Instant.now(),
      httpStatus.value(),
      httpStatus.getReasonPhrase(),
      message,
      path,
      null
    );
  }

  public static ApiErrorResponse of(
    HttpStatus httpStatus,
    String message,
    String path,
    Map<String, ArrayList<String>> errors
  ) {
    return new ApiErrorResponse(
      Instant.now(),
      httpStatus.value(),
      httpStatus.getReasonPhrase(),
      message,
      path,
      errors
    );
  }
}
