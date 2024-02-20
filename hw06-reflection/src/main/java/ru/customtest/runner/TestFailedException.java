package ru.customtest.runner;

public class TestFailedException extends RuntimeException {
  public TestFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
