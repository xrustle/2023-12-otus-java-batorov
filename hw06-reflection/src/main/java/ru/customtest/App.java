package ru.customtest;

import ru.customtest.runner.TestRunner;
import ru.customtest.tests.CustomTests;

public class App {
  public static void main(String[] args) {
    TestRunner.test(CustomTests.class);
  }
}
