package qwerty.scrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.mongodb.core.BulkOperations;
import qwerty.scrapper.dao.WordDao;
import qwerty.scrapper.model.Word;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class ScrapperApplication{


  private WordDao wordDao;

  @Autowired
  public ScrapperApplication(WordDao wordDao) {
    this.wordDao = wordDao;
  }

  public static void main(String[] args) {
    SpringApplicationBuilder builder = new SpringApplicationBuilder(ScrapperApplication.class);
    builder.headless(false);
    builder.run(args);
  }
}