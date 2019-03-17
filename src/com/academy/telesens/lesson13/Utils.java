package com.academy.telesens.lesson13;

import com.academy.telesens.lesson11.Gender;
import com.academy.telesens.lesson11.Operator;
import com.academy.telesens.lesson11.Subscriber;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.*;
import java.util.*;

import static com.academy.telesens.lesson11.Operator.getOperatorPhoneNumber;

public class Utils {
    private static String javaPartPropertiesPath; // путь к файлу настроек

    private static String maleFirstNamesPath; // путь к файлу мужских имен
    private static String maleLastNamesPath; // путь к файлу мужских фамилий
    private static String femaleFirstNamesPath; // путь к файлу женских имен
    private static String femaleLastNamesPath; // путь к файлу женских фамилий

    private static int ageFrom; // начальный возраст абонента
    private static int ageTo; // конечный возраст абонента

    private static int subscribersCount; // количество абонентов

    private static String JDBC_URL = "jdbc:mysql://localhost:3306/qa-ja-06?user=root&password=root&serverTimezone=UTC&useSSL=false";

    //================================================================================//

    public Utils() {
        // Читаем пути к файлам
        Properties properties = new Properties();

        File file = new File("d:/myprojects/java-part/data/java-part.properties");
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                properties.load(fis);

                // Получаем путь к файлу настроек
                javaPartPropertiesPath = properties.getProperty("java-part.prop");

                // Получаем путь к файлу мужских имен
                maleFirstNamesPath = properties.getProperty("male.firstnames");
                // Получаем путь к файлу мужских фамилий
                maleLastNamesPath = properties.getProperty("male.lastnames");

                // Получаем путь к файлу женских имен
                femaleFirstNamesPath = properties.getProperty("female.firstnames");
                // Получаем путь к файлу женских фамилий
                femaleLastNamesPath = properties.getProperty("female.lastnames");

                // Получаем начальный возраст абонента
                ageFrom = Integer.parseInt(properties.getProperty("age.from"));
                // Получаем конечный возраст абонента
                ageTo = Integer.parseInt(properties.getProperty("age.to"));

                // Получаем количество Абонентов
                subscribersCount = Integer.parseInt(properties.getProperty("subscribers.count"));
            } catch (Exception e) {
//              e.printStackTrace();
                System.out.println(e.getMessage());
            }
        } else
            System.out.println("Файл не существует!");
    }

    // Генерируем данные для Абонента
    public ArrayList<Subscriber> createAbonentData() {

        ArrayList<Subscriber> subscriberList = new ArrayList<>();

        for (Long i = 1L; i <= subscribersCount; i++) {
            Subscriber subscriber = new Subscriber();
            subscriber.setId(i);

            Random random = new Random();
            Gender[] genderArray = Gender.values();
            Gender gender = genderArray[random.nextInt(genderArray.length)];

            if (gender.toString().trim().toLowerCase().equals("м")) {
                subscriber.setFirstName(getRendomStringfromFile(maleFirstNamesPath));
                subscriber.setLastName(getRendomStringfromFile(maleLastNamesPath));
            } else {
                subscriber.setFirstName(getRendomStringfromFile(femaleFirstNamesPath));
                subscriber.setLastName(getRendomStringfromFile(femaleLastNamesPath));
            }

            subscriber.setGender(gender);

            int age = ageFrom + random.nextInt((ageTo - ageFrom) + 1);
            subscriber.setAge(age);

            Operator operator = new Operator();
            operator.setName(Operator.getOperatorName());
            operator.setId(Operator.getOperatorId(operator.getName()));

            subscriber.setPhoneNumber(getOperatorPhoneNumber(operator.getName()));
            subscriber.setOperator(operator);

            subscriberList.add(subscriber);
        }

        return subscriberList;
    }

    // Добавляем данные абонентов в таблицу abonent
    public void addSubscribersDataToAbonentTable() {
        try {
            // Получаем данные для Абонентов
            List<Subscriber> subscribers = this.createAbonentData();

            Connection connection = this.getConnection();

            // Автогенерация первичного ключа в таблице abonent
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO abonent(first_name, last_name, gender, age) VALUES(?, ?, ?, ?)");

            // Добавляем данные Абонентов в таблицу abonent
            for (Subscriber subscriber : subscribers) {
                preparedStatement.setString(1, subscriber.getFirstName());
                preparedStatement.setString(2, subscriber.getLastName());

                if (subscriber.getGender().equals(Gender.MALE))
                    preparedStatement.setString(3, "m");
                else
                    preparedStatement.setString(3, "f");

                preparedStatement.setInt(4, subscriber.getAge());

                preparedStatement.execute();
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Добавляем телефонные номера Оператора в таблицу operator_number
    public void addPhoneNumbersToOperatorNumberTable(String operatorName) {
        try {
            // Получаем телефонные номера Оператора
            List<String> operatorPhoneNumbers = this.createOperatorPhoneNumbers(operatorName);
            Long operatorId = Operator.getOperatorId(operatorName);

            Connection connection = this.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO operator_number(number, operator_id) VALUES(?, ?)");

            // Добавляем телефонные номера Оператора в таблицу operator_number
            for (String operatorPhoneNumber : operatorPhoneNumbers) {
                preparedStatement.setString(1, operatorPhoneNumber);
                preparedStatement.setLong(2, operatorId);

                preparedStatement.execute();
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Генерируем номера телефонов определенного Оператора
    public ArrayList<String> createOperatorPhoneNumbers(String operatorName) {

        ArrayList<String> operatorPhoneNumbers = new ArrayList<>();

        for (int i = 0; i <= 1000; i++) {
            String phoneNumber = Operator.getOperatorPhoneNumber(operatorName);
            operatorPhoneNumbers.add(phoneNumber);
        }

        return operatorPhoneNumbers;
    }

    // Наполняем таблицу phone_book
    public void addDataToPhoneBookTable() {

        // Берем свободные номера из таблицы operator_number
        List<String> phoneOperatorNumbers = this.getFreePhoneOperatorNumbers();
        List<Long> subscriberIds = new ArrayList<>();

        Connection connection = this.getConnection();

        try {
            // Берем абонентов из таблицы abonent
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT abonent_id FROM abonent");
            while (resultSet.next()) {
                Long abonentId = resultSet.getLong("abonent_id");
                subscriberIds.add(abonentId);
            }

            // Наполняем таблицу phone_book
            Random randomPhoneOperator = new Random();
            Random randomSubscriber = new Random();

            // Телефонных номеров должно быть больше, чем абонентов,
            // но на всякий случай делаем такое условие
            while (phoneOperatorNumbers.size() != 0 && subscriberIds.size() != 0) {

                String phoneNumber = phoneOperatorNumbers.get(randomPhoneOperator.nextInt(phoneOperatorNumbers.size()));
                Long subscriberId = subscriberIds.get(randomSubscriber.nextInt(subscriberIds.size()));

                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO phone_book(number, abonent_id) VALUES(?, ?)");
                preparedStatement.setString(1, phoneNumber);
                preparedStatement.setLong(2, subscriberId);

                preparedStatement.execute();

                phoneOperatorNumbers.remove(phoneNumber);
                subscriberIds.remove(subscriberId);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Выводим на консоль свободные номера по каждому оператору
    public void getFreePhoneOperatorNumbersByOperator() {
        Map<Long, String> operators = this.getOperators(); // все операторы
        Map<String, Long> phoneOperatorNumbers = new HashMap<>(); // все свободные номера

        List<String> freeLifePhoneNumbers = new ArrayList<>();
        List<String> freeKyivStarPhoneNumbers = new ArrayList<>();
        List<String> freeVodafonePhoneNumbers = new ArrayList<>();

        Connection connection = this.getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM operator_number WHERE NOT EXISTS " +
                    "(SELECT phone_book.number FROM phone_book WHERE operator_number.number = phone_book.number)");
            while (resultSet.next()) {
//              String phoneSubscriberNumber = resultSet.getString("operator_number.number");
                String phoneSubscriberNumber = resultSet.getString("number");
                Long operatorId = resultSet.getLong("operator_id");

                phoneOperatorNumbers.put(phoneSubscriberNumber, operatorId);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Map.Entry<Long, String> operator : operators.entrySet()) {
            switch (operator.getValue().trim().toLowerCase()) {
                case "life": {

                    for (Map.Entry<String, Long> phone : phoneOperatorNumbers.entrySet()) {
                        if (phone.getValue() == operator.getKey())
                            freeLifePhoneNumbers.add(phone.getKey());
                    }
                    break;
                }
                case "kyivstar": {

                    for (Map.Entry<String, Long> phone : phoneOperatorNumbers.entrySet()) {
                        if (phone.getValue() == operator.getKey())
                            freeKyivStarPhoneNumbers.add(phone.getKey());
                    }
                    break;
                }
                case "vodafone": {

                    for (Map.Entry<String, Long> phone : phoneOperatorNumbers.entrySet()) {
                        if (phone.getValue() == operator.getKey())
                            freeVodafonePhoneNumbers.add(phone.getKey());
                    }
                    break;
                }
                default:
                    break;
            }
        }

        // Печать на консоль
        System.out.println("Сободные номера оператора Life");
        System.out.println(freeLifePhoneNumbers);
        System.out.println();

        System.out.println("Сободные номера оператора KyivStar");
        System.out.println(freeKyivStarPhoneNumbers);
        System.out.println();

        System.out.println("Сободные номера оператора Vodafone");
        System.out.println(freeVodafonePhoneNumbers);
        System.out.println();
    }

    // Берем всех операторов
    private HashMap<Long, String> getOperators() {
        HashMap<Long, String> operators = new HashMap<>();
        Connection connection = this.getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT operator_id, name FROM operator");
            while (resultSet.next()) {
                Long operatorId = resultSet.getLong("operator_id");
                String operatorName = resultSet.getString("name");

                operators.put(operatorId, operatorName);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return operators;
    }

    // Берем свободные номера из таблицы operator_number
    public ArrayList<String> getFreePhoneOperatorNumbers() {
        ArrayList<String> phoneOperatorNumbers = new ArrayList<>();
        Connection connection = this.getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT operator_number.number FROM operator_number WHERE NOT EXISTS " +
                    "(SELECT phone_book.number FROM phone_book WHERE operator_number.number = phone_book.number)");
            while (resultSet.next()) {
//              String phoneSubscriberNumber = resultSet.getString("operator_number.number");
                String phoneSubscriberNumber = resultSet.getString("number");

                phoneOperatorNumbers.add(phoneSubscriberNumber);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phoneOperatorNumbers;
    }

    // Выводим на экран Абонентов, не имеющих мобильного номера
    public void getSubscribersWithOutPhoneNumber() {
        ArrayList<String> subscribersWithOutPhoneNumber = new ArrayList<>();
        Connection connection = this.getConnection();

        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT abonent.first_name, abonent.last_name FROM abonent WHERE NOT EXISTS " +
                    "(SELECT phone_book.abonent_id FROM phone_book WHERE abonent.abonent_id = phone_book.abonent_id)");

            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                subscribersWithOutPhoneNumber.add(firstName + " " + lastName);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Абоненты без мобильного номера");
        System.out.println();
        System.out.println(subscribersWithOutPhoneNumber);
        System.out.println();
    }

    // Инициализация драйвера
    private Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // Получаем случайную строку из файла
    private String getRendomStringfromFile(String filePath) {
        String returnString = "";
        ArrayList<String> stringArray = new ArrayList<>();

        try (FileReader fileReader = new FileReader(filePath)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String row = "";

            // перебираем все строки
            while ((row = bufferedReader.readLine()) != null) {
                stringArray.add(row);
            }

            Random random = new Random();
            returnString = stringArray.get(random.nextInt(stringArray.size()));

        } catch (Exception e) {
//          e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return returnString;
    }
}



