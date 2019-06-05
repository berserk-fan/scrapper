package qwerty.scrapper.parser;

import qwerty.scrapper.model.WordInfo;

public interface WordParser{
  WordInfo getWord(String word, String htmlBody);
}
