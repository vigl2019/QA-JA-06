/*

2) Используя Katalon Recorder протестировать не корректный ввод логина или пароля для сайта http://automationpractice.com
	a) Сценарий:
		- перейти по ссылке http://automationpractice.com/index.php
		- cделать клик по ссылке вверху 'Sign in'
		- заполнить некорректными значениями поля 'Email address' и 'Password'
		- нажать зеленую кнопку внизу 'Sign in'
		- убедиться, что на красном фоне отображается сообщение об ошибке

		(варианты значений логина, пароля и сообщения об ошибке взять из подготовленного
		на предыдущих занятиях excel файла)

	b) Экспортировать тест в код Java+TesnNg

	c) Запустить тест под управлением Maven

3) В проекте создать файл в директории src/main/resources/automationpractice.properties, в котором прописать следующие свойства:

	# путь к файлу 'automationpractice-auth-data.xlsx'
	automation.auth.data.exc=<корень_проекта>/data/automationpractice-auth-data.xlsx
	# путь к файлу 'auth-data.txt'
	automation.auth.data.txt=<корень_проекта>/data/automationpractice-auth-data.txt


4) Для сайта http://automationpractice.com, заполнить вручную файл automationpractice-auth-data.xlsx для проверки корректности логина и пароля:
	- файл должен находиться в директории data (<корень_проекта>/data/automationpractice-auth-data.xlsx)

	- необходимо проверить отрицательные кейсы, кот. включают варианты:
		не верный логин и пароль
		не верный только логин
		не верный только пароль
		оба поля пустые
		одно из полей пустых (2 кейса)
		неправильный логин

	- исходные данные для кейсов должные храниться в таблице excel и представлены 3-мя столбцами:

        ЛОГИН | ПАРОЛЬ  | СООБЩЕНИЕ_ОБ_ОШИБКЕ
        ------+---------+-----------------------
        user  |  passw  | Invalid email address.

	(сообщения об ошибки взять со страницы http://automationpractice.com/index.php?controller=authentication)

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

public class AuthDemo {

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
    public void testUntitledTestCase() throws Exception {
        driver.get("http://automationpractice.com/index.php");
        driver.findElement(By.linkText("Sign in")).click();
        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("some-password");
        driver.findElement(By.id("passwd")).click();
        driver.findElement(By.id("passwd")).clear();
        driver.findElement(By.id("passwd")).sendKeys("some-password");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Forgot your password?'])[1]/following::span[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Authentication'])[2]/following::li[1]")).click();
        try {
            assertEquals(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Authentication'])[2]/following::li[1]")).getText(), "Invalid email address.");
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
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




