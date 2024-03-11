package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {
  private List<String> data;

  public ObjectForMessage copy() {
    var objectForMessage = new ObjectForMessage();

    objectForMessage.setData(new ArrayList<>(this.getData()));

    return objectForMessage;
  }

  public List<String> getData() {
    return data;
  }

  public void setData(List<String> data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "ObjectForMessage{" + "data=" + data + '\'' + '}';
  }
}
