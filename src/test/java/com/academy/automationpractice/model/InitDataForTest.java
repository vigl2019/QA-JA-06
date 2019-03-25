package com.academy.automationpractice.model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class InitDataForTest {

    public static WebDriver initDrivers(String commonProperties) {

        Properties properties = new Properties();
        try {
            properties.load(new FileReader(commonProperties));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setProperty("webdriver.chrome.driver", properties.getProperty("chrome.driver"));
        System.setProperty("webdriver.gecko.driver", properties.getProperty("gecko.driver"));
        WebDriver driver = new ChromeDriver();
//      driver = new FirefoxDriver();

        return driver;
    }

    //==================================================//

    public static String getProperty(String propertiesFilePath, String property) {
        String returnProperty = "";
        Properties properties = new Properties();
        File file = new File(propertiesFilePath);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                properties.load(fis);

                // Получаем property
                returnProperty = properties.getProperty(property);
            } catch (Exception e) {
//              e.printStackTrace();
                System.out.println(e.getMessage());
            }
        } else
            System.out.println("Файл не существует!");

        return returnProperty;
    }
}
