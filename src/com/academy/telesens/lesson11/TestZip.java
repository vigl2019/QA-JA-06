package com.academy.telesens.lesson11;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class TestZip {

    private static String subscribersDataPath; // путь к Excel файлу для записи данных абонентов
    private static String subscribersTxtPath; // пусть к текстовому файлу, в который нужно записать данные абонентов из Excel файла
    private static String subscribersZipPath; // путь к архиву subscribers.zip

    public TestZip(){}

    //==================================================//

    public static void testZip (){
        // Читаем пути к файлам
        Properties properties = new Properties();

        File file = new File("d:/myprojects/java-part/data/java-part.properties");
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                properties.load(fis);

                // Получаем путь к Excel файлу для записи данных абонентов
                subscribersDataPath = properties.getProperty("subscriber.exc");

                // Получаем путь к текстовому файлу, в который нужно записать данные абонентов из Excel файла
                subscribersTxtPath = properties.getProperty("subscriber.txt");

                // Получаем путь к архиву subscribers.zip
                subscribersZipPath = properties.getProperty("subscriber.arc");

            } catch (Exception e) {
//              e.printStackTrace();
                System.out.println(e.getMessage());
            }
        } else
            System.out.println("Файл не существует!");

        //==================================================//

        // Упаковать файлы 'subscribers.txt' и 'java-part.properties' в zip архив 'subscribers.zip'
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(subscribersZipPath))) {
            makeZip(subscribersTxtPath, zos);
            makeZip(subscribersDataPath, zos);
        } catch (Exception e) {
//      e.printStackTrace();
            System.out.println(e.getMessage());
        }

        //==================================================//

        // Проверяем
        // - наличие zip архива:  'subscriber.zip'
        //        - кол-во упакованных файлов равно 2
        //       - наличие в zip архиве файлов:
        //                                    'subscribers.xlsx'
        //                                    'subscribers.txt'

        Path path = Paths.get(subscribersZipPath);

//        if(!java.nio.file.Files.exists(path)){
          if(java.nio.file.Files.notExists(path))
              System.out.println("Файл архива не существует!");
          else{
              try(ZipFile zipFile = new ZipFile(path.toString())){

                  if(zipFile.size() != 2)
                      System.out.println("Количество упакованных файлов не равно 2");

                  ArrayList<String> filesName = new ArrayList<>();
                  Enumeration entries = zipFile.entries();

                  while(entries.hasMoreElements()){
                      ZipEntry zipEntry = (ZipEntry)entries.nextElement();
                      filesName.add(zipEntry.getName());
                  }

                  if(!filesName.contains("subscribers.txt"))
                      System.out.println("В архиве отсутствует файл: subscribers.txt");

                  if(!filesName.contains("subscribers.xlsx"))
                      System.out.println("В архиве отсутствует файл: subscribers.xlsx");
              }
              catch(Exception e){
//                e.printStackTrace();
                  System.out.println(e.getMessage());
              }
          }
    }

    //==================================================//

    private static void makeZip(String filePath, ZipOutputStream zos) {

        try (FileInputStream fis = new FileInputStream(filePath)) {
            Path path = Paths.get(filePath);
            ZipEntry entry = new ZipEntry(path.getFileName().toString());
            zos.putNextEntry(entry);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            zos.write(buffer);
            zos.closeEntry();
        } catch (Exception e) {
//         e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
