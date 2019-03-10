package com.academy.telesens.lesson11;

public class Subscriber {
    private Long id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private int age;
    private String phoneNumber;
    private Operator operator;

    public Subscriber()
    {}

    public Subscriber(Long id, String firstName, String lastName, Gender gender, int age, String phoneNumber, Operator operator) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.operator = operator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "Subscriber: " +
                "id=" + id +
                " | firstName='" + firstName + '\'' +
                " | lastName='" + lastName + '\'' +
                " | gender=" + gender +
                " | age=" + age +
                " | phoneNumber='" + phoneNumber + '\'' +
                " | operator=" + operator + "\n";
    }
}





