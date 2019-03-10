package com.academy.telesens.lesson11;

public enum Gender {
    MALE("м"),
    FEMALE("ж");

    String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    //==================================================//

    @Override
    public String toString() {
        return this.gender;
    }
}
