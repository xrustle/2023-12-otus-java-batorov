package ru.atm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.atm.exception.NotEnoughMoneySlotException;

@Getter
@RequiredArgsConstructor
@ToString
public class Slot {
  private final Denomination denomination;
  private int counter;

  public void insertCash(int number) {
    counter += number;
  }

  public void ejectCash(int number) {
    if (counter < number) {
      throw new NotEnoughMoneySlotException("Slot " + denomination.getValue() + " is empty!");
    }

    counter -= number;
  }
}
