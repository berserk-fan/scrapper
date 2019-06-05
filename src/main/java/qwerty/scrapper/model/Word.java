package qwerty.scrapper.model;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@ToString
@Document("words")
public class Word{
  @Id
  private final String word;
  private final int uses;
  private final Boolean errorOnProcessing;

  public String getWord() {
    return word;
  }

  public Boolean getErrorOnProcessing() {
    return errorOnProcessing;
  }

  public Word withErrorOnProcessing(Boolean errorOnProcessing) {
    return new Word(word, uses, errorOnProcessing);
  }
}