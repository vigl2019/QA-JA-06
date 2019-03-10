/*

# Задачи на работу с файлами:
	- текстовыми
	- properties
	- excel
	- zip

===========================================================================
	Библиотека для работы с excel: https://poi.apache.org/
===========================================================================

1)  Создать файл 'java-part.properties' следующие свойства:

		subscriber.exc=d:/myprojects/java-part/data/subscriber.xlsx
		subscriber.txt=d:/myprojects/java-part/data/subscriber.txt
		subscriber.sort.txt=d:/myprojects/java-part/data/subscriber-sort.txt
		subscriber.arc=d:/myprojects/java-part/data/subscriber.zip

		male.firstnames=d:/myprojects/java-part/data/мужские имена.txt
		male.lastnames=d:/myprojects/java-part/data/мужские фамилии.txt
		female.firstnames=d:/myprojects/java-part/data/женские имена.txt
		female.lastnames=d:/myprojects/java-part/data/женские фамилии.txt

		# Диапазон возрастов
		age.from=5
		age.to=90

		# Использовать ли Гауссово распределение (true/false)
		age.gaussian=true

2) Написать Java приложение, которое наполнит файл subscribers.xlsx, случайными данными:
	(путь к файлу взять из 'java-part.properties')
	a) Наполнить таблицу абонентов excel(2000 строк):
		- имена фамилии взять в соответстсвующих файлах (см. 'java-part.properties') со списком имен/фамилий (женских/мужских);
		- возраст генерировать случайно от 5 до 90 (можно использовать Гауссово распределение для этого диапазона)
			(диапазон брать из файла 'java-part.properties')

	b)  Телефонные номера для каждого оператора со следующими префиксами:
		- Life номера с префиксами: 38063*******, 38093*******, 38073*******
		- Kievstar номера с префиксами: 38097*******, 38067*******, 38098*******
		- Vodafone номера с префиксами: 38050*******, 38066*******, 38095*******

	Результат subscribers.xlsx должен выглядеть так:
		   1 | Васильев  | Иван | м | 23 | 380630025465 | Life
		   2 | Петрова   | Катя | ж | 34 | 380670058694 | Kievstar
		...
		2000 | Борисов   | Коля | м | 48 | 380500025465 | Vodafone
		Всего 200 случайных строк

	с) Реализовать класс Subscriber с приватными полями:
		- private Long id;
		- private String firstName;
		- private String lastName;
		- private Gender gender; // создать перечисление Gender для мужского и женского пола
			- 'MALE'
			- 'FEMALE'
		- private int age;
		- private String phoneNumber;
		- private Operator operator;
				private Long id;
				private String name;

	d) Прочитать subscribers.xlsx в коллекцию Map<Long, Subscriber> и сохранить в текстовый файл: subscribers.txt
		(путь к файлу взять из 'java-part.properties')

	e) Прочитать subscribers.xlsx в коллекцию List<Subscriber> отсортировать сразу по 4 полям:
		- оператору
		- по возрасту
		- по фамилии
		- по имени

	f) Сохранить результат сортировки в файл 'sort-subscribers.txt'
		(путь к файлу взять из 'java-part.properties')


	g) Прочитать 1-ый десять строк файла sort-subscribers.txt и вывести на экран
		(путь к файлу взять из 'java-part.properties')

3)  Упаковать файлы 'subscribers.txt' и 'java-part.properties' в zip архив 'subscriber.zip'
		(путь к исходным файлам и zip архиву брать из файла 'java-part.properties')

4)*  Написать тест, который:
		- запускает 3 задание

	После этого проверяет:
		- наличие zip архива:  'subscriber.zip'
		- кол-во упакованных файлов равно 2
		- наличие в zip архиве файлов:
			'subscribers.xlsx'
			'subscribers.txt'
*/

package com.academy.telesens.lesson11;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SubscriberDemo {

    private static String javaPartPropertiesPath; // путь к файлу настроек

    private static String maleFirstNamesPath; // путь к файлу мужских имен
    private static String maleLastNamesPath; // путь к файлу мужских фамилий
    private static String femaleFirstNamesPath; // путь к файлу женских имен
    private static String femaleLastNamesPath; // путь к файлу женских фамилий

    private static int ageFrom; // начальный возраст абонента
    private static int ageTo; // конечный возраст абонента

    private static String subscribersDataPath; // путь к Excel файлу для записи данных абонентов
    private static int subscribersCount; // количество абонентов

    private static String subscribersTxtPath; // пусть к текстовому файлу, в который нужно записать данные абонентов из Excel файла

    private static String subscribersSortTxtPath; // путь к текстовому файлу, в который нужно записать результат соритровки коллекции List<Subscriber>

    private static String subscribersZipPath; // путь к архиву subscribers.zip

    public static void main(String[] args) {

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

                // Получаем путь к Excel файлу для записи данных абонентов
                subscribersDataPath = properties.getProperty("subscriber.exc");

                // Получаем количество Абонентов
                subscribersCount = Integer.parseInt(properties.getProperty("subscribers.count"));

                // Получаем путь к текстовому файлу, в который нужно записать данные абонентов из Excel файла
                subscribersTxtPath = properties.getProperty("subscriber.txt");

                // Получаем путь к текстовому файлу, в который нужно записать результат соритровки коллекции List<Subscriber>
                subscribersSortTxtPath = properties.getProperty("subscriber.sort.txt");

                // Получаем путь к архиву subscribers.zip
                subscribersZipPath = properties.getProperty("subscriber.arc");

            } catch (Exception e) {
//              e.printStackTrace();
                System.out.println(e.getMessage());
            }
        } else
            System.out.println("Файл не существует!");

/*
        // Получаем путь к файлу мужских имен
        maleFirstNamesPath = getFilePath("java-part.properties", "male.firstnames");
        // Получаем путь к файлу мужских фамилий
        maleLastNamesPath = getFilePath("java-part.properties", "male.lastnames");

        // Получаем путь к файлу женских имен
        femaleFirstNamesPath = getFilePath("java-part.properties", "female.firstnames");
        // Получаем путь к файлу женских фамилий
        femaleLastNamesPath = getFilePath("java-part.properties", "female.lastnames");

        // Получаем путь к Excel файлу для записи данных абонентов
        subscribersDataPath = getFilePath("java-part.properties", "subscriber.exc");
*/
        //==================================================//

        List<Subscriber> subscriberList = new ArrayList<>();

        // Генерируем данные для Абонента
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

            subscriber.setPhoneNumber(Operator.getOperatorPhoneNumber(operator.getName()));
            subscriber.setOperator(operator);

            subscriberList.add(subscriber);
        }

        //==================================================//

        // Добавляем данные Абонента в файл Excel
        putAbonentDataToExcelFile(subscriberList);

        //==================================================//

        // Пишем данные Абонентов из Excel файла в TXT файл

        // Читаем данные Абонентов из файла Excel в коллекцию Map<Long, Subscriber>
        Map<Long, Subscriber> subscribersMap = getAbonentDataFromExcelFileToMap(subscribersDataPath);

        // Пишем данные Абонентов из коллекции Map<Long, Subscriber> в текстовый файл
        try (PrintWriter pwMap = new PrintWriter(subscribersTxtPath)) {
            pwMap.println(subscribersMap);
        } catch (Exception e) {
//          e.printStackTrace();
            System.out.println(e.getMessage());
        }

        //==================================================//

        // Читаем данные Абонентов из файла Excel в коллекцию List<Subscriber>
        List<Subscriber> subscribersList = getAbonentDataFromExcelFileToList(subscribersDataPath);

        // Сортируем сразу по 4 полям:
        //                - оператору
        //                - по возрасту
        //                - по фамилии
        //                - по имени

        // В данном случае одновременная сортировка возможна только по оператору и возрасту
        Comparator<Subscriber> subscriberComparator = new OperatorComparator().thenComparing(new AgeComparator()).thenComparing(new LastNameComparator()).thenComparing(new FirstNameComparator());
        subscribersList.sort(subscriberComparator);

        // Сохраняем результат сортировки в текстовый файл
        try (PrintWriter pwList = new PrintWriter(subscribersSortTxtPath)) {
            pwList.println(subscribersList);
        } catch (Exception e) {
//          e.printStackTrace();
            System.out.println(e.getMessage());
        }

        // Читаем первые десять строк файла subscriber-sort.txt и выводим их на экран
        try (FileReader fileReader = new FileReader(subscribersSortTxtPath);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            for (int i = 0; i < 10; i++)
                System.out.println(bufferedReader.readLine());
        } catch (Exception e) {
//          e.printStackTrace();
            System.out.println(e.getMessage());
        }

        // Упаковать файлы 'subscribers.txt' и 'subscribers.xlsx' в zip архив 'subscribers.zip'
        try(ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(subscribersZipPath))){
            makeZip(subscribersTxtPath, zos);
            makeZip(subscribersDataPath, zos);
        }
        catch(Exception e){
//      e.printStackTrace();
            System.out.println(e.getMessage());
        }

        // Проверяем
        // - наличие zip архива:  'subscriber.zip'
        //        - кол-во упакованных файлов равно 2
        //       - наличие в zip архиве файлов:
        //                                    'subscribers.xlsx'
        //                                    'subscribers.txt'
        TestZip.testZip();
    }

    //==================================================//
/*
    // Получаем путь к файлу
    private static String getFilePath(String fileProperties, String targetProperty) {
        String filePath = "";

        try (FileReader fileReader = new FileReader(fileProperties)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String row = "";

            // перебираем все строки
            while ((row = bufferedReader.readLine()) != null) {
                if (row.isEmpty())
                    System.out.println("----- empty row -----");
                else {
                    if (row.matches(targetProperty)) {
                        String[] rowParts = row.split("=");
                        filePath = rowParts[1];
                    }
                }
            }
        } catch (Exception e) {
//          e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return filePath;
    }
*/

    //==================================================//

    // Получаем случайную строку из файла
    private static String getRendomStringfromFile(String filePath) {
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

    //==================================================//

    // Добавляем данные Абонентов в файл Excel
    private static void putAbonentDataToExcelFile(List<Subscriber> subscriberList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Subscribers Data");
//      Object[][] data = {};

        for (int i = 0; i < subscriberList.size(); i++) {

            Subscriber subscriber = subscriberList.get(i);

            XSSFRow row = sheet.createRow(i);

            // Заполняем строку данными Абонента
            XSSFCell cellId = row.createCell(0);
            cellId.setCellValue(subscriber.getId());

            XSSFCell cellLastName = row.createCell(1);
            cellLastName.setCellValue(subscriber.getLastName());

            XSSFCell cellFirstName = row.createCell(2);
            cellFirstName.setCellValue(subscriber.getFirstName());

            XSSFCell cellGender = row.createCell(3);
            cellGender.setCellValue(subscriber.getGender().toString());

            XSSFCell cellAge = row.createCell(4);
            cellAge.setCellValue(subscriber.getAge());

            XSSFCell cellPhoneNumber = row.createCell(5);
            cellPhoneNumber.setCellValue(subscriber.getPhoneNumber());

            XSSFCell cellOperatorName = row.createCell(6);
            cellOperatorName.setCellValue(subscriber.getOperator().getName());
/*
            XSSFCell cellOperatorId = row.createCell(7);
            cellOperatorId.setCellValue(subscriber.getOperator().getId());
*/
        }

        try (FileOutputStream out = new FileOutputStream(new File(subscribersDataPath))) {
            workbook.write(out);
        } catch (Exception e) {
//          e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    //==================================================//

    // Читаем данные Абонентов из файла Excel в коллекцию Map<Long, Subscriber>
    private static Map<Long, Subscriber> getAbonentDataFromExcelFileToMap(String filePath) {
        Long subscriberId = 1L;
        Map<Long, Subscriber> subscribers = new HashedMap<>();
        File file = new File(filePath);

        try (XSSFWorkbook workbook = new XSSFWorkbook(file)) {
            XSSFSheet sheet = workbook.getSheet("Subscribers Data");

            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);

                Long id = (long) row.getCell(0).getNumericCellValue();
                String lastName = row.getCell(1).getStringCellValue();
                String firstName = row.getCell(2).getStringCellValue();
                String gender = row.getCell(3).getStringCellValue();
                int age = (int) row.getCell(4).getNumericCellValue();
                String phoneNumber = row.getCell(5).getStringCellValue();
                String operatorName = row.getCell(6).getStringCellValue();

                Subscriber subscriber = new Subscriber();
                subscriber.setId(id);
                subscriber.setFirstName(firstName);
                subscriber.setLastName(lastName);

                if (gender.trim().toLowerCase().equals("м"))
                    subscriber.setGender(Gender.MALE);
                else
                    subscriber.setGender(Gender.FEMALE);

                subscriber.setAge(age);
                subscriber.setPhoneNumber(phoneNumber);

                Operator operator = new Operator();
                operator.setName(operatorName);
                operator.setId(Operator.getOperatorId(operator.getName()));
                subscriber.setOperator(operator);

                subscribers.put(subscriberId++, subscriber);
            }
        } catch (IOException | InvalidFormatException e) {
//            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return subscribers;
    }

    //==================================================//

    // Читаем данные Абонентов из файла Excel в коллекцию List<Subscriber>
    private static List<Subscriber> getAbonentDataFromExcelFileToList(String filePath) {
        List<Subscriber> subscribers = new ArrayList<>();
        File file = new File(filePath);

        try (XSSFWorkbook workbook = new XSSFWorkbook(file)) {
            XSSFSheet sheet = workbook.getSheet("Subscribers Data");

            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);

                Long id = (long) row.getCell(0).getNumericCellValue();
                String lastName = row.getCell(1).getStringCellValue();
                String firstName = row.getCell(2).getStringCellValue();
                String gender = row.getCell(3).getStringCellValue();
                int age = (int) row.getCell(4).getNumericCellValue();
                String phoneNumber = row.getCell(5).getStringCellValue();
                String operatorName = row.getCell(6).getStringCellValue();

                Subscriber subscriber = new Subscriber();
                subscriber.setId(id);
                subscriber.setFirstName(firstName);
                subscriber.setLastName(lastName);

                if (gender.trim().toLowerCase().equals("м"))
                    subscriber.setGender(Gender.MALE);
                else
                    subscriber.setGender(Gender.FEMALE);

                subscriber.setAge(age);
                subscriber.setPhoneNumber(phoneNumber);

                Operator operator = new Operator();
                operator.setName(operatorName);
                operator.setId(Operator.getOperatorId(operator.getName()));
                subscriber.setOperator(operator);

                subscribers.add(subscriber);
            }
        } catch (IOException | InvalidFormatException e) {
//            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return subscribers;
    }

    //==================================================//

    private static void makeZip(String filePath, ZipOutputStream  zos){

       try(FileInputStream fis = new FileInputStream(filePath)) {
           Path path = Paths.get(filePath);
           ZipEntry entry = new ZipEntry(path.getFileName().toString());
           zos.putNextEntry(entry);
           byte[] buffer = new byte[fis.available()];
           fis.read(buffer);
           zos.write(buffer);
           zos.closeEntry();
       }
       catch (Exception e){
//         e.printStackTrace();
           System.out.println(e.getMessage());
       }
    }
}


