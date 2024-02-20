package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

  // важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
  private final TreeMap<Customer, String> treeMap =
      new TreeMap<>(Comparator.comparingLong(Customer::getScores));

  public Map.Entry<Customer, String> getSmallest() {
    // Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
    if (treeMap.isEmpty()) return null;

    var smallest = treeMap.firstEntry();

    return Map.entry(new Customer(smallest.getKey()), smallest.getValue());
  }

  public Map.Entry<Customer, String> getNext(Customer customer) {
    if (treeMap.isEmpty() || customer == null) return null;

    var next = treeMap.higherEntry(customer);

    return next != null ? Map.entry(new Customer(next.getKey()), next.getValue()) : null;
  }

  public void add(Customer customer, String data) {
    treeMap.put(customer, data);
  }
}
