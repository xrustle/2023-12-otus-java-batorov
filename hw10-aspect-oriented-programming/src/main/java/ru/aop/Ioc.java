package ru.aop;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

@Slf4j
class Ioc {
    private Ioc() {
    }

    static TestLoggingInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLogging());

        return (TestLoggingInterface) Proxy.newProxyInstance(
                Ioc.class.getClassLoader(), new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLogging myClass;

        DemoInvocationHandler(TestLogging myClass) {
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isLogAnnotationPresent(method)) {
                log.info("executed method: {}, param: {}", method.getName(), args);
            }

            return method.invoke(myClass, args);
        }

        private boolean isLogAnnotationPresent(Method method) {
            return Arrays.stream(myClass.getClass().getMethods())
                    .filter(m -> m.getName().equals(method.getName()))
                    .filter(m -> m.getReturnType().equals(method.getReturnType()))
                    .filter(m -> Arrays.equals(m.getParameterTypes(), method.getParameterTypes()))
                    .anyMatch(m -> m.isAnnotationPresent(Log.class));
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" + "myClass=" + myClass + '}';
        }
    }
}
