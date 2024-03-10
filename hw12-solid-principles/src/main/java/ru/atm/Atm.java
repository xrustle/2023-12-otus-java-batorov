package ru.atm;

import java.util.Map;

public interface Atm {
  void loadCash(Map<Denomination, Integer> cash);

  Map<Denomination, Integer> withdrawCash(int amount);

  int countTotalAmount();
}
