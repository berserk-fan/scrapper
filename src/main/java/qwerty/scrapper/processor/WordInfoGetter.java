package qwerty.scrapper.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qwerty.scrapper.getting.HtmlGetter;
import qwerty.scrapper.model.Word;
import qwerty.scrapper.model.WordInfo;
import qwerty.scrapper.parser.ParsingError;
import qwerty.scrapper.parser.WordParser;

import javax.annotation.Nullable;

@Slf4j
@Component
public class WordInfoGetter {
  private final HtmlGetter htmlGetter;
  private final WordParser parser;

  @Autowired
  public WordInfoGetter(HtmlGetter htmlGetter, WordParser parser) {
    this.htmlGetter = htmlGetter;
    this.parser = parser;
  }

  @Nullable WordInfo getWordInfo(Word word) {
    String wordRaw = word.getWord();
    String html = htmlGetter.getHtml(wordRaw);
    String necessaryHtmlPart = getNecessaryHtmlPart(html);
    try {
      return parser.getWord(wordRaw, necessaryHtmlPart);
    } catch (ParsingError error) {
      log.info("Can't parse word: " + wordRaw, error);
      return null;
    }
  }

  private String getNecessaryHtmlPart(String html) {
    int startIndex = html.indexOf("<div class=\"gt-lc gt-lc-mobile\" style=\"\">");
    int endIndex = html.indexOf("<div class=\"feedback-link");
    if (startIndex == -1 || endIndex == -1) {
      log.debug("Necessary part was not found. Body part {}.", getBodyHtmlPart(html));
      return "";
    }
    return html.substring(startIndex, endIndex);
  }

  private String getBodyHtmlPart(String html) {
    int startIndex = html.indexOf("<body");
    if (startIndex == -1) {
      return "";
    }
    int endIndex = html.indexOf("</body>");
    if (endIndex == -1) {
      return "";
    }
    return html.substring(startIndex, endIndex);
  }
}
