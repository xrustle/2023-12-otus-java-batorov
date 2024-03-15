package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class EvenSecondExceptionProcessor implements Processor {
  private final DateTimeProvider time;

  public EvenSecondExceptionProcessor(DateTimeProvider time) {
    this.time = time;
  }

  @Override
  public Message process(Message message) {
    var second = time.getDate().getSecond();

    if ((second & 1) == 0) {
      throw new EvenSecondException("Even second: " + second);
    }

    return message;
  }
}
