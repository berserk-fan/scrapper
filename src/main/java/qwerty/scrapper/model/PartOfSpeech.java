package qwerty.scrapper.model;

import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum PartOfSpeech{
  NOUN("noun"),
  PRONOUN("pronoun"),
  VERB("verb"),
  ADJECTIVE("adjective"),
  ADVERB("adverb"),
  PREPOSITION("preposition"),
  CONJUNCTION("conjunction"),
  INTERJECTION("interjection"),
  PHRASE("phrase"),
  AUXILIARY_VERB("auxiliary verb"),
  ABBREVIATION("abbreviation"),
  PREFIX("prefix"),
  PARTICLE("particle"),
  SUFFIX("suffix");

  private String name;
  private static final Map<String, PartOfSpeech> ENUM_MAP;

  PartOfSpeech(String name) {
    this.name = name;
  }

  public String getName(){
    return name;
  }

  static{
    Map<String, PartOfSpeech> map = new ConcurrentHashMap<>();
    for(PartOfSpeech instance : PartOfSpeech.values()){
        map.put(instance.getName(), instance);
    }
    ENUM_MAP = Collections.unmodifiableMap(map);
  }

  public static @Nullable PartOfSpeech get(String name){
    return ENUM_MAP.get(name);
  }
}