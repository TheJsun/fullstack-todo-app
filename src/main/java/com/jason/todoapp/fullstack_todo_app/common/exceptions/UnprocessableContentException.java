package com.jason.todoapp.fullstack_todo_app.common.exceptions;

public class UnprocessableContentException extends RuntimeException {

  public UnprocessableContentException(String message) {
    super(message);
  }
}
