/*

5) 	Реализовать автотест фильтра для сайта automationpractice.com:
		- перейти на главную страницу по ссылке: http://automationpractice.com
		- в меню (WOMEN | DRESSES | T-SHIRTS) кликнуть пункт 'DRESSES'
		- установить фильтр диапазон цены с помощью ползунка
				Price->Range,
				например от 30.00 до 40.00

		- Выполнить проверку:
				1) все позиции имеют цену заданного диапазона (напр. 30.00 до 40.00)
		(точные значения границ цен не важны)

*/

package com.academy.lesson15;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class TestSlaider {

    private String commonProperties = "./src/main/resources/common.properties";
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
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
        driver.get("https://rozetka.com.ua/");
        driver.manage().window().maximize();

        WebElement search = driver.findElement(By.name("search"));
        search.click();
        search.clear();
        search.sendKeys("куртки мужские");

        WebElement searchButton = driver.findElement(By.className("search__button"));
        searchButton.click();

        // Левая кнопка на Слайдере
        WebElement leftSliderElement = driver.findElement(By.className("left-slider"));

/*
        // Получаем текущую позицию левой кнопки слайдера
//      Integer currentSliderLeftPosition = Integer.parseInt(leftSliderElement.getCssValue("left").replaceAll("px", ""));
        Point leftSliderElementPoint = leftSliderElement.getLocation();
        Integer currentSliderLeftPosition = leftSliderElementPoint.getX();
*/

        // Правая кнопка на Слайдере
        WebElement rightSliderElement = driver.findElement(By.className("right-slider"));

/*
        // Получаем текущую позицию правой кнопки слайдера
//      Integer currentSliderRightPosition = Integer.parseInt(rightSliderElement.getCssValue("right").replaceAll("px", ""));
        Point rightSliderElementPoint = rightSliderElement.getLocation();
        Integer currentSliderRightPosition = rightSliderElementPoint.getX();
*/

        // Получаем шаг изменения положения кнопки на Слайдере
        Integer step = this.getStep();

        // Минимальное значение цены
        WebElement priceMin = driver.findElement(By.id("price[min]"));
        Integer minPrice = Integer.parseInt(priceMin.getAttribute("value"));

        // Двигаем левую кнопку слайдера
        while (minPrice < 5500) {
            this.moveSliderElement(leftSliderElement, step);
            minPrice = Integer.parseInt(priceMin.getAttribute("value"));
        }

        // Максимальное значение цены
        WebElement priceMax = driver.findElement(By.id("price[max]"));
        Integer maxPrice = Integer.parseInt(priceMax.getAttribute("value"));

        // Двигаем правую кнопку слайдера
        while (maxPrice > 25500) {
            this.moveSliderElement(rightSliderElement, -step);
            maxPrice = Integer.parseInt(priceMax.getAttribute("value"));
        }

        // Нажимаем кнопку Ok, чтобы показать товары по выбранному диапазону цен
        WebElement submitPrice = driver.findElement(By.id("submitprice"));
        submitPrice.click();

        // Проверяем цены товаров на странице, чтобы они были в выбранном диапазоне
        List<WebElement> priceWebElements = driver.findElements(By.className("g-price-uah"));

        for (WebElement priceWebElement : priceWebElements) {

            String priceString = priceWebElement.getText();
            priceString = priceString.replace("грн", "");
            priceString = priceString.replaceAll("[\\s|\\u2009]+", "");
            // priceString = priceString.replaceAll("\\u2009", ""); // удаление узкого пробела \u2009, HTML &thinsp;
            // U+2009 Hex в Юникоде или десятичный код &#8201;
            Integer price = Integer.parseInt(priceString);

/*
            String priceString = priceWebElement.getText();
            priceString = priceString.replaceAll(" ", "").replace("грн", "");
            Integer price = Integer.parseInt(priceString);
*/

            if (price < 5500 || price > 25500) {
                throw new AssertionError("Цена не соответствует заданному диапазону цен!");
            }
        }

        // Выводим сообщение на экран
//      JavascriptExecutor js = (JavascriptExecutor) driver;
//      js.executeScript("alert('Цены соответствуют заданному диапазону цен!')");
    }

    //================================================================================//

    // Получаем шаг изменения положения кнопки на Слайдере
    public Integer getStep() {
        // Средняя часть Слайдера
        //*[@id="trackbarprice"]/table/tbody/tr/td[2]
        WebElement centralPartSliderElement = driver.findElement(By.cssSelector("#trackbarprice > table > tbody > tr > td.cb"));
        Integer sliderSize = centralPartSliderElement.getSize().getWidth();
        Integer step = sliderSize / 20; // шаг изменения положения кнопки на слайдере

        return step;
    }

    private void moveSliderElement(WebElement sliderElement, int step) {
        Actions actions = new Actions(driver);
/*
        actions.clickAndHold(sliderElement);
        actions.moveByOffset(step, 0);
        actions.perform();
*/

        actions.dragAndDropBy(sliderElement, step, 0);
        actions.perform();
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

