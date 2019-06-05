package qwerty.scrapper.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoTypeMapper;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoConfig {

  @Bean
  @Autowired
  public MongoTemplate mongoTemplate( MongoDbFactory factory) throws Exception {
    //remove _class
    MappingMongoConverter converter =
            new MappingMongoConverter(factory, new MongoMappingContext());
    converter.setTypeMapper(new DefaultMongoTypeMapper(null));

    return new MongoTemplate(factory, converter);
  }
}
