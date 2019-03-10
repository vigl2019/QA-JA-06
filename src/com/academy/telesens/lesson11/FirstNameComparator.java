package com.academy.telesens.lesson11;

import java.util.Comparator;

public class FirstNameComparator implements Comparator<Subscriber> {
    @Override
    public int compare(Subscriber subscriber1, Subscriber subscriber2){
        return (subscriber1.getFirstName().trim().compareTo(subscriber2.getFirstName()));
    }
}