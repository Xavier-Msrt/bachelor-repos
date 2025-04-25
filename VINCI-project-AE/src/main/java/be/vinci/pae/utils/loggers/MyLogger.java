package be.vinci.pae.utils.loggers;

/**
 * The MyLogger interface provides a contract for logging messages. Implementations of this
 * interface should provide a way to log requests messages and error messages.
 */
public interface MyLogger {

  /**
   * Logs an error message along with a stack trace of an exception.
   *
   * @param message    the error message to be logged
   * @param stackTrace the exception whose stack trace should be logged
   */
  void error(String message, Throwable stackTrace);

  /**
   * Logs a request message.
   *
   * @param message the request message to be logged
   */
  void request(String message);

}
