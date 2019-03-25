/*

10)* Реализовать авто тест для сайта automationpractice.com:
	- перейти на главную страницу по ссылке: http://automationpractice.com
	- залогиниться
	- нажать "My addresses"
	- нажать "Update"
	- изменить значение полей:
		First name
		Last name
		Address
		City
		State
		Zip/Postal Code
		Country
		Home phone
	- нажать "Save >"

	Выполнить следующие проверки:
		1) на странице отобразились обновленные значения полей для этого адреса
		2) значения полей для других адресов остались неизмененными

*/

package com.academy.automationpractice;

import com.academy.automationpractice.model.UserAddress;
import com.academy.automationpractice.model.InitDataForTest;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.fail;

public class AuthMainTests {
    private String commonProperties = "./src/main/resources/common.properties";
    private String automationpracticeProperties = "./src/main/resources/automationpractice.properties";
    private WebDriver driver;
    private String baseUrl;
    private String errorMessageCssLocator = "#center_column > div.alert.alert-danger > ol > li";

    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {

        baseUrl = InitDataForTest.getProperty(automationpracticeProperties, "base.url");
        initDrivers();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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

        String login = InitDataForTest.getProperty(automationpracticeProperties, "login");
        String password = InitDataForTest.getProperty(automationpracticeProperties, "password");

        // Заполняем форму логин/пароль
        WebElement loginField = driver.findElement(By.id("email"));
        loginField.click();
        loginField.clear();
        loginField.sendKeys(login);

        WebElement passwordField = driver.findElement(By.id("passwd"));
        passwordField.click();
        passwordField.clear();
        passwordField.sendKeys(password);

        // Логинимся
        driver.findElement(By.id("SubmitLogin")).click();

        // Переходим в адреса
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='My credit slips'])[1]/following::span[1]")).click();

        // Получаем все данные по адресам до изменения какого-либо адреса
        LinkedHashMap<String, UserAddress> userAddressesBeroreChanges = this.getUserAddresses();

        // Меняем данные одного из адресов, который выбираем случайным образом
        Random random = new Random();
        int selectAddress = random.nextInt(userAddressesBeroreChanges.size());

        // Получаем название адреса
        String addressHeader = userAddressesBeroreChanges.keySet().toArray()[selectAddress].toString();
//      this.updateAddress(addressHeader);

        // Получаем ссылку на кнопку Update
        UserAddress userAddress = userAddressesBeroreChanges.get(addressHeader);

        // Меняем данные выбранного адреса
        this.updateAddress(userAddress.getUpdateUrl());

        //==================================================//

        // Проверяем сделанные изменения

        // Получаем все данные по адресам после изменения выбранного адреса
        LinkedHashMap<String, UserAddress> userAddressesAfterChanges = this.getUserAddresses();

        // Для выбранного адреса
//      UserAddress userAddressBeforeChanges = userAddressesBeforeChanges.get(addressHeader);
//      UserAddress userAddressAfterChanges = userAddressesAfterChanges.get(addressHeader);

        this.checkChanges(userAddressesBeroreChanges, userAddressesAfterChanges);
    }

    //================================================================================//

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    //================================================================================//

    // Получаем все данные по адресам пользователя
    private LinkedHashMap<String, UserAddress> getUserAddresses() {

        LinkedHashMap<String, UserAddress> addressesMap = new LinkedHashMap<>();
//      LinkedHashMap<String, List<String>> addressesMap = new LinkedHashMap<>();

//      List<WebElement> elements = driver.findElements(By.className("addresses"));
//      List<WebElement> elements = driver.findElements(By.className("col-xs-12 col-sm-6 address"));

//      List<WebElement> elements = driver.findElements(By.className("col-sm-6"));
        List<WebElement> addressesList = driver.findElements(By.className("box"));

        for (WebElement address : addressesList) {

            String addressHeader = address.findElement(By.className("page-subheading")).getText(); // название адреса

            WebElement addressUpdate = address.findElement(By.cssSelector("li > a"));
            String href = addressUpdate.getAttribute("href"); // кнопка-ссылка Update

            List<WebElement> innerElements = address.findElements(By.cssSelector("li > span"));

            List<String> addressList = new ArrayList<>();

            for (WebElement innerElement : innerElements) {
                addressList.add(innerElement.getText());
            }

            addressList.add(href);

            UserAddress userAddress = new UserAddress();
            userAddress.setAddressHeader(addressHeader);
            userAddress.setFirstName(addressList.get(0));
            userAddress.setLastName(addressList.get(1));
            userAddress.setCompany(addressList.get(2));
            userAddress.setAddress(addressList.get(3));
            userAddress.setAddressLine2(addressList.get(4));
            userAddress.setCity(addressList.get(5));
            userAddress.setState(addressList.get(6));
            userAddress.setZip(addressList.get(7));
            userAddress.setCountry(addressList.get(8));
            userAddress.setHomePhone(addressList.get(9));
            userAddress.setMobilePhone(addressList.get(10));
            userAddress.setUpdateUrl(href);

            addressesMap.put(addressHeader, userAddress);

//          List<WebElement> innerElements = element.findElements(By.tagName("li"));
/*
            for(WebElement innerElement : innerElements) {
                String innerElementText = innerElement.getText();
            }
*/
        }

        return addressesMap;
    }

    //================================================================================//

    // Меняем данные для выбранного адреса

    // В выпадающем списке в форме можно выбрать только одну страну United States
    // поэтому страну не меняем
    private void updateAddress(String href) {

        String xpathExpression = "//a[@href='" + href + "']";

        WebElement updateButton = driver.findElement(By.xpath(xpathExpression));
        updateButton.click();

        // Меняем данные для выбранного адреса

        WebElement firstName = driver.findElement(By.id("firstname"));
        firstName.click();
        firstName.clear();
        firstName.sendKeys("New FirstName");

        WebElement lastName = driver.findElement(By.id("lastname"));
        lastName.click();
        lastName.clear();
        lastName.sendKeys("New LastName");

        WebElement address1 = driver.findElement(By.id("address1"));
        address1.click();
        address1.clear();
        address1.sendKeys("New Address - 1");

        WebElement city = driver.findElement(By.id("city"));
        city.click();
        city.clear();
        city.sendKeys("New City");

        Select id_state = new Select(driver.findElement(By.id("id_state")));
        Random random = new Random();
        int index = random.nextInt(id_state.getOptions().size());
        id_state.selectByIndex(index);

        WebElement postcode = driver.findElement(By.id("postcode"));
        postcode.click();
        postcode.clear();
        postcode.sendKeys(this.getRandomNumber(5));

        WebElement phone = driver.findElement(By.id("phone"));
        phone.click();
        phone.clear();
        phone.sendKeys(this.getRandomNumber(10));

        // Обновляем данные
        WebElement saveButton = driver.findElement(By.id("submitAddress"));
        saveButton.click();
    }

    //================================================================================//

    // Проверяем сделанные изменения
    private void checkChanges(LinkedHashMap<String, UserAddress> userAddressesBeforeChanges, LinkedHashMap<String, UserAddress> userAddressesAfterChanges) {
        for (String key : userAddressesBeforeChanges.keySet()) {
            UserAddress userAddressBeforeChanges = userAddressesBeforeChanges.get(key);
            UserAddress userAddressAfterChanges = userAddressesAfterChanges.get(key);
            this.compareUserAddresses(userAddressBeforeChanges, userAddressAfterChanges);
        }
    }

    //================================================================================//

    // Проверяем адреса после изменения полей
    // First name, Last name, Address, City, State, Zip/Postal Code, Country, Home phone
    private void compareUserAddresses(UserAddress userAddressBeforeChanges, UserAddress userAddressAfterChanges) {
        String logMessage = "Compare fields" + "\n\n" +
               "'" + userAddressBeforeChanges.getAddressHeader() + "'" + " before changes " + "via " +
               "'" + userAddressAfterChanges.getAddressHeader() + "'" + " after changes " + "\n\n";

        if (!userAddressBeforeChanges.getFirstName().equals(userAddressAfterChanges.getFirstName()))
            logMessage += userAddressBeforeChanges.getFirstName() + " => " + userAddressAfterChanges.getFirstName() + "\n";
        if (!userAddressBeforeChanges.getLastName().equals(userAddressAfterChanges.getLastName()))
            logMessage += userAddressBeforeChanges.getLastName() + " => " + userAddressAfterChanges.getLastName() + "\n";
        if (!userAddressBeforeChanges.getCompany().equals(userAddressAfterChanges.getCompany()))
            logMessage += userAddressBeforeChanges.getCompany() + " => " + userAddressAfterChanges.getCompany() + "\n";
        if (!userAddressBeforeChanges.getAddress().equals(userAddressAfterChanges.getAddress()))
            logMessage += userAddressBeforeChanges.getAddress() + " => " + userAddressAfterChanges.getAddress() + "\n";
        if (!userAddressBeforeChanges.getAddressLine2().equals(userAddressAfterChanges.getAddressLine2()))
            logMessage += userAddressBeforeChanges.getAddressLine2() + " => " + userAddressAfterChanges.getAddressLine2() + "\n";
        if (!userAddressBeforeChanges.getCity().equals(userAddressAfterChanges.getCity()))
            logMessage += userAddressBeforeChanges.getCity() + " => " + userAddressAfterChanges.getCity() + "\n";
        if (!userAddressBeforeChanges.getState().equals(userAddressAfterChanges.getState()))
            logMessage += userAddressBeforeChanges.getState() + " => " + userAddressAfterChanges.getState() + "\n";
        if (!userAddressBeforeChanges.getZip().equals(userAddressAfterChanges.getZip()))
            logMessage += userAddressBeforeChanges.getZip() + " => " + userAddressAfterChanges.getZip() + "\n";
        if (!userAddressBeforeChanges.getCountry().equals(userAddressAfterChanges.getCountry()))
            logMessage += userAddressBeforeChanges.getCountry() + " => " + userAddressAfterChanges.getCountry() + "\n";
        if (!userAddressBeforeChanges.getHomePhone().equals(userAddressAfterChanges.getHomePhone()))
            logMessage += userAddressBeforeChanges.getHomePhone() + " => " + userAddressAfterChanges.getHomePhone() + "\n";
        if (!userAddressBeforeChanges.getMobilePhone().equals(userAddressAfterChanges.getMobilePhone()))
            logMessage += userAddressBeforeChanges.getMobilePhone() + " => " + userAddressAfterChanges.getMobilePhone() + "\n\n";

        logMessage += "//--------------------------------------------------------------------------------//" + "\n\n";

        // Пишем в файл
        String logFilePath = InitDataForTest.getProperty(automationpracticeProperties, "automation.auth.data.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true));
            writer.append(logMessage);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Выводим сообщение на экран
//         JavascriptExecutor js = (JavascriptExecutor) driver;
//         js.executeScript("alert('" + logMessage + "')");
    }

    //================================================================================//

    // Генерируем случайное число
    private String getRandomNumber(int countDigits) {
        String randomNumber = "1";
        Random random = new Random();

        for (int i = 0; i < (countDigits - 1); i++)
            randomNumber += random.nextInt(10);

        return randomNumber;
    }
}
