package qwerty.scrapper.dao;

import com.mongodb.bulk.BulkWriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import qwerty.scrapper.model.WordInfo;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.springframework.data.mongodb.core.BulkOperations.BulkMode.UNORDERED;
import static org.springframework.data.mongodb.core.query.Criteria.where;


@Component
public class WordInfoDao {

  private final MongoTemplate mongoTemplate;

  @Autowired
  public WordInfoDao(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public Set<String> saveAll(List<WordInfo> wordInfos) {
    //delete if already existed
    Query removeAlreadyExistedQuery = Query.query(where("word").in(wordInfos.stream().map(WordInfo::getWord).toArray()));
    mongoTemplate.findAllAndRemove(removeAlreadyExistedQuery, WordInfo.class);

    //insert documents
    return mongoTemplate.insertAll(wordInfos).stream().map(WordInfo::getWord).collect(toSet());
  }
}
