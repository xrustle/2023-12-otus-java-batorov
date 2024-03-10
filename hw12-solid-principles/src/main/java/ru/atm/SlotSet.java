package ru.atm;

import lombok.ToString;
import ru.atm.exception.SlotNotFoundException;
import ru.atm.exception.WithdrawException;

import java.util.*;

@ToString
public class SlotSet {
  private final Set<Slot> slots;

  public SlotSet(Slot... slots) {
    this.slots = new HashSet<>(Arrays.asList(slots));
  }

  private Slot getSlot(Denomination denomination) {
    for (var slot : this.slots) {
      if (slot.getDenomination().equals(denomination)) {
        return slot;
      }
    }
    throw new SlotNotFoundException("Slot " + denomination.getValue() + " not found.");
  }

  private void removeCash(Map<Denomination, Integer> cash) {
    for (var entry : cash.entrySet()) {
      this.getSlot(entry.getKey()).ejectCash(entry.getValue());
    }
  }

  public void addCash(Map<Denomination, Integer> cash) {
    for (var entry : cash.entrySet()) {
      this.getSlot(entry.getKey()).insertCash(entry.getValue());
    }
  }

  public Map<Denomination, Integer> withdrawCash(int amount) {
    var slotList = this.slots.stream()
        .sorted(Comparator.comparingInt(s -> -s.getDenomination().getValue()))
        .toList();
    Map<Denomination, Integer> cash = new EnumMap<>(Denomination.class);

    for (var slot : slotList) {
      if (amount >= slot.getDenomination().getValue()) {
        var count = amount / slot.getDenomination().getValue();

        if (count > slot.getCounter()) {
          count = slot.getCounter();
        }

        cash.put(slot.getDenomination(), count);
        amount -= count * slot.getDenomination().getValue();
      }
    }

    if (amount != 0) {
      throw new WithdrawException(
          "The requested amount " + amount + " cannot be withdrawn from the ATM.");
    }

    this.removeCash(cash);

    return cash;
  }

  public int countTotalAmount() {
    var totalAmount = 0;

    for (var slot : slots) {
      totalAmount += slot.getCounter() * slot.getDenomination().getValue();
    }

    return totalAmount;
  }
}
