package ru.atm.exception;

public class NotEnoughMoneySlotException extends RuntimeException {
  public NotEnoughMoneySlotException(String message) {
    super(message);
  }
}
