package qwerty.scrapper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DefInfo {
  String defRow;
  String defExample;
  List<String> synonyms;
  PartOfSpeech pos;
}
