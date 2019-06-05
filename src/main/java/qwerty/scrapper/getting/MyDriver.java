package qwerty.scrapper.getting;

import org.openqa.selenium.WebDriver;

public interface MyDriver {
  WebDriver getDriver();
  void close();
}