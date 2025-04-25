package be.vinci.pae.utils.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * A custom exception extended from WebApplicationException that provides information on a not found
 * error (status code 404). This exception occurs when a user tries to access a resource that does
 * not exist or is not available.
 */
public class NotFoundException extends WebApplicationException {

  /**
   * Constructs a NotFoundException object with a default reason.
   */
  public NotFoundException() {
    super("NOT_FOUND", Status.NOT_FOUND);
  }

  /**
   * Constructs a NotFoundException object with a given reason.
   *
   * @param reason a coded message of the exception. The reason will be used as a coded message.
   */
  public NotFoundException(String reason) {
    super(reason, Status.NOT_FOUND);
  }

  /**
   * Constructs a NotFoundException object with a given cause and a default reason.
   *
   * @param cause the underlying exception that caused this NotFoundException (which is saved for
   *              the stack trace).
   */
  public NotFoundException(Throwable cause) {
    super("NOT_FOUND", cause, Status.NOT_FOUND);
  }

  /**
   * Constructs a NotFoundException object with a given reason and cause.
   *
   * @param reason a description of the exception. The reason will be used as a coded message.
   * @param cause  the underlying exception that caused this NotFoundException (which is saved for
   *               the stack trace).
   */
  public NotFoundException(String reason, Throwable cause) {
    super(reason, cause, Status.NOT_FOUND);
  }

}
