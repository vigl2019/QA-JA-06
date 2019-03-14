package com.academy.telesens.lesson12;

import com.academy.telesens.lesson11.Gender;
import com.academy.telesens.lesson11.Operator;
import com.academy.telesens.lesson11.Subscriber;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class LambdaDemo {
    public static void main(String[] args) {

        /*

        1) 	Составьте лямбда-выражение, которое возвращает значение true, если
		число принадлежит к диапазону чисел 10-20, включая граничные значения

         */

        System.out.println();
        System.out.println("Собственный функциональный интерфейс");

        // Собственный функциональный интерфейс
        FunctionalInterface fi = number -> {
            if ((number >= 10) && (number <= 20))
                return true;
            return false;
        };

        boolean result1 = fi.someMethod(15);
        assert result1 == true;
        System.out.println(result1);

        System.out.println();
        System.out.println("Встроенный функциональный интерфейс");

        // Встроенный функциональный интерфейс
        Predicate<Integer> predicate = i -> {
            if ((i >= 10) && (i <= 20))
                return true;
            return false;
        };

        boolean result2 = predicate.test(15);
        assert result2 == true;
        System.out.println(result2);

        //==================================================//

/*

2)	Создайте блочное лямбда-выражение для вычисления факториала целого числа.
		Продемонстрируйте его использование.

*/

        System.out.println();
        System.out.println("Вычисление факториала целого числа");

        FactorialInterface factorialInterface = n -> {
            //  BigInteger factorial = new BigInteger("1");
            BigInteger factorial = new BigInteger(BigInteger.ONE.toString());

            for (int i = 2; i <= n; i++)
                factorial = factorial.multiply(BigInteger.valueOf(i));
            return factorial;

        };

        BigInteger factorial = factorialInterface.calculateFactorial(10);
        assert factorial.equals(BigInteger.valueOf(3628800));
        System.out.println(factorial);

        //==================================================//

/*

3)  Создайте лямбда-выражение, которое переводит строку в верхний регистр.

		Перевести строку в верхний регистр с помощью лямбда-выражения.
		Перевести строку в верхний регистр с помощью ссылки на метод.

*/

        System.out.println();
        System.out.println("Перевод строки в верхний регистр с помощью лямбда-выражения");

        // Перевод строки в верхний регистр с помощью лямбда-выражения
        StringToUpperCaseInterface stringToUpperCaseInterface = startString -> {
            return startString.toUpperCase();
        };

        String startString1 = "someTestString1";
        String resultString1 = stringToUpperCaseInterface.stringToUppercase(startString1.trim());
        assert startString1.trim().toUpperCase().equals(resultString1);
        System.out.println("Исходная строка: " + startString1);
        System.out.println("UpperCase строка: " + resultString1);

        System.out.println();
        System.out.println("Перевод строки в верхний регистр с помощью ссылки на метод");

        // Перевод строки в верхний регистр с помощью ссылки на метод
        String startString2 = "someTestString2";
        String resultString2 = LambdaDemo.stringToUpperCaseMethod(String::toUpperCase, startString2);
        assert startString2.trim().toUpperCase().equals(resultString2);
        System.out.println("Исходная строка: " + startString2);
        System.out.println("UpperCase строка: " + resultString2);

        //==================================================//

/*
        4) Заданную строку

        - разбить на слова
        - отсортировать слова по алфавиту не учитывая регистр первой буквы
        - отсортировать слова по длине
        - отсортировать слова по последней букве
*/

        System.out.println();
        System.out.println("Сортировка слов");
        System.out.println();

        String string = "Earth is the third planet from the Sun and the only astronomical object known to harbor life";

        Consumer<String> consumer1 = inputString1 -> {

            // разбиваем строку на слова
            String[] strings = inputString1.trim().split(" ");
            List<String> stringArrayList = Arrays.asList(strings);

            System.out.println("Порядок слов до сортировки");
            System.out.println(stringArrayList);
            System.out.println();

            System.out.println("Сортировка слов по алфавиту не учитывая регистр первой буквы");
            // сортируем слова по алфавиту не учитывая регистр первой буквы
            AlphabetComparator alphabetComparator = new AlphabetComparator();
            stringArrayList.sort(alphabetComparator);
            System.out.println(stringArrayList);
            System.out.println();

            System.out.println("Сортировка слов по длине");
            // сортируем слова по длине
            LengthComparator lengthComparator = new LengthComparator();
            stringArrayList.sort(lengthComparator);
            System.out.println(stringArrayList);
            System.out.println();

            System.out.println("Сортировка слов по последней букве");
            // сортируем слова по последней букве
            LastLetterComparator lastLetterComparator = new LastLetterComparator();
            stringArrayList.sort(lastLetterComparator);
            System.out.println(stringArrayList);
            System.out.println();
        };

        consumer1.accept(string);

        //==================================================//

/*

5) Создайте лямбда-выражение, которое удаляет все пробелы из заданной строки и возвращает результат.
	Продемонстрируйте работу лямбда-выражения.

*/
        System.out.println("Лямбда-выражение, которое удаляет все пробелы из заданной строки и возвращает результат");

        // Используем встроенный функциональный интерфейс
        UnaryOperator<String> unaryOperator = inputString2 -> {

            String result = inputString2.replaceAll(" ", "");
            return result;
        };

        System.out.println(unaryOperator.apply(string));

        //==================================================//

/*

6) Отсортировать коллекцию лист List<Subscriber>, используя лямбда-выражения:
	a) по полю id
	b) по полям lastName и firstName
	c) по полю age по убыванию

*/

        System.out.println();
        System.out.println("Сортировка коллекции лист List<Subscriber>, используя лямбда-выражения");
        System.out.println();

        List<Subscriber> subscribersList = getSubscribers();

        System.out.println("Список Абонентов до сортировки");
        System.out.println();
        System.out.println(subscribersList);
        System.out.println();

        Consumer<List<Subscriber>> consumerId = subscribers -> {
            // Сортируем по полю id
            SubscriberIdComparator subscriberIdComparator = new SubscriberIdComparator();
            subscribers.sort(subscriberIdComparator);
        };
        consumerId.accept(subscribersList);

        System.out.println("Список Абонентов после сортировки по полю id");
        System.out.println();
        System.out.println(subscribersList);
        System.out.println();

        Consumer<List<Subscriber>> consumerLastNameFirstName = subscribers -> {
            // Сортируем по полям lastName и firstName
            SubscriberLastNameFirstNameComparator subscriberLastNameFirstNameComparator = new SubscriberLastNameFirstNameComparator();
            subscribers.sort(subscriberLastNameFirstNameComparator);
        };
        consumerLastNameFirstName.accept(subscribersList);

        System.out.println("Список Абонентов после сортировки по полям lastName и firstName");
        System.out.println();
        System.out.println(subscribersList);
        System.out.println();

        Consumer<List<Subscriber>> consumerAge = subscribers -> {
            // Сортируем по полю age по убыванию
            SubscriberAgeComparator subscriberAgeComparator = new SubscriberAgeComparator();
            subscribers.sort(subscriberAgeComparator);
        };
        consumerAge.accept(subscribersList);

        System.out.println("Список Абонентов после сортировки по полю age по убыванию");
        System.out.println();
        System.out.println(subscribersList);
        System.out.println();
    }

    //================================================================================//

    // Функциональный интерфейс
    public interface FunctionalInterface {
        boolean someMethod(int number);
    }

    // Интерфейс для вычисления факториала целого числа
    public interface FactorialInterface {
        BigInteger calculateFactorial(int n);
    }

    //==================================================//

    // Интерфейс для перевода строки в верхний регистр
    public interface StringToUpperCaseInterface {
        String stringToUppercase(String startString);
    }

    // Метод для перевода строки в верхний регистр
    public static String stringToUpperCaseMethod(StringToUpperCaseInterface stringToUpperCaseInterface, String startString) {
        return stringToUpperCaseInterface.stringToUppercase(startString.trim());
    }

    //==================================================//

    private static ArrayList<Subscriber> getSubscribers() {
        // Operators
        Operator operatorLife = new Operator();
        operatorLife.setId(1L);
        operatorLife.setName("Life");

        Operator operatorKievstar = new Operator();
        operatorKievstar.setId(2L);
        operatorKievstar.setName("Kievstar");

        Operator operatorVodafone = new Operator();
        operatorVodafone.setId(3L);
        operatorVodafone.setName("Vodafone");

        // Subscribers
        Subscriber subscriber1 = new Subscriber();
        subscriber1.setId(1L);
        subscriber1.setFirstName("Иван");
        subscriber1.setLastName("Васильев");
        subscriber1.setGender(Gender.MALE);
        subscriber1.setAge(23);
        subscriber1.setPhoneNumber("380630025465");
        subscriber1.setOperator(operatorLife);

        Subscriber subscriber2 = new Subscriber();
        subscriber2.setId(2L);
        subscriber2.setFirstName("Катя");
        subscriber2.setLastName("Петрова");
        subscriber2.setGender(Gender.FEMALE);
        subscriber2.setAge(34);
        subscriber2.setPhoneNumber("380670058694");
        subscriber2.setOperator(operatorKievstar);

        Subscriber subscriber3 = new Subscriber();
        subscriber3.setId(33L);
        subscriber3.setFirstName("Николай");
        subscriber3.setLastName("Поликарпов");
        subscriber3.setGender(Gender.MALE);
        subscriber3.setAge(32);
        subscriber3.setPhoneNumber("380630025999");
        subscriber3.setOperator(operatorLife);

        Subscriber subscriber4 = new Subscriber();
        subscriber4.setId(24L);
        subscriber4.setFirstName("Илона");
        subscriber4.setLastName("Сидорова");
        subscriber4.setGender(Gender.FEMALE);
        subscriber4.setAge(41);
        subscriber4.setPhoneNumber("380670058111");
        subscriber4.setOperator(operatorKievstar);

        ArrayList<Subscriber> subscribers = new ArrayList<>();

        subscribers.add(subscriber1);
        subscribers.add(subscriber2);
        subscribers.add(subscriber3);
        subscribers.add(subscriber4);

        return subscribers;
    }
}
