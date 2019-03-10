package com.academy.telesens.lesson11;

import java.util.Comparator;

public class AgeComparator implements Comparator<Subscriber> {
    @Override
    public int compare(Subscriber subscriber1, Subscriber subscriber2){
        if(subscriber1.getAge() == subscriber2.getAge())
            return 0;
        if(subscriber1.getAge() > subscriber2.getAge())
            return 1;
        else
            return -1;
    }
}
