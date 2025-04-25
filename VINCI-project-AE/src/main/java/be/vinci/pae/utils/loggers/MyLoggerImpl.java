package be.vinci.pae.utils.loggers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * MyLoggerImpl is an implementation of the MyLogger interface. It uses Log4j for logging error and
 * request messages.
 */
public class MyLoggerImpl implements MyLogger {

  private static final Logger ERROR_LOGGER = LogManager.getLogger("ErrorLogger");
  private static final Logger REQUEST_LOGGER = LogManager.getLogger("RequestLogger");

  @Override
  public void error(String message, Throwable stackTrace) {
    ERROR_LOGGER.error(message, stackTrace);
  }

  @Override
  public void request(String message) {
    REQUEST_LOGGER.info(message);
  }

}
