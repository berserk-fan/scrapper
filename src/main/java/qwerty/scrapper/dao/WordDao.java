package qwerty.scrapper.dao;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import qwerty.scrapper.model.Word;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardOpenOption.APPEND;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
@Slf4j
public class WordDao {
  private MongoTemplate mongoTemplate;

  @Autowired
  public WordDao(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public List<Word> getNotProcessedWords(int limit) {
    Query findUnprocessedQuery = query(where("errorOnProcessing").exists(false))
            .with(new Sort(Sort.Direction.DESC, "uses"));
    return mongoTemplate.find(findUnprocessedQuery.limit(limit), Word.class);
  }

  public boolean updateErrorOnProcessing(String word, boolean errorOnProcessing) {
    UpdateResult updateResult = mongoTemplate.updateFirst(query(
            where("word").is(word)),
            Update.update("errorOnProcessing", errorOnProcessing),
            Word.class);
    return updateResult.wasAcknowledged();
  }

  public boolean saveAll(List<Word> words) {
    BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Word.class);
    for (Word w : words) {
      bulkOperations.insert(w);
    }
    BulkWriteResult result = bulkOperations.execute();
    return result.wasAcknowledged();
  }

  /*For now just logs error and writes bad words to file. Maybe think of moves word from goodWordsCollection to BadWordsCollection;
   * @param word word object to be solved.
   */
  public void solveBad(Word word) {
    log.error("Word can't be read! Word " + word);
    Path badWords = Paths.get("classpath:badWords");
    try {
      Files.writeString(badWords, word.toString(), APPEND);
    } catch (IOException e) {
      log.error("Can not write to file", e);
    }
  }

  public void a(){
    mongoTemplate.updateMulti(query(where("processed").is(false)), new Update().unset("processed").unset("errorOnProcessing"), Word.class);
  }
}
