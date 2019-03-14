package com.academy.telesens.lesson12;

import java.util.Comparator;

public class AlphabetComparator implements Comparator<String> {
    @Override
    public int compare(String string1, String string2){

        String firstLetter1 = string1.trim().substring(0, 1);
        String firstLetter2 = string2.trim().substring(0, 1);

        firstLetter1 = firstLetter1.toLowerCase();
        firstLetter2 = firstLetter2.toLowerCase();

        String s1 = string1.trim().substring(1);
        String s2 = string2.trim().substring(1);

        string1 = firstLetter1 + s1;
        string2 = firstLetter2 + s2;

//      return string1.trim().toLowerCase().compareTo(string2.trim().toLowerCase());
        return string1.trim().compareToIgnoreCase(string2.trim());
    }
}
