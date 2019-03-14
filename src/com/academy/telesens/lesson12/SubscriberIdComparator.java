package com.academy.telesens.lesson12;

import com.academy.telesens.lesson11.Subscriber;

import java.util.Comparator;

public class SubscriberIdComparator implements Comparator<Subscriber> {
    @Override
    public int compare(Subscriber subscriber1, Subscriber subscriber2) {
        if (subscriber1.getId() > subscriber2.getId())
            return 1;

        if (subscriber1.getId() < subscriber2.getId())
            return -1;

        return 0;
    }
}
