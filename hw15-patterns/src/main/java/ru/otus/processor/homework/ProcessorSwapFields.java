package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorSwapFields implements Processor {

  @Override
  public Message process(Message message) {
    var newField12 = message.getField11();

    return message.toBuilder().field11(message.getField12()).field12(newField12).build();
  }
}
