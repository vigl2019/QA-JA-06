package com.academy.lesson15;

import com.academy.automationpractice.model.AuthData;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.fail;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class AuthTests {

    // Все локаторы положить в доступное место
    // пока перенести их в поля класса

    private String commonProperties = "./src/main/resources/common.properties";
    private String automationpracticeProperties = "./src/main/resources/automationpractice.properties";
    private WebDriver driver;
    private String baseUrl;
    private String errorMessageCssLocator = "#center_column > div.alert.alert-danger > ol > li";

    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {

        //      driver = new FirefoxDriver();
//      baseUrl = "https://www.katalon.com/";

        baseUrl = this.getProperty("base.url");
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
        driver.get(baseUrl);
        driver.findElement(By.linkText("Sign in")).click();

        // Читаем тестовые данные из Excel файла
        List<AuthData> authDataList = readTestData();

        for (AuthData authData : authDataList) {
            // Заполняем форму логин/пароль
            WebElement loginField = driver.findElement(By.id("email"));
            loginField.click();
            loginField.clear();
            loginField.sendKeys(authData.getEmail());

            WebElement passwordField = driver.findElement(By.id("passwd"));
            passwordField.click();
            passwordField.clear();
            passwordField.sendKeys(authData.getPassword());

            driver.findElement(By.id("SubmitLogin")).click();

            WebElement webElementWithErrorMessage = driver.findElement(By.cssSelector(errorMessageCssLocator));
            String actualErrorMessage = webElementWithErrorMessage.getText();
            try {
//              assertEquals(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Authentication'])[2]/following::li[1]")).getText(), "Authentication failed.");
                assertEquals(actualErrorMessage, authData.getErrorMessage());
            } catch (Error e) {
                verificationErrors.append(e.toString());
            }
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

    //==================================================//

    private List<AuthData> readTestData() {

        String filePath = this.getProperty("automation.auth.data.exc");
        List<AuthData> testDataList = new ArrayList<>();
        File file = new File(filePath);

        try (XSSFWorkbook workbook = new XSSFWorkbook(file)) {
            XSSFSheet sheet = workbook.getSheet("AuthData");

          for (int i = 1; i <= sheet.getLastRowNum(); i++) {
              XSSFRow row = sheet.getRow(i);

              String email = row.getCell(0).getStringCellValue();
              String password = row.getCell(1).getStringCellValue();
              String errorMessage = row.getCell(2).getStringCellValue();

              AuthData authData = new AuthData();
              authData.setEmail(email);
              authData.setPassword(password);
              authData.setErrorMessage(errorMessage);

              testDataList.add(authData);
          }
        } catch (IOException | InvalidFormatException e) {
//            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return testDataList;
    }

    //==================================================//

    private String getProperty(String property) {
        String returnProperty = "";
        Properties properties = new Properties();
        File file = new File(automationpracticeProperties);
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








