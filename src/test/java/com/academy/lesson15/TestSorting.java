/*

7) Реализовать автотест сортировки 'DRESSES' для сайта automationpractice.com:
		- перейти на главную страницу по ссылке: http://automationpractice.com
		- в меню (WOMEN | DRESSES | T-SHIRTS) кликнуть пункт 'DRESSES'
		- отсортировать по Price: Lowest first
		- Сделать следующие проверки:
			1) присутствуют пять позиций
			2) Цены идут по возрастанию: [$16.40, $26.00, $28.98, $30.50, $50.99] (точное значение цен не важно, важно - упорядоченность)

		(см. скриншоты в директории '07_sort_dresses')

*/

package com.academy.lesson15;

import java.util.List;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class TestSorting {

    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testUntitledTestCase() throws Exception {
        driver.get("https://rozetka.com.ua/");
        driver.manage().window().maximize();

        WebElement search = driver.findElement(By.name("search"));
        search.click();
        search.clear();
        search.sendKeys("куртки мужские");

        WebElement searchButton = driver.findElement(By.className("search__button"));
        searchButton.click();

        // Ставим диапазон цен от 1000 грн. до 5000 грн.
        WebElement priceMin = driver.findElement(By.id("price[min]"));
        priceMin.click();
//      priceMin.sendKeys(Keys.BACK_SPACE);
        priceMin.sendKeys(Keys.CONTROL, "a");
        priceMin.sendKeys(Keys.DELETE);
        priceMin.sendKeys("1000");

        WebElement priceMax = driver.findElement(By.id("price[max]"));
        priceMax.click();
        priceMax.sendKeys(Keys.CONTROL, "a");
        priceMax.sendKeys(Keys.DELETE);
        priceMax.sendKeys("5000");

        // Нажимаем кнопку Ok, чтобы показать товары по выбранному диапазону цен
        WebElement submitPrice = driver.findElement(By.id("submitprice"));
        submitPrice.click();

        // Сортируем товары по цене от дешевых к дорогим
        WebElement sortWebElement = driver.findElement(By.name("drop_link"));
        sortWebElement.click();
        sortWebElement = driver.findElement(By.cssSelector(".sort-view-l-i-link.novisited.sprite-side"));
        sortWebElement.click();

        // Проверяем сортировку
        List<WebElement> jackets = driver.findElements(By.className("g-price-uah"));

        if (jackets.size() < 32)
            throw new AssertionError(
                    "Количество товара на странице меньше 32");

        if (jackets.size() > 32)
            throw new AssertionError(
                    "Количество товара на странице больше 32");

        // Проверяем, что цены идут по возрастанию
        for (int i = 0; i < (jackets.size() - 1); i++) {

            WebElement priceWebElement1 = jackets.get(i);
            WebElement priceWebElement2 = jackets.get(i + 1);

            String priceString1 = priceWebElement1.getText().replaceAll(" ", "").replace("грн", "");
            Integer price1 = Integer.parseInt(priceString1);

            String priceString2 = priceWebElement2.getText().replaceAll(" ", "").replace("грн", "");
            Integer price2 = Integer.parseInt(priceString2);

            if (price2 < price1)
                throw new AssertionError(
                        "Цены на товар на странице идут не по возрастанию!");
        }

        // Выводим сообщение на экран
//     JavascriptExecutor js = (JavascriptExecutor) driver;
//     js.executeScript("alert('Товары отсортированы по возрастанию цены!')");
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



