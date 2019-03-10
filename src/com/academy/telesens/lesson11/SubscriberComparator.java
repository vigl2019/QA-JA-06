package com.academy.telesens.lesson11;

import java.util.Comparator;

public class SubscriberComparator implements Comparator<Subscriber> {
    public int compare(Subscriber subscriber1, Subscriber subscriber2){
        if(subscriber1.getId() > subscriber2.getId())
            return -1;
        if(subscriber1.getId() < subscriber2.getId())
            return 1;
        else
            return 0;
    }
}
