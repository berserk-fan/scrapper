package qwerty.scrapper.getting;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

@Component
public class SimpleDriver implements MyDriver {
  private WebDriver driver;

  public SimpleDriver() {
    System.setProperty("webdriver.chrome.driver", "/usr/lib/chromium-browser/chromedriver");
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");
    driver = new ChromeDriver(options);
  }

  public WebDriver getDriver() {
    return driver;
  }

  public void close() {
    driver.quit();
  }
}