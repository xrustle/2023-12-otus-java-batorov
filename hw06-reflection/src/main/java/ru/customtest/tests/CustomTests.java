package ru.customtest.tests;

import ru.customtest.annotations.After;
import ru.customtest.annotations.Before;
import ru.customtest.annotations.Test;

import java.util.logging.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CustomTests {
  Logger logger = Logger.getLogger(CustomTests.class.getName());

  @Before
  public void before1() {
    logger.info("Before 1");
  }

  @Before
  public void before2() {
    logger.info("Before 2");
  }

  @After
  public void after() {
    logger.info("After");
  }

  @Test
  public void test1() {
    logger.info("test1 start. Should fail.");
    assertThat("1").isEqualTo("2");
  }

  @Test
  public void test2() {
    logger.info("test2 start. Should be successful.");
    assertThat("1").isEqualTo("1");
  }
}
