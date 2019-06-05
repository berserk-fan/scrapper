package qwerty.scrapper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Document("wordInfos")
public class WordInfo {
  @Id
  private String word;

  private List<DefInfo> definitions;
  private List<BafEntry> translations;
  private List<String> otherExamples;
}