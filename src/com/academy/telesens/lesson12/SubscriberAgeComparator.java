package com.academy.telesens.lesson12;

import com.academy.telesens.lesson11.Subscriber;

import java.util.Comparator;

public class SubscriberAgeComparator implements Comparator<Subscriber> {
    @Override
    public int compare(Subscriber subscriber1, Subscriber subscriber2){
        if(subscriber1.getAge() == subscriber2.getAge())
            return 0;
        if(subscriber1.getAge() > subscriber2.getAge())
            return -1;
        else
            return 1;
    }
}

