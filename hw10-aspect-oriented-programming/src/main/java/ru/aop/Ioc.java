package ru.aop;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
class Ioc {
    private static final Map<Method, Boolean> checkedMethods = new HashMap<>();

    private Ioc() {
    }

    static <T> Object instanceOf(Class<T> clazz) throws ReflectiveOperationException {
        var constructor = clazz.getConstructor();

        InvocationHandler handler = new ProxyInvocationHandler<>(constructor.newInstance());
        return Proxy.newProxyInstance(Ioc.class.getClassLoader(), clazz.getInterfaces(), handler);
    }

    static class ProxyInvocationHandler<T> implements InvocationHandler {
        private final T myClass;

        ProxyInvocationHandler(T myClass) {
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
            if (checkedMethods.containsKey(method)) return checkedMethods.get(method);

            var hasLogAnnotation = Arrays.stream(myClass.getClass().getMethods())
                    .filter(m -> m.getName().equals(method.getName()))
                    .filter(m -> m.getReturnType().equals(method.getReturnType()))
                    .filter(m -> Arrays.equals(m.getParameterTypes(), method.getParameterTypes()))
                    .anyMatch(m -> m.isAnnotationPresent(Log.class));

            checkedMethods.put(method, hasLogAnnotation);

            return hasLogAnnotation;
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" + "myClass=" + myClass + '}';
        }
    }
}
