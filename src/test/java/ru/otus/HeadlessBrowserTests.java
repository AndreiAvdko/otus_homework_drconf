package ru.otus;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import util.factory.WebDriverFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/*
    1. Открыть Chrome в headless режиме
    2. Перейти на https://duckduckgo.com/
    3. В поисковую строку ввести ОТУС
    4. Проверить что в поисковой выдаче первый результат Онлайн‑курсы для профессионалов, дистанционное обучение современным ...
*/

public class HeadlessBrowserTests {
    WebDriver webDriver;
    private final String BASE_URL = "https://duckduckgo.com";

    @BeforeAll
    public static void setup() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void initDriver() {
        webDriver = new WebDriverFactory().create("headless");
    }

    @Test
    @DisplayName("Checking the location of the OTUS-website in duckduckgo search results with the query 'OTUS'")
    public void findOtusSiteInDuckDuckGoSearchResult() {
        String actualFirstSearchResult;
        String expectedFirstSearchResult = "Онлайн‑курсы для профессионалов, дистанционное обучение современным ...";
        webDriver.get(BASE_URL + "/");
        WebElement searchBox  =  webDriver.findElement(new By.ByCssSelector("input.searchbox_input__bEGm3"));
        searchBox.sendKeys("ОТУС");
        WebElement searchButton = webDriver.findElement(new By.ByCssSelector("button[type=\"submit\"]"));
        searchButton.click();
        WebElement firstResultInSearch = webDriver.findElement(new By.ByCssSelector("h2"));
        actualFirstSearchResult = firstResultInSearch.getText();
        assertThat(actualFirstSearchResult)
                .as("Otus is first in search")
                .isEqualTo(expectedFirstSearchResult);
    }

    @AfterEach
    public void quitDriverInstance() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
