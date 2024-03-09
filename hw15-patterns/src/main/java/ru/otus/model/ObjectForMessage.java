package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {
  private List<String> data;

  public ObjectForMessage() {}

  public ObjectForMessage(ObjectForMessage template) {
    this.data = new ArrayList<>(template.getData());
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
