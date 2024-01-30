package ru.customtest.runner;

import ru.customtest.annotations.After;
import ru.customtest.annotations.Before;
import ru.customtest.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public final class TestRunner {
    static Logger logger = Logger.getLogger(TestRunner.class.getName());

    private TestRunner() {
    }

    @SuppressWarnings({"squid:S3011", "squid:S3776"})
    public static <T> void test(Class<T> clazz) {
        var methods = clazz.getDeclaredMethods();
        var beforeMethods = new ArrayList<Method>();
        var afterMethods = new ArrayList<Method>();
        var testMethods = new ArrayList<Method>();
        var constructor = clazz.getConstructors()[0];
        AtomicInteger testsFailed = new AtomicInteger();

        Arrays.stream(methods).forEach(method -> {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            }
            if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            }
            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        });

        testMethods.forEach(testMethod -> {
            try {
                var testInstance = constructor.newInstance();

                beforeMethods.forEach(beforeMethod -> {
                    try {
                        beforeMethod.setAccessible(true);
                        beforeMethod.invoke(testInstance);
                    } catch (IllegalAccessException e) {
                        logger.info("IllegalAccessException");
                    } catch (InvocationTargetException e) {
                        logger.info("InvocationTargetException");
                    }
                });

                testMethod.setAccessible(true);
                testMethod.invoke(testInstance);

                afterMethods.forEach(afterMethod -> {
                    try {
                        afterMethod.setAccessible(true);
                        afterMethod.invoke(testInstance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        logger.info("Invoking exception");
                    }
                });
            } catch (Exception e) {
                logger.info("Test " + testMethod.getName() + " failed.");
                testsFailed.getAndIncrement();
            }
            logger.info("Test " + testMethod.getName() + " successful.");
        });

        logger.info("Successful tests: " + (testMethods.size() - testsFailed.get()));
        logger.info("Failed tests: " + testsFailed.get());
        logger.info("Total test: " + testMethods.size());
    }
}
