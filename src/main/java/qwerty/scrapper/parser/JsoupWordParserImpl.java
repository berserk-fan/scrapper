package qwerty.scrapper.parser;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import qwerty.scrapper.model.BafEntry;
import qwerty.scrapper.model.DefInfo;
import qwerty.scrapper.model.PartOfSpeech;
import qwerty.scrapper.model.WordInfo;

import java.util.*;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toUnmodifiableList;

@Component
@Slf4j
public class JsoupWordParserImpl implements WordParser {
  private static final String defListsParent = ".gt-cd-mmd";
  private static final String defLists = ".gt-cd-c";
  private static final String defList = "gt-def-list";
  private static final String defPOS = "gt-cd-pos";
  private static final String defInfo = "gt-def-info";
  private static final String definition = ".gt-def-row";
  private static final String defExample = ".gt-def-example";
  private static final String synonyms = "gt-cd-cl";

  private static final String examplesParent = ".gt-cd-mex";
  private static final String examples = ".gt-cd-c";
  private static final String example = ".gt-ex-text";

  private static final String bafEntries = ".gt-baf-table";
  private static final String bafEntry = "gt-baf-entry";
  private static final String tranPOS = ".gt-cd-pos";
  private static final String translation = ".gt-baf-term-text";
  private static final String backTranslations = "gt-baf-back";
  private static final String frequencies = "filled";

  @Override
  public WordInfo getWord(String word, String html) {
    Objects.requireNonNull(word, "Word can't be null");
    Objects.requireNonNull(html, "Html can't be null");

    if (word.isBlank()) {
      throw new ParsingError("Word cant' be empty");
    }
    if (html.isBlank()) {
      throw new ParsingError("Html can't be empty");
    }
    final Document doc = Jsoup.parse(html);
    final List<DefInfo> defInfos = getDefInfos(doc);
    final List<String> examples = getExamples(doc);
    final List<BafEntry> bafEntries = getBafEntries(doc);

    return new WordInfo(word, defInfos, bafEntries, examples);
  }


  private List<String> getExamples(Document document) {
    return Optional.of(document)
            .map(doc -> doc.selectFirst(examplesParent))
            .map(element -> element.selectFirst(examples))
            .map(this::parseExamples)
            .orElse(null);
  }

  private List<String> parseExamples(Element mainExamples) {
    return mainExamples.children().stream()
            .map(exElem -> exElem.selectFirst(example))
            .filter(Objects::nonNull)
            .map(Element::text)
            .collect(toUnmodifiableList());
  }


  private List<BafEntry> getBafEntries(Document document) {
    return Optional.of(document)
            .map(doc -> doc.selectFirst(bafEntries))
            .map(element -> element.child(0))
            .map(this::parseBafEntries)
            .orElse(null);
  }


  private List<BafEntry> parseBafEntries(Element mainBafEntry) {
    PartOfSpeech partOfSpeech = null;
    List<BafEntry> bafEntries = new ArrayList<>();
    Elements children = mainBafEntry.children();
    for (Element child : children) {
      //get tranPOS
      Element posElement = child.selectFirst(tranPOS);
      if (posElement != null) {
        partOfSpeech = PartOfSpeech.get(posElement.text());
        if (partOfSpeech == null) {
          throw new ParsingError("Unknown part of speech: " + posElement.text());
        }
        //find translation
      } else if (child.hasClass(bafEntry)) {
        ensurePOSNonNull(partOfSpeech, child);
        bafEntries.add(parseBafEntry(child, partOfSpeech));
        //if smth wrong.
      } else {
        //в ангельском нет числительных как я понял. Они не подсвечиваются ничем. Поэтому пустой td будем считать UNKNOWN pos.
        log.debug("Bad structure. Element: {}. Parent element: {}.", child.html(), mainBafEntry.html());
        String message = String.format("Unknown element. TagName: %s, ClassNames: %s.", child.tagName(), child.className());
        throw new ParsingError(message);
      }
    }
    return Collections.unmodifiableList(bafEntries);
  }


  private List<DefInfo> getDefInfos(Document document) {
    return Optional.of(document)
            .map(doc -> doc.selectFirst(defListsParent))
            .map(elem -> elem.selectFirst(defLists))
            .map(this::parseDefLists)
            .orElse(null);
  }

  private List<DefInfo> parseDefLists(Element mainDefinitions) {

    List<DefInfo> defInfos = new ArrayList<>();

    Elements children = mainDefinitions.children();
    int i = 0;
    PartOfSpeech partOfSpeech = null;
    for (Element child : children) {
      if (i % 2 == 0) {
        ensureContainsClass(defPOS, child);
        String partOfSpeechName = child.text();
        partOfSpeech = PartOfSpeech.get(partOfSpeechName);
      } else {
        ensureContainsClass(defList, child);
        defInfos.addAll(parseDefList(child, partOfSpeech));
      }
      i++;
    }
    return Collections.unmodifiableList(defInfos);
  }

  private void ensurePOSNonNull(PartOfSpeech partOfSpeech, Element element) {
    if (partOfSpeech == null) {
      log.debug("No partOfSpeech was found before first translation. Element: {}. Parent element: {}", element.html(), element.parent().html());
      throw new ParsingError("No partOfSpeech was found before first translation.");
    }
  }

  private List<DefInfo> parseDefList(Element defList, PartOfSpeech pos) {
    return defList.getElementsByClass(defInfo).stream()
            .map(el -> parseDefInfo(el, pos))
            .collect(toUnmodifiableList());
  }

  private DefInfo parseDefInfo(Element defInfo, PartOfSpeech pos) {
    //get definition
    Element defElem = defInfo.selectFirst(definition);
    String definition = defElem != null ? defElem.text() : null;
    //get example
    Element exampleElem = defInfo.selectFirst(defExample);
    String example = exampleElem != null ? exampleElem.text() : null;
    //get synonyms
    List<String> synonyms = defInfo
            .getElementsByClass(JsoupWordParserImpl.synonyms).stream()
            .map(Element::text)
            .filter(not(String::isEmpty))
            .collect(toUnmodifiableList());

    return new DefInfo(definition, example, !synonyms.isEmpty() ? synonyms : null, pos);
  }

  private BafEntry parseBafEntry(Element badEntryEl, PartOfSpeech pos) {
    //get translation
    Element tranElem = badEntryEl.selectFirst(translation);
    String translation = tranElem != null ? tranElem.text() : null;
    //get backTranslations
    List<String> backTr = badEntryEl
            .getElementsByClass(backTranslations).stream()
            .map(Element::text)
            .filter(not(String::isEmpty))
            .collect(toUnmodifiableList());
    //get frequency
    int filledBlockCount = badEntryEl.getElementsByClass(frequencies).size();
    BafEntry.Frequency f = BafEntry.Frequency.get(filledBlockCount);

    return new BafEntry(translation, backTr, f, pos);
  }


  private void ensureContainsClass(String expectedClass, Element foundElement) {
    if (!foundElement.hasClass(expectedClass)) {
      log.debug("Element doesn't have class: {}, elementHtml: {}.", expectedClass, foundElement.html());
      throw new ParsingError(String.format("Class %s wan't found.", expectedClass));
    }
  }
}
