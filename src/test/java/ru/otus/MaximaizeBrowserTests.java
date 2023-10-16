package ru.otus;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import util.factory.WebDriverFactory;
import util.waiters.Waiters;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/*
    1. Открыть Chrome в режиме __полного экрана__
    2. Перейти на `https://otus.ru`
    3. Авторизоваться под каким-нибудь тестовым пользователем(можно создать нового)
    4. Вывести в лог все cookie
*/
public class MaximaizeBrowserTests {
    private WebDriver webDriver;
    private final String BASE_URL = "https://otus.ru";

    @BeforeAll
    public static void setup() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void initDriver() {
        webDriver = new WebDriverFactory().create(null);
    }

    @Test
    @DisplayName("Check authorization and receiving cookies from site")
    public void checkAllCookiesWithAuthorizedUser() {
        webDriver.manage().window().maximize();
        webDriver.get(BASE_URL + "/");
        WebElement inputButton = webDriver.findElement(By.cssSelector("button.sc-mrx253-0"));
        inputButton.click();
        WebElement emailInput = webDriver.findElement(By.cssSelector("input[name='email']"));
        emailInput.sendKeys(System.getProperty("login"));
        WebElement passwordInput = webDriver.findElement(By.cssSelector("input[type='password']"));
        passwordInput.sendKeys(System.getProperty("password"));
        WebElement authorizeButton = webDriver.findElement(By.xpath("//button[@type='button']//div[text()='Войти']"));
        authorizeButton.click();
        assertThat(new Waiters(webDriver).waitForCondition(ExpectedConditions.presenceOfElementLocated(By.xpath("//div/span[text()='Андрей']"))))
                .as("Check that user was authorized")
                .isTrue();
        Logger log = (Logger) LogManager.getLogger(MaximaizeBrowserTests.class);
        log.info("COOKIES:" + webDriver.manage().getCookies());
    }

    @AfterEach
    public void quitDriverInstance() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
