package qwerty.scrapper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.util.List;

@Getter
@AllArgsConstructor
public class BafEntry {
  private String word;
  private List<String> synonyms;
  private Frequency frequency;
  private PartOfSpeech pos;

  public enum Frequency{
    COMMON,
    NOT_COMMON,
    RARE;

    /**
     * Returns COMMON if num == 3, NOT_COMMON if num == 2, RARE if num == 1
     * @param num number to instantiate enum
     * @return enum if num matches else null.
     */
    public static @Nullable Frequency get(int num) {
      if (num == 3) {
        return COMMON;
      }
      if (num == 2) {
        return NOT_COMMON;
      }
      if (num == 1) {
        return RARE;
      }
      return null;
    }
  }
}
