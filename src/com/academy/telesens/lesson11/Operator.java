package com.academy.telesens.lesson11;

import java.util.ArrayList;
import java.util.Random;

public class Operator {
    private Long id;
    private String name;

    public Operator() {
    }

    public Operator(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {

        char firstLetterChar = this.name.trim().charAt(0);
        String firstLetterString = String.valueOf(firstLetterChar);
        String nameStringWithoutFirstLetter = this.name.substring(1);
        String resultNameString = firstLetterString + nameStringWithoutFirstLetter.toLowerCase();

        return resultNameString;
    }

    public void setName(String name) {
        this.name = name;
    }

    //==================================================//

    // Получаем id Оператора
    public static Long getOperatorId(String operatorName){
        Operators operator =  Operators.valueOf(operatorName.toUpperCase());
        int ordinal = operator.ordinal();
        Long operatorId = ordinal + 1L;
        return operatorId;
    }

    // Получаем имя оператора
    public static String getOperatorName() {
        String operatorName = "";
        Random random = new Random();
        int randomOperator = random.nextInt(3);
        Operators[] operators = Operators.values();
        for (int i = 0; i < Operators.values().length; i++)
            if (operators[i].ordinal() == randomOperator) {
                operatorName = operators[i].name();
                break;
            }
        return operatorName;
    }

    //==================================================//

    // Получаем телефонные номера определенного оператора
    public static String getOperatorPhoneNumber(String operatorName) {
        String operatorPhoneNumber = "";
        Random random = new Random();

        switch (operatorName.trim().toLowerCase()) {
            case "life": {
                operatorPhoneNumber = Operators.LIFE.phoneNumbers.get(random.nextInt(3)) + Operator.getPhoneNumber();
                break;
            }
            case "kyivstar": {
                operatorPhoneNumber = Operators.KYIVSTAR.phoneNumbers.get(random.nextInt(3)) + Operator.getPhoneNumber();
                break;
            }
            case "vodafone": {
                operatorPhoneNumber = Operators.VODAFONE.phoneNumbers.get(random.nextInt(3)) + Operator.getPhoneNumber();
                break;
            }
            default:
                break;
        }
/*
        if(operatorName.trim().toLowerCase().equals(Operators.valueOf("LIFE").toString().toLowerCase())){
            operatorPhoneNumber = Operators.LIFE.phoneNumbers.get(random.nextInt(3)) + Operator.getPhoneNumber();
        }
*/

        return operatorPhoneNumber;
    }

    //==================================================//

    // Генерируем номер телефона для оператора
    private static String getPhoneNumber() {
        String numbers = "";
        Random random = new Random();

        // генерируем последние 7 цифр
        for (int i = 0; i < 7; i++)
            numbers += String.valueOf(random.nextInt(10));

        return numbers;
    }

    //==================================================//

    public enum Operators {
        LIFE("38063", "38093", "38073"),
        VODAFONE("38050", "38066", "38095"),
        KYIVSTAR("38097", "38067", "38098");

        private ArrayList<String> phoneNumbers = new ArrayList<>();

        private Operators(String... numbers) {
            for (int i = 0; i < numbers.length; i++)
                phoneNumbers.add(numbers[i]);
        }
    }

    //==================================================//

    @Override
    public String toString() {
        return "Operator{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
