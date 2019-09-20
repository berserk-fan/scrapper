# Working jar file is in repository 
## note: WordPage = 40 words.
# This is a scrapper for google translate website

It gets everything it can about a word: definitions, translations, synonyms, uses of examples and back-translations.
How it works:
  1) Selenium webdriver recieves a google tranlate webpage (html).
  2) Jsoup parses site and make a new words.
  3) Words are uploaded to MongoDb cloud.(using Spring Data).
## How fast does it works?
  It was intended to work for 3 days and parse all english words(300k with speed 2word/sec). But my poor hardware is lagging(processor), so I stopped.
  
## Why Selenium?
As I understood google translator gets content asynchronously using some JavaScript, so using a driver is inevitable.
Maybe I am wrong but simple GET request returns html page without usefull content.

## Why I made it?
I wanted a dictionary with 2 features:
  1) Add words with definitions and/or tranlsations and/or synonyms, etc.
  2) Ability to add my own use examples.
After some time I realized that parsing google kive is slow(1 sec), so I decided to create my own words database. 
