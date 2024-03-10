package ru.atm;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@AllArgsConstructor
@Slf4j
public final class AtmImpl implements Atm {
  private final SlotSet slotSet;
  private final String name;

  public void loadCash(Map<Denomination, Integer> cash) {
    log.debug("Cash loading: " + cash);
    slotSet.addCash(cash);
    log.debug("Current state: " + slotSet);
  }

  public Map<Denomination, Integer> withdrawCash(int amount) {
    log.debug("Cash withdrawing: " + amount);

    var cash = slotSet.withdrawCash(amount);

    log.debug("Cash: " + cash);
    log.debug("Current state: " + slotSet);

    return cash;
  }

  public int countTotalAmount() {
    return slotSet.countTotalAmount();
  }
}
