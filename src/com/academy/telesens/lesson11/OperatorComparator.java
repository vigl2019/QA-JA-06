package com.academy.telesens.lesson11;

import java.util.Comparator;

public class OperatorComparator implements Comparator<Subscriber> {
    @Override
    public int compare(Subscriber subscriber1, Subscriber subscriber2) {
        Operator operator1 = subscriber1.getOperator();
        Operator operator2 = subscriber2.getOperator();

        return operator1.getName().trim().compareTo(operator2.getName().trim());
    }
}