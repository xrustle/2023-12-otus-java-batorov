package ru.aop;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestLogging implements TestLoggingInterface {
  @Log
  @Override
  public void calculation(int param) {
    log.info("Text from method calculation(int)");
  }

  @Override
  public void calculation(int param1, int param2) {
    log.info("Text from method calculation(int, int)");
  }

  @Log
  @Override
  public void calculation(int param1, int param2, String param3) {
    log.info("Text from method calculation(int, int, String)");
  }
}
