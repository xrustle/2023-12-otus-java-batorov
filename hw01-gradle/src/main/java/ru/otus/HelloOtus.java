package ru.otus;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.List;
import java.util.logging.Logger;

public class HelloOtus {
  static Logger logger = Logger.getLogger(HelloOtus.class.getName());

  public static void main(String... args) {
    List<Integer> list = List.of(1, 1, 1, 2, 2, 4, 4, 4, 4, 4);
    Multiset<Integer> wordsMultiset = HashMultiset.create();

    wordsMultiset.addAll(list);
    logger.info(wordsMultiset::toString);
  }
}
