package ru.otus.dataprocessor;

import com.google.gson.Gson;
import ru.otus.model.Measurement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class ResourcesFileLoader implements Loader {
  private final InputStream resourceStream;

  public ResourcesFileLoader(String fileName) {
    this.resourceStream = getClass().getClassLoader().getResourceAsStream(fileName);
  }

  @Override
  public List<Measurement> load() throws IOException {
    // читает файл, парсит и возвращает результат
    String jsonString = readFileContent();
    return parseJsonMeasurements(jsonString);
  }

  private String readFileContent() throws IOException {
    try (var fileReader = new BufferedReader(new InputStreamReader(this.resourceStream))) {
      return fileReader.readLine();
    }
  }

  private List<Measurement> parseJsonMeasurements(String jsonString) {
    var gson = new Gson();
    return Arrays.asList(gson.fromJson(jsonString, Measurement[].class));
  }
}
