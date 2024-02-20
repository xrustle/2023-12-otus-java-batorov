package ru.customtest.runner;

import ru.customtest.annotations.After;
import ru.customtest.annotations.Before;
import ru.customtest.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class TestRunner {
  static Logger logger = Logger.getLogger(TestRunner.class.getName());

  private TestRunner() {}

  public static <T> void test(Class<T> clazz) {
    var beforeMethods = getMethods(Before.class, clazz);
    var afterMethods = getMethods(After.class, clazz);
    var testMethods = getMethods(Test.class, clazz);

    var testsFailed = testMethods.stream()
        .map(m -> TestRunner.executeTest(m, clazz, beforeMethods, afterMethods))
        .mapToInt(Integer::valueOf)
        .sum();

    logger.log(Level.INFO, "Successful tests: {0}", testMethods.size() - testsFailed);
    logger.log(Level.INFO, "Failed tests: {0}", testsFailed);
    logger.log(Level.INFO, "Total test: {0}", testMethods.size());
  }

  private static int executeTest(
      Method testMethod, Class<?> inClass, List<Method> beforeMethods, List<Method> afterMethods) {
    try {
      var testClassInstance = inClass.getConstructor().newInstance();

      beforeMethods.forEach(method -> invokeTest(method, testClassInstance));
      invokeTest(testMethod, testClassInstance);
      afterMethods.forEach(method -> invokeTest(method, testClassInstance));

    } catch (Exception e) {
      logger.log(Level.INFO, "Test {0} failed.", testMethod.getName());
      return 1;
    }
    logger.log(Level.INFO, "Test {0} successful.", testMethod.getName());
    return 0;
  }

  private static List<Method> getMethods(
      Class<? extends Annotation> withAnnotation, Class<?> fromClass) {
    return Arrays.stream(fromClass.getDeclaredMethods())
        .filter(m -> m.isAnnotationPresent(withAnnotation))
        .toList();
  }

  @SuppressWarnings("squid:S3011")
  private static <T> void invokeTest(Method method, T ofClassInstance) {
    try {
      method.setAccessible(true);
      method.invoke(ofClassInstance);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new TestFailedException("Test failed", e);
    }
  }
}
