package com.academy.telesens.lesson12;

import java.util.Comparator;

public class LastLetterComparator implements Comparator<String> {
    @Override
    public int compare(String string1, String string2){
        String s1 = string1.trim().substring(string1.trim().length() - 1);
        String s2 = string2.trim().substring(string2.trim().length() - 1);

        return s1.compareTo(s2);
    }
}

