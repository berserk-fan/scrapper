package qwerty.scrapper.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import qwerty.scrapper.dao.WordDao;
import qwerty.scrapper.dao.WordInfoDao;
import qwerty.scrapper.model.Word;
import qwerty.scrapper.model.WordInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
class WordPage {
  @Value("${page.read.size}")
  private int pageSize;
  @Value("${max.badWords}")
  private int maxBadWords;
  private List<Word> words = new ArrayList<>();
  private List<WordInfo> wordInfos = new ArrayList<>();
  private final WordDao wordDao;
  private final WordInfoDao wordInfoDao;
  private final WordInfoGetter wordInfoGetter;
  private final AtomicInteger badWordsCount = new AtomicInteger();

  @Autowired
  public WordPage(WordDao dao, WordInfoDao wordInfoDao, WordInfoGetter wordInfoGetter) {
    this.wordDao = dao;
    this.wordInfoDao = wordInfoDao;
    this.wordInfoGetter = wordInfoGetter;
  }

  void readNewWordPage() {
    words.clear();
    wordInfos.clear();
    words = wordDao.getNotProcessedWords(pageSize);
    log.debug("Got {} words. PageSize is {}", words.size(), pageSize);
  }

  void receivePageInfo() {
    words.forEach(this::receiveWordInfo);
  }

  private void receiveWordInfo(Word word) {
    WordInfo wordInfo = wordInfoGetter.getWordInfo(word);
    if (wordInfo == null) {
      handleBad(word);
    }else{
      wordInfos.add(wordInfo);
      log.debug("Word {} was successfully processed.", word);
    }
  }

  private void handleBad(Word word){
    badWordsCount.incrementAndGet();
    wordDao.updateErrorOnProcessing(word.getWord(), true);
    log.debug("Bad word: {}.Bad words count: {}.", word, badWordsCount);
  }

  void persistPageInfo() {
    Set<String> persisted = wordInfoDao.saveAll(wordInfos);
    words.stream()
            .map(Word::getWord)
            .filter(persisted::contains)
            .forEach(w -> wordDao.updateErrorOnProcessing(w, false));
  }

  boolean tooMuchBadWords() {
    return badWordsCount.get() > maxBadWords;
  }
}