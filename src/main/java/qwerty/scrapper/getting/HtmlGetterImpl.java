package qwerty.scrapper.getting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class HtmlGetterImpl implements HtmlGetter{
  private static final String GOOGLE_TRANSLATE_ULR = "https://translate.google.com/#view=home&op=translate&sl=en&tl=ru&text=";
  private MyDriver driver;

  @Autowired
  public HtmlGetterImpl(MyDriver driver) {
    this.driver = driver;
  }

  public String getHtml(String word) {
    return getHtmlPage(word);
  }

  private String getHtmlPage(String word) {
    driver.getDriver().get(GOOGLE_TRANSLATE_ULR + word);
    try {
      Thread.sleep(100L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return driver.getDriver().getPageSource();
  }

  @PreDestroy
  public void close(){
    driver.close();
  }
}