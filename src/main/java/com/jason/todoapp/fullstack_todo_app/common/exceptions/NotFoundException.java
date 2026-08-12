package com.jason.todoapp.fullstack_todo_app.common.exceptions;

public class NotFoundException extends RuntimeException {

  public NotFoundException(String message) {
    super(message);
  }
}
