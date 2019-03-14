package com.academy.telesens.lesson12;

import java.util.Comparator;

public class LengthComparator implements Comparator<String> {
    @Override
    public int compare(String string1, String string2) {
        if (string1.trim().length() > string2.trim().length())
            return 1;
        if (string1.trim().length() < string2.trim().length())
            return -1;

        return 0;
    }
}
