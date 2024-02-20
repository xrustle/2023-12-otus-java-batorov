package ru.atm;

import org.junit.jupiter.api.Test;
import ru.atm.exception.WithdrawException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.atm.Denomination.FIFTY;
import static ru.atm.Denomination.HUNDRED;

class AtmTests {
  @Test
  void atmLoadCashTest() {
    var slotSet = new SlotSet(new Slot(FIFTY), new Slot(HUNDRED));
    var atm = new Atm(slotSet, "ATM 1");

    Map<Denomination, Integer> cash = new HashMap<>();
    cash.put(FIFTY, 3);
    cash.put(HUNDRED, 5);

    atm.loadCash(cash);

    assertEquals(3 * 50 + 5 * 100, atm.countTotalAmount());
  }

  @Test
  void atmWithdrawCashTest() {
    var slotSet = new SlotSet(new Slot(FIFTY), new Slot(HUNDRED));
    var atm = new Atm(slotSet, "ATM 1");
    Map<Denomination, Integer> cash = new HashMap<>();

    cash.put(FIFTY, 3);
    cash.put(HUNDRED, 5);

    atm.loadCash(cash);

    var withdrawal = atm.withdrawCash(600);

    assertEquals(50, atm.countTotalAmount());
    assertEquals(5, withdrawal.get(HUNDRED));
    assertEquals(2, withdrawal.get(FIFTY));
  }

  @Test
  void atmWithdrawCashExceptionTest() {
    var slotSet = new SlotSet(new Slot(FIFTY), new Slot(HUNDRED));
    var atm = new Atm(slotSet, "ATM 1");
    Map<Denomination, Integer> cash = new HashMap<>();

    cash.put(FIFTY, 3);
    cash.put(HUNDRED, 5);

    atm.loadCash(cash);

    assertThrows(WithdrawException.class, () -> atm.withdrawCash(630));
  }
}
