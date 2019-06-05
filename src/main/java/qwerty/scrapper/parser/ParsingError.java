package qwerty.scrapper.parser;

public class ParsingError extends RuntimeException {
  public ParsingError() {
  }

  public ParsingError(String message) {
    super(message);
  }

  public ParsingError(String message, Throwable cause) {
    super(message, cause);
  }

/*  *//**
   * Constructor for errors when expected class does not found in correspondent element.
   * Helps us handle structure errors.Here class means cssClass.
   * @param found {@code Element} which was expected to contain expectedClass.
   * @param expectedClass {@code className} cssClass name which we does not found.
   *//*
  public ParsingError(Element found, String expectedClass) {
    super("Assumed class: " + expectedClass + "Given element does not contain such class." + found.data());
  }*/
}
