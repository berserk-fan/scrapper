package qwerty.scrapper.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import qwerty.scrapper.dao.WordDao;

import javax.swing.*;

import static javax.swing.JOptionPane.*;

@Component
@Slf4j
public class Processor implements CommandLineRunner {

  private final WordPage wordPage;
  @Value("${page.read.size}")
  int pageSize;

  @Autowired
  public WordDao wordDao;
  @Autowired
  public Processor(WordPage wordPage) {
    this.wordPage = wordPage;
  }

  @Override
  public void run(String... args) throws Exception {
   /* do {
      long before = System.currentTimeMillis();
      wordPage.readNewWordPage();
      wordPage.receivePageInfo();
      wordPage.persistPageInfo();
      long after = System.currentTimeMillis();
      log.info(String.format("PageParsing took: %d ms", (after - before)));
    } while (!wordPage.tooMuchBadWords());*/

    wordDao.a();
    /*String msg = "You reached max bad words limit!";
    String title = "Congrats!";
    JOptionPane.showConfirmDialog(null, msg, title, DEFAULT_OPTION, INFORMATION_MESSAGE);
  */}
}
