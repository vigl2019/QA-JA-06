/*

1) Используя Katalon Recorder протестировать корректный ввод логина и пароля для сайта http://automationpractice.com
	a) Сценарий:
		- перейти по ссылке http://automationpractice.com/index.php
		- cделать клик по ссылке вверху 'Sign in'
		- заполнить поля 'Email address' и 'Password'
		- нажать зеленую кнопку внизу 'Sign in'
		- убедиться, что в верхнем меню отображается гиперссылка с именем пользователя:  Contact us | Sign out | 'Username'
		- сделать выход, нажав в верхнем меню гиперссылку Sign out

	b) Экспортировать тест в код Java+TesnNg

	c) Запустить тест под управлением Maven

*/

package com.academy.lesson14;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class LogIn {
    private String commonProperties = "./src/main/resources/common.properties";
    private WebDriver driver;
    private String baseUrl = "http://automationpractice.com/index.php";

    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
//      driver = new FirefoxDriver();
//      baseUrl = "https://www.katalon.com/";

        initDrivers();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    private void initDrivers() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(commonProperties));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setProperty("webdriver.chrome.driver", properties.getProperty("chrome.driver"));
        System.setProperty("webdriver.gecko.driver", properties.getProperty("gecko.driver"));
        driver = new ChromeDriver();
//      driver = new FirefoxDriver();
    }

    @Test
    public void testLogIn() throws Exception {
//      driver.get("http://automationpractice.com/index.php");
        driver.get(baseUrl);
        driver.findElement(By.linkText("Sign in")).click();
        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("info.biz.box1@gmail.com");
        driver.findElement(By.id("passwd")).click();
        driver.findElement(By.id("passwd")).clear();
        driver.findElement(By.id("passwd")).sendKeys("p*Te$5@+m%Wi");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Forgot your password?'])[1]/following::span[1]")).click();
        try {
            assertEquals(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Sign out'])[1]/preceding::span[1]")).getText(), "Info Biz");
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        driver.findElement(By.linkText("Sign out")).click();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}



