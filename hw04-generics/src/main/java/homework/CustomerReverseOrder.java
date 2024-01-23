package homework;

import java.util.ArrayDeque;

public class CustomerReverseOrder {

    // надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    ArrayDeque<Customer> customerStack = new ArrayDeque<>();

    public void add(Customer customer) {
        customerStack.addLast(customer);
    }

    public Customer take() {
        return customerStack.pollLast();
    }
}
