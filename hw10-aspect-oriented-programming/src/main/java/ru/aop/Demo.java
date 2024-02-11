package ru.aop;

public class Demo {
    public static void main(String[] args) {
        TestLoggingInterface myClass = Ioc.createMyClass();

        myClass.calculation(6);
        myClass.calculation(6, 4);
        myClass.calculation(1, 2, "123");
    }
}
