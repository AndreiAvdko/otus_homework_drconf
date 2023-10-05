package ru.otus;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.Random;

public class HwTests {
    WebDriver webDriver;

    @BeforeAll
    public static void setup() {
        WebDriverManager.chromedriver().setup();
    }

    /*
        1. Открыть Chrome в headless режиме
        2. Перейти на https://duckduckgo.com/
        3. В поисковую строку ввести ОТУС
        4. Проверить что в поисковой выдаче первый результат Онлайн‑курсы для профессионалов, дистанционное обучение современным ...
    */

    // AAA-pattern
    @Test
    public void findOtusSiteInDuckDuckGoSearchResult() throws InterruptedException {
        String actualFirstSearchResult;
        String expectedFirstSearchResult = "Онлайн‑курсы для профессионалов, дистанционное обучение современным ...";
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        webDriver = new ChromeDriver(options);
        webDriver.get("https://duckduckgo.com/");
        WebElement searchBox  =  webDriver.findElement(new By.ByCssSelector("input.searchbox_input__bEGm3"));
        searchBox.sendKeys("ОТУС");
        WebElement searchButton = webDriver.findElement(new By.ByCssSelector("button[type=\"submit\"]"));
        searchButton.click();
        WebElement firstResultInSearch = webDriver.findElement(new By.ByCssSelector("h2"));
        actualFirstSearchResult = firstResultInSearch.getText();
        Assertions.assertEquals(expectedFirstSearchResult, actualFirstSearchResult);
    }


    /*
        1. Открыть Chrome в __режиме киоска__
        2. Перейти на https://demo.w3layouts.com/demos_new/template_demo/03-10-2020/photoflash-liberty-demo_Free/685659620/web/index.html?_ga=2.181802926.889871791.1632394818-2083132868.1632394818
        3. Нажать на любую картинку
        4. Проверить что картинка открылась в модальном окне
    */

    @Test
    public void checkThatPictureWasOpenedInMidaleWindow() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--kiosk");
        webDriver = new ChromeDriver(options);
        
        webDriver.get("https://demo.w3layouts.com/demos_new/template_demo/03-10-2020" +
                "/photoflash-liberty-demo_Free/685659620/web" +
                "/index.html?_ga=2.181802926.889871791.1632394818-2083132868.1632394818");

        List<WebElement> photosWebElements = webDriver.findElements(new By.ByCssSelector("span.image-block"));

        int randomPhotoNumber = new Random().nextInt(0, photosWebElements.size());

        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", photosWebElements.get(randomPhotoNumber));
        Thread.sleep(1000);
        photosWebElements.get(randomPhotoNumber).click();

        WebElement pictureWindow = webDriver.findElement(By.cssSelector("div.pp_pic_holder"));

        Assertions.assertTrue(pictureWindow.isDisplayed());
    }

    /*
        1. Открыть Chrome в режиме __полного экрана__
        2. Перейти на `https://otus.ru`
        3. Авторизоваться под каким-нибудь тестовым пользователем(можно создать нового)
        4. Вывести в лог все cookie
    */
    @Test
    public void checkAllCookiesWithAuthorizedUser() throws InterruptedException {
        webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
        webDriver.get("https://otus.ru");
        WebElement inputButton = webDriver.findElement(By.cssSelector("button.sc-mrx253-0"));
        inputButton.click();
        WebElement emailInput = webDriver.findElement(By.cssSelector("input[name='email']"));
        emailInput.sendKeys("yesakas564@gronasu.com");
        WebElement passwordInput = webDriver.findElement(By.cssSelector("input[type='password']"));
        passwordInput.sendKeys("1.yjdsqgfhjkM");
        WebElement authorizeButton = webDriver.findElement(By.xpath("//button[@type='button']//div[text()='Войти']"));
        authorizeButton.click();
        Logger log = (Logger) LogManager.getLogger(HwTests.class);
        log.info("COOKIES:" + webDriver.manage().getCookies());
    }

    @AfterEach
    public void quitDriverInstance() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}

