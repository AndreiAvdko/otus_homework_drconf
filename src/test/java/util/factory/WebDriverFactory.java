package util.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverFactory {
    private final String BROWSER_NAME = System.getProperty("browser.name", "chrome");
    public WebDriver create(String options) {
        switch(BROWSER_NAME) {
            case "chrome": {
                if (options != null) {
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments(options);
                    return new ChromeDriver(chromeOptions);
                } else return new ChromeDriver();
            }
            default: return null;
        }
    }
}

