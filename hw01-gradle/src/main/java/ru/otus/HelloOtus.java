package ru.otus;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.List;

public class HelloOtus {
    public static void main(String... args) {
        List<Integer> list = List.of(1, 1, 1, 2, 2, 4, 4, 4, 4, 4);
        Multiset<Integer> wordsMultiset = HashMultiset.create();

        wordsMultiset.addAll(list);
        System.out.println(wordsMultiset);
    }
}
