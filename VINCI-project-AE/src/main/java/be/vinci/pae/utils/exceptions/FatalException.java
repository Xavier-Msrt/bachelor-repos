package be.vinci.pae.utils.exceptions;

/**
 * A custom exception extended from RuntimeException that provides information on an internal server
 * error (status code 500). This exception occurs when an error occurs in the server that is not
 * caused by the client.
 */
public class FatalException extends RuntimeException {

  /**
   * Constructs a FatalException object with a default reason.
   */
  public FatalException() {
    super("Fatal error occurred without any reason or cause provided.");
  }

  /**
   * Constructs a FatalException object with a given reason.
   *
   * @param reason a description of the exception.
   */
  public FatalException(String reason) {
    super(reason);
  }

  /**
   * Constructs a FatalException object with a given cause.
   *
   * @param cause the underlying exception that caused this FatalException (which is saved for the
   *              stack trace).
   */
  public FatalException(Throwable cause) {
    super(cause.getMessage(), cause);
  }

  /**
   * Constructs a FatalException object with a given reason and cause.
   *
   * @param reason a description of the exception.
   * @param cause  the underlying exception that caused this FatalException (which is saved for the
   *               stack trace).
   */
  public FatalException(String reason, Throwable cause) {
    super(reason, cause);
  }

}
