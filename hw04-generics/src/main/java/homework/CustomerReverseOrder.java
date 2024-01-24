package homework;

import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    // надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    private final Deque<Customer> customerStack = new ArrayDeque<>();

    public void add(Customer customer) {
        customerStack.push(customer);
    }

    public Customer take() {
        return customerStack.pop();
    }
}
