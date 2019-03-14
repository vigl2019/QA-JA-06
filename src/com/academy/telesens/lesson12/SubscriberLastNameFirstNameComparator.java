package com.academy.telesens.lesson12;

import com.academy.telesens.lesson11.Subscriber;

import java.util.Comparator;

public class SubscriberLastNameFirstNameComparator implements Comparator<Subscriber> {
    @Override
    public int compare(Subscriber subscriber1, Subscriber subscriber2) {
        if (subscriber1.getLastName().equals(subscriber2.getLastName()))
            return subscriber1.getFirstName().compareTo(subscriber2.getFirstName());
        else
            return subscriber1.getLastName().compareTo(subscriber2.getLastName());
    }
}
