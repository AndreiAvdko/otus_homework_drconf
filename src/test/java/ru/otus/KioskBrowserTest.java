package ru.otus;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import util.factory.WebDriverFactory;
import util.waiters.Waiters;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/*
    1. Открыть Chrome в __режиме киоска__
    2. Перейти на https://demo.w3layouts.com/demos_new/template_demo/03-10-2020/photoflash-liberty-demo_Free/685659620/web/index.html?_ga=2.181802926.889871791.1632394818-2083132868.1632394818
    3. Нажать на любую картинку
    4. Проверить что картинка открылась в модальном окне
*/

public class KioskBrowserTest {
    private WebDriver webDriver;
    private final String SITE_URL = "https://demo.w3layouts.com/demos_new/template_demo/03-10-2020" +
                                    "/photoflash-liberty-demo_Free/685659620/web" +
                                    "/index.html?_ga=2.181802926.889871791.1632394818-2083132868.1632394818";

    @BeforeAll
    public static void setup() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void initDriver() {
        webDriver = new WebDriverFactory().create("--kiosk");
    }

    @Test
    @DisplayName("Checking the opening of an image in a modal window")
    public void checkThatPictureWasOpenedInMidaleWindow() {
        webDriver.get(SITE_URL);
        List<WebElement> photosWebElements = webDriver.findElements(new By.ByCssSelector("span.image-block > a"));
        int randomPhotoNumber = new Random().nextInt(0, photosWebElements.size());

        assertThat(new Waiters(webDriver).
                waitForCondition(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.pp_pic_holder"))))
                .as("Check that modal window is not displayed")
                .isFalse();

        ((JavascriptExecutor) webDriver).executeScript(String.format("$('span.image-block > a')[%s].click()", randomPhotoNumber));

        WebElement pictureWindow = webDriver.findElement(By.cssSelector("div.pp_pic_holder"));
        assertThat(pictureWindow.isDisplayed())
                .as("Modal window is displayed")
                .isTrue();
    }

    @AfterEach
    public void quitDriverInstance() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
