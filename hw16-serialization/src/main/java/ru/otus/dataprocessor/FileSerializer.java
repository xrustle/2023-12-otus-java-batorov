package ru.otus.dataprocessor;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {
  private final String filename;

  public FileSerializer(String fileName) {
    this.filename = fileName;
  }

  @Override
  public void serialize(Map<String, Double> data) throws IOException {
    // формирует результирующий json и сохраняет его в файл
    var json = convertToJson(data);
    writeToFile(json);
  }

  private String convertToJson(Map<String, Double> data) {
    var gson = new Gson();
    return gson.toJson(data);
  }

  private void writeToFile(String json) throws IOException {
    try (var bufferedWriter = new BufferedWriter(new FileWriter(filename))) {
      bufferedWriter.write(json);
    }
  }
}
