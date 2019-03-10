package com.academy.telesens.lesson11;

import java.util.Comparator;

public class LastNameComparator implements Comparator<Subscriber> {
    @Override
    public int compare(Subscriber subscriber1, Subscriber subscriber2){
        return (subscriber1.getLastName().trim().compareTo(subscriber2.getLastName().trim()));
    }
}